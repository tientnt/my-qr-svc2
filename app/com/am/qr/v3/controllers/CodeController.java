package com.am.qr.v3.controllers;

import com.am.common.exception.AMException;
import com.am.common.services.DbService;
import com.am.common.utils.*;
import com.am.common.utils.Response;
import com.am.qr.v3.dtos.*;
import com.am.qr.v3.models.Route;
import com.am.qr.v3.services.CodeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CodeController extends Controller {
    private static final Logger.ALogger logger = Logger.of(CodeController.class);

    private final WSClient wsClient;

    private final FormFactory formFactory;

    private final Config config;

    private CodeService codeService;

    private DbService dbService;

    private static final String HTTP = "http";

    private static final String APP_DOT_LINK = "app.link";

    private static final String AMPERSAND = "&";

    private static final String CODE_QUERY_PARAM = "c=";

    @Inject
    public CodeController(FormFactory formFactory,
                          WSClient wsClient,
                          Config config,
                          CodeService codeService,
                          DbService dbService) {
        this.formFactory = formFactory;
        this.wsClient = wsClient;
        this.config = config;
        this.codeService = codeService;
        this.dbService = dbService;
    }

    @Security.Authenticated(JWTSecured.class)
    public CompletionStage<Result> scanCode() throws AMException {
        Form<CodeRequest> formData = formFactory.form(CodeRequest.class)
                                                .bind(FormHelper.requestDataCamelCase(request()),
                                                      CodeRequest.ALLOWED_FIELDS);
        if (formData.hasErrors()) {
            return CompletableFuture.completedFuture(
                    badRequest(Json.toJson(new Response(HttpStatus.SC_BAD_REQUEST,
                                                        Constants.INVALID_REQUEST_PARAMS,
                                                        null,
                                                        formData.errorsAsJson()))));

        }

        JsonNode requestAsJson = request().body().asJson();
        String serviceAsString = requestAsJson.get(CodeRequest.SVC_TAG).asText();
        String typeAsString = requestAsJson.get(CodeRequest.TYPE_TAG).asText();
        logger.debug("Request as JsonNode: \n{}", AMObjectMapper.toPrettyJsonString(requestAsJson));

        String requestToken = request().header(Constants.AUTH_TOKEN_HEADER).orElse(null);
        Service requestService = Service.fromServiceName(serviceAsString);

        if (Constants.ProcessCodeType.VALIDATE.getValue().equals(typeAsString)
            && requestService == Service.EVOUCHER) {
            String uLiveCustomCode = detectULiveService(requestAsJson, requestService);
            if (StringUtils.isNotEmpty(uLiveCustomCode)) {
                return processUliveCode(uLiveCustomCode, requestService, requestAsJson, requestToken);
            }
        }

        return processCode(requestService, requestAsJson, requestToken);
    }

    @Security.Authenticated(JWTSecured.class)
    public CompletionStage<Result> codesValidateNoSvc() {
        String endpoint = config.getString("app.evoucher_codes_validate_url");
        return processCodesNoSvc(endpoint, Constants.ProcessCodeType.VALIDATE);
    }

    @Security.Authenticated(JWTSecured.class)
    public CompletionStage<Result> codesRedeemNoSvc() {
        String endpoint = config.getString("app.evoucher_codes_redeem_url");
        return processCodesNoSvc(endpoint, Constants.ProcessCodeType.REDEEM);
    }

    private CompletionStage<Result> processCodesNoSvc(String endpoint, Constants.ProcessCodeType type) {
        JsonNode requestAsJson = request().body().asJson();
        String codes = requestAsJson.get("code").asText();
        if (StringUtils.isEmpty(codes)) {
            return CompletableFuture.completedFuture(
                    badRequest(Json.toJson(new Response(HttpStatus.SC_BAD_REQUEST,
                                                        Constants.INVALID_REQUEST_PARAMS,
                                                        null,
                                                        null))));
        }

        codes = codes.replace(" ", "");
        List<String> codeList = Arrays.asList(codes.split(","));
        List<String> invalidCodeList = new ArrayList<>();
        String svcStr = null;
        String error = null;
        String hpbServiceType = detectHPBService(codeList.get(0));
        if (StringUtils.isNotEmpty(hpbServiceType)) {
            if (codeList.size() > 1) {
                logger.error("More than 1 code in one request {}: service: {}", codes, hpbServiceType);
                Map<String, String> responseData = new HashMap<>();
                error = Constants.INVALID_VOUCHER + ": " + StringUtils.join(invalidCodeList, ",");
                responseData.put("status", Constants.INVALID_VOUCHER);
                responseData.put("message", error);
                return CompletableFuture.completedFuture(
                        badRequest(Json.toJson(new Response(HttpStatus.SC_OK,
                                                            error,
                                                            responseData,
                                                            null))));
            }
            Route route = codeService.findByCode(codeList.get(0));
            if (null != route
                && null != route.getStatus()
                && route.getStatus().equals(Constants.QrStatus.USED.getValue())) {
                logger.error("Code has been used {}: service: {}", codes, hpbServiceType);
                Map<String, String> responseData = new HashMap<>();
                error = Constants.INVALID_VOUCHER_USED + ": " + codes;
                responseData.put("status", Constants.INVALID_VOUCHER_USED);
                responseData.put("message", error);
                return CompletableFuture.completedFuture(
                        badRequest(Json.toJson(new Response(HttpStatus.SC_OK,
                                                            error,
                                                            responseData,
                                                            null))));
            }
            String requestToken = request().header(Constants.AUTH_TOKEN_HEADER).orElse(null);
            if (Constants.ProcessCodeType.VALIDATE == type) {
                String hpbEndpoint = config.getString("hpb.code_validate_url");
                return proceedToNextService(hpbEndpoint, requestAsJson, requestToken);
            } else if (Constants.ProcessCodeType.REDEEM == type) {
                String hpbEndpoint = config.getString("hpb.code_redeem_url");
                return proceedToNextService(hpbEndpoint, requestAsJson, requestToken);
            }
        }
        for (String code : codeList) {
            Route route = codeService.findByCode(code);
            if (route == null) {
                invalidCodeList.add(code);
            } else {
                if (route.getSvc().contains(",")) {
                    List<String> svcList = new ArrayList<>();
                    String[] svcArr = route.getSvc().split(",");
                    for (String svc : svcArr) {
                        if (!svcList.contains(svc)) {
                            svcList.add(svc);
                        }
                    }
                    //update route if it has duplicate svc
                    if (svcArr.length != svcList.size()) {
                        route.setSvc(StringUtils.join(svcList, ","));
                        dbService.saveEntity(route);
                    }
                    if (svcList.size() > 1) {
                        logger.error("code {}, id {} belong to multiple services {}",
                                     code,
                                     route.getId(),
                                     route.getSvc());
                        error = String.format(Constants.INVALID_MULTIPLE_SVC, code, svcList.get(0), svcList.get(1));
                        break;
                    }
                }
                if (!StringUtils.isEmpty(svcStr) && !svcStr.equalsIgnoreCase(route.getSvc())) {
                    logger.error("More than 1 service type in one request {}: {} - {}", code, svcStr, route.getSvc());
                    error = String.format(Constants.INVALID_MULTIPLE_SVC, code, svcStr, route.getSvc());
                    break;
                }
                svcStr = route.getSvc();
            }
        }

        if (invalidCodeList.size() > 0) {
            Map<String, String> responseData = new HashMap<>();
            error = Constants.INVALID_VOUCHER + ": " + StringUtils.join(invalidCodeList, ",");
            responseData.put("status", Constants.INVALID_VOUCHER);
            responseData.put("message", error);
            return CompletableFuture.completedFuture(
                    badRequest(Json.toJson(new Response(HttpStatus.SC_OK,
                                                        error,
                                                        responseData,
                                                        null))));
        }
        if (!StringUtils.isEmpty(error)) {
            Map<String, String> responseData = new HashMap<>();
            responseData.put("status", Constants.INVALID_VOUCHER);
            responseData.put("message", error);
            return CompletableFuture.completedFuture(
                    badRequest(Json.toJson(new Response(HttpStatus.SC_OK,
                                                        error,
                                                        responseData,
                                                        null))));
        }

        String requestToken = request().header(Constants.AUTH_TOKEN_HEADER).orElse(null);
        Service requestService = Service.fromServiceName(svcStr);
        switch (requestService) {
            case EVOUCHER:
                return proceedToNextService(endpoint, requestAsJson, requestToken);
            default:
                return CompletableFuture.completedFuture(
                        badRequest(Json.toJson(new Response(HttpStatus.SC_BAD_REQUEST,
                                                            Constants.INVALID_SVC_TYPE,
                                                            null,
                                                            null))));

        }
    }

    /**
     * Using for HPB detection
     *
     * @param code
     * @return NULL if no any format matching with code
     */
    private String detectHPBService(String code) {
        try {
            String prefix = config.getString("hpb.prefix");
            int length = config.getInt("hpb.length");

            if (code.length() == length) {
                List<String> prefixList = Arrays.stream(prefix.split(","))
                                                .map(s -> s.trim())
                                                .collect(Collectors.toList());
                boolean isCodeStartWithPrefix = prefixList.stream().anyMatch(code::startsWith);
                if (isCodeStartWithPrefix) {
                    return Constants.HPB_SERVICE;
                }
            }
        } catch (Exception ex) {
            logger.error("Cannot detectHPBService. Error: {}", ex.getMessage());
        }
        return "";
    }

    private String detectULiveService(JsonNode jsonBody, Service svc) {
        try {
            MerchantScanCodeRequest request = AMObjectMapper.toObject(jsonBody, MerchantScanCodeRequest.class, true);
            if (null == request || null == request.getData()) {
                logger.warn("No merchant detail data. Cannot map to merchant scan code request");
                return "";
            }
            String listCodes = config.getString("ulive.custom_outlet_codes");
            String code = request.getData().getCustomOutletCode();

            List<String> matchList = Arrays.stream(listCodes.split(","))
                                           .map(s -> s.trim())
                                           .collect(Collectors.toList());
            boolean isMatched = matchList.stream().anyMatch(code::equalsIgnoreCase);
            if (isMatched) {
                return Constants.ULIVE_SERVICE;
            }

            //new logic, detect by current group
            String linkWithCode = jsonBody.get("code").asText();
            String originalCode = extractOnlyVoucherCode(linkWithCode);
            Route preRoute = codeService.findByCodeAndSvc(originalCode, svc.getServiceName());
            if (preRoute != null && StringUtils.isNotEmpty(preRoute.getGroup()) &&
                preRoute.getGroup().startsWith(Constants.ULIVE_SERVICE)) {
                return Constants.ULIVE_SERVICE;
            }

        } catch (Exception ex) {
            logger.error("Cannot detectULiveService. Error: {}", ex.getMessage());
        }
        return "";
    }

    public String extractOnlyVoucherCode(String linkWithCode) {
        if (linkWithCode.contains(HTTP) || linkWithCode.contains(APP_DOT_LINK)) {
            int cIndex = linkWithCode.indexOf(CODE_QUERY_PARAM);
            if (cIndex == -1) {
                logger.error("Unable to locate voucher code from URL {}", linkWithCode);
                return linkWithCode;
            }
            int ampersandIndex = linkWithCode.indexOf(AMPERSAND, cIndex);

            if (ampersandIndex == -1) {
                ampersandIndex = linkWithCode.length();
            }
            String code = linkWithCode.substring(cIndex + CODE_QUERY_PARAM.length(), ampersandIndex);
            logger.debug("Extracted code {} from link {}", code, linkWithCode);
            return code;
        } else {
            logger.debug("Code {} is not a link", linkWithCode);
            return linkWithCode;
        }
    }

    private CompletionStage<Result> processCode(Service svc, JsonNode requestBody, String requestToken) {
        String nextUrl;
        switch (svc) {
            case DOOR_ACCESS:
                nextUrl = config.getString("app.door_access_code_url");
                return proceedToNextService(nextUrl, requestBody, requestToken);
            case EVOUCHER:
                nextUrl = config.getString("app.evoucher_merchant_url");
                return proceedToNextService(nextUrl, requestBody, requestToken);
            case WWPM:
                nextUrl = config.getString("app.wwpm_validate_code_url");
                return proceedToNextService(nextUrl, requestBody, requestToken);
            default:
                return CompletableFuture.completedFuture(
                        badRequest(Json.toJson(new Response(HttpStatus.SC_BAD_REQUEST,
                                                            Constants.INVALID_SVC_TYPE,
                                                            null,
                                                            null))));
        }
    }

    private CompletionStage<Result> processUliveCode(String uLiveCustomCode,
                                                     Service svc,
                                                     JsonNode requestBody,
                                                     String requestToken) {
        String nextMerchantUrl = config.getString("app.evoucher_merchant_url");
        String linkWithCode = requestBody.get("code").asText();
        String code = extractOnlyVoucherCode(linkWithCode);

        int ntucCodeLength = config.getInt("ulive.ntuc_code_length");
        int publicCodeLength = config.getInt("ulive.public_code_length");
        String merchantQrType = null;
        if (ntucCodeLength == code.length()) {
            merchantQrType = Constants.ULiveCodeType.NTUC.getValue();
        } else if (publicCodeLength == code.length()) {
            merchantQrType = Constants.ULiveCodeType.PUBLIC.getValue();
        }
        if (StringUtils.isEmpty(merchantQrType)) {
            logger.warn("Detect ULive but scanned code {} not matching with NTUC/Public", code);
            return processCode(svc, requestBody, requestToken);
        }

        String zoneId = config.getString("app.app_time_zone_id");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter logDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        ZoneId localZoneId = ZoneId.of(zoneId);
        LocalDateTime now = LocalDateTime.now();
        Timestamp tsNow = Timestamp.valueOf(now);
        Instant instantTime = tsNow.toInstant();
        ZonedDateTime sporeTime = instantTime.atZone(localZoneId);

        String dateFormatted = sporeTime.format(dateFormat);
        String merchantVoucherType = Constants.ULIVE_SERVICE + "." + merchantQrType + "." + dateFormatted;
        logger.debug("ULive merchant check voucher group with local zone date {}", sporeTime.format(logDateFormat));

        Route route = codeService.findByCodeSvcGroup(code, svc.getServiceName(), merchantVoucherType);
        if (null == route) {
            logger.info("Start minting ULive vouchers");
            String nextMintUrl = config.getString("app.evoucher_merchant_mint_url");
            MerchantMintVoucherRequest request = new MerchantMintVoucherRequest();
            request.setQrCode(code);

            request.setMerchantVoucherType(merchantVoucherType);
            request.setCustomCode(uLiveCustomCode);
            JsonNode requestMintAsJson = AMObjectMapper.toJsonNode(request, true);
            return wsClient.url(nextMintUrl)
                           .setContentType(Constants.CONTENT_TYPE)
                           .addHeader(Constants.AUTH_TOKEN_HEADER, requestToken)
                           .post(requestMintAsJson)
                           .thenApply(wsResponse -> {
                               logger.info("ULive mint response: \n {}", wsResponse.getBody());
                               if (HttpStatus.SC_OK == wsResponse.getStatus()) { //mint successful
                                   Route preRoute = codeService.findByCodeAndSvc(code, svc.getServiceName());
                                   if (preRoute != null && (StringUtils.isEmpty(preRoute.getGroup()) ||
                                                            !merchantVoucherType.equals(preRoute.getGroup()))) {
                                       logger.info("ULive manual import hash for QR {} and Group {}",
                                                   code,
                                                   merchantVoucherType);
                                       String hashCode = DigestUtils.sha256Hex(code);
                                       List<String> hashList = Arrays.asList(hashCode);
                                       List<String> groupList = Arrays.asList(merchantVoucherType);
                                       codeService.importHashes(svc.getServiceName(),
                                                                hashList,
                                                                null,
                                                                groupList);
                                   }
                                   try {
                                       return proceedToNextService(nextMerchantUrl,
                                                                   requestBody,
                                                                   requestToken).toCompletableFuture()
                                                                                .get(60, TimeUnit.SECONDS);
                                   } catch (Exception e) {
                                       logger.error(
                                               "Cannot process merchant scan code after minting successfully, error: {}",
                                               e.getMessage());
                                   }
                               }
                               logger.error("Merchant minted failed. Please check!!!");
                               // mint failed; please check
                               return status(wsResponse.getStatus(), wsResponse.getBody()).as(Constants.CONTENT_TYPE);
                           });

        } else { // import to QR & Minted already
            return proceedToNextService(nextMerchantUrl, requestBody, requestToken);
        }
    }

    private CompletionStage<Result> proceedToNextService(String nextUrl, JsonNode requestAsJson, String requestToken) {
        return wsClient.url(nextUrl)
                       .setContentType(Constants.CONTENT_TYPE)
                       .addHeader(Constants.AUTH_TOKEN_HEADER, requestToken)
                       .post(requestAsJson)
                       .thenApply(wsResponse -> status(wsResponse.getStatus(),
                                                       wsResponse.getBody()).as(Constants.CONTENT_TYPE));
    }

    @Security.Authenticated(JWTSecured.class)
    public CompletionStage<Result> importHashes() {
        ImportHashRequest data = Json.fromJson(request().body().asJson(), ImportHashRequest.class);

        return CompletableFuture.supplyAsync(() -> {
            boolean result = codeService.importHashes(data.getSvc(),
                                                      data.getHashes(),
                                                      data.getStatus(),
                                                      data.getGroups());
            if (result) {
                return Response.success("import hashes success", null);
            }
            return Response.badRequest("import failed", null);
        });
    }

    @Security.Authenticated(JWTSecured.class)
    public CompletionStage<Result> getSvc() {
        String code = request().getQueryString("code");

        return CompletableFuture.supplyAsync(() -> {
            String result = codeService.findServiceByCode(code);
            if (StringUtils.isNotEmpty(result)) {
                return Response.success(result, null);
            }
            return Response.badRequest("no hash found", null);
        });
    }

    @Security.Authenticated(JWTSecured.class)
    public CompletionStage<Result> scanCodeV2() throws AMException {
        Form<CodeRequest> formData = formFactory.form(CodeRequest.class)
                                                .bind(FormHelper.requestDataCamelCase(request()));
        if (formData.hasErrors()) {
            return CompletableFuture.completedFuture(
                    badRequest(Json.toJson(new Response(HttpStatus.SC_BAD_REQUEST,
                                                        Constants.INVALID_REQUEST_PARAMS,
                                                        null,
                                                        formData.errorsAsJson()))));

        }

        JsonNode requestAsJson = request().body().asJson();
        String serviceAsString = requestAsJson.get(CodeRequest.SVC_TAG).asText();
        logger.debug("Request as JsonNode: \n{}", AMObjectMapper.toPrettyJsonString(requestAsJson));

        String code = requestAsJson.get("code").asText();
        Route route = codeService.findByCodeAndSvc(code, serviceAsString);
        Map<String, String> responseData = new HashMap<>();
        if (route == null) {
            String error = Constants.INVALID_CODE + ": " + code;
            responseData.put("status", Constants.INVALID_CODE);
            responseData.put("message", error);
            return CompletableFuture.completedFuture(
                    badRequest(Json.toJson(new Response(HttpStatus.SC_BAD_REQUEST,
                                                        error,
                                                        responseData,
                                                        null))));
        }
        responseData.put("status", Constants.SUCCESS);
        responseData.put("message", Constants.SUCCESS);
        return CompletableFuture.completedFuture(
                ok(Json.toJson(new Response(HttpStatus.SC_OK,
                                            Constants.SUCCESS,
                                            responseData,
                                            null))));
    }

    @Security.Authenticated(JWTSecured.class)
    public CompletionStage<Result> validateNewCode() throws AMException {
        Form<ValidateNewCodeRequest> formData = formFactory.form(ValidateNewCodeRequest.class)
                                                           .bind(FormHelper.requestDataCamelCase(request())
                                                                   , ValidateNewCodeRequest.ALLOWED_FIELDS);
        if (formData.hasErrors()) {
            return CompletableFuture.completedFuture(
                    badRequest(Json.toJson(new Response(HttpStatus.SC_BAD_REQUEST,
                                                        Constants.INVALID_REQUEST_PARAMS,
                                                        null,
                                                        formData.errorsAsJson()))));

        }

        JsonNode requestAsJson = request().body().asJson();
        String serviceAsString = requestAsJson.get(CodeRequest.SVC_TAG).asText();
        logger.debug("Request as JsonNode: \n{}", AMObjectMapper.toPrettyJsonString(requestAsJson));

        String code = requestAsJson.get("code").asText();
        Route route = codeService.findByCodeAndSvc(code, serviceAsString);
        Map<String, String> responseData = new HashMap<>();
        if (null == route) {
            Service requestService = Service.fromServiceName(serviceAsString);
            String requestToken = request().header(Constants.AUTH_TOKEN_HEADER).orElse(null);
            return processCode(requestService, requestAsJson, requestToken);
        } else {
            String error = Constants.EXISTED_CODE + ": " + code;
            responseData.put("status", Constants.EXISTED_CODE);
            responseData.put("message", error);
            return CompletableFuture.completedFuture(
                    badRequest(Json.toJson(new Response(HttpStatus.SC_BAD_REQUEST,
                                                        error,
                                                        responseData,
                                                        null))));
        }
    }
}

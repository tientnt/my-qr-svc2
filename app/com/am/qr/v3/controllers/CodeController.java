package com.am.qr.v3.controllers;

import com.am.common.exception.AMException;
import com.am.common.utils.*;
import com.am.qr.v3.dtos.CodeRequest;
import com.am.qr.v3.dtos.ImportHashRequest;
import com.am.qr.v3.services.CodeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CodeController extends Controller {
    private static final Logger.ALogger logger = Logger.of(CodeController.class);

    private final WSClient wsClient;

    private final FormFactory formFactory;

    private final Config config;

    private CodeService codeService;

    @Inject
    public CodeController(FormFactory formFactory, WSClient wsClient, Config config, CodeService codeService) {
        this.formFactory = formFactory;
        this.wsClient = wsClient;
        this.config = config;
        this.codeService = codeService;
    }

    @Security.Authenticated(JWTSecured.class)
    public CompletionStage<Result> scanCode() throws AMException {
        Form<CodeRequest> formData = formFactory.form(CodeRequest.class)
                                                .bind(FormHelper.requestDataCamelCase(request())
                                                        , CodeRequest.ALLOWED_FIELDS);
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

        String requestToken = request().header(Constants.AUTH_TOKEN_HEADER).orElse(null);
        Service requestService = Service.fromServiceName(serviceAsString);
        String nextUrl;
        switch (requestService) {
            case DOOR_ACCESS:
                nextUrl = config.getString("app.door_access_code_url");
                return proceedToNextService(nextUrl, requestAsJson, requestToken);
            case EVOUCHER:
                nextUrl = config.getString("app.e_voucher_url");
                return proceedToNextService(nextUrl, requestAsJson, requestToken);
            default:
                return CompletableFuture.completedFuture(
                        badRequest(Json.toJson(new Response(HttpStatus.SC_BAD_REQUEST,
                                                            Constants.INVALID_SVC_TYPE,
                                                            null,
                                                            formData.errorsAsJson()))));
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
            boolean result = codeService.importHashes(data.getSvc(), data.getHashes());
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
}

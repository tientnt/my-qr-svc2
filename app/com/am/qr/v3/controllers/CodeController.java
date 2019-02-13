package com.am.qr.v3.controllers;

import com.am.common.utils.Constants;
import com.am.common.utils.FormHelper;
import com.am.common.utils.JWTSecured;
import com.am.common.utils.Response;
import com.am.qr.v3.dtos.CodeRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
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

    @Inject
    public CodeController(FormFactory formFactory, WSClient wsClient, Config config) {
        this.formFactory = formFactory;
        this.wsClient = wsClient;
        this.config = config;
    }

    @Security.Authenticated(JWTSecured.class)
    public CompletionStage<Result> scanCode() {
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
        CodeRequest requestData = formData.get();
        String requestToken = request().header(Constants.AUTH_TOKEN_HEADER).orElse(null);
        switch (requestData.getSvc()) {
            case Constants.SVC_DOOR_ACCESS:
                return doorAccessResult(requestData.getCode(), requestData.getType(), requestToken);
            case Constants.SVC_EVOUCHER:
                return evoucherResult(requestData.getCode(), requestData.getType(), requestToken);
        }
        return CompletableFuture.completedFuture(
                badRequest(Json.toJson(new Response(HttpStatus.SC_BAD_REQUEST,
                                                    Constants.INVALID_SVC_TYPE,
                                                    null,
                                                    formData.errorsAsJson()))));

    }

    private CompletionStage<Result> evoucherResult(String code, String type, String requestToken) {
        String mockupData = "{\n" +
                            "   \"code\": 200,\n" +
                            "   \"message\": \"Scan code result\",\n" +
                            "   \"data\":    {\n" +
                            "      \"status\": \"SUCCESS\",\n" +
                            "      \"message\": \"\",\n" +
                            "      \"scanned_code\": \"" + code + "\",\n" +
                            "      \"detail\": {\n" +
                            "         \"code_status\": \"VALID\",\n" +
                            "         \"code_message\": \"You may proceed to redeem\",\n" +
                            "         \"image\": \"\",\n" +
                            "         \"voucher_name\": \"DTE $10 Voucher\",\n" +
                            "         \"voucher_value\": \"$10.00\",\n" +
                            "         \"validity_start_date\": \"10 Jan 2018\",\n" +
                            "         \"validity_end_date\": \"10 Feb 2018\"\n" +
                            "       }\n" +
                            "   },\n" +
                            "   \"errors\": null\n" +
                            "}\n";
        JsonNode result = Json.parse(mockupData);
        return CompletableFuture.completedFuture(ok(result));
    }

    private CompletionStage<Result> doorAccessResult(String code, String type, String requestToken) {
        String codeUrl = config.getString("app.door_access_code_url");
        JsonNode codeRequest = Json.newObject().put("code", code).put("type", type);
        return wsClient.url(codeUrl)
                       .setContentType(Constants.CONTENT_TYPE)
                       .addHeader(Constants.AUTH_TOKEN_HEADER, requestToken)
                       .post(codeRequest)
                       .thenApply(wsResponse -> status(wsResponse.getStatus(),
                                                       wsResponse.getBody()).as(Constants.CONTENT_TYPE));
    }
}

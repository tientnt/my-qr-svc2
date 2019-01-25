package com.am.qr.v3.controllers;

import com.am.common.utils.Oauth2Secure;
import com.am.qr.v3.dtos.Response;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpStatus;
import play.Logger;
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

    @Inject
    private WSClient ws;

    public CompletionStage<Result> scanCode() {
        String type;
        String code;
        try {
            JsonNode body = request().body().asJson();
            code = body.get("code").asText();
            type = body.get("type").asText();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return CompletableFuture.completedFuture(badRequest(Json.toJson(
                    new Response(HttpStatus.SC_BAD_REQUEST,
                                 "Invalid request params",
                                 null,
                                 null))));
        }

        String mockupData = "\n" +
                            "{\n" +
                            "   \"code\": 200,\n" +
                            "   \"message\": \"Scan code result\",\n" +
                            "   \"data\":    {\n" +
                            "      \"status\": \"SUCCESS\",\n" +
                            "      \"message\": \"\",\n" +
                            "      \"scanned_code\": \"xyz123\",\n" +
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
        return CompletableFuture.completedFuture(ok(mockupData));
    }

}

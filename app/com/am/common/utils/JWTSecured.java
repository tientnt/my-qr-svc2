package com.am.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import io.jsonwebtoken.Claims;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class JWTSecured extends Security.Authenticator {

    private final Logger.ALogger logger = Logger.of(JWTSecured.class);

    private static final String TOKEN_EXPIRED = "token_expired";

    private static int TOKEN_EXPIRED_CODE = 498;

    public final static String AUTH_TOKEN_HEADER = "Authorization";

    public final static String CONTENT_TYPE = "Content-Type";

    @Inject
    private Config config;

    @Override
    public String getUsername(Http.Context ctx) {
        logger.debug("Begin getUserName for context");

        String bearer = ctx.request().header(AUTH_TOKEN_HEADER).orElse(null);
        if (bearer == null) {
            bearer = ctx.request().getQueryString(AUTH_TOKEN_HEADER);
        }
        if (bearer == null) {
            String contentType = ctx.request().header(CONTENT_TYPE).orElse(null);
            if ("application/json".equalsIgnoreCase(contentType)) {
                JsonNode body = ctx.request().body().asJson();
                bearer = body.has(AUTH_TOKEN_HEADER) ? body.get(AUTH_TOKEN_HEADER).asText() : null;
            }
            //Temporary ignore POST form, use csrf token
        }
        if (bearer == null) {
            return null;
        }
        String accessToken = bearer.replace("Bearer ", "");

        if (ctx.args.containsKey(TOKEN_EXPIRED)) {
            ctx.args.remove(TOKEN_EXPIRED);
        }
        JsonNode expDateNode = SecurityHelper.getJWTClaim(accessToken, "exp");
        if (null == expDateNode) {
            return null;
        }
        try {
            LocalDateTime localExpiredAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(expDateNode.asLong()),
                                                                   ZoneOffset.UTC);
            LocalDateTime localNow = LocalDateTime.now(ZoneOffset.UTC);
            if (localExpiredAt.isBefore(localNow)) {
                // token expired
                ctx.args.put(TOKEN_EXPIRED, "true");
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }

        JsonNode subClaim = SecurityHelper.getJWTClaim(accessToken, "client_id");
        if (subClaim == null) {
            return null;
        }
        String regId = subClaim.asText();

        final List<String> clientList = config.getStringList("app.client_ids");
        final List<String> secretList = config.getStringList("app.client_secrets");
        int clientIndex = clientList.indexOf(regId);
        if (clientIndex < 0) {
            logger.error("Cannot find regId in configuration: {}", regId);
            return null;
        }
        String jwtSecret = secretList.get(clientIndex);

        Claims claims = SecurityHelper.verifyJWTToken(accessToken, jwtSecret);
        if (claims == null) {
            //invalid access token
            return null;
        }

        return regId;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        if (ctx.args.containsKey(TOKEN_EXPIRED)) {
            return Response.response(TOKEN_EXPIRED_CODE, TOKEN_EXPIRED, null, null);
        }
        return super.onUnauthorized(ctx);
    }
}

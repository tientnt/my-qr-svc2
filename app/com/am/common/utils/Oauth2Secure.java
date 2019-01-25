package com.am.common.utils;

import com.am.qr.v3.repositories.AppDeviceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import com.typesafe.config.Config;
import io.jsonwebtoken.Claims;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.Date;

public class Oauth2Secure extends Security.Authenticator {
    private final Logger.ALogger logger = Logger.of(Oauth2Secure.class);

    private static final String TOKEN_EXPIRED = "token_expired";

    private static int TOKEN_EXPIRED_CODE = 498;

    @Inject
    private AppDeviceRepository appDeviceRepository;

    @Inject
    private Config config;

    @Override
    public String getUsername(Http.Context ctx) {
        if (config.hasPath("app.test_username")) {
            String username = config.getString("app.test_username");
            ctx.args.put("username", username);
            return username;
        }

        String bearer = ctx.request().header(Constants.AUTH_TOKEN_HEADER).orElse(null);
        if (Strings.isNullOrEmpty(bearer) || !bearer.contains(Constants.AUTH_TOKEN_TYPE)) {
            return null;
        }

        String accessToken = bearer.replace(Constants.AUTH_TOKEN_TYPE, "");

        String clientId = SecurityHelper.getJWTClaim(accessToken, "client_id").textValue();
        if (!config.getString("app.client_id").equalsIgnoreCase(clientId)) {
            //invalid client_id
            return null;
        }

        try {
            Claims claims = SecurityHelper.verifyJWTToken(accessToken, config.getString("app.client_secret"));
            if (claims != null) {
                ctx.args.put("username", claims.getSubject());
                return claims.getSubject();
            }

            JsonNode expNode = SecurityHelper.getJWTClaim(accessToken, "exp");
            if (expNode != null) {
                Date expiration = new Date(expNode.asInt());
                if (expiration.before(new Date())) {
                    ctx.args.put(TOKEN_EXPIRED, true);
                    logger.error(" token expires: {}", expiration);
                    return null;
                }
            }

        } catch (Exception ex) {
            logger.error("getUsername: {} - {}", accessToken, ex.getMessage());
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        if (ctx.args.containsKey(TOKEN_EXPIRED)) {
            return Response.response(TOKEN_EXPIRED_CODE, TOKEN_EXPIRED, null, null);
        }
        return super.onUnauthorized(ctx);
    }
}

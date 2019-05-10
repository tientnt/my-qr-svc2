package com.am.common.services.authentication;

/**
 * Created by: billnguyen
 * at: 17/2/19 10:12
 * <p>
 * AppliedMesh Pte. Ltd.
 */

import com.am.common.utils.Response;
import com.am.common.utils.SecurityHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticate the request coming into Door Access Service from other servers
 */
public class InboundServerAuthenticator extends Security.Authenticator {

    private Logger.ALogger logger = Logger.of(InboundServerAuthenticator.class);

    public final static String AUTH_TOKEN_HEADER = "Authorization";

    public final static String CONTENT_TYPE = "Content-Type";

    private static final String TOKEN_EXPIRED = "token_expired";

    public static final String USER_REGISTRATION_ID = "user_registration_id";

    private static int TOKEN_EXPIRED_CODE = 498;

    private List<String> jwtClientIdList = new ArrayList<>();

    private List<String> jwtClientSecretList = new ArrayList<>();

    private List<String> jwtClientNameList = new ArrayList<>();

    @Inject
    private Config config;

    @Override
    public String getUsername(Http.Context ctx) {
        String ipAddress = ctx.request().remoteAddress();
        String path = ctx.request().host() + ctx.request().path();
        logger.debug("Begin authorizing incoming request {} from {}", path, ipAddress);

        String jwtSub = getUserNameFromJwtBearer(ctx);
        if (null != jwtSub) {
            ctx.args.put(USER_REGISTRATION_ID, jwtSub);
            logger.info("Request {} from {} (IP {}) is AUTHENTICATED", path, jwtSub, ipAddress);
        } else {
            logger.error("Request {} from IP {} is UNAUTHENTICATED", path, ipAddress);
        }

        return jwtSub;
    }

    protected String getUserNameFromJwtBearer(Http.Context ctx) {
        String ipAddress = ctx.request().remoteAddress();
        String path = ctx.request().host() + ctx.request().path();
        logger.debug("Begin authorizing incoming request {} from {}", path, ipAddress);

        //TODO: remove this test code on production
        if (config.hasPath("app.test_username") && StringUtils.isNotEmpty(config.getString("app.test_username"))) {
            return config.getString("app.test_username");
        }

        String bearer = ctx.request()
                           .header(AUTH_TOKEN_HEADER)
                           .orElseGet(() -> ctx.request().getQueryString(AUTH_TOKEN_HEADER));

        if (bearer == null) {
            logger.debug("bearer not found in " + AUTH_TOKEN_HEADER +
                         " header or query string. Trying to get from json payload if any");
            String contentType = ctx.request().header(CONTENT_TYPE).orElse(null);
            if ("application/json".equalsIgnoreCase(contentType)) {
                JsonNode body = ctx.request().body().asJson();
                bearer = body.has(AUTH_TOKEN_HEADER) ? body.get(AUTH_TOKEN_HEADER).asText() : null;
            }
            //Temporary ignore POST form, use csrf token
        }
        if (bearer == null) {
            logger.error("Bearer not found. Unauthorized!");
            return null;
        }
        String accessToken = bearer.replace("Bearer ", "");

        loadConfig();

        String jwtClientId = SecurityHelper.getJWTClientId(accessToken);

        int jwtClientIndex = jwtClientIdList.indexOf(jwtClientId);
        if (-1 == jwtClientIndex) {
            //TODO What if there is no jwt client id sent?
            logger.error("JWT Client id {} is not recognized", jwtClientId);
            return null;
        } else {
            String jwtClientSecret = jwtClientSecretList.get(jwtClientIndex);
            String jwtClientName = jwtClientNameList.get(jwtClientIndex);
            logger.debug("Request from {}", jwtClientName);

            Claims claims = SecurityHelper.verifyJWTToken(accessToken, jwtClientSecret);
            if (claims == null) {
                JsonNode expNode = SecurityHelper.getJWTExpNode(accessToken);
                // ExpNode present: Expired token
                if (expNode != null) {
                    Date expiration = new Date(expNode.asInt());
                    if (expiration.before(new Date())) {
                        ctx.args.put(TOKEN_EXPIRED, true);
                        logger.error(" token expires: {}", expiration);
                        return null;
                    }
                }
                // else: invalid access token
                logger.error("Unable to get claims. Return null");
                return null;
            }

            JsonNode subClaim = SecurityHelper.getJWTSubNode(accessToken);
            if (subClaim == null) {
                logger.error("Unable to get subClaim. Return null");
                return null;
            }

            String subAsStr = subClaim.asText();
            return subAsStr;
        }
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        if (ctx.args.containsKey(TOKEN_EXPIRED)) {
            return Response.response(TOKEN_EXPIRED_CODE, TOKEN_EXPIRED, null, null);
        }
        return super.onUnauthorized(ctx);
    }

    private void loadConfig() {
        String jwtClientIdCsv = config.getString("app.inbound_auth.jwt_client_ids");
        String jwtClientSecretCsv = config.getString("app.inbound_auth.jwt_client_secrets");
        String jwtClientNameCsv = config.getString("app.inbound_auth.jwt_client_names");

        if (StringUtils.isNotEmpty(jwtClientIdCsv)) {
            jwtClientIdList = Arrays.stream(jwtClientIdCsv.split(",")).map(s -> s.trim()).collect(Collectors.toList());
        }

        if (StringUtils.isNotEmpty(jwtClientSecretCsv)) {
            jwtClientSecretList = Arrays.stream(jwtClientSecretCsv.split(","))
                                        .map(s -> s.trim())
                                        .collect(Collectors.toList());
        }

        if (StringUtils.isNotEmpty(jwtClientNameCsv)) {
            jwtClientNameList = Arrays.stream(jwtClientNameCsv.split(","))
                                      .map(s -> s.trim())
                                      .collect(Collectors.toList());
        }
    }
}

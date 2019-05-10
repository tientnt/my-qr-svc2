package com.am.common.services.authentication;

import com.am.common.utils.SecurityHelper;
import com.typesafe.config.Config;
import org.apache.commons.lang3.StringUtils;
import play.Logger;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by: billnguyen
 * at: 2019-03-11 11:34
 * <p>
 * AppliedMesh Pte. Ltd.
 */
public class OutboundServerAuthenticationServiceImpl implements OutboundServerAuthenticationService {

    private static Logger.ALogger logger = Logger.of(OutboundServerAuthenticationServiceImpl.class);

    private static String eVoucherOutboundJwtToken;

    private static Date jwtTokenExpiredAt;

    private final Config config;

    private String jwtClientId;

    private int expireAfter;

    private String jwtClientSecret;

    public OutboundServerAuthenticationServiceImpl(Config _config) {
        this.config = _config;
        jwtClientId = config.getString("app.outbound_auth.jwt_client_id");
        jwtClientSecret = config.getString("app.outbound_auth.jwt_client_secret");
        expireAfter = config.getInt("app.outbound_auth.jwt_expire_after");
    }

    private void generateNewJwtToken() {
        logger.debug("OutboundServerAuthenticationServiceImpl - generateNewJwtToken");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, expireAfter);
        Date newExpireAt = c.getTime();
        jwtTokenExpiredAt = newExpireAt;
        logger.debug("New Expiry time: {}", newExpireAt.toString());

        String newJwt = SecurityHelper.generateAccessTokenWithClientId("evoucher",
                                                                       jwtClientId,
                                                                       jwtClientSecret,
                                                                       jwtTokenExpiredAt);
        eVoucherOutboundJwtToken = "Bearer " + newJwt;
    }

    @Override
    public String getJwtToken() {
        if (StringUtils.isEmpty(eVoucherOutboundJwtToken)) {
            logger.debug("eVoucherServiceJwtToken has not been generated. Generating one...");
            generateNewJwtToken();
        } else if (jwtTokenExpiredAt.before(new Date())) {
            logger.debug("JWT token expired. Generating new one");
            generateNewJwtToken();
        } else {
            logger.debug("There is a non-expired JWT token for eVoucher Service. Reuse this one");
        }

        return eVoucherOutboundJwtToken;
    }

}

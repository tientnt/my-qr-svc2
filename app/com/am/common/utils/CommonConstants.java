package com.am.common.utils;

import com.google.common.base.Enums;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by: billnguyen
 * at: 19/2/19 16:48
 * <p>
 * AppliedMesh Pte. Ltd.
 */
public class CommonConstants {

    public final static String AUTH_TOKEN_TYPE = "Bearer";

    public final static String AUTH_TOKEN_HEADER = "Authorization";

    public final static String AUTH_TOKEN_EXPIRED = "TokenExpired";

    public final static String AUTH_TOKEN_OLD_TOKEN = "OldToken";

    public final static String AUTH_PASSWORD_GRANT_TYPE = "password";

    public final static String AUTH_REFRESH_GRANT_TYPE = "refresh_token";

    public static final DateTimeFormatter EXCEPTION_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss.SSSXXX");

    public static final ZoneId SINGAPORE_ZONE_ID = ZoneId.of("Asia/Singapore");

    public static final String CONTENT_TYPE_XWWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    public static final String ENCODING_UTF8 = "UTF-8";

    public enum LocaleCode {
        EN_US("en_US"),
        ZH_MO("zn_MO");

        private String localeCode;

        LocaleCode(String localeCode){
            this.localeCode = localeCode;
        }

        public String getLocaleCode(){
            return localeCode;
        }

        public static LocaleCode fromLocaleCodeIfPresent(String localeCode){
            return Enums.getIfPresent(LocaleCode.class, localeCode).orNull();
        }
    }
}


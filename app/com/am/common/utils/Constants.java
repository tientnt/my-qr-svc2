package com.am.common.utils;

import java.text.SimpleDateFormat;

public final class Constants {
    public final static String AUTH_TOKEN_TYPE = "Bearer";

    public final static String AUTH_TOKEN_HEADER = "Authorization";

    public final static String AUTH_TOKEN_EXPIRED = "TokenExpired";

    public final static String AUTH_TOKEN_OLD_TOKEN = "OldToken";

    public final static String AUTH_PASSWORD_GRANT_TYPE = "password";

    public final static String AUTH_REFRESH_GRANT_TYPE = "refresh_token";

    public final static String AUTH_LOGIN_INVALID = "You have entered an invalid email";

    public final static String AUTH_SINGLE_SIGNIN_INVALID = "You've already logged out. Please login again";

    public final static String INVALID_REQUEST_PARAMS = "Invalid request parameters";

    public final static String TYPE_ACTIVATION_DEVICE = "device";

    public final static String TYPE_ACTIVATION_CARD = "card";

    public static final String PROCESS_CARD_RESULT = "Process card result";

    public static final String VERITY_OTP_RESULT = "Verify OTP result";

    public static final String PENDING_SETUP = "待定設置";

    public static final String STATUS_ACTIVE = "卡激活";

    public static final String STATUS_INACTIVE = "Inactive";

    public static final String STATUS_BLOCKED = "禁止";

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    public static SimpleDateFormat dateFormatApiResponse = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    public static SimpleDateFormat dateFormatWithoutTime = new SimpleDateFormat("dd MMM yyyy");

    public enum CardStatus {
        DEVICE_INVALID("DEVICE_INVALID", "Device is not found (設備尚未激活)", "設備尚未激活"),
        DEVICE_NOT_ACTIVE("DEVICE_NOT_ACTIVE", "設備尚未激活。如果您有設置權限，可以使用驗證碼通過getkaki應用激活設備。", "設備尚未激活"),
        CARD_INVALID("CARD_INVALID", "卡無效。如果您有權限，可以使用驗證碼通過getkaki應用激活您的卡。", "卡無效。"),
        CARD_NOT_ACTIVE("CARD_NOT_ACTIVE", "卡無效。如果您有權限，可以使用驗證碼通過getkaki應用激活您的卡。", "卡無效。"),
        CARD_GATE_NOT_FOUND("CARD_GATE_NOT_FOUND", "您的卡無法打開此門，您可以通過getkaki應用註冊權限。", "您的卡無法打開此門"),
        CARD_ACTIVATED("CARD_ACTIVATED", "門卡已被激活", "門卡已被激活"),
        CARD_EXPIRED("CARD_EXPIRED", "門卡已過期", "門卡已過期"),
        SUCCESS("SUCCESS", "大門已解鎖", "大門已解鎖");

        final String name;
        final String description;
        final String shortDesc;

        CardStatus(String name, String description, String shortDesc) {
            this.name = name;
            this.description = description;
            this.shortDesc = shortDesc;
        }

        public String getName() {
            return name;
        }

        public String getDescription(){
            return description;
        }

        public String getShortDesc() { return shortDesc; }
    }

    public enum VerifyOtpStatus {
        OTP_INVALID("OTP_INVALID", "找不到驗證碼"),
        OTP_EXPIRED("OTP_EXPIRED", "驗證碼已過期"),
        ACTIVATION_TYPE_INVALID("ACTIVATION_TYPE_INVALID", "Activation Type for this OTP is not valid"),
        DEVICE_NOT_ACTIVE("DEVICE_NOT_ACTIVE", "設備未激活"),
        INVALID_CARD_ORDER("INVALID_CARD_ORDER", "找不到門卡訂單"),
        INVALID_CARD_GATE("INVALID_CARD_GATE", "您的卡因為沒有此門的權限無法激活。"),
        INVALID_CARD_USER("INVALID_CARD_USER", "找不到門卡訂單"),
        SUCCESS("SUCCESS", "");

        final String name;
        final String description;

        VerifyOtpStatus(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription(){
            return description;
        }
    }
}

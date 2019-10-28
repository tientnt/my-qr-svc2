package com.am.common.utils;

import java.text.SimpleDateFormat;

public final class Constants {
    public final static String AUTH_TOKEN_HEADER = "Authorization";

    public static final String CONTENT_TYPE = "application/json";

    public final static String INVALID_REQUEST_PARAMS = "Invalid request parameters";

    public final static String INVALID_VOUCHER = "Invalid Voucher";

    public final static String INVALID_VOUCHER_USED = "Invalid Voucher - Code has been used";

    public final static String INVALID_MULTIPLE_SVC = "Multiple Services %s: %s - %s";

    public final static String INVALID_SVC_TYPE = "Invalid svc type";

    public final static String INVALID_CODE = "Invalid Code";

    public final static String EXISTED_CODE = "Code has been existed in our system";

    public final static String SUCCESS = "Success";

    public static final String REDEEM_SUCCESS = "SUCCESS";

    public final static String SVC_DOOR_ACCESS = "door-access";

    public final static String SVC_EVOUCHER = "evoucher";

    public final static String HPB_SERVICE = "hpb";

    public final static String ULIVE_SERVICE = "ulive";

    public enum QrStatus {
        NEW("new"),
        USED("used");

        private final String value;

        QrStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ProcessCodeType {
        VALIDATE("validate"),
        REDEEM("redeem");

        private final String value;

        ProcessCodeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}

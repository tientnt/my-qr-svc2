package com.am.common.utils;

import java.sql.Timestamp;

public class DateHelper {
    public static Timestamp min(Timestamp a, Timestamp b) {
        return a == null ? b : (b == null ? a : (a.before(b) ? a : b));
    }

    public static Timestamp max(Timestamp a, Timestamp b) {
        return a == null ? b : (b == null ? a : (a.after(b) ? a : b));
    }
}

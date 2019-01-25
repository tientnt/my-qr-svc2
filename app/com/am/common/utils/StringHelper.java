package com.am.common.utils;

public class StringHelper {
    public static String toString(Object obj) {
        if (null == obj) {
            return null;
        }
        return obj.toString();
    }
}

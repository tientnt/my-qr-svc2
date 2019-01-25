package com.am.common.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CommonData {

    private Map<String, Boolean> boolMap;

    private Map<String, Integer> intMap;

    private Map<String, Long> longMap;

    private Map<String, BigDecimal> decimalMap;

    private Map<String, String> strMap;

    private Map<String, Object> dataMap;

    public CommonData() {
        this.boolMap = new HashMap<>();
        this.intMap = new HashMap<>();
        this.longMap = new HashMap<>();
        this.decimalMap = new HashMap<>();
        this.strMap = new HashMap<>();
        this.dataMap = new HashMap<>();
    }

    public Map<String, Boolean> getBoolMap() {
        return boolMap;
    }

    public void setBoolMap(Map<String, Boolean> boolMap) {
        this.boolMap = boolMap;
    }

    public Map<String, Integer> getIntMap() {
        return intMap;
    }

    public void setIntMap(Map<String, Integer> intMap) {
        this.intMap = intMap;
    }

    public Map<String, Long> getLongMap() {
        return longMap;
    }

    public void setLongMap(Map<String, Long> longMap) {
        this.longMap = longMap;
    }

    public Map<String, BigDecimal> getDecimalMap() {
        return decimalMap;
    }

    public void setDecimalMap(Map<String, BigDecimal> decimalMap) {
        this.decimalMap = decimalMap;
    }

    public Map<String, String> getStrMap() {
        return strMap;
    }

    public void setStrMap(Map<String, String> strMap) {
        this.strMap = strMap;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }
}

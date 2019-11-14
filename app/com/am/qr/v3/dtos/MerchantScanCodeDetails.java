package com.am.qr.v3.dtos;

public class MerchantScanCodeDetails {
    private Long outletId;

    private Long templateId;

    private Long outletAppUserId;

    private Boolean isReserve;

    private Long merchantId;

    private String merchantName;

    private String customOutletCode;

    private String customMerchantPayeeName;

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getOutletAppUserId() {
        return outletAppUserId;
    }

    public void setOutletAppUserId(Long outletAppUserId) {
        this.outletAppUserId = outletAppUserId;
    }

    public Boolean getReserve() {
        return isReserve;
    }

    public void setReserve(Boolean reserve) {
        isReserve = reserve;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCustomOutletCode() {
        return customOutletCode;
    }

    public void setCustomOutletCode(String customOutletCode) {
        this.customOutletCode = customOutletCode;
    }

    public String getCustomMerchantPayeeName() {
        return customMerchantPayeeName;
    }

    public void setCustomMerchantPayeeName(String customMerchantPayeeName) {
        this.customMerchantPayeeName = customMerchantPayeeName;
    }
}

package com.am.qr.v3.dtos;

public class MerchantMintVoucherRequest {
    private String merchantVoucherType;

    private String qrCode;

    private String customCode;

    public String getMerchantVoucherType() {
        return merchantVoucherType;
    }

    public void setMerchantVoucherType(String merchantVoucherType) {
        this.merchantVoucherType = merchantVoucherType;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }
}

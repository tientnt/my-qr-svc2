package com.am.qr.v3.dtos;

public class MerchantMintVoucherRequest {
    private String merchantVoucherType;

    private String qrCode;

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
}

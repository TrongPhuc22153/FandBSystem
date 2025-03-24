package com.phucx.phucxfoodshop.model;

public class ShippingInfoBuilder{
    private Integer serviceId;
    private Integer insuranceValue;
    private String coupon;
    private String toWardCode;
    private Integer toDistrictId;
    private Integer fromDistrictId;
    private Integer height;
    private Integer length;
    private Integer weight;
    private Integer width;

    public ShippingInfoBuilder withServiceId(Integer serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public ShippingInfoBuilder withInsuranceValue(Integer insuranceValue) {
        this.insuranceValue = insuranceValue;
        return this;
    }

    public ShippingInfoBuilder withCoupon(String coupon) {
        this.coupon = coupon;
        return this;
    }

    public ShippingInfoBuilder withToWardCode(String toWardCode) {
        this.toWardCode = toWardCode;
        return this;
    }

    public ShippingInfoBuilder withToDistrictId(Integer toDistrictId) {
        this.toDistrictId = toDistrictId;
        return this;
    }

    public ShippingInfoBuilder withFromDistrictId(Integer fromDistrictId) {
        this.fromDistrictId = fromDistrictId;
        return this;
    }

    public ShippingInfoBuilder withHeight(Integer height) {
        this.height = height;
        return this;
    }

    public ShippingInfoBuilder withLength(Integer length) {
        this.length = length;
        return this;
    }

    public ShippingInfoBuilder withWeight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public ShippingInfoBuilder withWidth(Integer width) {
        this.width = width;
        return this;
    }

    public ShippingInfo build() {
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setServiceId(serviceId);
        shippingInfo.setCoupon(coupon);
        shippingInfo.setInsuranceValue(insuranceValue);
        shippingInfo.setFromDistrictId(fromDistrictId);
        shippingInfo.setToDistrictId(toDistrictId);
        shippingInfo.setToWardCode(toWardCode);
        shippingInfo.setHeight(height);
        shippingInfo.setWeight(weight);
        shippingInfo.setLength(length);
        shippingInfo.setWidth(width);
        return shippingInfo;
    }
}
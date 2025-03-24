package com.phucx.phucxfoodshop.model;

public class LocationBuilder {
    private String address;
    private String city;
    private String district;
    private String ward;
    private Integer cityId;
    private Integer districtId;
    private String wardCode;

    public LocationBuilder withAddress(String address){
        this.address = address;
        return this;
    }

    public LocationBuilder withCity(String city){
        this.city = city;
        return this;
    }

    public LocationBuilder withDistrict(String district){
        this.district = district;
        return this;
    }

    public LocationBuilder withWardCode(String wardCode){
        this.wardCode = wardCode;
        return this;
    }

    public LocationBuilder withCityId(Integer cityId){
        this.cityId = cityId;
        return this;
    }

    public LocationBuilder withDistrictId(Integer districtId){
        this.districtId = districtId;
        return this;
    }

    public LocationBuilder withWard(String ward){
        this.ward = ward;
        return this;
    }
    
    public Location build(){
        Location location = new Location();
        location.setAddress(address);
        location.setCity(city);
        location.setDistrict(district);
        location.setWard(ward);
        location.setCityId(cityId);
        location.setDistrictId(districtId);
        location.setWardCode(wardCode);
        return location;
    }
}

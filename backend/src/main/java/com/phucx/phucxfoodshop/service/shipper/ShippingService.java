package com.phucx.phucxfoodshop.service.shipper;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.exceptions.EmptyCartException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.dto.District;
import com.phucx.phucxfoodshop.model.dto.Location;
import com.phucx.phucxfoodshop.model.dto.Province;
import com.phucx.phucxfoodshop.model.dto.ShippingResponse;
import com.phucx.phucxfoodshop.model.dto.Ward;

public interface ShippingService {
    // estimate shipping cost
    public ShippingResponse costEstimate(Integer userCityID, Integer 
        userDistrictID, String userWardCode, String username, String encodedCartJson) 
        throws NotFoundException, JsonProcessingException, EmptyCartException, InvalidOrderException;
    public List<Province> getProvinces();
    public List<District> getDistricts(Integer provinceID);
    public List<Ward> getWards(Integer districtID);
    public Location getStoreLocation() throws NotFoundException;
}

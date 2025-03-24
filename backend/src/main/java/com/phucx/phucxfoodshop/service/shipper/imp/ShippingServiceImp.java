package com.phucx.phucxfoodshop.service.shipper.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.config.GHNProperties;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmptyCartException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.District;
import com.phucx.phucxfoodshop.model.Location;
import com.phucx.phucxfoodshop.model.LocationBuilder;
import com.phucx.phucxfoodshop.model.OrderItem;
import com.phucx.phucxfoodshop.model.OrderWithProducts;
import com.phucx.phucxfoodshop.model.ProductSize;
import com.phucx.phucxfoodshop.model.Province;
import com.phucx.phucxfoodshop.model.ShippingInfo;
import com.phucx.phucxfoodshop.model.ShippingInfoBuilder;
import com.phucx.phucxfoodshop.model.ShippingResponse;
import com.phucx.phucxfoodshop.model.UserProfile;
import com.phucx.phucxfoodshop.model.Ward;
import com.phucx.phucxfoodshop.service.cart.CartService;
import com.phucx.phucxfoodshop.service.currency.CurrencyService;
import com.phucx.phucxfoodshop.service.product.ProductSizeService;
import com.phucx.phucxfoodshop.service.shipper.ShippingService;
import com.phucx.phucxfoodshop.service.user.UserProfileService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShippingServiceImp implements ShippingService{
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GHNProperties shippingProperties;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ProductSizeService productSizeService;
    @Autowired
    private CartService cartService;

    private Integer getShippingCost(ShippingInfo shippingInfo) {
        log.info("getShippingCost(shippingInfo={})", shippingInfo);
        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", shippingProperties.getToken());
        headers.set("shop_id", shippingProperties.getShopId());

        // create body
        Map<String, Object> params = new HashMap<>();

        params.put("service_id", shippingInfo.getServiceId());
        params.put("insurance_value", shippingInfo.getInsuranceValue());
        params.put("coupon", shippingInfo.getCoupon());
        params.put("to_ward_code", shippingInfo.getToWardCode());
        params.put("to_district_id", shippingInfo.getToDistrictId());
        params.put("from_district_id", shippingInfo.getFromDistrictId());
        params.put("height", shippingInfo.getHeight());
        params.put("length", shippingInfo.getLength());
        params.put("weight", shippingInfo.getWeight());
        params.put("width", shippingInfo.getWidth());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
    
        ResponseEntity<Map> response = restTemplate.postForEntity(
            shippingProperties.getFeeUrl(), entity, Map.class);
        if(!response.getStatusCode().is2xxSuccessful()) return null;
        Map<String, Object> body = response.getBody();
        Map<String, Object> data = (Map<String, Object>) body.get("data");
        Integer total = (Integer) data.get("total");
        return total;
    }

    @Override
    public List<Province> getProvinces() {
        log.info("getProvinces()");
        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", shippingProperties.getToken());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            shippingProperties.getProvinceUrl(),
            HttpMethod.GET, 
            entity, 
            Map.class);

        if(!response.getStatusCode().is2xxSuccessful()) return null;
        Map<String, Object> body = response.getBody();
        List<Province> data =  (List<Province>) body.get("data");
        
        return data;
    }

    @Override
    public List<District> getDistricts(Integer provinceID) {
        log.info("getDistricts(provinceID={})", provinceID);
        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", shippingProperties.getToken());
        // create body
        Map<String, Object> params = new HashMap<>();
        params.put("province_id", provinceID);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
    
        ResponseEntity<Map> response = restTemplate.postForEntity(
            shippingProperties.getDistrictUrl(), entity, Map.class);
        if(!response.getStatusCode().is2xxSuccessful()) return null;
        Map<String, Object> body = response.getBody();
        List<District> data = (List<District> ) body.get("data");
        return data;
    }

    @Override
    public List<Ward> getWards(Integer districtID) {
        log.info("getWards(districtID={})", districtID);
        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", shippingProperties.getToken());
        // create body
        Map<String, Object> params = new HashMap<>();
        params.put("district_id", districtID);
        // request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
    
        ResponseEntity<Map> response = restTemplate.postForEntity(
            shippingProperties.getWardUrl(), entity, Map.class);
        if(!response.getStatusCode().is2xxSuccessful()) return null;
        Map<String, Object> body = response.getBody();
        List<Ward> data = (List<Ward> ) body.get("data");
        return data;
    }

    private List<ShippingService> getServices(Integer fromDistrictId, Integer toDistrictId){
        log.info("getServices(fromDistrictId={}, toDistrictId={})", fromDistrictId, toDistrictId);
        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", shippingProperties.getToken());
        // create body
        Map<String, Object> params = new HashMap<>();
        params.put("shop_id", Integer.valueOf(shippingProperties.getShopId()));
        params.put("from_district", fromDistrictId);
        params.put("to_district", toDistrictId);
        // request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
            shippingProperties.getServicesUrl(), entity, Map.class);
        if(!response.getStatusCode().is2xxSuccessful()) return null;
        Map<String, Object> body = response.getBody();
        List<ShippingService> data = (List<ShippingService>) body.get("data");
        return data;
    }

    @Override
    public Location getStoreLocation() throws NotFoundException {
        log.info("getStoreLocation()");
        String administrator = "another2001";
        UserProfile profile = userProfileService
            .getUserProfileByUsername(administrator);

        Integer provinceID = null;
        Integer districtID = null;
        String wardCode = null;
        List<?> provinces = this.getProvinces();
        for (Object object : provinces) {
            Map<String, Object> province = (Map<String, Object>) object;
            if(province.get("ProvinceName").toString().equalsIgnoreCase(profile.getCity())){
                provinceID = (Integer) province.get("ProvinceID");
            }
        }
        List<?> districts = this.getDistricts(provinceID);
        for (Object object : districts) {
            Map<String, Object> district = (Map<String, Object>) object;
            if(district.get("DistrictName").toString().equalsIgnoreCase(profile.getDistrict())){
                districtID = (Integer) district.get("DistrictID");
            }
        }
        List<?> wards = this.getWards(districtID);
        for (Object object : wards) {
            Map<String, Object> ward = (Map<String, Object>) object;
            if(ward.get("WardName").toString().equalsIgnoreCase(profile.getWard())){
                wardCode = ward.get("WardCode").toString();
            }
        }

        Location location = new LocationBuilder()
            .withAddress(profile.getAddress())
            .withWard(profile.getWard())
            .withDistrict(profile.getWard())
            .withCity(profile.getCity())
            .withWardCode(wardCode)
            .withDistrictId(districtID)
            .withCityId(provinceID)
            .build();
        return location;
    }

    @Override
    public ShippingResponse costEstimate(Integer userCityID, Integer userDistrictID, 
        String userWardCode, String username, String encodedCartJson
    ) throws NotFoundException, JsonProcessingException, CustomerNotFoundException, EmptyCartException, InvalidOrderException {
        log.info("costEstimate(userDistrictID={})", userDistrictID);
        // get store location
        Location storeLocation = this.getStoreLocation();
        List<?> shippingServices = this.getServices(
            storeLocation.getDistrictId(), userDistrictID);
        // get user's order
        OrderWithProducts order = this.cartService.getOrder(encodedCartJson, username);
        // exchange total price
        String price = currencyService.exchangeRateFromUSDToVND(
            order.getTotalPrice().doubleValue());
        // get product sizes
        Integer totalWeight = 0;
        Integer totalHeight = 0;
        Integer totalLength = 0;
        Integer totalWidth = 0;
        for(OrderItem item : order.getProducts()) {
            ProductSize productSizeInfo = productSizeService.getProductSize(item.getProductID());
            totalWeight += item.getQuantity()*productSizeInfo.getWeight();
            totalHeight += item.getQuantity()*productSizeInfo.getHeight();
            totalLength += item.getQuantity()*productSizeInfo.getLength();
            totalWidth += item.getQuantity()*productSizeInfo.getWidth();
        }
        // get shipping services
        List<Map<String, Object>> services = shippingServices.stream()
            .map(service -> (Map<String, Object>) service)
            .collect(Collectors.toList());
        Integer serviceID = (Integer)services.get(0).get("service_id");
        // get shipping cost
        ShippingInfo shippingInfo = new ShippingInfoBuilder()
            .withServiceId(serviceID)
            .withInsuranceValue(Integer.valueOf(price))
            .withFromDistrictId(storeLocation.getDistrictId())
            .withToDistrictId(userDistrictID)
            .withToWardCode(userWardCode)
            .withHeight(totalHeight)
            .withWeight(totalWeight)
            .withLength(totalLength)
            .withWidth(totalWidth)
            .build();
        Integer total = this.getShippingCost(shippingInfo);
        String totalUSD = currencyService.exchangeRateFromVNDToUSD(total.longValue());
        ShippingResponse response = new ShippingResponse(Double.valueOf(totalUSD));
        return response;
    }


    
}

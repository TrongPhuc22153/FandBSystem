package com.phucx.phucxfoodshop.service.shipper.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.model.Shipper;
import com.phucx.phucxfoodshop.repository.ShipperRepository;
import com.phucx.phucxfoodshop.service.shipper.ShipperService;

@Service
public class ShipperServiceImp implements ShipperService{
    @Autowired
    private ShipperRepository shipperRepository;

    @Override
    public Shipper getShipperByID(Integer shipperID) throws ShipperNotFoundException {
        return shipperRepository.findById(shipperID)
            .orElseThrow(()-> new ShipperNotFoundException("Shipper "+ shipperID + " does not found"));
    }
    
}

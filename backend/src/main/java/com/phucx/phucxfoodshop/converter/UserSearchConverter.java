package com.phucx.phucxfoodshop.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.phucx.phucxfoodshop.constant.UserSearch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserSearchConverter implements Converter<String, UserSearch> {

    @Override
    public UserSearch convert(String search) {
        try {
            return UserSearch.valueOf(search.toUpperCase());
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }
    
}

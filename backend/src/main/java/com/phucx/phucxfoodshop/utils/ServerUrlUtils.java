package com.phucx.phucxfoodshop.utils;

import jakarta.servlet.http.HttpServletRequest;

public class ServerUrlUtils {
    // get base url
    public static String getBaseUrl(HttpServletRequest request){
        String uri = request.getRequestURI().toString();
        String url = request.getRequestURL().toString();
        String baseurl = url.substring(0, url.length()-uri.length());
        return baseurl;
    }
}

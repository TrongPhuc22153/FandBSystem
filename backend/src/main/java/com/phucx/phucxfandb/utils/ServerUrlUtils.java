package com.phucx.phucxfandb.utils;

import jakarta.servlet.http.HttpServletRequest;

public class ServerUrlUtils {
    // get base url
    public static String getBaseUrl(HttpServletRequest request){
        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();
        return url.substring(0, url.length()-uri.length());
    }
}

package com.phucx.phucxfandb.utils;

import com.phucx.phucxfandb.constant.ApiEndpoint;
import com.phucx.phucxfandb.exception.ImageException;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class ImageUtils {

    public static String getCurrentUrl(String baseUrl) {
        String normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
        return normalizedBaseUrl + ApiEndpoint.IMAGES_ENDPOINT + "/";
    }

    public static String extractImageNameFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();

            String imageName = path.substring(path.lastIndexOf('/') + 1);
            if (imageName.isEmpty()) {
                throw new ImageException("No image name found in URL");
            }
            return imageName;
        } catch (URISyntaxException e) {
            throw new ImageException("Invalid URL format: " + url);
        }
    }
}

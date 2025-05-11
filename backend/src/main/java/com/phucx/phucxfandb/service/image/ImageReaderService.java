package com.phucx.phucxfandb.service.image;

import java.io.IOException;
import java.io.InputStream;

public interface ImageReaderService {
    String getMimeType(String imageName) throws IOException;
    String getImageUrl(String imageName);
    InputStream getImage(String imageName) throws IOException;
}

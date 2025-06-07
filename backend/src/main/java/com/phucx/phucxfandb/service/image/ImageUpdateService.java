package com.phucx.phucxfandb.service.image;

import com.phucx.phucxfandb.dto.response.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageUpdateService {
    // upload image
    ImageDTO uploadImage(MultipartFile file)  throws Exception;
    List<ImageDTO> uploadImages(MultipartFile[] files)  throws Exception;
    void removeImages(List<String> images);
}

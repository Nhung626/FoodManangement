package com.hivetech.service.implement;

import com.hivetech.entity.Media;
import com.hivetech.repository.MediaRepository;
import com.hivetech.service.interfaces.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class MediaServiceImp implements MediaService {
    @Value("${media.img_path}")
    private String imgPath;
    private final MediaRepository mediaRepository;

    public Media getMedia(Long id) {
        return mediaRepository.findById(id).orElse(null);
    }

    public String getPathImage(Long id) {
        Media media = mediaRepository.findById(id).orElse(null);
        String path = "";
        if (media != null) {
            path = imgPath + File.separator + media.getName();
        }
        return path;
    }
}

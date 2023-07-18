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
    @Value("${pocketbase.host}")
    private String host;
    private final MediaRepository mediaRepository;

    public Media getMedia(String id) {
        return mediaRepository.findById(id).orElse(null);
    }
    public void createMedia(Media media){
        mediaRepository.save(media);
    }
    public String getPathImage(String id) {
        Media media = mediaRepository.findById(id).orElse(null);
        String path = "";
        if (media != null) {
            path = String.format("%sapi/files/%s/%s/%s", host, media.getType(), media.getId(), media.getName());
        }
        return path;
    }
}

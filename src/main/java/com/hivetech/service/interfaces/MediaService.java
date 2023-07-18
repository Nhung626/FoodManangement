package com.hivetech.service.interfaces;

import com.hivetech.entity.Media;
import org.springframework.stereotype.Service;

@Service
public interface MediaService {
    Media getMedia(String id);
    void createMedia(Media media);
    String getPathImage(String id);
}

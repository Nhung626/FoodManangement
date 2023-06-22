package com.hivetech.service.interfaces;

import com.hivetech.entity.Media;
import org.springframework.stereotype.Service;

@Service
public interface MediaService {
    Media getMedia(Long id);

    String getPathImage(Long id);
}

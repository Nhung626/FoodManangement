package com.hivetech.controller;

import com.hivetech.entity.Media;
import com.hivetech.service.interfaces.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MediaController {
    final private MediaService mediaService;

    @PostMapping("/api/v1/media")
    public ResponseEntity createMedia(@RequestBody Media media){
        mediaService.createMedia(media);
        return ResponseEntity.ok().build();
    }
}

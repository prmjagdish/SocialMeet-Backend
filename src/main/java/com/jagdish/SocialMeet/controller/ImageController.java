package com.jagdish.SocialMeet.controller;

import com.jagdish.SocialMeet.service.impl.ImageUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {


    private final ImageUploadService imageUploadService;

    public ImageController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            String url = imageUploadService.uploadImage(file);
            return ResponseEntity.ok().body(Map.of("url", url, "message", "Image uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Upload failed", "error", e.getMessage()));
        }
    }
}

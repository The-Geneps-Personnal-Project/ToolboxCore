package com.toolbox.toolboxcore.controller;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.toolbox.toolboxcore.service.GrpcFormattingService;
import com.toolbox.toolboxcore.service.GrpcConvertingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/")
public class FileController {

    @Autowired
    private GrpcFormattingService grpcFormattingService;

    @Autowired
    private GrpcConvertingService grpcConvertingService;

    @PostMapping("/convert")
    public ResponseEntity<byte[]> processFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("format") String extension
    ) throws IOException {
        if (file.isEmpty() || extension.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        byte[] result;

        if (isVideoFile(file.getOriginalFilename())) {
            result = grpcConvertingService.convertFile(file, extension);
        } else {
            result = grpcFormattingService.convertFile(file, extension);
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=converted." + file.getOriginalFilename() + "." + extension + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(result);
    }

    private boolean isVideoFile(String filename) {
        String [] AvailableExtensions = {".mp4", ".ogg", ".mov", "mp3", "mkv", "avi", "wav", "mid", "aac", "webm"};
        return Arrays.stream(AvailableExtensions).anyMatch(filename::endsWith);
    }
}

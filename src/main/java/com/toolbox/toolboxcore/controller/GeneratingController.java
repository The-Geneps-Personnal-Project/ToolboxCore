package com.toolbox.toolboxcore.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.toolbox.toolboxcore.service.GrpcGeneratingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/generate")
public class GeneratingController {

    @Autowired
    private GrpcGeneratingService grpcGeneratingService;

    @GetMapping("/uid")
    public ResponseEntity<String> generateUID() throws IOException {
        String result = grpcGeneratingService.GenerateUID();

        return ResponseEntity.ok()
            .body(result);
    }

    @GetMapping("/random")
    public ResponseEntity<Integer> generateRandom(@RequestParam("min") int min, @RequestParam("max") int max) throws IOException {
        int result = grpcGeneratingService.GenerateRandom(min, max);

        return ResponseEntity.ok()
            .body(result);
    }

    @GetMapping("/password")
    public ResponseEntity<String> generatePassword(@RequestParam("length") String length, @RequestParam("special") Boolean SpecialCharacter) throws IOException {
        String result = grpcGeneratingService.GeneratePassword(length, SpecialCharacter);

        return ResponseEntity.ok()
            .body(result);
    }

    @GetMapping("/qrcode")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam("data") String data) throws IOException {
        byte[] result = grpcGeneratingService.GenerateQRCode(data);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=qrcode.png")
            .contentType(MediaType.IMAGE_PNG)
            .body(result);
    }

    @GetMapping("/barcode")
    public ResponseEntity<byte[]> generateBarcode(@RequestParam("data") String data) throws IOException {
        byte[] result = grpcGeneratingService.GenerateBarcode(data);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=barcode.png")
            .contentType(MediaType.IMAGE_PNG)
            .body(result);
    }

    @GetMapping("/convertTime")
    public ResponseEntity<String> convertTime(@RequestParam("time") String time, @RequestParam("from") String from, @RequestParam("to") String to) throws IOException {
        String result = grpcGeneratingService.ConvertTime(time, from, to);

        return ResponseEntity.ok()
            .body(result);
    }

}

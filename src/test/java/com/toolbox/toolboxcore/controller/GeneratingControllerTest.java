package com.toolbox.toolboxcore.controller;

import com.toolbox.toolboxcore.service.GrpcGeneratingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GeneratingController.class)
public class GeneratingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GrpcGeneratingService grpcGeneratingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for /generate/uid
    @Test
    void testGenerateUID_Success() throws Exception {
        when(grpcGeneratingService.GenerateUID()).thenReturn("generated-uid");

        mockMvc.perform(get("/api/generate/uid"))
            .andExpect(status().isOk())
            .andExpect(content().string("generated-uid"));
    }

    @Test
    void testGenerateUID_Error() throws Exception {
        when(grpcGeneratingService.GenerateUID()).thenThrow(new RuntimeException("gRPC failed"));

        mockMvc.perform(get("/api/generate/uid"))
            .andExpect(status().isInternalServerError());
    }

    // Test for /generate/password
    @Test
    void testGeneratePassword_Success() throws Exception {
        when(grpcGeneratingService.GeneratePassword("12", true)).thenReturn("password123!");

        mockMvc.perform(get("/api/generate/password")
                .param("length", "12")
                .param("special", "true"))
            .andExpect(status().isOk())
            .andExpect(content().string("password123!"));
    }

    @Test
    void testGeneratePassword_Error() throws Exception {
        when(grpcGeneratingService.GeneratePassword("12", true))
            .thenThrow(new RuntimeException("gRPC failed"));

        mockMvc.perform(get("/api/generate/password")
                .param("length", "12")
                .param("special", "true"))
            .andExpect(status().isInternalServerError());
    }

    // Test for /generate/qrcode
    @Test
    void testGenerateQRCode_Success() throws Exception {
        byte[] mockImage = new byte[]{1, 2, 3};
        when(grpcGeneratingService.GenerateQRCode("sample-data")).thenReturn(mockImage);

        mockMvc.perform(get("/api/generate/qrcode")
                .param("data", "sample-data"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.IMAGE_PNG))
            .andExpect(header().string("Content-Disposition", "attachment; filename=qrcode.png"))
            .andExpect(content().bytes(mockImage));
    }

    @Test
    void testGenerateQRCode_Error() throws Exception {
        when(grpcGeneratingService.GenerateQRCode("sample-data"))
            .thenThrow(new RuntimeException("gRPC failed"));
    
        mockMvc.perform(get("/api/generate/qrcode")
                .param("data", "sample-data"))
            .andExpect(status().isInternalServerError())  // Test for 500 status
            .andExpect(content().string("Internal Server Error: gRPC failed"));
    }
    

    // Test for /generate/barcode
    @Test
    void testGenerateBarcode_Success() throws Exception {
        byte[] mockImage = new byte[]{4, 5, 6};
        when(grpcGeneratingService.GenerateBarcode("123456")).thenReturn(mockImage);

        mockMvc.perform(get("/api/generate/barcode")
                .param("data", "123456"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.IMAGE_PNG))
            .andExpect(header().string("Content-Disposition", "attachment; filename=barcode.png"))
            .andExpect(content().bytes(mockImage));
    }

    @Test
    void testGenerateBarcode_Error() throws Exception {
        when(grpcGeneratingService.GenerateBarcode("123456"))
            .thenThrow(new RuntimeException("gRPC failed"));

        mockMvc.perform(get("/api/generate/barcode")
                .param("data", "123456"))
            .andExpect(status().isInternalServerError());
    }

    // Test for /generate/random
    @Test
    void testGenerateRandom_Success() throws Exception {
        // Mock the gRPC service response
        when(grpcGeneratingService.GenerateRandom(1, 100)).thenReturn(42);
    
        // Perform the request with `min` and `max` parameters
        mockMvc.perform(get("/api/generate/random")
                .param("min", "1")   // Add the min param
                .param("max", "100")) // Add the max param
            .andExpect(status().isOk())             // Expect status 200 OK
            .andExpect(content().string("42"));     // Expect the returned value to be "42"
    }

    @Test
    void testGenerateRandom_Error() throws Exception {
        // Mock gRPC service to throw a RuntimeException
        when(grpcGeneratingService.GenerateRandom(1, 100))
            .thenThrow(new RuntimeException("gRPC failed"));
    
        // Perform the request and expect 500 Internal Server Error
        mockMvc.perform(get("/api/generate/random")
                .param("min", "1")
                .param("max", "100"))
            .andExpect(status().isInternalServerError())  // Expect 500
            .andExpect(content().string("Internal Server Error: gRPC failed"));  // Check error message
    }
    

    // Test for /generate/convertTime
    @Test
    void testConvertTime_Success() throws Exception {
        when(grpcGeneratingService.ConvertTime("2023-10-19 08:45", "yyyy-MM-dd HH:mm", "dd/MM/yyyy HH:mm"))
            .thenReturn("19/10/2023 08:45");

        mockMvc.perform(get("/api/generate/convertTime")
                .param("time", "2023-10-19 08:45")
                .param("from", "yyyy-MM-dd HH:mm")
                .param("to", "dd/MM/yyyy HH:mm"))
            .andExpect(status().isOk())
            .andExpect(content().string("19/10/2023 08:45"));
    }

    @Test
    void testConvertTime_Error() throws Exception {
        when(grpcGeneratingService.ConvertTime("2023-10-19 08:45", "yyyy-MM-dd HH:mm", "dd/MM/yyyy HH:mm"))
            .thenThrow(new RuntimeException("gRPC failed"));

        mockMvc.perform(get("/api/generate/convertTime")
                .param("time", "2023-10-19 08:45")
                .param("from", "yyyy-MM-dd HH:mm")
                .param("to", "dd/MM/yyyy HH:mm"))
            .andExpect(status().isInternalServerError());
    }
}

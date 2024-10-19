package com.toolbox.toolboxcore.controller;

import com.toolbox.toolboxcore.service.GrpcFormattingService;
import com.toolbox.toolboxcore.service.GrpcConvertingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class FileControllerTest {

    @Mock
    private GrpcFormattingService grpcFormattingService;

    @Mock
    private GrpcConvertingService grpcConvertingService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessFileWithVideoFormat() throws IOException {
        MultipartFile mockFile = new MockMultipartFile("file", "video.mp4", "video/mp4", "video content".getBytes());
        String targetExtension = "ogg";

        // Mock response from gRPC converting service
        byte[] mockResponse = new byte[]{1, 2, 3};  // Simulate converted video file
        when(grpcConvertingService.convertFile(mockFile, targetExtension)).thenReturn(mockResponse);

        ResponseEntity<byte[]> responseEntity = fileController.processFile(mockFile, targetExtension);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
        assertEquals("attachment; filename=converted.video.mp4.ogg\"", responseEntity.getHeaders().getFirst("Content-Disposition"));
        assertEquals("application/octet-stream", responseEntity.getHeaders().getContentType().toString());
    }

    @Test
    public void testProcessFileWithDocumentFormat() throws IOException {
        MultipartFile mockFile = new MockMultipartFile("file", "document.pdf", "application/pdf", "document content".getBytes());
        String targetExtension = "txt";

        // Mock response from gRPC formatting service
        byte[] mockResponse = new byte[]{4, 5, 6};  // Simulate formatted document
        when(grpcFormattingService.convertFile(mockFile, targetExtension)).thenReturn(mockResponse);

        ResponseEntity<byte[]> responseEntity = fileController.processFile(mockFile, targetExtension);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
        assertEquals("attachment; filename=converted.document.pdf.txt\"", responseEntity.getHeaders().getFirst("Content-Disposition"));
        assertEquals("application/octet-stream", responseEntity.getHeaders().getContentType().toString());
    }

    @Test
    public void testInvalidFormatParameter() throws IOException {
        MultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        String targetExtension = "";

        ResponseEntity<byte[]> responseEntity = fileController.processFile(mockFile, targetExtension);

        // Simulate returning bad request for invalid format
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }
}

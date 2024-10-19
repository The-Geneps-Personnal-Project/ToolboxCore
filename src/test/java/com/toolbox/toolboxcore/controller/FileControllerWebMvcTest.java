package com.toolbox.toolboxcore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.toolbox.toolboxcore.service.GrpcFormattingService;
import com.toolbox.toolboxcore.service.GrpcConvertingService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FileController.class)
public class FileControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GrpcFormattingService grpcFormattingService;

    @MockBean
    private GrpcConvertingService grpcConvertingService;

    @Test
    public void testProcessFileWithValidVideoFormat() throws Exception {
        MockMultipartFile mockVideoFile = new MockMultipartFile("file", "video.mp4", "video/mp4", "video content".getBytes());
        String targetExtension = "ogg";
        byte[] mockResponse = new byte[]{1, 2, 3};

        when(grpcConvertingService.convertFile(mockVideoFile, targetExtension)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/convert")
                .file(mockVideoFile)
                .param("format", targetExtension)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=converted.video.mp4.ogg\""))
            .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    public void testProcessFileWithValidDocumentFormat() throws Exception {
        MockMultipartFile mockDocFile = new MockMultipartFile("file", "document.pdf", "application/pdf", "document content".getBytes());
        String targetExtension = "txt";
        byte[] mockResponse = new byte[]{4, 5, 6};

        when(grpcFormattingService.convertFile(mockDocFile, targetExtension)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/convert")
                .file(mockDocFile)
                .param("format", targetExtension)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=converted.document.pdf.txt\""))
            .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    public void testProcessFileWithInvalidFormat() throws Exception {
        MockMultipartFile mockInvalidFile = new MockMultipartFile("file", "invalid.xyz", "application/xyz", "invalid content".getBytes());
        String targetExtension = "unknown";

        // Assuming you would handle this with a BadRequest or some error handling logic
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/convert")
                .file(mockInvalidFile)
                .param("format", targetExtension)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk());  // Or expect some error status
    }
}

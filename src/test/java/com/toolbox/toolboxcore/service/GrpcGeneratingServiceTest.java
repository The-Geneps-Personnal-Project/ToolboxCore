package com.toolbox.toolboxcore.service;

import com.toolbox.grpc.GeneratingServiceGrpc.GeneratingServiceBlockingStub;
import com.toolbox.grpc.GeneratingServiceOuterClass.*;
import com.toolbox.toolboxcore.service.GrpcGeneratingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GrpcGeneratingServiceTest {

    @Mock
    private GeneratingServiceBlockingStub grpcClient;

    @InjectMocks
    private GrpcGeneratingService grpcGeneratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for GenerateUID
    @Test
    void testGenerateUID_Success() {
        UIDResponse mockResponse = UIDResponse.newBuilder()
            .setUid("generated-uid")
            .build();
        when(grpcClient.generateUID(any(EmptyRequest.class))).thenReturn(mockResponse);

        String result = grpcGeneratingService.GenerateUID();
        assertEquals("generated-uid", result);
    }

    @Test
    void testGenerateUID_Error() {
        when(grpcClient.generateUID(any(EmptyRequest.class)))
            .thenThrow(new RuntimeException("gRPC service failed"));
        assertThrows(RuntimeException.class, () -> grpcGeneratingService.GenerateUID());
    }

    // Test for GeneratePassword
    @Test
    void testGeneratePassword_Success() {
        PasswordResponse mockResponse = PasswordResponse.newBuilder()
            .setPassword("password123!")
            .build();
        when(grpcClient.generatePassword(any(PasswordRequest.class))).thenReturn(mockResponse);

        String result = grpcGeneratingService.GeneratePassword("12", true);
        assertEquals("password123!", result);
    }

    @Test
    void testGeneratePassword_Error() {
        when(grpcClient.generatePassword(any(PasswordRequest.class)))
            .thenThrow(new RuntimeException("gRPC service failed"));

        assertThrows(RuntimeException.class, () -> grpcGeneratingService.GeneratePassword("12", true));
    }

    // Test for GenerateQRCode
    @Test
    void testGenerateQRCode_Success() {
        QRCodeResponse mockResponse = QRCodeResponse.newBuilder()
            .setImage(com.google.protobuf.ByteString.copyFrom(new byte[]{1, 2, 3}))
            .build();
        when(grpcClient.generateQRCode(any(QRCodeRequest.class))).thenReturn(mockResponse);

        byte[] result = grpcGeneratingService.GenerateQRCode("sample-data");
        assertArrayEquals(new byte[]{1, 2, 3}, result);
    }

    @Test
    void testGenerateQRCode_Error() {
        when(grpcClient.generateQRCode(any(QRCodeRequest.class)))
            .thenThrow(new RuntimeException("gRPC service failed"));

        assertThrows(RuntimeException.class, () -> grpcGeneratingService.GenerateBarcode("sample-data"));
    }

    // Test for GenerateBarcode
    @Test
    void testGenerateBarcode_Success() {
        BarcodeResponse mockResponse = BarcodeResponse.newBuilder()
            .setImage(com.google.protobuf.ByteString.copyFrom(new byte[]{1, 2, 3}))
            .build();
        when(grpcClient.generateBarcode(any(BarcodeRequest.class))).thenReturn(mockResponse);

        byte[] result = grpcGeneratingService.GenerateBarcode("123456789");
        assertArrayEquals(new byte[]{1, 2, 3}, result);
    }

    @Test
    void testGenerateBarcode_Error() {
        when(grpcClient.generateBarcode(any(BarcodeRequest.class)))
            .thenThrow(new RuntimeException("gRPC service failed"));

        assertThrows(RuntimeException.class, () -> grpcGeneratingService.GenerateBarcode("123456789"));
    }

    // Test for GenerateRandom
    @Test
    void testGenerateRandom_Success() {
        RandomResponse mockResponse = RandomResponse.newBuilder()
            .setRandom(42)
            .build();
        when(grpcClient.generateRandom(any(RandomRequest.class))).thenReturn(mockResponse);

        int result = grpcGeneratingService.GenerateRandom(1, 100);
        assertEquals(42, result);
    }

    @Test
    void testGenerateRandom_Error() {
        when(grpcClient.generateRandom(any(RandomRequest.class)))
            .thenThrow(new RuntimeException("gRPC service failed"));

        assertThrows(RuntimeException.class, () -> grpcGeneratingService.GenerateRandom(1, 100));
    }

    // Test for ConvertTime
    @Test
    void testConvertTime_FormatSuccess() {
        // Example: Converting from "2005-06-07" to "07-06-2005" format
        TimeConversionResponse mockResponse = TimeConversionResponse.newBuilder().setConvertedTime("07-06-2005").build();
        when(grpcClient.convertTime(any(TimeConversionRequest.class))).thenReturn(mockResponse);
    
        String result = grpcGeneratingService.ConvertTime("2005-06-07", "yyyy-MM-dd", "dd-MM-yyyy");
        assertEquals("07-06-2005", result);
    }
    
    @Test
    void testConvertTime_FormatWithTimeSuccess() {
        // Example: Converting from "08:45:25" to "08h45m25s"
        TimeConversionResponse mockResponse = TimeConversionResponse.newBuilder().setConvertedTime("08h45m25s").build();
        when(grpcClient.convertTime(any(TimeConversionRequest.class))).thenReturn(mockResponse);
    
        String result = grpcGeneratingService.ConvertTime("08:45:25", "HH:mm:ss", "HH'h'mm'm'ss's'");
        assertEquals("08h45m25s", result);
    }
    
    @Test
    void testConvertTime_ISO8601Success() {
        // Example: Converting from ISO 8601 "1977-04-01T14:00:30" to "04/01/1977 14:00"
        TimeConversionResponse mockResponse = TimeConversionResponse.newBuilder().setConvertedTime("04/01/1977 14:00").build();
        when(grpcClient.convertTime(any(TimeConversionRequest.class))).thenReturn(mockResponse);
    
        String result = grpcGeneratingService.ConvertTime("1977-04-01T14:00:30", "yyyy-MM-dd'T'HH:mm:ss", "MM/dd/yyyy HH:mm");
        assertEquals("04/01/1977 14:00", result);
    }
    
    @Test
    void testConvertTime_WithTimeZoneSuccess() {
        // Example: Converting from "1901-01-01T00:00:01-04:00" to "01/01/1901 00:00 -04:00"
        TimeConversionResponse mockResponse = TimeConversionResponse.newBuilder().setConvertedTime("01/01/1901 00:00 -04:00").build();
        when(grpcClient.convertTime(any(TimeConversionRequest.class))).thenReturn(mockResponse);
    
        String result = grpcGeneratingService.ConvertTime("1901-01-01T00:00:01-04:00", "yyyy-MM-dd'T'HH:mm:ssXXX", "MM/dd/yyyy HH:mm XXX");
        assertEquals("01/01/1901 00:00 -04:00", result);
    }
    
}

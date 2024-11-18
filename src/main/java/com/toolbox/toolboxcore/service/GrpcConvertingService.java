package com.toolbox.toolboxcore.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.toolbox.grpc.FormattingServiceGrpc.FormattingServiceBlockingStub;
import com.toolbox.grpc.FormatRequest;
import com.toolbox.grpc.FormatResponse;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class GrpcConvertingService {

    @GrpcClient("convertingService")
    private FormattingServiceBlockingStub grpcClient;

    public byte[] convertFile(MultipartFile file, String extension) throws IOException {
        // Convert MultipartFile to byte array
        byte[] fileBytes = file.getBytes();

        // Create the gRPC request
        FormatRequest request = FormatRequest.newBuilder()
                .setFileBytes(com.google.protobuf.ByteString.copyFrom(fileBytes)) // Send file as bytes
                .setFormat(extension)
                .build();

        // Call the gRPC service
        FormatResponse response = grpcClient.convertFile(request);

        // Return the converted file bytes from the gRPC response
        return response.getFileBytes().getBytes();
    }
}
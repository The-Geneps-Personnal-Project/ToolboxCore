package com.toolbox.toolboxcore.service;

import org.springframework.stereotype.Service;
import com.toolbox.grpc.GeneratingServiceGrpc;
import com.toolbox.grpc.GeneratingServiceOuterClass.EmptyRequest;
import com.toolbox.grpc.GeneratingServiceOuterClass.UIDResponse;
import com.toolbox.grpc.GeneratingServiceOuterClass.PasswordRequest;
import com.toolbox.grpc.GeneratingServiceOuterClass.PasswordResponse;
import com.toolbox.grpc.GeneratingServiceOuterClass.QRCodeRequest;
import com.toolbox.grpc.GeneratingServiceOuterClass.QRCodeResponse;
import com.toolbox.grpc.GeneratingServiceOuterClass.BarcodeRequest;
import com.toolbox.grpc.GeneratingServiceOuterClass.BarcodeResponse;
import com.toolbox.grpc.GeneratingServiceOuterClass.RandomRequest;
import com.toolbox.grpc.GeneratingServiceOuterClass.RandomResponse;
import com.toolbox.grpc.GeneratingServiceOuterClass.TimeConversionRequest;
import com.toolbox.grpc.GeneratingServiceOuterClass.TimeConversionResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class GrpcGeneratingService {

    @GrpcClient("generatingService")
    private GeneratingServiceGrpc.GeneratingServiceBlockingStub grpcClient;

    public String GenerateUID() {
        // Create the gRPC request
        EmptyRequest request = EmptyRequest.newBuilder().build();

        // Call the gRPC service
        UIDResponse response = grpcClient.generateUID(request);

        return response.getUid();
    }

    public int GenerateRandom(int min, int max) {
        // Create the gRPC request
        RandomRequest request = RandomRequest.newBuilder()
                .setMin(min)
                .setMax(max)
                .build();

        // Call the gRPC service
        RandomResponse response = grpcClient.generateRandom(request);

        return response.getRandom();
    }

    public String GeneratePassword(String length, boolean hasSpecialChars) {
        // Create the gRPC request
        PasswordRequest request = PasswordRequest.newBuilder()
                .setLength(length)
                .setHasSpecialChars(hasSpecialChars)
                .build();

        // Call the gRPC service
        PasswordResponse response = grpcClient.generatePassword(request);

        return response.getPassword();
    }

    public byte[] GenerateQRCode(String data) {
        // Create the gRPC request
        QRCodeRequest request = QRCodeRequest.newBuilder()
                .setData(data)
                .build();

        // Call the gRPC service
        QRCodeResponse response = grpcClient.generateQRCode(request);

        return response.getImage().toByteArray();
    }

    public byte[] GenerateBarcode(String data) {
        // Create the gRPC request
        BarcodeRequest request = BarcodeRequest.newBuilder()
                .setData(data)
                .build();

        // Call the gRPC service
        BarcodeResponse response = grpcClient.generateBarcode(request);

        return response.getImage().toByteArray();
    }

    public String ConvertTime(String time, String from, String to) {
        // Create the gRPC request
        TimeConversionRequest request = TimeConversionRequest.newBuilder()
                .setTime(time)
                .setFrom(from)
                .setTo(to)
                .build();

        // Call the gRPC service
        TimeConversionResponse response = grpcClient.convertTime(request);

        return response.getConvertedTime();
    }
}

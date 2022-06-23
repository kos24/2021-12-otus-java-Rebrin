package ru.otus.protobuf;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.otus.protobuf.service.StreamValueServiceImpl;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8081;

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(new StreamValueServiceImpl())
                .build();
        server.start();
        System.out.println("Server started...");
        server.awaitTermination();
    }
}

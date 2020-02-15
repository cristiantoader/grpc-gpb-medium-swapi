package org.ctoader.medium;

import io.grpc.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SwapiGrpcServerRunner implements ApplicationRunner {

    private final Server nettyServer;

    @Autowired
    public SwapiGrpcServerRunner(Server nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server since JVM is shutting down");
            SwapiGrpcServerRunner.this.nettyServer.shutdown();
            System.err.println("Server shut down");
        }));

        nettyServer.awaitTermination();
    }
}

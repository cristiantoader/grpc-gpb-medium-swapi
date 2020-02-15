package com.ctoader.medium;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.ctoader.medium.CharacterServiceGrpc;
import org.ctoader.medium.PlanetServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SwapiGrpcClientRunner implements ApplicationRunner {

    @Value("${grpc-client.host:localhost}")
    private String host;

    @Value("${grpc-client.port:9111}")
    private Integer port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ManagedChannel nettyChannel = NettyChannelBuilder.forAddress(this.host, this.port)
                .usePlaintext()
                .build();

        PlanetServiceGrpc.PlanetServiceBlockingStub planetService = PlanetServiceGrpc.newBlockingStub(nettyChannel);
        planetService.findAll(Empty.getDefaultInstance())
                     .forEachRemaining(planet -> log.info("Received planet {}.", planet));


        CharacterServiceGrpc.CharacterServiceBlockingStub characterService = CharacterServiceGrpc.newBlockingStub(nettyChannel);
        characterService.findAll(Empty.getDefaultInstance())
                        .forEachRemaining(character -> log.info("Received character {}.", character));
    }
}

package org.ctoader.medium.planet;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.ctoader.medium.PlanetServiceGrpc;
import org.ctoader.medium.PlanetWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SwapiPlanetGrpcApi extends PlanetServiceGrpc.PlanetServiceImplBase {

    private final SwapiPlanetService swapiPlanetService;

    @Autowired
    public SwapiPlanetGrpcApi(SwapiPlanetService swapiPlanetService) {
        this.swapiPlanetService = swapiPlanetService;
    }

    @Override
    public void findAll(Empty request, StreamObserver<PlanetWrapper.Planet> responseObserver) {
        log.info("Received request to find all planets.");
        swapiPlanetService.findAll()
                          .forEach(responseObserver::onNext);

        log.info("Successfully completed request to find all planets.");
        responseObserver.onCompleted();
    }
}

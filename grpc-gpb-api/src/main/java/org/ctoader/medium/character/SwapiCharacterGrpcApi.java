package org.ctoader.medium.character;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.ctoader.medium.CharacterServiceGrpc;
import org.ctoader.medium.CharacterWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SwapiCharacterGrpcApi extends CharacterServiceGrpc.CharacterServiceImplBase {

    private final SwapiCharacterService swapiCharacterService;

    @Autowired
    public SwapiCharacterGrpcApi(SwapiCharacterService swapiCharacterService) {
        this.swapiCharacterService = swapiCharacterService;
    }

    @Override
    public void findAll(Empty request, StreamObserver<CharacterWrapper.Character> responseObserver) {
        log.info("Receiver request to find all characters.");
        swapiCharacterService.findAll()
                             .forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }

    @Override
    public void findByFilter(CharacterWrapper.CharacterFilter filter, StreamObserver<CharacterWrapper.Character> responseObserver) {
        log.info("Received request to find characters for filter {}.", filter);
        swapiCharacterService.findAllByFilter(filter)
                             .forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }
}

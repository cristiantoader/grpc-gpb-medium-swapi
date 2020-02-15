package org.ctoader.medium.planet;

import com.google.protobuf.AbstractMessageLite;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.ctoader.medium.PlanetWrapper;
import org.ctoader.medium.ResponseAnalyser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class SwapiPlanetService {

    private static final String DATA_PLANETS_CSV = "data/planets.csv";

    private final ResponseAnalyser responseAnalyser;

    @Autowired
    public SwapiPlanetService(ResponseAnalyser responseAnalyser) {
        this.responseAnalyser = responseAnalyser;
    }

    public Stream<PlanetWrapper.Planet> findAll() {

        try (Reader reader = Files.newBufferedReader(Paths.get(DATA_PLANETS_CSV))) {
            CsvToBean<PlanetCsvBean> csvToBean = new CsvToBeanBuilder<PlanetCsvBean>(reader)
                    .withType(PlanetCsvBean.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(1)
                    .build();

            List<PlanetCsvBean> planets = csvToBean.parse();
            List<PlanetWrapper.Planet> planetsGpbs = planets
                    .stream()
                    .map(it -> convertToPlanetGpb(it))
                    .collect(Collectors.toList());

            long jsonResponseSize = responseAnalyser.getJsonResponseSize(planets);
            long gpbResponseSize = responseAnalyser.getGpbResponseSize(planetsGpbs, AbstractMessageLite::toByteArray);
            log.info("GPB {} vs JSON {} response bytes size.", gpbResponseSize, jsonResponseSize);

            return planetsGpbs.stream();

        } catch (IOException e) {
            log.error("", e);
            return Stream.empty();
        }

    }

    private static PlanetWrapper.Planet convertToPlanetGpb(PlanetCsvBean csvBean) {
        return PlanetWrapper.Planet.newBuilder()
                .setName(csvBean.getName())
                .setRotationPeriod(csvBean.getRotationPeriod())
                .setOrbitalPeriod(csvBean.getOrbitalPeriod())
                .setDiameter(csvBean.getDiameter())
                .addAllClimate(Arrays.asList(csvBean.getClimate().split(",")))
                .addAllGravity(Arrays.asList(csvBean.getGravity().split(",")))
                .addAllTerrain(Arrays.asList(csvBean.getTerrain().split(",")))
                .setSurfaceWater(csvBean.getSurfaceWater())
                .setPopulation(csvBean.getPopulation())
                .build();
    }
}

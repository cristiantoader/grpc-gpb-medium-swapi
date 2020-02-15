package org.ctoader.medium.character;

import com.google.protobuf.AbstractMessageLite;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ctoader.medium.CharacterWrapper;
import org.ctoader.medium.ResponseAnalyser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class SwapiCharacterService {

    private static final String DATA_CHARACTERS_CSV = "data/characters.csv";

    private final ResponseAnalyser responseAnalyser;

    public SwapiCharacterService(ResponseAnalyser responseAnalyser) {
        this.responseAnalyser = responseAnalyser;
    }

    public Stream<CharacterWrapper.Character> findAll() {
        try (Reader reader = Files.newBufferedReader(Paths.get(DATA_CHARACTERS_CSV))) {
            CsvToBean<CharacterCsvBean> csvToBean = new CsvToBeanBuilder<CharacterCsvBean>(reader)
                    .withType(CharacterCsvBean.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(1)
                    .build();

            List<CharacterCsvBean> characters = csvToBean.parse();

            List<CharacterWrapper.Character> charactersGpbs = characters.stream()
                                                                        .map(it -> convertToCharacterGpb(it))
                                                                        .collect(Collectors.toList());

            long jsonResponseSize = responseAnalyser.getJsonResponseSize(characters);
            long gpbResponseSize = responseAnalyser.getGpbResponseSize(charactersGpbs, AbstractMessageLite::toByteArray);

            log.info("GPB {} vs JSON {} response bytes size.", gpbResponseSize, jsonResponseSize);

            return charactersGpbs.stream();

        } catch (IOException e) {
            log.error("", e);
            return Stream.empty();
        }
    }

    public Stream<CharacterWrapper.Character> findAllByFilter(CharacterWrapper.CharacterFilter filter) {
        return findAll().filter(character -> conditionalFilter(character.getHomeworld(),
                filter.getHomeworld(),
                !StringUtils.isEmpty(filter.getHomeworld())));
    }

    private static CharacterWrapper.Character convertToCharacterGpb(CharacterCsvBean csvBean) {
        return CharacterWrapper.Character.newBuilder()
                .setName(csvBean.getName())
                .setHeight(csvBean.getHeight())
                .setMass(csvBean.getMass())
                .addAllHairColor(Arrays.asList(csvBean.getHairColor().split(",")))
                .addAllSkinColor(Arrays.asList(csvBean.getSkinColor().split(",")))
                .setEyeColor(csvBean.getEyeColor())
                .setBirthYear(csvBean.getBirthYear())
                .setGender(csvBean.getGender())
                .setHomeworld(csvBean.getHomeworld())
                .setSpecies(csvBean.getSpecies())
                .build();
    }

    private static <T> boolean conditionalFilter(T value, T filterValue, boolean shouldApply) {
        if (!shouldApply) {
            return true;
        }

        return Objects.equals(value, filterValue);
    }
}

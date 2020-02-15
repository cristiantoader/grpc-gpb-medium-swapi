package org.ctoader.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@Slf4j
public class ResponseAnalyser {

    private final ObjectMapper objectMapper;

    @Autowired
    public ResponseAnalyser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public long getJsonResponseSize(List<?> entries) {
        return entries.stream()
                .map(Unchecked.function(planet -> objectMapper.writeValueAsString(planet)))
                .mapToLong(it -> it.getBytes().length)
                .sum();

    }

    public <T> long getGpbResponseSize(List<T> entries, Function<T, byte[]> byteSizeConverter) {
        return entries.stream()
                .map(it -> byteSizeConverter.apply(it))
                .mapToLong(it -> it.length)
                .sum();
    }
}

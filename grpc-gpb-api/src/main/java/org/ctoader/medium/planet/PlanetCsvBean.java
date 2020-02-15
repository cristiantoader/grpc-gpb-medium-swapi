package org.ctoader.medium.planet;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanetCsvBean {
    @CsvBindByPosition(position = 0)
    private String name;

    @CsvBindByPosition(position = 1)
    private int rotationPeriod;

    @CsvBindByPosition(position = 2)
    private int orbitalPeriod;

    @CsvBindByPosition(position = 3)
    private long diameter;

    @CsvBindByPosition(position = 4)
    private String climate;

    @CsvBindByPosition(position = 5)
    private String gravity;

    @CsvBindByPosition(position = 6)
    private String terrain;

    @CsvBindByPosition(position = 7)
    private int surfaceWater;

    @CsvBindByPosition(position = 8)
    private long population;
}

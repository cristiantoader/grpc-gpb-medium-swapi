package org.ctoader.medium.character;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterCsvBean {
    @CsvBindByPosition(position = 0)
    private String name;

    @CsvBindByPosition(position = 1)
    private int height;

    @CsvBindByPosition(position = 2)
    private double mass;

    @CsvBindByPosition(position = 3)
    private String hairColor;

    @CsvBindByPosition(position = 4)
    private String skinColor;

    @CsvBindByPosition(position = 5)
    private String eyeColor;

    @CsvBindByPosition(position = 6)
    private String birthYear;

    @CsvBindByPosition(position = 7)
    private String gender;

    @CsvBindByPosition(position = 8)
    private String homeworld;

    @CsvBindByPosition(position = 9)
    private String species;
}

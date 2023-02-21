package by.rusetskaya.tests.exchangerates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RateSpecificDateDTO {
    @JsonProperty("Cur_ID")
    private int id;

    @JsonProperty("Date")
    private String date;

    @JsonProperty("Cur_OfficialRate")
    private String officialRate;
}

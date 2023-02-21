package by.rusetskaya.tests.exchangerates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
        {
    Cur_ID: 1,
    Cur_ParentID: 1,
    Cur_Code: "008",
    Cur_Abbreviation: "ALL",
    Cur_Name: "Албанский лек",
    Cur_Name_Bel: "Албанскі лек",
    Cur_Name_Eng: "Albanian Lek",
    Cur_QuotName: "1 Албанский лек",
    Cur_QuotName_Bel: "1 Албанскі лек",
    Cur_QuotName_Eng: "1 Albanian Lek",
    Cur_NameMulti: "",
    Cur_Name_BelMulti: "",
    Cur_Name_EngMulti: "",
    Cur_Scale: 1,
    Cur_Periodicity: 1,
    Cur_DateStart: "1991-01-01T00:00:00",
    Cur_DateEnd: "2007-11-30T00:00:00"
    }
 */
@NoArgsConstructor
@Getter
@Setter
public class RateDTO {
    @JsonProperty("Cur_ID")
    private int id;

    @JsonProperty("Cur_ParentID")
    private int parentId;

    @JsonProperty("Cur_Code")
    private String code;

    @JsonProperty("Cur_Abbreviation")
    private String abbreviation;

    @JsonProperty("Cur_Name")
    private String name;

    @JsonProperty("Cur_Name_Bel")
    private String nameBel;

    @JsonProperty("Cur_Name_Eng")
    private String nameEng;

    @JsonProperty("Cur_QuotName")
    private String quotName;

    @JsonProperty("Cur_QuotName_Bel")
    private String quotNameBel;

    @JsonProperty("Cur_QuotName_Eng")
    private String quotNameEng;

    @JsonProperty("Cur_NameMulti")
    private String nameMulti;

    @JsonProperty("Cur_Name_BelMulti")
    private String nameBelMulti;

    @JsonProperty("Cur_Name_EngMulti")
    private String nameEngMulti;

    @JsonProperty("Cur_Scale")
    private int scale;

    @JsonProperty("Cur_Periodicity")
    private int periodicity;

    @JsonProperty("Cur_DateStart")
    private String dateStart;

    @JsonProperty("Cur_DateEnd")
    private String dateEnd;
}

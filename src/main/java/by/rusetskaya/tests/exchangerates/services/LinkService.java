package by.rusetskaya.tests.exchangerates.services;


import by.rusetskaya.tests.exchangerates.dto.RateDTO;
import by.rusetskaya.tests.exchangerates.dto.RateSpecificDateDTO;
import by.rusetskaya.tests.exchangerates.errors.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service for link to bank api by RestTemplate
 */
@Service
public class LinkService {
    @Autowired
    private RestTemplate restTemplate;

    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private final String GET_ALL_RATES = "https://www.nbrb.by/api/exrates/currencies";

    private final String GET_EXCHANGES = "https://www.nbrb.by/api/exrates/rates/";

    private final int HTTP_STATUS_OK = 200;

    private final int DENOMINATOR_2000 = 1000;

    private final int DENOMINATOR_2017 = 10000;

    LocalDate startLocaleDateMin = null;

    LocalDate endLocaleDateMax = null;

    //Get all rates list
    public List<RateDTO> getAllRates() {
        ResponseEntity<RateDTO[]> responseEntity = restTemplate
                .getForEntity(GET_ALL_RATES, RateDTO[].class);
        RateDTO[] ratesDtoArray = responseEntity.getBody();
        List<RateDTO> rateDtoList = Arrays.asList(ratesDtoArray);

        startLocaleDateMin = LocalDate.parse(ratesDtoArray[0].getDateStart().substring(0, 10));
        endLocaleDateMax = LocalDate.parse(ratesDtoArray[0].getDateEnd().substring(0, 10));
        ;
        //Cut time in date start and date end
        for (RateDTO rateDTO : rateDtoList) {
            String dateStart = rateDTO.getDateStart().substring(0, 10);
            String dateEnd = rateDTO.getDateEnd().substring(0, 10);
            rateDTO.setDateStart(dateStart);
            rateDTO.setDateEnd(dateEnd);
            LocalDate startLocaleDate = LocalDate.parse(dateStart);
            LocalDate endLocaleDate = LocalDate.parse(dateEnd);

            if (startLocaleDate.isBefore(startLocaleDateMin)) {
                startLocaleDateMin = startLocaleDate;
            }

            if (endLocaleDate.isAfter(startLocaleDateMin)) {
                endLocaleDateMax = endLocaleDate;
            }
        }
        return rateDtoList;
    }

    public List<String> getAllDatesFromAllRates(int stepMonths) {
        if (startLocaleDateMin != null && endLocaleDateMax != null) {
            List<String> dateList = new ArrayList<>();
            dateList.add(startLocaleDateMin.toString());
            LocalDate i = LocalDate.from(startLocaleDateMin);
            while (i.isBefore(endLocaleDateMax)) {
                i = i.plusMonths(stepMonths);
                dateList.add(i.toString());
            }

            return dateList;
        } else {
            throw new CustomRuntimeException(500, "Min and Max date not defined, get rates from api");
        }
    }

    //Get rates list by step months
    public List<RateSpecificDateDTO> getRateSpecificDateList(int id, String startDate, String endDate, int stepMonths, int month, int day) {
        LocalDate startLocaleDate = LocalDate.parse(startDate);
        LocalDate endLocaleDate = LocalDate.parse(endDate);

        List<RateSpecificDateDTO> rateSpecificDateDTOList = new ArrayList<>();

        //get rate for every year by loop
        while (startLocaleDate.isBefore(endLocaleDate.plusMonths(stepMonths))) {

            int year = startLocaleDate.getYear();

            //Add months tp start point
            startLocaleDate = startLocaleDate.plusMonths(stepMonths);

            //get path
            String path = getUrlPathSpecificRate(id, year, month, day);

            //the bank's api does not provide exchange rates for some years, so we first check url
            if (getStatusCodeConnection(path) != HTTP_STATUS_OK) {
                continue;
            }

            //Get json from bank api
            RateSpecificDateDTO rateSpecificDate = getRateSpecificDate(restTemplate, path);

            //Delete time from end od date
            String dateWithoutTime = rateSpecificDate.getDate().substring(0, 10);
            rateSpecificDate.setDate(dateWithoutTime);

            //Correct rates in years when was denomination
            if (year < 2017) {
                changeRate(rateSpecificDate, decimalFormat, DENOMINATOR_2017);
            }
            if (year <= 2000) {
                changeRate(rateSpecificDate, decimalFormat, DENOMINATOR_2000);
            }

            //format rate to desired length
            changeRate(rateSpecificDate, decimalFormat);

            rateSpecificDateDTOList.add(rateSpecificDate);

        }

        return rateSpecificDateDTOList;
    }


    private int getStatusCodeConnection(String path) throws RuntimeException {
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            return code;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RateSpecificDateDTO getRateSpecificDate(RestTemplate restTemplate, String path) {
        ResponseEntity<RateSpecificDateDTO> responseEntity = restTemplate
                .getForEntity(path,
                        RateSpecificDateDTO.class
                );
        RateSpecificDateDTO rateSpecificDate = responseEntity.getBody();
        return rateSpecificDate;
    }

    private String getUrlPathSpecificRate(int id, int year, int month, int day) {
        String path = GET_EXCHANGES + id + "?ondate=" + year + "-" + month + "-" + day;
        return path;
    }

    private void changeRate(RateSpecificDateDTO rateSpecificDateDTO,
                            DecimalFormat decimalFormat,
                            int denominator) {
        Double rate = Double.parseDouble(rateSpecificDateDTO.getOfficialRate());
        Double newRate = rate / denominator;
        rateSpecificDateDTO.setOfficialRate(decimalFormat.format(newRate));
    }

    private void changeRate(RateSpecificDateDTO rateSpecificDateDTO,
                            DecimalFormat decimalFormat) {
        Double rate = Double.parseDouble(rateSpecificDateDTO.getOfficialRate());
        rateSpecificDateDTO.setOfficialRate(decimalFormat.format(rate));
    }
}

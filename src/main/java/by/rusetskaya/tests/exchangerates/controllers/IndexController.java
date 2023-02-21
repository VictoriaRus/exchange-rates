package by.rusetskaya.tests.exchangerates.controllers;


import by.rusetskaya.tests.exchangerates.dto.RateDTO;
import by.rusetskaya.tests.exchangerates.dto.RateSpecificDateDTO;
import by.rusetskaya.tests.exchangerates.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

    private static int STEP_MONTH_QUANTITY = 12;

    private static int RATE_MONTH = 1;

    private static int RATE_DAY = 1;

    @Autowired
    private LinkService linkService;

    @GetMapping()
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView("index");

        List<RateDTO> allRatesList = linkService.getAllRates();

        modelAndView.addObject("allRatesList",allRatesList);

        List<String> allDates = linkService.getAllDatesFromAllRates(STEP_MONTH_QUANTITY);
        modelAndView.addObject("allDates", allDates);
        return modelAndView;
    }

    @PostMapping()
    @ResponseBody
    public List<RateSpecificDateDTO> postIndex(
            @RequestParam Map<String, String> reqParams
    ) {
        ModelAndView modelAndView = new ModelAndView("index");

        List<RateSpecificDateDTO> rateSpecificDateDtoList = linkService.getRateSpecificDateList(
                Integer.parseInt(reqParams.get("code")),
                reqParams.get("start"),
                reqParams.get("end"),
                STEP_MONTH_QUANTITY,
                RATE_MONTH,
                RATE_DAY
        );


        modelAndView.addObject("rateSpecificDateDtoList", rateSpecificDateDtoList);

        return rateSpecificDateDtoList;
    }
}

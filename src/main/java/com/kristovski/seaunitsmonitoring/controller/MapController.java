package com.kristovski.seaunitsmonitoring.controller;

import com.kristovski.seaunitsmonitoring.model.seaunit.SeaUnitPoint;
import com.kristovski.seaunitsmonitoring.service.SeaUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@Slf4j
public class MapController {

    private static final int MILITARY_UNITS_TYPE = 35;
    private final SeaUnitService seaUnitService;

    @Autowired
    public MapController(SeaUnitService seaUnitService) {
        this.seaUnitService = seaUnitService;
    }

    @GetMapping(value = {"/app", "/app?unitType={value}"})
    public String getMap(@RequestParam(required = false) Map<String, String> params, Model model) {

        String value = params.entrySet().stream()
                .map(Map.Entry::getValue)
                .findFirst().orElse("");

        log.info("UnitType number: " + value);

        if (isNumeric(value)) {
            List<SeaUnitPoint> seaUnitsByType = seaUnitService.getSeaUnitsByType(Integer.parseInt(value));

            model.addAttribute("seaUnits", seaUnitsByType);
            // Save only military units to db
            if (isMilitary(value)) {
                seaUnitService.saveDto(seaUnitsByType);
            }
        } else {
            // Commented out because
            // it takes a long time to load all units from api
            // for testing purposes it's better to show empty map after call "/app"
            // if you want to show all units remove the comment below
            //
            //   model.addAttribute("seaUnits", seaUnitService.getSeaUnits());
        }
        return "map";
    }

    private boolean isMilitary(String value) {
        return Integer.parseInt(value) == MILITARY_UNITS_TYPE;
    }

    private boolean isNumeric(String s) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (s == null) {
            return false;
        }
        return pattern.matcher(s).matches();
    }
}

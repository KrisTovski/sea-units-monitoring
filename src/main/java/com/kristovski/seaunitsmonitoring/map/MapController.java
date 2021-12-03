package com.kristovski.seaunitsmonitoring.map;

import com.kristovski.seaunitsmonitoring.seaunits.SeaUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.regex.Pattern;

@Controller
@Slf4j
public class MapController {

    private static final int DEFAULT_UNIT_TYPE_MILITARY = 35;
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
            model.addAttribute("seaUnits", seaUnitService.getSeaUnits(Integer.parseInt(value)));
        } else {
            model.addAttribute("seaUnits", seaUnitService.getSeaUnits(DEFAULT_UNIT_TYPE_MILITARY));
        }
        return "map";
    }

    private boolean isNumeric(String s) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (s == null) {
            return false;
        }
        return pattern.matcher(s).matches();
    }
}

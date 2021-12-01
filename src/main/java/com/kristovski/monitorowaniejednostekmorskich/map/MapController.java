package com.kristovski.monitorowaniejednostekmorskich.map;

import com.kristovski.monitorowaniejednostekmorskich.seaunits.SeaUnitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    private final SeaUnitService seaUnitService;

    public MapController(SeaUnitService seaUnitService) {
        this.seaUnitService = seaUnitService;
    }

    @GetMapping
    public String getMap(Model model){
        model.addAttribute("seaUnits", seaUnitService.getSeaUnits());
        return "map";
    }
}

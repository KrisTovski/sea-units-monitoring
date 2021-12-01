package com.kristovski.seaunitsmonitoring.map;

import com.kristovski.seaunitsmonitoring.seaunits.SeaUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    private final SeaUnitService seaUnitService;

    @Autowired
    public MapController(SeaUnitService seaUnitService) {
        this.seaUnitService = seaUnitService;
    }

    @GetMapping
    public String getMap(Model model){
        model.addAttribute("seaUnits", seaUnitService.getSeaUnits(35));
        return "map";
    }
}

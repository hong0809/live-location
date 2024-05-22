package com.rotary.livelocation.controller;


import com.rotary.livelocation.model.jpa.Location;
import com.rotary.livelocation.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocationController {
    @Autowired
    PositionService positionService;

    @GetMapping(value = "/positions/all", produces = "application/json")
    public List<Location> getAllLocations() {
        return positionService.getAllLocations();
    }

}

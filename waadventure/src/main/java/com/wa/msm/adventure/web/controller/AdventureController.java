package com.wa.msm.adventure.web.controller;

import com.wa.msm.adventure.repository.AdventureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdventureController {

    @Autowired
    AdventureRepository adventureRepository;
}

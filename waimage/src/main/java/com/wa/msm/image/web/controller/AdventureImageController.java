package com.wa.msm.image.web.controller;

import com.wa.msm.image.repository.AdventureImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdventureImageController {

    @Autowired
    private AdventureImageRepository adventureImageRepository;
}

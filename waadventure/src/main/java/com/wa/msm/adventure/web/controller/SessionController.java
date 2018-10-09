package com.wa.msm.adventure.web.controller;

import com.wa.msm.adventure.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @Autowired
    SessionRepository sessionRepository;
}

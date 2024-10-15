package com.deltadc.iot.controller;


import com.deltadc.iot.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/alert")
@Deprecated
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/getAlertCnt")
    public int getAlertCnt() {
        return alertService.getAlertCnt();
    }
}

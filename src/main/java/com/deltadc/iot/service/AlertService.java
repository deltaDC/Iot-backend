package com.deltadc.iot.service;

import com.deltadc.iot.model.entities.Alert;
import com.deltadc.iot.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Deprecated
public class AlertService {

    private final AlertRepository alertRepository;

    public void increaseAlertCount() {
        Alert alert = alertRepository.findById(1L).orElseThrow(() -> new RuntimeException("Alert not found"));

        alert.setAlertCnt(alert.getAlertCnt() + 1);

        alertRepository.save(alert);
    }

    public int getAlertCnt() {
        Alert alert = alertRepository.findById(1L).orElseThrow(() -> new RuntimeException("Alert not found"));
        return alert.getAlertCnt();
    }
}

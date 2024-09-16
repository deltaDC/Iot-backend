package com.deltadc.iot.controller;

import com.deltadc.iot.model.entities.sensor.Sensor;
import com.deltadc.iot.response.BaseResponse;
import com.deltadc.iot.service.SensorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/sensor")
@Slf4j
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;
    private final MqttClient mqttClient;

//    mosquitto_pub -h localhost -t iot-data -m '{"name":"sensor 1","data":{"temperature":{"value":22,"unit":"Celsius"},"humidity":{"value":45,"unit":"Percentage"},"brightness":{"value":300,"unit":"Lux"}}}'

    @PostConstruct
    public void init() {
        try {
            mqttClient.subscribe("iot-data", (topic, message) -> {
                String jsonMessage = new String(message.getPayload());
                sensorService.handleNewSensorData(jsonMessage);
            });
        } catch (MqttException e) {
            log.error("Failed to subscribe to topic", e);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse> list (@Nullable @RequestParam Map<String, String> params,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "30") int size,
                                              @RequestParam(required = false) String sortBy,
                                              @RequestParam(required = false) String sortDirection) {
        Page<Sensor> entities = sensorService.list(page, size, params, sortBy, sortDirection);
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK)
                        .message("Success")
                        .response(entities)
                        .build()
        );
    }
}
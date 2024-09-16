package com.deltadc.iot.service;

import com.deltadc.iot.model.entities.sensor.Sensor;
import com.deltadc.iot.repository.SensorRepository;
import com.deltadc.iot.specification.SensorSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;

    private final ObjectMapper objectMapper;

    public void handleNewSensorData(String jsonMessage) {
        try {
            log.info("Handling sensor data: {}", jsonMessage);
            JsonNode jsonNode = objectMapper.readTree(jsonMessage);
            String name = jsonNode.get("name").asText();
            JsonNode dataNode = jsonNode.get("data");
            String data = objectMapper.writeValueAsString(dataNode);

            Double temperature = dataNode.has("temperature") && dataNode.get("temperature").has("value") ? dataNode.get("temperature").get("value").asDouble() : null;
            Double humidity = dataNode.has("humidity") && dataNode.get("humidity").has("value") ? dataNode.get("humidity").get("value").asDouble() : null;
            Double brightness = dataNode.has("brightness") && dataNode.get("brightness").has("value") ? dataNode.get("brightness").get("value").asDouble() : null;

            Sensor sensor = Sensor.builder()
                    .name(name)
                    .data(data)
                    .temperature(temperature)
                    .humidity(humidity)
                    .brightness(brightness)
                    .build();
            log.info("Sensor data: {}", sensor);
            sensorRepository.save(sensor);
        } catch (JsonProcessingException e) {
            log.error("Failed to handle sensor data", e);
        }
    }

    public Page<Sensor> list(int page, int size, Map<String, String> params,
                             @RequestParam(required = false) String sortBy,
                             @RequestParam(required = false) String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(
                sortDirection != null ? sortDirection : "ASC"),
                sortBy != null ? sortBy : "id"
        );

        Pageable pageable = PageRequest.of(page, size, sort);
        return sensorRepository.findAll(
                SensorSpecification.withDynamicQuery(params, Sensor.class),
                pageable
        );
    }
}

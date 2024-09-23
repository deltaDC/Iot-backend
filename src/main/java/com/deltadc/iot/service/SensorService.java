package com.deltadc.iot.service;

import com.deltadc.iot.controller.SensorSocketHandler;
import com.deltadc.iot.model.entities.Sensor;
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

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final SensorSocketHandler sensorSocketHandler;
    private final ObjectMapper objectMapper;

    /**
     * Handle new sensor data from the MQTT broker, parse it and save it to the database
     * @param jsonMessage The JSON message from the MQTT broker
     */
    public void handleNewSensorData(String jsonMessage) {
        try {
            log.info("Handling sensor data: {}", jsonMessage);
            JsonNode jsonNode = objectMapper.readTree(jsonMessage);
            JsonNode dataNode = jsonNode.get("data");
            String data = objectMapper.writeValueAsString(dataNode);

            Double temperature = dataNode.has("temperature") && dataNode.get("temperature").has("value")
                    ? dataNode.get("temperature").get("value").asDouble()
                    : null;
            Double humidity = dataNode.has("humidity") && dataNode.get("humidity").has("value")
                    ? dataNode.get("humidity").get("value").asDouble()
                    : null;
            Double brightness = dataNode.has("brightness") && dataNode.get("brightness").has("value")
                    ? dataNode.get("brightness").get("value").asDouble()
                    : null;

            Sensor sensor = Sensor.builder()
                    .data(data)
                    .temperature(temperature)
                    .humidity(humidity)
                    .brightness(brightness)
                    .build();
            log.info("Sensor data: {}", sensor);
            sensorRepository.save(sensor);
            try {
                  sensorSocketHandler.broadcastSensorData(sensor);
            } catch (IOException e) {
                log.error("Failed to broadcast sensor data", e);
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to handle sensor data", e);
        }
    }

    /**
     * List the sensor data with pagination and sorting by params
     * @param params This Map should contain the key-value pairs of the fields to filter of
     *               the Sensor entity class like: name, temperature, humidity, brightness, createdAt
     * @param page The page number to retrieve, default is 0
     * @param size The number of items to retrieve per page, default is 30
     * @param sortBy The field to sort by, default is id
     * @param sortDirection The direction to sort by, default is ASC
     * @return A Page of Sensor entities
     */
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

    /**
     * Get the latest sensor data
     * @return The latest Sensor entity
     */
    public Sensor latest() {
        return sensorRepository.findTopByOrderByIdDesc();
    }
}

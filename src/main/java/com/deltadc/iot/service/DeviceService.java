package com.deltadc.iot.service;

import com.deltadc.iot.model.entities.Device;
import com.deltadc.iot.repository.DeviceRepository;
import com.deltadc.iot.request.ToggleLedRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final MqttService mqttService;

    /**
     * List all devices
     *
     * @return List of devices
     */
    public List<Device> list() {
        return deviceRepository.findAll();
    }

    /**
     * Find device by id
     *
     * @param id Long
     * @return Device
     */
    public Device findById(Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    /**
     * Create a new device
     *
     * @param device Device
     * @return Created device
     */
    public Device create(Device device) {
        return deviceRepository.save(device);
    }

    /**
     * Toggle LED status by publish a topic to mqtt server
     * and wait for response after toggle
     * @param request ToggleLedRequest
     */
    public String toggleLed(ToggleLedRequest request) throws ExecutionException, InterruptedException {
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        String deviceName = device.getName();

        String mqttControlMessage = deviceName + " " + request.getStatus();

        mqttService.publish("led/control", mqttControlMessage);

        String mqttStatusResponse = mqttService.subscribeAndWaitForMessage("led/status");

        device.setStatus(
                Objects.equals(device.getStatus(), "ON")
                        ? "OFF"
                        : "ON"
        );

        deviceRepository.save(device);

        return mqttStatusResponse;
    }

    /**
     * Get device name by id
     *
     * @param deviceId Long
     * @return String
     */
    public String getDeviceNameById(Long deviceId) {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        return device != null ? device.getName() : null;
    }
}

package com.deltadc.iot.controller;

import com.deltadc.iot.model.entities.Device;
import com.deltadc.iot.model.entities.History;
import com.deltadc.iot.request.ToggleLedRequest;
import com.deltadc.iot.response.BaseResponse;
import com.deltadc.iot.service.DeviceService;
import com.deltadc.iot.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/device")
public class DeviceController {

    private final DeviceService deviceService;
    private final HistoryService historyService;

    @GetMapping("/list")
    public ResponseEntity<BaseResponse> list() {
        List<Device> devices = deviceService.list();

        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK)
                        .message("Success")
                        .response(devices)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        Device device = deviceService.findById(id);

        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK)
                        .message("Success")
                        .response(device)
                        .build()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> create(@RequestBody Device device) {
        Device createdDevice = deviceService.create(device);

        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK)
                        .message("Success")
                        .response(createdDevice)
                        .build()
        );
    }

    @PostMapping("/toggle")
    public ResponseEntity<BaseResponse> create(@RequestBody ToggleLedRequest request) throws ExecutionException, InterruptedException {
        deviceService.toggleLed(request);

        History history = historyService.create(
                History.builder()
                    .deviceId(request.getDeviceId())
                    .status(request.getStatus())
                    .build()
        );

        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK)
                        .message("Success")
                        .response(history)
                        .build()
        );
    }
}
package com.deltadc.iot.controller;


import com.deltadc.iot.model.entities.History;
import com.deltadc.iot.response.BaseResponse;
import com.deltadc.iot.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/history")
@Slf4j
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/list")
    public ResponseEntity<BaseResponse> list (@Nullable @RequestParam Map<String, String> params,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(required = false, defaultValue = "id") String sortBy,
                                              @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {
        Page<History> entities = historyService.list(page, size, params, sortBy, sortDirection);

        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK)
                        .message("History list fetched successfully")
                        .response(entities)
                        .build()
        );
    }
}

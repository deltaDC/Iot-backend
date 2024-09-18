package com.deltadc.iot.request;

import lombok.Data;

@Data
public class ToggleLedRequest {

    private Long deviceId;

    private String status;
}

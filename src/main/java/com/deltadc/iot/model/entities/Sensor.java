package com.deltadc.iot.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sensor")
public class Sensor extends BaseEntity {

    @Lob
    private String data;

    private Double temperature;
    private Double humidity;
    private Double brightness;
    private Double wind;

    private String weather;
}

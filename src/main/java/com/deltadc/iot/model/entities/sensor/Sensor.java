package com.deltadc.iot.model.entities.sensor;

import com.deltadc.iot.model.entities.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Sensor name is required")
    private String name;

//    @Embedded
//    private Data data;

    @Lob
    private String data;

    private Double temperature;
    private Double humidity;
    private Double brightness;
}

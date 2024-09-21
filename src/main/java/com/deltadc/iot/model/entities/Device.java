package com.deltadc.iot.model.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device")
public class Device extends BaseEntity {

    private String name;

    private String status;
}

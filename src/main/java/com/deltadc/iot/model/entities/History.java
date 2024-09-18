package com.deltadc.iot.model.entities;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "history")
public class History extends BaseEntity{

    private Long deviceId;

    private String status;
}

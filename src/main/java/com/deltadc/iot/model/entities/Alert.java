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
@Table(name = "alert")
@Deprecated
public class Alert extends BaseEntity {

    private int alertCnt;
}

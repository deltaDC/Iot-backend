package com.deltadc.iot.repository;

import com.deltadc.iot.model.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}

package com.deltadc.iot.repository;

import com.deltadc.iot.model.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public interface AlertRepository extends JpaRepository<Alert, Long> {
}

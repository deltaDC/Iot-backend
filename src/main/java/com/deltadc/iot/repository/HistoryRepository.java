package com.deltadc.iot.repository;

import com.deltadc.iot.model.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>, JpaSpecificationExecutor<History> {
    long countByStatus(String alert);
}

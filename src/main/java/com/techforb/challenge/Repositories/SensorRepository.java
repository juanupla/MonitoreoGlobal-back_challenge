package com.techforb.challenge.Repositories;

import com.techforb.challenge.Entities.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<SensorEntity, Long> {
    List<SensorEntity> findByPlantId(Long plantId);
}


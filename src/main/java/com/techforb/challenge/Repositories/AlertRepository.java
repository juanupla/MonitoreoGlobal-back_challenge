package com.techforb.challenge.Repositories;

import com.techforb.challenge.Entities.AlertEntity;
import com.techforb.challenge.Entities.TypeAlertEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
    List<AlertEntity> findByTypeAlertEnum(TypeAlertEnum typeAlertEnum);
}


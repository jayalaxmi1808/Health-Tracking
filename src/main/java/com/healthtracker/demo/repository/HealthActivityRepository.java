
package com.healthtracker.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthtracker.demo.model.HealthActivity;

public interface HealthActivityRepository
        extends JpaRepository<HealthActivity, Long> {

    List<HealthActivity> findByUsername(String username);

    Optional<HealthActivity> findByIdAndUsername(
            Long id, String username);
}

package com.zaytsevp.testcontainersexample.repository;

import com.zaytsevp.testcontainersexample.model.Auto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutoRepository extends JpaRepository<Auto, UUID> {
}

package com.kristovski.seaunitsmonitoring.repository;

import com.kristovski.seaunitsmonitoring.model.dto.SeaUnitDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeaUnitRepository extends JpaRepository<SeaUnitDto, Long> {
}

package com.kristovski.seaunitsmonitoring.seaunits;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilsRepository extends JpaRepository<Token, Long> {

}

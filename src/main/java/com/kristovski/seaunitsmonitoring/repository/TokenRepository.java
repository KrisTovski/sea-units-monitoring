package com.kristovski.seaunitsmonitoring.repository;

import com.kristovski.seaunitsmonitoring.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

}

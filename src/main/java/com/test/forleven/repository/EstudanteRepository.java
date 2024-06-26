package com.test.forleven.repository;

import com.test.forleven.model.entity.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudanteRepository extends JpaRepository<Estudante, String> {

    Optional<Estudante> findByEmail(String email);
}

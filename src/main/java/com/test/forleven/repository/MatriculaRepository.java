package com.test.forleven.repository;

import com.test.forleven.model.entity.Estudante;
import com.test.forleven.model.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, String> {

    Optional<Matricula> findByNumeroMatricula(String numeroMatricula);

    Optional<Matricula> findByEstudante(Estudante estudante);

    @Query("SELECT m FROM Matricula m WHERE m.estudante.cpf = :cpf")
    Optional<Matricula> findByMatriculaPorCpf(@Param("cpf") String cpf);
}


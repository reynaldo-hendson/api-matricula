package com.test.forleven.repository;

import com.test.forleven.model.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

    Optional<Telefone> findByPhone (String phone);

    @Query("select r from Telefone r join r.estudante p where p.id = :estudante_id")
    List<Telefone> findByPhones(@Param("estudante_id") String estudanteId);

    @Modifying
    @Query("delete from Telefone t where t.estudante.id = :estudante_id")
    void deleteByEstudanteId(@Param("estudante_id") String estudanteId);
}

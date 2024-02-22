package com.test.forleven.repository;

import com.test.forleven.model.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhonesRepository extends JpaRepository<Phone, Long> {

    Optional<Phone> findByPhone (String phone);
    @Query("select r from Phone r join r.student p where p.id = :student_id")
    List<Phone> findByPhone(@Param("student_id") Long student_id);
}

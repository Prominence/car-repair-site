package com.github.prominence.carrepair.repository;

import com.github.prominence.carrepair.model.domain.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {

    @Query(value = "select m from Mechanic m where m.firstName like concat('%', :query, '%') or m.middleName like concat('%', :query, '%') or m.lastName like concat('%', :query, '%')")
    List<Mechanic> findAllByInitials(@Param(value = "query") String query);
}

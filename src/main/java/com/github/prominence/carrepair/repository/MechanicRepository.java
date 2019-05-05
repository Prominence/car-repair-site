package com.github.prominence.carrepair.repository;

import com.github.prominence.carrepair.model.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {

    @Query(value = "select m.id from Mechanic m")
    public List<Long> findAllMechanicIds();
}

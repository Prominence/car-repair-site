package com.github.prominence.carrepair.repository;

import com.github.prominence.carrepair.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "select c.id from Client c")
    public List<Long> findAllClientIds();
}

package com.github.prominence.carrepair.repository;

import com.github.prominence.carrepair.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "select c from Client c where c.firstName like concat('%', :query, '%') or c.middleName like concat('%', :query, '%') or c.lastName like concat('%', :query, '%')")
    public List<Client> findAllByInitials(@Param(value = "query") String query);
}

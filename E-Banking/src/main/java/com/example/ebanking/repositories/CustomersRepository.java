package com.example.ebanking.repositories;

import com.example.ebanking.entities.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomersRepository extends JpaRepository<Customers,Long> {
    @Query("select c from Customers c where c.name like:kw")
    List<Customers> customerSearch(@Param("kw") String keyword);
    //List<Customers> findByNameContains(String keyword);
}

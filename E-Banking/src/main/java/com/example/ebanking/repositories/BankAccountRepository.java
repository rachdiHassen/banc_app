package com.example.ebanking.repositories;

import com.example.ebanking.dtos.BankAccountDTO;
import com.example.ebanking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    @Query("select b from BankAccount b where b.customers.id= :id")
    List<BankAccount> listAccounts(@Param("id") Long id);
    List<BankAccount> findByCustomersId(Long customerId);


}

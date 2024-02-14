package com.example.ebanking.repositories;

import com.example.ebanking.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
    public List<AccountOperation> findByBankAccountId(String accountId);
    Page<AccountOperation> findByBankAccountIdOrderByDateDesc (String accountId, Pageable pageable);
}

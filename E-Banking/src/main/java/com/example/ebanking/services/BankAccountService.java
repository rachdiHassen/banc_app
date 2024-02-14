package com.example.ebanking.services;

import com.example.ebanking.dtos.*;
import com.example.ebanking.entities.*;
import com.example.ebanking.exceptions.AccountNotFoundException;
import com.example.ebanking.exceptions.BalanceIsuffisantException;
import com.example.ebanking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentAccountDTO saveCurrentBankAccount (Long customerId, double overDraft, double initialBalance) throws CustomerNotFoundException;
    SavingAccountDTO saveSavingBankAccount (Long customerId, double interestRate, double initialBalance) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws AccountNotFoundException;

    void debit (String accountId, double amount, String description) throws AccountNotFoundException, BalanceIsuffisantException;

    void credit(String accountId, double amount,String description) throws BalanceIsuffisantException, AccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceIsuffisantException, AccountNotFoundException;
    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long id);

    List<AccountOperationDTO> operationHistory(String accountId);

    PageOperationDTO pageOperationHistory(String accountId,int page, int size) throws AccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
    List<BankAccountDTO> customerAccounts(Long customerId);
}

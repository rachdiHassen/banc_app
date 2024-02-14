package com.example.ebanking.services;

import com.example.ebanking.dtos.*;
import com.example.ebanking.entities.*;
import com.example.ebanking.enums.AccountStatus;
import com.example.ebanking.enums.OperationType;
import com.example.ebanking.exceptions.AccountNotFoundException;
import com.example.ebanking.exceptions.BalanceIsuffisantException;
import com.example.ebanking.exceptions.CustomerNotFoundException;
import com.example.ebanking.mappers.BankAccountMapperImpl;
import com.example.ebanking.repositories.AccountOperationRepository;
import com.example.ebanking.repositories.BankAccountRepository;
import com.example.ebanking.repositories.CustomersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
    private BankAccountRepository bankAccountRepository;
    private CustomersRepository customersRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl mapper;
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customers customer=mapper.fromCustomersDTO(customerDTO);
        Customers savedCustomer=customersRepository.save(customer);
        return mapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(Long customerId, double overDraft, double initialBalance) throws CustomerNotFoundException {
        Customers customer=customersRepository.findById(customerId).orElse(null);
        if (customer==null){
            throw new CustomerNotFoundException("customer not found");
        }
        else {
            CurrentAccount currentAccount= new CurrentAccount();
            currentAccount.setId(UUID.randomUUID().toString());
            currentAccount.setCreatedAt(new Date());
            currentAccount.setBalance(initialBalance);
            currentAccount.setStatus(AccountStatus.CREATED);
            currentAccount.setOverDraft(overDraft);
            currentAccount.setCustomers(customer);
            CurrentAccount savedCurrentAccount=bankAccountRepository.save(currentAccount);
            return mapper.fromCurrentAccount(savedCurrentAccount);
        }

    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(Long customerId, double interestRate, double initialBalance) throws CustomerNotFoundException {
        Customers customer=customersRepository.findById(customerId).orElse(null);
        if (customer==null){
            throw new CustomerNotFoundException("customer not found");
        }
        else {
            SavingAccount savingAccount= new SavingAccount();
            savingAccount.setId(UUID.randomUUID().toString());
            savingAccount.setCreatedAt(new Date());
            savingAccount.setBalance(initialBalance);
            savingAccount.setStatus(AccountStatus.CREATED);
            savingAccount.setInterestRate(interestRate);
            savingAccount.setCustomers(customer);
            SavingAccount savedSavingAccount=bankAccountRepository.save(savingAccount);
            return mapper.fromSavingAccount(savedSavingAccount);
        }
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customers> listCustomer=customersRepository.findAll();
        List<CustomerDTO> customerDtoList=listCustomer.stream().map(cust->mapper.fromCustomer(cust)).collect(Collectors.toList());
       /* List<CustomerDTO> customerDtoList=new ArrayList<>();
        for(Customers cust:list){
            CustomerDTO map=mapper.fromCustomer(cust);
            customerDtoList.add(map);
        }*/
        return customerDtoList;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws AccountNotFoundException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId)
                .orElseThrow(()->new AccountNotFoundException("this account doesnt found"));
        if (bankAccount instanceof CurrentAccount){
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
            return mapper.fromCurrentAccount(currentAccount);
        }
        else {
            return mapper.fromSavingAccount((SavingAccount)bankAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount,String description) throws AccountNotFoundException, BalanceIsuffisantException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId)
                .orElseThrow(()->new AccountNotFoundException("this account doesnt found"));
        if (bankAccount.getBalance()<amount){
            throw new BalanceIsuffisantException("insiffusant sold");
        }
        else {
            AccountOperation operation=new AccountOperation();
           operation.setDate(new Date());
           operation.setType(OperationType.DEBIT);
           operation.setAmount(amount);
           operation.setDescription(description);
           operation.setBankAccount(bankAccount);
            accountOperationRepository.save(operation);
            bankAccount.setBalance(bankAccount.getBalance()-amount);
            bankAccountRepository.save(bankAccount);
        }
    }

    @Override
    public void credit(String accountId, double amount,String description) throws AccountNotFoundException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId)
                .orElseThrow(()->new AccountNotFoundException("this account doesnt found"));
            AccountOperation operation=new AccountOperation();
            operation.setDate(new Date());
            operation.setType(OperationType.CREDIT);
            operation.setAmount(amount);
            operation.setDescription(description);
            operation.setBankAccount(bankAccount);
            accountOperationRepository.save(operation);
            bankAccount.setBalance(bankAccount.getBalance()+amount);
            bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceIsuffisantException, AccountNotFoundException {
        debit(accountIdSource,amount,"transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccount= bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS= bankAccount
                .stream().map(account->{
                    if (account instanceof CurrentAccount){
                       return mapper.fromCurrentAccount((CurrentAccount) account);
                    }
                    else {
                        SavingAccount savingAccount=(SavingAccount)account;
                        return mapper.fromSavingAccount(savingAccount);
                    }
                }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customers customer=customersRepository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("customer not found"));
        CustomerDTO customerDTO=mapper.fromCustomer(customer);
        return customerDTO;
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customers customer=mapper.fromCustomersDTO(customerDTO);
        Customers savedCustomer=customersRepository.save(customer);
        return mapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long id){
        customersRepository.deleteById(id);
    }
    @Override
    public List<AccountOperationDTO> operationHistory(String accountId){
        List<AccountOperation> operations=accountOperationRepository.findByBankAccountId(accountId);
        return operations.stream().map(op->mapper.fromAccountOperation(op)).collect(Collectors.toList());

    }

    @Override
    public PageOperationDTO pageOperationHistory(String accountId, int page, int size) throws AccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount==null) throw new AccountNotFoundException("acount dosent exist");

        Page<AccountOperation> accountOperationPage= accountOperationRepository.findByBankAccountIdOrderByDateDesc(accountId, PageRequest.of(page,size));
        List<AccountOperationDTO> accountOperationDTOS=accountOperationPage.getContent()
                .stream().map(opp->mapper.fromAccountOperation(opp)).collect(Collectors.toList());

        PageOperationDTO pageOperationDTO=new PageOperationDTO();
        pageOperationDTO.setAccountId(bankAccount.getId());
        pageOperationDTO.setBalance(bankAccount.getBalance());
        pageOperationDTO.setAccountOperationDTOS(accountOperationDTOS);
        pageOperationDTO.setCurrentPage(page);
        pageOperationDTO.setPageSize(size);
        pageOperationDTO.setTotalPages(accountOperationPage.getTotalPages());

        return pageOperationDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customers> customers=customersRepository.customerSearch(keyword);
        List<CustomerDTO> customerDTOS=customers.stream().map(cust ->mapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public List<BankAccountDTO> customerAccounts(Long customerId) {
        List<BankAccount> bankAccount= bankAccountRepository.listAccounts(customerId);
        List<BankAccountDTO> custAccountsDTO= bankAccount
                .stream().map(account->{
                    if (account instanceof CurrentAccount){
                        return mapper.fromCurrentAccount((CurrentAccount) account);
                    }
                    else {
                        SavingAccount savingAccount=(SavingAccount)account;
                        return mapper.fromSavingAccount(savingAccount);
                    }
                }).collect(Collectors.toList());
        return custAccountsDTO;
    }
}

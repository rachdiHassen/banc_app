package com.example.ebanking;

import com.example.ebanking.dtos.BankAccountDTO;
import com.example.ebanking.dtos.CurrentAccountDTO;
import com.example.ebanking.dtos.CustomerDTO;
import com.example.ebanking.dtos.SavingAccountDTO;
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
import com.example.ebanking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class EBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomersRepository customersRepository,
                                        BankAccountRepository bankAccountRepository,
                                        AccountOperationRepository accountOperationRepository){
        return args -> {
           Stream.of("habiba","mohamed","hassen").forEach(nom->{
               Customers customer=new Customers();
               customer.setName(nom);
               customer.setEmail(nom+"@gmail.com");
               customersRepository.save(customer);
           });
           List<Customers> cos= customersRepository.findAll();
           cos.forEach(cus->{
               CurrentAccount curr=new CurrentAccount();
               curr.setId(UUID.randomUUID().toString());
               curr.setCreatedAt(new Date());
               curr.setBalance(Math.random()*90000);
               curr.setStatus(AccountStatus.ACTIVATED);
               curr.setCustomers(cus);
               curr.setOverDraft(9000);
               bankAccountRepository.save(curr);

               SavingAccount sav=new SavingAccount();
              sav.setId(UUID.randomUUID().toString());
              sav.setCreatedAt(new Date());
              sav.setBalance(Math.random()*90000);
              sav.setStatus(AccountStatus.ACTIVATED);
              sav.setCustomers(cus);
              sav.setInterestRate(5.5);
               bankAccountRepository.save(sav);
           });

           bankAccountRepository.findAll().forEach(cpt->{
               for (int i = 0; i < 5; i++) {
                   AccountOperation op= new AccountOperation();
                   op.setDate(new Date());
                   op.setAmount(Math.random()*12000);
                   op.setType(Math.random()>0.5? OperationType.CREDIT:OperationType.DEBIT);
                   op.setBankAccount(cpt);
                   accountOperationRepository.save(op);
               }
           });
        };
    }

    //@Bean
    CommandLineRunner commandLineRunner(AccountOperationRepository accountOperationRepository,
                                        BankAccountRepository bankAccountRepository){
        return args -> {
          BankAccount bank= bankAccountRepository.findById("9963a368-f850-4aa4-8f44-1abc2b329586").orElse(null);
          if (bank!=null){
              System.out.println(bank.getId());
              System.out.println(bank.getCreatedAt());
              System.out.println(bank.getBalance());
              System.out.println(bank.getStatus());
              System.out.println(bank.getCustomers().getName());
              System.out.println(bank.getClass().getSimpleName());
              if (bank instanceof CurrentAccount){
                  System.out.println("over draft => " + ((CurrentAccount) bank).getOverDraft());
              } else if (bank instanceof SavingAccount) {
                  System.out.println("Interest Rate => " + ((SavingAccount) bank).getInterestRate());
              }
              System.out.println("les opertaions effectues par ce compte dont l'id est"+bank.getId());
              bank.getAccountOperations().forEach(op->{
                  System.out.println("*******************");
                  System.out.println(op.getId());
                  System.out.println(op.getDate());
                  System.out.println(op.getAmount());
                  System.out.println(op.getType());
              });
          }

        };
    }
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService,
                                        BankAccountMapperImpl mapper){
       return args -> {
           Stream.of("haboub","hassen","samira").forEach(nom->{
               Customers customer=new Customers();
               customer.setName(nom);
               customer.setEmail(nom +"@gmail.com");
               CustomerDTO customerDTO= mapper.fromCustomer(customer);
               bankAccountService.saveCustomer(customerDTO);
           });
           bankAccountService.listCustomers().forEach(customer->{
               try {
                   bankAccountService.saveCurrentBankAccount(customer.getId(),90000,10000+Math.random()*90000);
                   bankAccountService.saveSavingBankAccount(customer.getId(),Math.random()*10,10000+Math.random()* 120000);
               } catch (CustomerNotFoundException e) {
                   e.printStackTrace();
               }
               List<BankAccountDTO> ListAccount = bankAccountService.bankAccountList();
               for(BankAccountDTO compte: ListAccount){
                   String accountId;
                   if (compte instanceof CurrentAccountDTO){
                       accountId=((CurrentAccountDTO) compte).getId();
                   }
                   else {
                       accountId=((SavingAccountDTO) compte).getId();
                   }
                   for (int i = 0; i < 10; i++) {
                           try {
                               bankAccountService.credit(accountId,10000+Math.random()*10000,"Credit");
                               bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
                           } catch (BalanceIsuffisantException | AccountNotFoundException e) {
                               throw new RuntimeException(e);
                           }
                   }
               }

           });


       };
    };
}

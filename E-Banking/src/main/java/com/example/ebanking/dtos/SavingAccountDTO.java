package com.example.ebanking.dtos;

import com.example.ebanking.enums.AccountStatus;
import lombok.Data;

import java.util.Date;
@Data
public class SavingAccountDTO extends BankAccountDTO{
    private String id;
    private Date createdAt;
    private double balance;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}

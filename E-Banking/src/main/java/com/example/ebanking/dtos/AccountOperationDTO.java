package com.example.ebanking.dtos;

import com.example.ebanking.entities.BankAccount;
import com.example.ebanking.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
public class AccountOperationDTO {
    private Long id;
    private Date date;
    private  double amount;
    private OperationType type;
    private String description;
}

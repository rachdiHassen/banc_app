package com.example.ebanking.dtos;

import lombok.Data;

import java.util.List;
@Data
public class PageOperationDTO {
   private  String accountId;
    private double balance;
    private List<AccountOperationDTO> accountOperationDTOS;
    private int currentPage;
   private int pageSize;
    private int totalPages;
}

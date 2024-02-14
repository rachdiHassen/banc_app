package com.example.ebanking.web;

import com.example.ebanking.dtos.*;
import com.example.ebanking.entities.BankAccount;
import com.example.ebanking.exceptions.AccountNotFoundException;
import com.example.ebanking.exceptions.BalanceIsuffisantException;
import com.example.ebanking.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class BankAccountController {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws AccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<BankAccountDTO> listAccounts (){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.operationHistory(accountId);
    }
    @GetMapping("/accounts/{accountId}/page")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public PageOperationDTO getPageHistory (@PathVariable String accountId,
                                            @RequestParam(name ="page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "5") int size) throws AccountNotFoundException {
        return bankAccountService.pageOperationHistory(accountId,page,size);
    }

    @PostMapping("accounts/credit")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BalanceIsuffisantException, AccountNotFoundException {
        bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }
    @PostMapping("accounts/debit")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BalanceIsuffisantException, AccountNotFoundException {
        bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("accounts/transfer")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BalanceIsuffisantException, AccountNotFoundException {
        bankAccountService.transfer(transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());
    }

}

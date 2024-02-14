import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AccountsService} from "../services/accounts.service";
import {catchError, Observable, throwError} from "rxjs";
import {AccountDetails} from "../model/accountOperation.model";


@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {
  searchAccountOperationForm !: FormGroup
  operationFormGroup !: FormGroup
  currentPage : number=0
  pageSize:number=5
  errorMessage! : string
  accountObservable !:Observable<AccountDetails>
  constructor(private fb:FormBuilder,
              private accountService:AccountsService) { }

  ngOnInit(): void {
    this.searchAccountOperationForm=this.fb.group({
      accountId:this.fb.control(null)
    })

    this.operationFormGroup=this.fb.group({
      operationType:this.fb.control(null),
      amount:this.fb.control(0),
      description:this.fb.control(null),
      accountDestination:this.fb.control(null),
    })
  }

  handleSearchOperation() {
    let accountId=this.searchAccountOperationForm.value.accountId
    this.accountObservable=this.accountService.getOperationPage(accountId,this.currentPage,this.pageSize).pipe(
      catchError (err => {
        this.errorMessage=err.message
        return throwError(err)
      })
    )
  }


  handleGoTo( numberPage: number) {
    this.currentPage=numberPage;
    this.handleSearchOperation();
  }

  handleOperations() {
    let accountId:string=this.searchAccountOperationForm.value.accountId
    let accountDest:string=this.operationFormGroup.value.accountDestination
    let amount=this.operationFormGroup.value.amount
    let description=this.operationFormGroup.value.description
    let operationType=this.operationFormGroup.value.operationType
    if (operationType=="CREDIT"){
      this.accountService.creditOperation(accountId,amount,description).subscribe({
        next:(data=>{
          alert("saved with success")
          this.operationFormGroup.reset()
          this.handleSearchOperation()
        }),
        error:(err=>{
          this.errorMessage=err.message
        })
      })
    }
    else if (operationType=="DEBIT"){
      this.accountService.debitOperation(accountId,amount,description).subscribe({
        next:(data=>{
          alert("saved with success")
          this.operationFormGroup.reset()
          this.handleSearchOperation()
        }),
        error:(err=>{
          this.errorMessage=err.message
        })
      })
    }
    else if (operationType=="TRANSFER"){
      this.accountService.transferOperation(accountId,accountDest,amount).subscribe({
        next:(data=>{
          alert("saved with success")
          this.operationFormGroup.reset()
          this.handleSearchOperation()
        }),
        error:(err=>{
          this.errorMessage=err.message
        })
      })
    }




  }
}

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountDetails} from "../model/accountOperation.model";


@Injectable({
  providedIn: 'root'
})
export class AccountsService {

  constructor(private http:HttpClient) { }

  public getOperationPage(id:string,page:number,size:number):Observable<AccountDetails>{
    return this.http.get<AccountDetails>("http://localhost:8085/accounts/"+id +"/page?page="+page+"&size="+size)
  }

  public creditOperation(accountId:string,amount:number,description:string){
    let data={accountId:accountId,amount:amount,description:description}
    return this.http.post("http://localhost:8085/accounts/credit",data);
  }

  public debitOperation(accountId:string,amount:number,description:string){
    let data={accountId,amount,description}
    return this.http.post("http://localhost:8085/accounts/debit",data);
  }

  public transferOperation(accountSource:string,accountDestination:string,amount:number){
    let data={accountSource,accountDestination,amount}
    return this.http.post("http://localhost:8085/accounts/transfer",data);
  }
}

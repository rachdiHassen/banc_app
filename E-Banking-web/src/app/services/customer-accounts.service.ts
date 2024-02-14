import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CustomerAccounts} from "../model/customerAccounts.model";

@Injectable({
  providedIn: 'root'
})
export class CustomerAccountsService {

  constructor(private http:HttpClient) { }

  public getCustomerAccounts(customerId:number):Observable<Array<CustomerAccounts>>{
    return this.http.get<Array<CustomerAccounts>>("http://localhost:8085/customers/accounts/"+customerId)
  }
}

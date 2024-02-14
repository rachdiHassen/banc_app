import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Customer} from "../model/customer.model";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private customerHost:string="http://localhost:8085/customers"
  constructor(private http:HttpClient,
              private authService:AuthService) { }
  public getCustomers():Observable<Array<Customer>>{
    return this.http.get<Customer[]>(this.customerHost)
  }

  public searchCustomers(keyword:string):Observable<Array<Customer>>{

    return this.http.get<Array<Customer>>(this.customerHost+"/search?keyword="+keyword)
  }

  public saveCustomer(customer:Customer):Observable<Customer>{
    return this.http.post<Customer>(this.customerHost,customer);
  }

  public deleteCustomer(id:number){
    return this.http.delete("http://localhost:8085/customers/" +id);
  }
}

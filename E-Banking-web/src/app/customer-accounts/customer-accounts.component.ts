import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Customer} from "../model/customer.model";
import {CustomerAccountsService} from "../services/customer-accounts.service";
import {CustomerService} from "../services/customer.service";
import {CustomerAccounts} from "../model/customerAccounts.model";

@Component({
  selector: 'app-customer-accounts',
  templateUrl: './customer-accounts.component.html',
  styleUrls: ['./customer-accounts.component.css']
})
export class CustomerAccountsComponent implements OnInit {
  customerId!: number
  customer!:Customer
  customerAccounts!:Array<CustomerAccounts>
  errorMessage!: string
  constructor(private activeRoute:ActivatedRoute,private router:Router,
              private customerAccountsService:CustomerAccountsService) {
    this.customer=this.router.getCurrentNavigation()?.extras.state as Customer;
  }

  ngOnInit(): void {
    this.customerId=this.activeRoute.snapshot.params['id']
    this.handleCustomerAccounts();

  }

  handleCustomerAccounts(){
    this.customerAccountsService.getCustomerAccounts(this.customerId).subscribe({
      next:(data=>{
        this.customerAccounts=data
      }),
      error:(err=>{
        this.errorMessage=err.message
      })
    })
  }
}

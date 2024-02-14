import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {Customer} from "../model/customer.model";
import {CustomerService} from "../services/customer.service";
import {catchError, throwError} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrls: ['./new-customer.component.css']
})
export class NewCustomerComponent implements OnInit {
  newCustomerFormGroup :FormGroup |undefined
  errorMessage ! :string
  constructor(private fb:FormBuilder,
              private customerService:CustomerService,
              private router:Router) { }

  ngOnInit(): void {
    this.newCustomerFormGroup=this.fb.group({
      name:this.fb.control(null,[Validators.required,Validators.minLength(4)]),
      email:this.fb.control(null,[Validators.required,Validators.email])
    })
  }

  handleSaveNewCustomer() {
    let customer:Customer=this.newCustomerFormGroup?.value;
    this.customerService.saveCustomer(customer).subscribe({
      next:(data=>{
        alert("customer has been succefully saved")
        this.newCustomerFormGroup?.reset()
        this.router.navigateByUrl("/app-customers")
      }),
      error: (err=> this.errorMessage=err.message)
    })
  }

  getErrorMessage(fieldName:string,error:ValidationErrors){
    if(error['required']){
      return fieldName+ ' is Reuired';
    }
    else if (error['minlength']){
      return fieldName+' should have at least '+error['minlength']['requiredLength']+ ' characters'
    }
    else if (error['email']){
      return fieldName+' should have a valid form '
    }
    else {
      return "";
    }

  }
}

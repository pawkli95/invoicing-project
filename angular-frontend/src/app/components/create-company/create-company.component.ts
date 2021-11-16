import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CompanyDto } from 'src/app/dto/company-dto';
import { CompanyService } from 'src/app/services/company.service';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-create-company',
  templateUrl: './create-company.component.html',
  styleUrls: ['./create-company.component.scss']
})
export class CreateCompanyComponent implements OnInit {

  constructor(private companyService: CompanyService, private router: Router, private toastr: ToastrService) { }

  formGroup: FormGroup = new FormGroup({
    taxIdentificationNumber: new FormControl('', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]),
    name: new FormControl('', [Validators.required, Validators.maxLength(100)]),
    address: new FormControl('', [Validators.required, Validators.maxLength(200)]),
    pensionInsurance: new FormControl('', [Validators.required, Validators.pattern("^[0-9]+(.[0-9]{0,2})?$")]),
    healthInsurance: new FormControl('', [Validators.required, Validators.pattern("^[0-9]+(.[0-9]{0,2})?$")])
  })

  ngOnInit(): void {
  }

  save(): void {
    this.companyService.saveCompany({
      ...this.formGroup.value
    }).subscribe(() => {
      this.toastr.success("Created successfully")
    },
    err => {
      this.toastr.error(err.error.message);
    }, () => {
      this.goBack()
    })
  }
  
  goBack(): void {
    this.router.navigate(['companies'])
  }

  get taxId() {
    return this.formGroup.get('taxIdentificationNumber') as FormControl
  }

  get name() {
    return this.formGroup.get('name') as FormControl
  }

  get address() {
    return this.formGroup.get('address') as FormControl
  }

  get pension() {
    return this.formGroup.get('pensionInsurance') as FormControl
  }

  get health() {
    return this.formGroup.get('healthInsurance') as FormControl
  }

}

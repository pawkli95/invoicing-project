import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CompanyDto } from 'src/app/dto/company-dto';
import { InvoiceDto } from 'src/app/dto/invoice-dto';
import { InvoiceEntry } from 'src/app/dto/invoice-entries';
import { CompanyService } from 'src/app/services/company.service';
import { InvoiceService } from 'src/app/services/invoice.service';

@Component({
  selector: 'app-create-invoice',
  templateUrl: './create-invoice.component.html',
  styleUrls: ['./create-invoice.component.scss']
})
export class CreateInvoiceComponent implements OnInit {

  constructor(private invoiceService: InvoiceService, private router: Router, private companyService: CompanyService) { }

  
companyList: Array<CompanyDto> = []

vat = {
  vat_0: 'VAT_0',
  vat_5: 'VAT_5',
  vat_8: 'VAT_8',
  vat_23: 'VAT_23'
}


  ngOnInit(): void {
    this.companyService.getCompaniesList().subscribe(data => {
      console.log(data)
      this.companyList = data;
  
    }, error => {
      console.log(error);
    })
  }

  formGroup = new FormGroup({
    number: new FormControl('', [Validators.required, Validators.maxLength(8)]),
    seller: new FormControl('', [Validators.required]),
    buyer: new FormControl('', [Validators.required]),
    invoiceEntries: new FormArray([], [Validators.required])
  })

  get entries() {
    return this.formGroup.get('invoiceEntries') as FormArray
  }

  get number() {
    return this.formGroup.get('number') as FormControl
  }

  get seller() {
    return this.formGroup.get('seller') as FormControl
  }

  get buyer() {
    return this.formGroup.get('buyer') as FormControl
  }

  addEntry() {
    const entry = new FormGroup({
      description: new FormControl('', [Validators.required]),
      price: new FormControl('', [Validators.required, Validators.pattern("^[0-9]+(.[0-9]{0,2})?$")]),
      vatRate: new FormControl('', [Validators.required]),
      personalCar: new FormControl('', [Validators.required])

    })

    this.entries.push(entry)
  }

  deleteEntry(i: number) {
    this.entries.removeAt(i)
  }

  save() {
    this.invoiceService.saveInvoice({
      ...this.formGroup.value
    }).subscribe()
    this.goBack()

  }

  goBack() {
    this.router.navigate(['invoices'])
  }



}


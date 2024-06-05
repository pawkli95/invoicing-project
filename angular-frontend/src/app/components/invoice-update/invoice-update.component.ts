import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, FormArray } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CompanyDto } from 'src/app/dto/company-dto';
import { InvoiceDto } from 'src/app/dto/invoice-dto';
import { InvoiceEntry } from 'src/app/dto/invoice-entries';
import { CompanyService } from 'src/app/services/company.service';
import { InvoiceService } from 'src/app/services/invoice.service';
import { InvoiceListComponent } from '../invoice-list/invoice-list.component';

@Component({
  selector: 'app-invoice-update',
  templateUrl: './invoice-update.component.html',
  styleUrls: ['./invoice-update.component.scss']
})
export class InvoiceUpdateComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute, private companyService: CompanyService
    , private router: Router, private invoiceService: InvoiceService) { 
    this.id = this.activatedRoute.snapshot.paramMap.get('id')
  }

  ngOnInit(): void {
    if(!!this.id)
    this.invoiceService.getInvoice(this.id).subscribe(data =>{
      this.formGroup.patchValue(data)
      this.invoice = data,
      this.updateEntries()
      this.companyService.getCompaniesList().subscribe(data => {
        this.companyList = data;
      }, error => {
        console.log(error);
      })
    })

    this.updateEntries()
  }

  updateEntries() {
    for(let e of this.invoice.invoiceEntries) {
      const entry = new FormGroup({
        description: new FormControl('', [Validators.required]),
        price: new FormControl('', [Validators.required, Validators.pattern("^[0-9]+(.[0-9]{0,2})?$")]),
        vatRate: new FormControl('', [Validators.required]),
        personalCar: new FormControl('', [Validators.required])
      })

      entry.patchValue(e)
      this.entries.push(entry)
    }
  }

  formGroup = new FormGroup({
    number: new FormControl('', [Validators.required, Validators.maxLength(8)]),
    seller: new FormControl('', [Validators.required]),
    buyer: new FormControl('', [Validators.required]),
    invoiceEntries: new FormArray([], [Validators.required])
  })

  id: string | null

  companyList: Array<CompanyDto> = []

  vat = {
    vat_0: 'VAT_0',
    vat_5: 'VAT_5',
    vat_8: 'VAT_8',
    vat_23: 'VAT_23'
  }

  invoice: InvoiceDto = {number: '',
  seller: {
    "id": '',
  "taxIdentificationNumber": '',
  "name": '',
  "address": '',
  "pensionInsurance": 0,
  "healthInsurance": 0
  },
  buyer: {
    "id": '',
  "taxIdentificationNumber": '',
  "name": '',
  "address": '',
  "pensionInsurance": 0,
  "healthInsurance": 0
  },
  invoiceEntries: [],
  id: '',
  date: '' }

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

  update() {
    console.log(this.formGroup.value)
    this.invoiceService.updateInvoice({
      id: this.id as string,
      date: this.invoice.date,
      ...this.formGroup.value
    }).subscribe()
    this.router.navigate(['invoices'])

  }

  goBack() {
    this.router.navigate(['invoices', this.id as string])
  }


}

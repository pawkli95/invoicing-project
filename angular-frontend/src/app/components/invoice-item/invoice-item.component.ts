import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { InvoiceDto } from 'src/app/dto/invoice-dto';
import { InvoiceService } from 'src/app/services/invoice.service';

@Component({
  selector: 'app-invoice-item',
  templateUrl: './invoice-item.component.html',
  styleUrls: ['./invoice-item.component.scss']
})
export class InvoiceItemComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute, private invoiceService: InvoiceService, private router: Router) { 
    this.id = this.activatedRoute.snapshot.paramMap.get('id')
  }

  id: string | null

  invoice: InvoiceDto = {
    id: '',
    number: '',
    date: '',
    seller: {
      id: '',
      taxIdentificationNumber: '',
      name: '',
      address: '',
      pensionInsurance: 0,
      healthInsurance: 0,
    },
    buyer: {
      id: '',
      taxIdentificationNumber: '',
      name: '',
      address: '',
      pensionInsurance: 0,
      healthInsurance: 0,
    },
    invoiceEntries: [] 
  }

  ngOnInit(): void {
    if(!!this.id) {
    this.invoiceService.getInvoice(this.id).subscribe(data => {
      this.invoice = data
    }, error => {
      console.log(error)
    })
    }
  }

  public goToUpdate(id: string): void {
    this.router.navigate(['invoices', 'update', id])
  }

  public goBack(): void {
    this.router.navigate(['invoices'])
  }

}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { InvoiceDto } from 'src/app/dto/invoice-dto';
import { InvoiceService } from 'src/app/services/invoice.service';

@Component({
  selector: 'app-company-invoice-list',
  templateUrl: './company-invoice-list.component.html',
  styleUrls: ['./company-invoice-list.component.scss']
})
export class CompanyInvoiceListComponent implements OnInit {

  constructor(private invoiceService: InvoiceService, private activatedRoute: ActivatedRoute, private router: Router ) {
    this.taxId = this.activatedRoute.snapshot.paramMap.get('taxId')
   }

  taxId: string | null

  invoicesAsSeller: Array<InvoiceDto> = []

  invoicesAsBuyer: Array<InvoiceDto> = []

   companyName: string = ''

  ngOnInit(): void {
    this.fetchInvoicesAsSeller()
    this.fetchInvoicesAsBuyer()
  }

  fetchInvoicesAsSeller() {
    if(!!this.taxId) {
      this.invoiceService.getInvoicesAsSeller(this.taxId).subscribe(data => {
        this.invoicesAsSeller = data
        this.getName()
      }, error => {
        console.log(error)
      })
    }
  }

  fetchInvoicesAsBuyer() {
    if(!!this.taxId) {
      this.invoiceService.getInvoicesAsBuyer(this.taxId).subscribe(data => {
        this.invoicesAsBuyer = data
      }, error => {
        console.log(error)
      })
    }
  }

  getName() {
    for(let invoice of this.invoicesAsSeller) {
      this.companyName = invoice.seller.name
      return
    }
  }

  public goToDetails(id: string) {
    this.router.navigate(['invoices', id])
  }

  public deleteInvoiceAsSeller(id: string) {
    this.invoiceService.deleteInvoice(id).subscribe(() => {
      this.fetchInvoicesAsSeller()
    })
  }

  public deleteInvoiceAsBuyer(id: string) {
    this.invoiceService.deleteInvoice(id).subscribe(() => {
      this.fetchInvoicesAsBuyer()
    })
  }

}

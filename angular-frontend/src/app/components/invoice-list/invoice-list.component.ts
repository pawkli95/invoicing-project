import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { InvoiceDto } from 'src/app/dto/invoice-dto';
import { InvoiceService } from 'src/app/services/invoice.service';

@Component({
  selector: 'app-invoice-list',
  templateUrl: './invoice-list.component.html',
  styleUrls: ['./invoice-list.component.scss']
})
export class InvoiceListComponent implements OnInit {

  constructor(private invoiceService: InvoiceService, private router: Router) { }

  invoices: Array<InvoiceDto> = []

  ngOnInit(): void {
    this.fetchData()
  }

  public goToDetails(id: string) {
    this.router.navigate(['invoices', id])
  }

  public delete(id: string) {
    this.invoiceService.deleteInvoice(id).subscribe(() => {
      this.fetchData()
    })
  }

  public goToCreate(): void {
    this.router.navigate(['invoices', 'new'])
  }

  fetchData(): void {
    this.invoiceService.getInvoices().subscribe(data => {
      this.invoices = data
    }, error => {
      console.log(error)
    })
  }

}

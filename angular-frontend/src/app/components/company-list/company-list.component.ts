import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CompanyDto } from 'src/app/dto/company-dto';
import { InvoiceDto } from 'src/app/dto/invoice-dto';
import { CompanyService } from 'src/app/services/company.service';
import { InvoiceService } from 'src/app/services/invoice.service';

@Component({
  selector: 'app-company-list',
  templateUrl: './company-list.component.html',
  styleUrls: ['./company-list.component.scss']
})
export class CompanyListComponent implements OnInit {

  constructor(private companyService: CompanyService, private router: Router, private invoiceService: InvoiceService,
      private toastr: ToastrService) { }

  list: Array<CompanyDto> = []

  invoices: Array<InvoiceDto> = []

  searchText: string = ''

  ngOnInit(): void {
    this.fetchData()
    this.invoiceService.getInvoices().subscribe(data => {
      this.invoices = data
    }, error => {
      console.log(error)
    })
  }

  public goToCompanyDetails(id: string): void {
    this.router.navigate(["companies", id])
  }

  public delete(id: string): void {
    this.companyService.deleteCompany(id).subscribe(() => {
      this.fetchData()
    }, err => {
      console.log(err.error)
      this.toastr.error(err.error.message)
    })
  }

  public goToTaxReport(taxId: string) {
    this.router.navigate(['tax', taxId ])
  }

  public goToSave(): void {
    this.router.navigate(['companies', 'new'])
  }

  public fetchData(): void {
    this.companyService.getCompaniesList().subscribe(data => {
      this.list = data;
    }, error => {
      console.log(error)
    });
  }

  isListEmpty(): boolean {
    return this.list.length === 0
  }
}




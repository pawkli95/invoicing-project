import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyInvoiceListComponent } from './components/company-invoice-list/company-invoice-list.component';
import {CompanyListComponent} from "./components/company-list/company-list.component"
import { CompanyComponent } from './components/company/company.component';
import { CreateCompanyComponent } from './components/create-company/create-company.component';
import { CreateInvoiceComponent } from './components/create-invoice/create-invoice.component';
import { InvoiceItemComponent } from './components/invoice-item/invoice-item.component';
import { InvoiceListComponent } from './components/invoice-list/invoice-list.component';
import { InvoiceUpdateComponent } from './components/invoice-update/invoice-update.component';
import { TaxReportComponent } from './components/tax-report/tax-report.component';

const routes: Routes = [
{
  path: "companies",
  component: CompanyListComponent
},
{ 
  path: "companies/new",
  component: CreateCompanyComponent
},
{
  path: "companies/invoices/:taxId",
  component: CompanyInvoiceListComponent
},
{
  path: "companies/:id",
  component: CompanyComponent
},
{
  path: "invoices",
  component: InvoiceListComponent
},
{
  path: "invoices/new",
  component: CreateInvoiceComponent
},
{
  path: "invoices/update/:id",
  component: InvoiceUpdateComponent
},
{
  path:"invoices/:id",
  component: InvoiceItemComponent
},
{
  path:"tax/:taxId",
  component: TaxReportComponent
}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

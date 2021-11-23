import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PrivateAuthGuard} from './auth/auth-guards/private-auth.guard';
import { CompanyInvoiceListComponent } from './components/company-invoice-list/company-invoice-list.component';
import {CompanyListComponent} from "./components/company-list/company-list.component"
import { CompanyComponent } from './components/company/company.component';
import { CreateCompanyComponent } from './components/create-company/create-company.component';
import { CreateInvoiceComponent } from './components/create-invoice/create-invoice.component';
import { HomeComponent } from './components/home/home.component';
import { InvoiceItemComponent } from './components/invoice-item/invoice-item.component';
import { InvoiceListComponent } from './components/invoice-list/invoice-list.component';
import { InvoiceUpdateComponent } from './components/invoice-update/invoice-update.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { TaxReportComponent } from './components/tax-report/tax-report.component';
import { UserListComponent } from './components/user-list/user-list.component';

const routes: Routes = [
{
  path: "",
  component: HomeComponent

},
{
  path: "companies",
  component: CompanyListComponent,
  canActivate: [PrivateAuthGuard],
  data: {
    roles: 'USER'
  }
},
{ 
  path: "companies/new",
  component: CreateCompanyComponent,
  canActivate: [PrivateAuthGuard],
  data: {
    roles: 'USER'
  }
},
{
  path: "companies/invoices/:taxId",
  component: CompanyInvoiceListComponent,
  canActivate: [PrivateAuthGuard],
  data: {
    roles: 'USER'
  }
},
{
  path: "companies/:id",
  component: CompanyComponent,
  canActivate: [PrivateAuthGuard],
  data: {
    roles: 'USER'
  }
},
{
  path: "invoices",
  component: InvoiceListComponent,
  canActivate: [PrivateAuthGuard],
  data: {
    roles: 'USER'
  }
},
{
  path: "invoices/new",
  component: CreateInvoiceComponent,
  canActivate: [PrivateAuthGuard],
  data: {
    roles: 'USER'
  }
},
{
  path: "invoices/update/:id",
  component: InvoiceUpdateComponent,
  canActivate: [PrivateAuthGuard],
  data: {
    roles: 'USER'
  }
},
{
  path:"invoices/:id",
  component: InvoiceItemComponent,
  canActivate: [PrivateAuthGuard],
  data: {
    roles: 'USER'
  }
},
{
  path:"tax/:taxId",
  component: TaxReportComponent,
  canActivate: [PrivateAuthGuard],
  data: {
    roles: 'USER'
  }
},
{
  path: "register",
  component: RegisterComponent,
},
{
  path: "login",
  component: LoginComponent
},
{
  path: "admin",
  component: UserListComponent,
  canActivate: [PrivateAuthGuard],
  data: {
    roles: 'ADMIN'
  }
},
{ path: '**', redirectTo: '' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

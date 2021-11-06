import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CompanyListComponent } from './components/company-list/company-list.component';
import {HttpClientModule} from "@angular/common/http";
import { CompanyComponent } from './components/company/company.component'
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CreateCompanyComponent } from './components/create-company/create-company.component';
import { CreateInvoiceComponent } from './components/create-invoice/create-invoice.component';
import { FormsModule } from '@angular/forms';
import { InvoiceItemComponent } from './components/invoice-item/invoice-item.component';
import { InvoiceListComponent } from './components/invoice-list/invoice-list.component';
import { InvoiceUpdateComponent } from './components/invoice-update/invoice-update.component';
import { TaxReportComponent } from './components/tax-report/tax-report.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import { CompanyPipe } from './pipes/company.pipe';
import { CompanyInvoiceListComponent } from './components/company-invoice-list/company-invoice-list.component';


@NgModule({
  declarations: [
    AppComponent,
    CompanyListComponent,
    CompanyComponent,
    CreateCompanyComponent,
    CreateInvoiceComponent,
    InvoiceItemComponent,
    InvoiceListComponent,
    InvoiceUpdateComponent,
    TaxReportComponent,
    CompanyPipe,
    CompanyInvoiceListComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

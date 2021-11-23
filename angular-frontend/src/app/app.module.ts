import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CompanyListComponent } from './components/company-list/company-list.component';
import {HttpClientModule, HTTP_INTERCEPTORS} from "@angular/common/http";
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
import { InvoicePipe } from './pipes/invoice.pipe';
import { ToastrModule } from 'ngx-toastr';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { JwtInterceptor } from './auth/interceptors/jwt.interceptor';
import { UserListComponent } from './components/user-list/user-list.component';


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
    InvoicePipe,
    RegisterComponent,
    LoginComponent,
    HomeComponent,
    UserListComponent,
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
    ToastrModule.forRoot()
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

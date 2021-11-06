import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'angular-frontend';

  constructor(private router: Router) {}

  public goToCompanies(): void {
    this.router.navigate(['companies'])
  }

  public goToInvoices(): void {
    this.router.navigate(['invoices'])
  }
 }

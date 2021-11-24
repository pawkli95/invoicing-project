import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'angular-frontend';

  constructor(private router: Router,
    private authenticationService: AuthenticationService) {}

  public goToCompanies(): void {
    this.router.navigate(['companies'])
  }

  public goToInvoices(): void {
    this.router.navigate(['invoices'])
  }

  public goToLogin(): void {
    this.router.navigate(['login'])
  }

  public goToRegister(): void {
    this.router.navigate(['register'])
  }

  public goToUsers() {
    this.router.navigate(['admin'])
  }

  public goToHome() {
    this.router.navigate([''])
  }

  public logout() {
    this.authenticationService.logout()
    this.goToHome()
  }

  get isLogged() {
    return this.authenticationService.isLogged()
  }

  get role() {
    return this.authenticationService.getRole
  }

  get isUser() {
    if(this.isLogged && this.role === 'USER') {
      return true
    }
    return false
  }

  get isAdmin() {
    if(this.isLogged && this.role === 'ADMIN') {
      return true
    }
    return false
  }
 }

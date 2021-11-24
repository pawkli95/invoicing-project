import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class PrivateAuthGuard implements CanActivate {

  constructor(private authenticationService: AuthenticationService, private router: Router){}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    let role = this.authenticationService.getRole
    let isLogged = this.authenticationService.isLogged()
    if(role && isLogged) {
      if (route.data['roles'] && route.data['roles'].indexOf(role) === -1) {
        // role not authorised so redirect to home page
        this.router.navigate(['/']);
        return false;
    }

    return true
    }
    this.router.navigate(['login'])
    return false
  }
  
}

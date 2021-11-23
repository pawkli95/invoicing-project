import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment.prod';
import { AuthResponse } from '../dto/auth-response';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private url: string = environment.apiBaseUrl + 'api/auth'
  

  constructor(private http: HttpClient, private toastr: ToastrService) { 
  
  }

  public login(username: string, password: string) {
    return this.http.post<any>(`${this.url}/login`, {username, password})
    .pipe(map(user => {
      let token = 'Bearer ' + user.token
      localStorage.setItem('token', token)
      console.log(token)
      let role = user.role.authority
      localStorage.setItem('role', role)
      console.log(role)
    }))
  }

  isLogged() {
    if(localStorage.getItem('token')) {
      return true
    }
    return false
  }

  get getRole() {
    return localStorage.getItem('role')
  }

  public logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('role')
  }
}

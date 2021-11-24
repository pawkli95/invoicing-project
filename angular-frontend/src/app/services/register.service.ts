import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { Role } from '../dto/role';
import { UserDto } from '../dto/user-dto';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private url: string = environment.apiBaseUrl + '/api/users'

  constructor(private http: HttpClient) { }

  public register(user: UserDto): Observable<UserDto> {
     return this.http.post<UserDto>(`${this.url}/register`, user)
  }

  public getRoles(): Observable<Array<Role>> {
    return this.http.get<Array<Role>>(`${this.url}/roles`)
  }
}

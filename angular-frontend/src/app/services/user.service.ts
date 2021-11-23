import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { UserDto } from '../dto/user-dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url: string = environment.apiBaseUrl + '/api/users'

  constructor(private http: HttpClient) { }

  public getUsers(): Observable<Array<UserDto>> {
    return this.http.get<Array<UserDto>>(`${this.url}`)
  }

  public deleteUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`)
  }
}

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http"
import { CompanyDto } from '../dto/company-dto';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  private url: string = environment.apiBaseUrl + "/companies"

  constructor(private httpClient: HttpClient) { }

  public getCompaniesList(): Observable<Array<CompanyDto>> {
    return this.httpClient.get<Array<CompanyDto>>(`${this.url}`)
  }

  public getCompany(id: string): Observable<CompanyDto> {
    return this.httpClient.get<CompanyDto>(`${this.url}/${id}`)
  }

  public updateCompany(data: CompanyDto): Observable<CompanyDto> {
    return this.httpClient.put<CompanyDto>(`${this.url}`, data)
  }

  public deleteCompany(id: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.url}/${id}`)
  }

  public saveCompany(data: CompanyDto): Observable<CompanyDto> {
    return this.httpClient.post<CompanyDto>(`${this.url}`, data)
  }

 
}

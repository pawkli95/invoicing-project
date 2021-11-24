import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { TaxCalculation } from '../dto/tax-report';

@Injectable({
  providedIn: 'root'
})
export class TaxReportService {

  private url: string = environment.apiBaseUrl + "/api/tax"

  constructor(private httpClient: HttpClient) { }

  public calculateTax(taxId: string): Observable<TaxCalculation> {
    console.log(taxId)
    return this.httpClient.get<TaxCalculation>(`${this.url}/${taxId}`)
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { InvoiceDto } from '../dto/invoice-dto';
import { InvoiceEntry } from '../dto/invoice-entries';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {

  private url: string = environment.apiBaseUrl + "/invoices"

  constructor(private httpClient: HttpClient) { }

  public saveInvoice(data: InvoiceDto): Observable<InvoiceDto> {
    return this.httpClient.post<InvoiceDto>(`${this.url}`, data)
  }

  public getInvoices(): Observable<Array<InvoiceDto>> {
  return this.httpClient.get<Array<InvoiceDto>>(`${this.url}`)
  }

  public getInvoice(id: string): Observable<InvoiceDto> {
    return this.httpClient.get<InvoiceDto>(`${this.url}/${id}`)
  }

  public deleteInvoice(id: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.url}/${id}`)
  }

  public updateInvoice(data: InvoiceDto): Observable<InvoiceDto> {
    return this.httpClient.put<InvoiceDto>(`${this.url}`, data)
  }

  public getInvoicesAsSeller(taxId: string): Observable<Array<InvoiceDto>> {
    return this.httpClient.get<Array<InvoiceDto>>(`${this.url}?sellerTaxId=${taxId}`)
  }

  public getInvoicesAsBuyer(taxId: string): Observable<Array<InvoiceDto>> {
    return this.httpClient.get<Array<InvoiceDto>>(`${this.url}?buyerTaxId=${taxId}`)
  }

}

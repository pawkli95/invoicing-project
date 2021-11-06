import { Timestamp } from "rxjs";
import { CompanyDto } from "./company-dto";
import { InvoiceEntry } from "./invoice-entries";

export interface InvoiceDto {
    id: string,
    number: string,
    date: string,
    seller: CompanyDto,
    buyer: CompanyDto,
    invoiceEntries: Array<InvoiceEntry>
}
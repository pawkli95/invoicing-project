export interface InvoiceEntry {
    id: string,
    description: string,
    price: number,
    vatRate: string,
    vatValue: number,
    personalCar: boolean
}
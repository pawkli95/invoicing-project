import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'invoice'
})
export class InvoicePipe implements PipeTransform {

  transform(items: any[], searchText: string): any[] {
    if(!items) {
      return []
    }

    if(!searchText) {
      return items
    }

    searchText = searchText.toLocaleLowerCase()

    return items.filter(it => {
      return it.number.includes(searchText) || it.seller.taxIdentificationNumber.includes(searchText)
      || it.seller.name.toLowerCase().includes(searchText) || it.buyer.taxIdentificationNumber.includes(searchText)
      || it.buyer.name.toLowerCase().includes(searchText) || it.date.includes(searchText)

    })
  }

}

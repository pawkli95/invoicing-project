import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'company'
})
export class CompanyPipe implements PipeTransform {

  transform(items: any[], searchText: string): any[] {
    if(!items) {
      return []
    }

    if(!searchText) {
      return items
    }

    searchText = searchText.toLocaleLowerCase()

    return items.filter(it => {
      return it.name.toLowerCase().includes(searchText) || it.taxIdentificationNumber.includes(searchText)
    })
  }

}

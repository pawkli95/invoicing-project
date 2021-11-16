import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TaxCalculation } from 'src/app/dto/tax-report';
import { TaxReportService } from 'src/app/services/tax-report.service';

@Component({
  selector: 'app-tax-report',
  templateUrl: './tax-report.component.html',
  styleUrls: ['./tax-report.component.scss']
})
export class TaxReportComponent implements OnInit {

  constructor(private taxService: TaxReportService, private activatedRoute: ActivatedRoute, private router: Router) {
    this.taxId = this.activatedRoute.snapshot.paramMap.get('taxId')
   }

   taxId: string | null 

   taxReport: TaxCalculation = {
    income: 0,
    costs: 0,
    incomeMinusCosts: 0,
    pensionInsurance: 0,
    incomeMinusCostsMinusPensionInsurance: 0,
    taxCalculationBase: 0,
    incomeTax: 0,
    healthInsurance775: 0,
    incomeTaxMinusHealthInsurance: 0,
    finalIncomeTaxValue: 0,
    incomingVat: 0,
    outgoingVat: 0,
    vatToReturn: 0
   }

  ngOnInit(): void {
    if(!!this.taxId) {
    this.taxService.calculateTax(this.taxId).subscribe(data => {
      console.log(data)
      this.taxReport = data
      console.log(this.taxReport)
    }, error => {
      console.log(error)
    })
  }
}

public goBack(): void {
  this.router.navigate(['companies'])
}
  
}

export interface TaxCalculation {
    income: number,
    costs: number,
    incomeMinusCosts: number,
    pensionInsurance: number,
    incomeMinusCostsMinusPensionInsurance: number,
    taxCalculationBase: number,
    incomeTax: number,
    healthInsurance775: number,
    incomeTaxMinusHealthInsurance: number,
    finalIncomeTaxValue: number,
    incomingVat: number,
    outgoingVat: number,
    vatToReturn: number
}
package pl.futurecollars.invoicing.service.taxCalculatorService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.fixtures.InvoiceEntryFixture
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.dto.TaxCalculation
import pl.futurecollars.invoicing.service.TaxCalculatorService
import spock.lang.Shared
import spock.lang.Specification
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("jpaTest")
class TaxCalculatorServiceIntegrationTest extends Specification {

    @Autowired
    Database<Invoice> invoiceDatabase

    @Autowired
    Database<Company> companyDatabase

    @Autowired
    TaxCalculatorService taxCalculatorService

    @Shared
    Company company1 = CompanyFixture.getCompany()

    def "should calculate tax without personal car expenses"() {
        given:
        clearDatabase()
        addInvoicesWithoutPersonalCarEntries()

        when:
        TaxCalculation taxCalculation = taxCalculatorService.getTaxCalculation(company1.getTaxIdentificationNumber())

        then:
        taxCalculation.getIncome() == BigDecimal.valueOf(4200)
        taxCalculation.getCosts() == BigDecimal.valueOf(2000)
        taxCalculation.getIncomeMinusCosts() == BigDecimal.valueOf(2200)
        taxCalculation.getIncomingVat() == BigDecimal.valueOf(966)
        taxCalculation.getOutgoingVat() == BigDecimal.valueOf(460)
        taxCalculation.getVatToReturn() == BigDecimal.valueOf(506)
        taxCalculation.getPensionInsurance() == BigDecimal.valueOf(500)
        taxCalculation.getIncomeMinusCostsMinusPensionInsurance() == BigDecimal.valueOf(1700)
        taxCalculation.getTaxCalculationBase() == BigDecimal.valueOf(1700)
        taxCalculation.getIncomeTax() == BigDecimal.valueOf(323)
        taxCalculation.getHealthInsurance9() == BigDecimal.valueOf(90)
        taxCalculation.getHealthInsurance775() == BigDecimal.valueOf(77.5)
        taxCalculation.getIncomeTaxMinusHealthInsurance() == BigDecimal.valueOf(245.5)
        taxCalculation.getFinalIncomeTaxValue() == BigDecimal.valueOf(245)
    }

    def "should calculate tax with personal car expenses"() {
        given:
        clearDatabase()
        addInvoicesWithPersonalCarEntries()

        when:
        TaxCalculation taxCalculation = taxCalculatorService.getTaxCalculation(company1.getTaxIdentificationNumber())

        then:
        taxCalculation.getIncome() == BigDecimal.valueOf(4200)
        taxCalculation.getCosts() == BigDecimal.valueOf(2138)
        taxCalculation.getIncomeMinusCosts() == BigDecimal.valueOf(2062)
        taxCalculation.getIncomingVat() == BigDecimal.valueOf(966)
        taxCalculation.getOutgoingVat() == BigDecimal.valueOf(322)
        taxCalculation.getVatToReturn() == BigDecimal.valueOf(644)
        taxCalculation.getPensionInsurance() == BigDecimal.valueOf(500)
        taxCalculation.getIncomeMinusCostsMinusPensionInsurance() == BigDecimal.valueOf(1562)
        taxCalculation.getTaxCalculationBase() == BigDecimal.valueOf(1562)
        taxCalculation.getIncomeTax() == BigDecimal.valueOf(296.78)
        taxCalculation.getHealthInsurance9() == BigDecimal.valueOf(90)
        taxCalculation.getHealthInsurance775() == BigDecimal.valueOf(77.5)
        taxCalculation.getIncomeTaxMinusHealthInsurance() == BigDecimal.valueOf(219.28)
        taxCalculation.getFinalIncomeTaxValue() == BigDecimal.valueOf(219)
    }

    def "should throw NoSuchElementException when tax id is not in database"() {
        given:
        clearDatabase()

        when:
        taxCalculatorService.getTaxCalculation(company1.getTaxIdentificationNumber())

        then:
        thrown(NoSuchElementException)
    }

    void addInvoicesWithPersonalCarEntries() {
        Company company2 = CompanyFixture.getCompany()
        Company c1 = companyDatabase.save(company1)
        Company c2 = companyDatabase.save(company2)
        Invoice invoice1 = new Invoice(UUID.randomUUID(), "number1", LocalDateTime.now(), c1, c2, InvoiceEntryFixture.getInvoiceEntryListWithPersonalCar(6))
        Invoice invoice2 = new Invoice(UUID.randomUUID(), "number2", LocalDateTime.now(), c2, c1, InvoiceEntryFixture.getInvoiceEntryListWithPersonalCar(4))
        invoiceDatabase.save(invoice1)
        invoiceDatabase.save(invoice2)
    }

    void addInvoicesWithoutPersonalCarEntries() {
        Company company2 = CompanyFixture.getCompany()
        Company c1 = companyDatabase.save(company1)
        Company c2 = companyDatabase.save(company2)
        Invoice invoice1 = new Invoice(UUID.randomUUID(), "number1", LocalDateTime.now(), c1, c2, InvoiceEntryFixture.getInvoiceEntryListWithoutPersonalCar(6))
        Invoice invoice2 = new Invoice(UUID.randomUUID(), "number2", LocalDateTime.now(), c2, c1, InvoiceEntryFixture.getInvoiceEntryListWithoutPersonalCar(4))
        invoiceDatabase.save(invoice1)
        invoiceDatabase.save(invoice2)
    }

    void clearDatabase() {
        deleteInvoices()
        deleteCompanies()
    }

    void deleteInvoices() {
        List<Invoice> list = invoiceDatabase.getAll()
        for(Invoice i : list) {
            invoiceDatabase.delete(i.getId())
        }
    }

    void deleteCompanies() {
        List<Company> list = companyDatabase.getAll()
        for(Company c : list) {
            companyDatabase.delete(c.getId())
        }
    }
}

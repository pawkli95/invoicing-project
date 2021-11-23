package pl.futurecollars.invoicing.controller.taxCalculatorController

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.dto.CompanyDto
import pl.futurecollars.invoicing.dto.InvoiceDto
import pl.futurecollars.invoicing.dto.mappers.CompanyMapper
import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.fixtures.InvoiceEntryFixture
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.dto.TaxCalculation
import spock.lang.Shared
import spock.lang.Specification
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@WithMockUser(authorities = "USER")
class TaxCalculatorControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    JacksonTester<InvoiceDto> invoiceJsonService

    @Autowired
    JacksonTester<TaxCalculation> taxCalculationJsonService

    @Autowired
    JacksonTester<List<InvoiceDto>> invoiceListJsonService

    @Autowired
    JacksonTester<CompanyDto> companyJsonService

    @Autowired
    JacksonTester<List<CompanyDto>> companyListJsonService

    @Shared
    CompanyDto company1 = CompanyFixture.getCompanyDto()

    def "should calculate tax without personal car expenses"() {
        given:
        clearDatabase()
        addInvoicesWithoutPersonalCarEntries()

        when:
        def response = mockMvc
                .perform(get("/api/tax/" + company1.getTaxIdentificationNumber()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        TaxCalculation taxCalculation = taxCalculationJsonService.parseObject(response)
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
        deleteInvoices()
        addInvoicesWithPersonalCarEntries()

        when:
        def response = mockMvc
                .perform(get("/api/tax/" + company1.getTaxIdentificationNumber()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        TaxCalculation taxCalculation = taxCalculationJsonService.parseObject(response)
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

    def "should return 404 NotFound http status when tax id doesn't exist"() {
        given:
        clearDatabase()
        String invalidTaxId = "1234567890"

        expect:
                 mockMvc
                .perform(get("/api/tax/" + invalidTaxId))
                .andExpect(status().isNotFound())
    }

    void addInvoicesWithPersonalCarEntries() {
        CompanyDto company2 = CompanyFixture.getCompanyDto()
        company1 = addCompany(company1)
        company2 = addCompany(company2)
        InvoiceDto invoice1 = new InvoiceDto(UUID.randomUUID(), "number1", LocalDate.now(), company1, company2, InvoiceEntryFixture.getInvoiceEntryListWithPersonalCar(6))
        InvoiceDto invoice2 = new InvoiceDto(UUID.randomUUID(), "number2", LocalDate.now(), company2, company1, InvoiceEntryFixture.getInvoiceEntryListWithPersonalCar(4))
        addInvoice(invoice1)
        addInvoice(invoice2)
    }

    void addInvoicesWithoutPersonalCarEntries() {
        CompanyDto company2 = CompanyFixture.getCompanyDto()
        company1 = addCompany(company1)
        company2 = addCompany(company2)
        InvoiceDto invoice1 = new InvoiceDto(UUID.randomUUID(), "number1", LocalDate.now(), company1, company2, InvoiceEntryFixture.getInvoiceEntryListWithoutPersonalCar(6))
        InvoiceDto invoice2 = new InvoiceDto(UUID.randomUUID(), "number2", LocalDate.now(), company2, company1, InvoiceEntryFixture.getInvoiceEntryListWithoutPersonalCar(4))
        addInvoice(invoice1)
        addInvoice(invoice2)
    }

    void addInvoice(InvoiceDto invoice) {
        String jsonString = invoiceJsonService.write(invoice).getJson()
        mockMvc
                .perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
    }

     CompanyDto addCompany(CompanyDto companyDto) {
        String jsonString = companyJsonService.write(companyDto).getJson()
        def response = mockMvc
                .perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andReturn()
                .getResponse()
                .getContentAsString()
            return companyJsonService.parseObject(response)

    }

    void clearDatabase() {
        deleteInvoices()
        deleteCompanies()
    }

    void deleteInvoices() {
        def response = mockMvc
                .perform(get("/api/invoices"))
                .andReturn()
                .getResponse()
                .getContentAsString()
        List<InvoiceDto> list = invoiceListJsonService.parseObject(response)
        for(InvoiceDto i : list) {
            deleteInvoice(i.getId())
        }
    }

    void deleteInvoice(UUID id) {
        mockMvc
                .perform(delete("/api/invoices/" + id.toString()))
    }

    void deleteCompanies() {
        def response = mockMvc
                .perform(get("/api/companies"))
                .andReturn()
                .getResponse()
                .getContentAsString()
        List<CompanyDto> list = companyListJsonService.parseObject(response)
        for(CompanyDto c : list) {
            deleteCompany(c.getId())
        }
    }

    void deleteCompany(UUID id) {
        mockMvc.perform(delete("/api/companies/" + id.toString()))
    }


}

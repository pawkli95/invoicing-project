package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.PostgreSQLContainer
import pl.futurecollars.invoicing.dto.InvoiceDto
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.repository.InvoiceRepository
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Subject
import java.util.stream.Collectors
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Stepwise
@ActiveProfiles("testcontainer")
@WithMockUser(authorities = "USER")
class InvoiceControllerStepwiseTest extends Specification {

    @Subject.Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")

    static {
        postgreSQLContainer.start()
        System.setProperty("DB_PORT", String.valueOf(postgreSQLContainer.getFirstMappedPort()))
    }

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private InvoiceRepository invoiceRepository

    @Autowired
    JacksonTester<InvoiceDto> jsonInvoiceService

    @Autowired
    JacksonTester<List<InvoiceDto>> jsonInvoiceListService

    @Shared InvoiceDto invoiceDto = InvoiceFixture.getInvoiceDto(1)
    @Shared InvoiceDto updatedInvoiceDto = InvoiceFixture.getInvoiceDto(1)


    def "should return empty list"() {
        given:
        deleteAllInvoices()

        when:
        def response = mockMvc
                .perform(get("/api/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        response == "[]"

    }

    def "should save invoice"() {
        given:
        String jsonString = jsonInvoiceService.write(invoiceDto).getJson()

        when:
        def response = mockMvc
                .perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString()

        def responseInvoice = jsonInvoiceService.parseObject(response)
        invoiceDto.setId(responseInvoice.getId())
        invoiceDto.getSeller().setId(responseInvoice.getSeller().getId())
        invoiceDto.getBuyer().setId(responseInvoice.getBuyer().getId())
        invoiceDto.getInvoiceEntries().get(0).setId(responseInvoice.getInvoiceEntries().get(0).getId())


        then:
        invoiceDto == responseInvoice
    }

    def "should return invoice by id"() {
        given:
        UUID id = invoiceDto.getId()

        when:
        def response = mockMvc
                .perform(get("/api/invoices/" + id.toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        jsonInvoiceService.parseObject(response) == invoiceDto

    }

    def "should return list of invoices"() {
        when:
        def response = mockMvc
                .perform(get("/api/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        def invoices = jsonInvoiceListService.parseObject(response)
        invoices.size() == 1
        invoices[0] == invoiceDto
    }

    def "should return 404 NotFound status when getting invoice by id which doesn't exist"() {
        given:
        UUID invalidId = UUID.randomUUID()

        expect:
        mockMvc
                .perform(get("/api/invoices/" + invalidId))
                .andExpect(status().isNotFound())
    }

    def "should filter the database"() {
        given:
        String sellerTaxId = invoiceDto.getSeller().getTaxIdentificationNumber()
        String buyerTaxId = invoiceDto.getBuyer().getTaxIdentificationNumber()

        when:
        def response = mockMvc
                .perform(get("/api/invoices")
                        .queryParam("sellerTaxId", sellerTaxId)
                        .queryParam("buyerTaxId", buyerTaxId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        def invoices = jsonInvoiceListService.parseObject(response)
        invoices.size() == 1
        invoices[0] == invoiceDto
    }

    def "should update the invoice"() {
        given:
        updatedInvoiceDto.setId(invoiceDto.getId())
        String updatedJsonString = jsonInvoiceService.write(updatedInvoiceDto).getJson()

        when:
        def response = mockMvc
                .perform(put("/api/invoices/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJsonString))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
        def responseInvoice = jsonInvoiceService.parseObject(response)
        updatedInvoiceDto.getSeller().setId(responseInvoice.getSeller().getId())
        updatedInvoiceDto.getBuyer().setId(responseInvoice.getBuyer().getId())
        updatedInvoiceDto.getInvoiceEntries().get(0).setId(responseInvoice.getInvoiceEntries().get(0).getId())

        then:
        responseInvoice == updatedInvoiceDto

    }

    def "should return updated invoice by id"() {
        given:
        UUID id = invoiceDto.getId()

        when:
        def response = mockMvc
                .perform(get("/api/invoices/" + id.toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        jsonInvoiceService.parseObject(response) == updatedInvoiceDto
    }

    def "should return 404 Not Found when trying to update nonexistent invoice"() {
        given:
        InvoiceDto invalidInvoiceDto = InvoiceFixture.getInvoiceDto(1)
        String jsonString = jsonInvoiceService.write(invalidInvoiceDto).getJson()

        expect:
        mockMvc
                    .perform(put("/api/invoices")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonString))
                    .andExpect(status().isNotFound())
    }

    def "should delete invoice by id"() {
        given:
        UUID id = invoiceDto.getId()

        expect:
        mockMvc
                .perform(delete("/api/invoices/" + id.toString()))
                .andExpect(status().isAccepted())

        and:
        mockMvc
                .perform(delete("/api/invoices/" + id.toString()))
                .andExpect(status().isNotFound())

        and:
        mockMvc
                .perform(get("/api/invoices/" + id.toString()))
                .andExpect(status().isNotFound())
    }

    private List<InvoiceDto> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .map(i -> invoiceMapper.toDto(i))
                .collect(Collectors.toList())
    }

    private void deleteAllInvoices() {
        invoiceRepository.deleteAll()
    }
}

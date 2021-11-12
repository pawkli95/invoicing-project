package pl.futurecollars.invoicing.service.invoiceService

import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.dto.InvoiceDto
import pl.futurecollars.invoicing.dto.mappers.InvoiceMapper
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.service.InvoiceService
import spock.lang.Specification
import java.util.function.Predicate

@ActiveProfiles("test")
@SpringBootTest
class InvoiceServiceIntegrationTest extends Specification {

    @Autowired
    Database<Invoice> database

    @Autowired
    InvoiceService invoiceService

    InvoiceDto invoiceDto = InvoiceFixture.getInvoiceDto(1)

    InvoiceMapper invoiceMapper = Mappers.getMapper(InvoiceMapper.class)

    def setup() {
        clearDatabase()
    }

    def "should save invoice to database"() {
        when: "we ask invoice service to save invoice"
        InvoiceDto returnedInvoiceDto = invoiceService.saveInvoice(invoiceDto)

        then: "invoice is saved in database"
        Invoice response = database.getById(returnedInvoiceDto.getId())
        InvoiceDto responseDto = invoiceMapper.toDto(response)
        responseDto == returnedInvoiceDto
    }

    def "should get invoice by id from database"() {
        given: "invoice saved to database"
        Invoice invoice = invoiceMapper.toEntity(invoiceDto)
        Invoice returnedInvoice = database.save(invoice)
        InvoiceDto returnedInvoiceDto = invoiceMapper.toDto(returnedInvoice)

        when: "we ask invoice service for invoice by id"
        InvoiceDto responseDto = invoiceService.getById(returnedInvoice.getId())

        then: "invoice is returned"
        returnedInvoiceDto == responseDto
    }

    def "should throw exception when id is not used"() {
        when: "we ask invoice service to get nonexistent invoice"
        invoiceService.getById(UUID.randomUUID())

        then: "exception is thrown"
        thrown(NoSuchElementException)
    }

    def "should get list of invoices"() {
        given: "invoice saved to database"
        InvoiceDto returnedInvoiceDto = invoiceService.saveInvoice(invoiceDto)

        when: "we ask invoice service for a list of all invoices"
        List<InvoiceDto> invoiceList = invoiceService.getAll()

        then: "list od invoices is returned"
        invoiceList.size() == 1
        invoiceList.get(0) == returnedInvoiceDto
    }

    def "should get an empty list of invoices"() {
        expect:
        invoiceService.getAll().isEmpty()
    }

    def "should filter database"() {
        given: "a list of invoices and a Predicate"
        Invoice invoice = invoiceMapper.toEntity(invoiceDto)
        Invoice returnedInvoice = database.save(invoice)
        String taxId = invoice.getSeller().getTaxIdentificationNumber()
        Predicate<Invoice> invoicePredicate = (Invoice i) -> i.getSeller().getTaxIdentificationNumber().equals(taxId)
        InvoiceDto returnedInvoiceDto = invoiceMapper.toDto(returnedInvoice)

        when: "we ask invoice service to filter the database based on Predicate"
        List<InvoiceDto> invoiceList = invoiceService.filter(invoicePredicate)

        then: "database is filtered"
        invoiceList.size() == 1
        invoiceList.get(0) == returnedInvoiceDto
    }

        def "should update invoice in database"() {
            given: "invoice saved to database"
            Invoice invoice = invoiceMapper.toEntity(invoiceDto)
            Invoice returnedInvoice = database.save(invoice)

            and: "updated invoice"
            InvoiceDto updatedInvoice = InvoiceFixture.getInvoiceDto(1)
            updatedInvoice.setId(returnedInvoice.getId())

            when: "we ask invoice service to update database"
            InvoiceDto returnedInvoiceDto = invoiceService.updateInvoice(updatedInvoice)

            then: "database is updated"
            InvoiceDto responseDto = invoiceService.getById(returnedInvoiceDto.getId())
            responseDto == returnedInvoiceDto
        }

        def "should throw exception when updating nonexistent invoice"() {
            when: "we ask invoice service to update nonexistent invoice"
            invoiceService.updateInvoice(invoiceDto)

            then: "exception is thrown"
            thrown(NoSuchElementException)
        }

        def "should delete invoice from database"() {
            given: "invoice saved to database"
            Invoice invoice = InvoiceFixture.getInvoice(1)
            Invoice returnedInvoice = database.save(invoice)

            when: "we ask invoice service to delete invoice"
            invoiceService.deleteInvoice(returnedInvoice.getId())

            then: "database is empty"
            database.getAll().isEmpty()
        }

        def "should throw exception when deleting nonexistent invoice"() {
            when: "we ask invoice service to delete nonexistent invoice"
            invoiceService.deleteInvoice(UUID.randomUUID())

            then: "exception is thrown"
            thrown(NoSuchElementException)
        }

        def clearDatabase() {
            for (Invoice invoice : database.getAll()) {
                database.delete(invoice.getId())
            }
        }
}


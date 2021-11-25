package pl.futurecollars.invoicing.service.unitTests

import org.mapstruct.factory.Mappers

import pl.futurecollars.invoicing.repository.InvoiceRepository
import pl.futurecollars.invoicing.dto.InvoiceDto
import pl.futurecollars.invoicing.dto.mappers.InvoiceMapper
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.helpers.FilterParameters
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.InvoiceService
import spock.lang.Specification

class InvoiceServiceTest extends Specification {

    InvoiceRepository database;
    InvoiceDto invoiceDto = InvoiceFixture.getInvoiceDto(1)
    InvoiceService invoiceService
    InvoiceMapper invoiceMapper = Mappers.getMapper(InvoiceMapper.class)

    def setup() {
        database = Mock()
        invoiceService = new InvoiceService(database, invoiceMapper)
    }

    def "calling saveInvoice() should map dto to entity and delegate to database save()"() {
        when: "we ask invoice service to save invoice"
        invoiceService.saveInvoice(invoiceDto)

        then: "database save() is called"
        1 * database.save(invoiceMapper.toEntity(invoiceDto))
    }

    def "should get an invoice from database by id and map to dto"() {
        given: "an invoice returned by database"
        Invoice invoice = invoiceMapper.toEntity(invoiceDto)
        database.getById(invoice.getId()) >> invoice
        database.existsById(invoice.getId()) >> true

        when: "we ask invoice service for invoice by id"
        InvoiceDto returnedInvoiceDto = invoiceService.getById(invoiceDto.getId())

        then: "invoice is returned"
        returnedInvoiceDto == invoiceDto
    }

    def "calling getAll() should return list of InvoiceDto"() {
        given:
        database.findAll() >> List.of(invoiceMapper.toEntity(invoiceDto))

        when: "we ask invoice service for list of all invoices"
        def list = invoiceService.getAll()

        then: "database getAll() is called"
        list == [invoiceDto]
    }

    def "calling updateInvoice() should map dto to entity and delegate to database update()"() {
        given:
        database.existsById(invoiceDto.getId()) >> true

        when: "we ask invoice service to update invoice"
        invoiceService.updateInvoice(invoiceDto)

        then: "invoice is updated"
        1 * database.save(invoiceMapper.toEntity(invoiceDto))
    }

    def "calling deleteInvoice() should delegate to database delete()"() {
        given:
        UUID id = UUID.randomUUID()
        database.existsById(id) >> true

        when: "we ask invoice service to delete invoice"
        invoiceService.deleteInvoice(id)

        then: "database delete() is called"
        1 * database.deleteById(id)
    }

    def "should filter database"() {
        given: "a list of invoices"
        Invoice invoice = invoiceMapper.toEntity(invoiceDto)
        database.findAll() >> [invoice]
        String taxId = invoice.getSeller().getTaxIdentificationNumber()
        FilterParameters filterParameters = FilterParameters.builder().sellerTaxId(taxId).build()

        when: "we ask invoice service to filter the database based on Predicate"
        List<InvoiceDto> invoiceList = invoiceService.filter(filterParameters)

        then: "database is filtered"
        invoiceList == [invoiceDto]
    }
}

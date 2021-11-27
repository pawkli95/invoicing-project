package pl.futurecollars.invoicing.service.unitTests

import org.mapstruct.factory.Mappers

import pl.futurecollars.invoicing.db.invoices.InvoiceRepository
import pl.futurecollars.invoicing.dto.InvoiceDto
import pl.futurecollars.invoicing.dto.mappers.InvoiceMapper
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.helpers.FilterParameters
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.InvoiceService
import spock.lang.Specification

class InvoiceServiceTest extends Specification {

    InvoiceRepository invoiceRepository;
    InvoiceDto invoiceDto = InvoiceFixture.getInvoiceDto(1)
    InvoiceService invoiceService
    InvoiceMapper invoiceMapper = Mappers.getMapper(InvoiceMapper.class)

    def setup() {
        invoiceRepository = Mock()
        invoiceService = new InvoiceService(invoiceRepository, invoiceMapper)
    }

    def "calling saveInvoice() should map dto to entity and delegate to database save()"() {
        when: "we ask invoice service to save invoice"
        invoiceService.save(invoiceDto)

        then: "database save() is called"
        1 * invoiceRepository.save(invoiceMapper.toEntity(invoiceDto))
    }

    def "should get an invoice from database by id and map to dto"() {
        given: "an invoice returned by database"
        Invoice invoice = invoiceMapper.toEntity(invoiceDto)
        invoiceRepository.findById(invoice.getId()) >> Optional.ofNullable(invoice)

        when: "we ask invoice service for invoice by id"
        InvoiceDto returnedInvoiceDto = invoiceService.getById(invoiceDto.getId())

        then: "invoice is returned"
        returnedInvoiceDto == invoiceDto
    }

    def "should throw NoSuchElementException when getting invoice by id if invoice doesn't exist"() {
        given:
        UUID id = UUID.randomUUID()
        invoiceRepository.findById(id) >> Optional.ofNullable(null)

        when:
        invoiceService.getById(id)

        then:
        thrown(NoSuchElementException)
    }

    def "calling getAll() should return list of InvoiceDto"() {
        given:
        invoiceRepository.findAll() >> List.of(invoiceMapper.toEntity(invoiceDto))

        when: "we ask invoice service for list of all invoices"
        def list = invoiceService.getAll()

        then: "database getAll() is called"
        list == [invoiceDto]
    }

    def "calling updateInvoice() should map dto to entity and delegate to database update()"() {
        given:
        invoiceRepository.existsById(invoiceDto.getId()) >> true

        when: "we ask invoice service to update invoice"
        invoiceService.update(invoiceDto)

        then: "invoice is updated"
        1 * invoiceRepository.save(invoiceMapper.toEntity(invoiceDto))
    }

    def "should throw NoSuchElementException when updating invoice if invoice doesn't exist"() {
        given:
        UUID id = invoiceDto.getId()
        invoiceRepository.existsById(id) >> false

        when:
        invoiceService.update(invoiceDto)

        then:
        thrown(NoSuchElementException)
    }

    def "calling deleteInvoice() should delegate to database delete()"() {
        given:
        UUID id = UUID.randomUUID()
        invoiceRepository.existsById(id) >> true

        when: "we ask invoice service to delete invoice"
        invoiceService.delete(id)

        then: "database delete() is called"
        1 * invoiceRepository.deleteById(id)
    }

    def "should throw NoSuchElementException when deleting invoice by id if invoice doesn't exist"() {
        given:
        UUID id = UUID.randomUUID()
        invoiceRepository.existsById(id) >> false

        when:
        invoiceService.delete(id)

        then:
        thrown(NoSuchElementException)
    }

    def "should filter database"() {
        given: "a list of invoices"
        Invoice invoice = invoiceMapper.toEntity(invoiceDto)
        invoiceRepository.findAll() >> [invoice]
        String taxId = invoice.getSeller().getTaxIdentificationNumber()
        FilterParameters filterParameters = FilterParameters.builder().sellerTaxId(taxId).build()

        when: "we ask invoice service to filter the database based on Predicate"
        List<InvoiceDto> invoiceList = invoiceService.filter(filterParameters)

        then: "database is filtered"
        invoiceList == [invoiceDto]
    }
}

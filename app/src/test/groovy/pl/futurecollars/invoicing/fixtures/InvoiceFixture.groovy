package pl.futurecollars.invoicing.fixtures

import org.mapstruct.factory.Mappers
import pl.futurecollars.invoicing.dto.InvoiceDto
import pl.futurecollars.invoicing.dto.mappers.InvoiceMapper
import pl.futurecollars.invoicing.model.Invoice

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class InvoiceFixture {

    static InvoiceMapper invoiceMapper = Mappers.getMapper(InvoiceMapper.class)
    static int number = 0;

    static Invoice getInvoice(int numberOfEntries) {
        number++;
        return Invoice.builder()
                .id(UUID.randomUUID())
                .date(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .seller(CompanyFixture.getCompany())
                .buyer(CompanyFixture.getCompany())
                .number(String.valueOf(number))
                .invoiceEntries(InvoiceEntryFixture.getInvoiceEntryListWithPersonalCar(numberOfEntries))
                .build()
    }

    static InvoiceDto getInvoiceDto(int numberOfInvoiceEntries) {
        return invoiceMapper.toDto(getInvoice(numberOfInvoiceEntries));
    }

    static Invoice getInvoiceWithoutId() {
        number++;
        return Invoice.builder()
                .number("12/12/20002")
                .date(LocalDateTime.now())
                .seller(CompanyFixture.getCompany())
                .buyer(CompanyFixture.getCompany())
                .invoiceEntries(InvoiceEntryFixture.getInvoiceEntryListWithPersonalCar(4))
                .build()
    }

    static InvoiceDto getInvoiceDtoWithoutId() {
        return invoiceMapper.toDto(getInvoiceWithoutId());
    }


}

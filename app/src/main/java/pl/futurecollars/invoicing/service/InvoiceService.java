package pl.futurecollars.invoicing.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.invoices.InvoiceRepository;
import pl.futurecollars.invoicing.dto.InvoiceDto;
import pl.futurecollars.invoicing.dto.mappers.InvoiceMapper;
import pl.futurecollars.invoicing.exceptions.ConstraintException;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@RequiredArgsConstructor
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public InvoiceDto saveInvoice(InvoiceDto invoiceDto) {
        if(invoiceRepository.existsByNumber(invoiceDto.getNumber())) {
            throw new ConstraintException("This number is already in use");
        }
        invoiceDto.setDate(LocalDate.now());
        invoiceDto.getInvoiceEntries().forEach(InvoiceEntry::calculateVatValue);
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        return invoiceMapper.toDto(invoiceRepository.save(invoice));
    }

    public InvoiceDto getById(UUID id) throws NoSuchElementException {
        return invoiceMapper.toDto(invoiceRepository.getById(id));
    }

    public List<InvoiceDto> getAll() {
        return invoiceRepository.findAll().stream()
                .map(invoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<InvoiceDto> filter(Predicate<Invoice> predicate) {
        return invoiceRepository.findAll()
                .stream()
                .filter(predicate)
                .map(invoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    public InvoiceDto updateInvoice(InvoiceDto updatedInvoice) throws NoSuchElementException {
        Invoice returnedInvoice = invoiceRepository.save(invoiceMapper.toEntity(updatedInvoice));
        return invoiceMapper.toDto(returnedInvoice);
    }

    public void deleteInvoice(UUID id) throws NoSuchElementException {
        invoiceRepository.deleteById(id);
    }
}

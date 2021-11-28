package pl.futurecollars.invoicing.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.dto.InvoiceDto;
import pl.futurecollars.invoicing.dto.mappers.InvoiceMapper;
import pl.futurecollars.invoicing.exceptions.ConstraintException;
import pl.futurecollars.invoicing.helpers.FilterParameters;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.repository.InvoiceRepository;

@RequiredArgsConstructor
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public InvoiceDto save(InvoiceDto invoiceDto) {
        if (invoiceRepository.existsByNumber(invoiceDto.getNumber())) {
            throw new ConstraintException("This number is already in use");
        }
        invoiceDto.setDate(LocalDate.now());
        invoiceDto.getInvoiceEntries().forEach(InvoiceEntry::calculateVatValue);
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        return invoiceMapper.toDto(invoiceRepository.save(invoice));
    }

    public InvoiceDto getById(UUID id) throws NoSuchElementException {
        return invoiceRepository.findById(id)
               .map(invoiceMapper::toDto)
               .orElseThrow(() -> new NoSuchElementException("Invoice doesn't exist"));
    }

    public List<InvoiceDto> getAll() {
        return invoiceRepository.findAll().stream()
                .map(invoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<InvoiceDto> filter(FilterParameters filterParameters) {
        return invoiceRepository.findAll()
                .stream()
                .filter(createPredicate(filterParameters))
                .map(invoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    public InvoiceDto update(InvoiceDto updatedInvoice) throws NoSuchElementException {
        if (!invoiceRepository.existsById(updatedInvoice.getId())) {
            throw new NoSuchElementException("Invoice doesn't exist");
        }
        Invoice returnedInvoice = invoiceRepository.save(invoiceMapper.toEntity(updatedInvoice));
        return invoiceMapper.toDto(returnedInvoice);
    }

    public void delete(UUID id) throws NoSuchElementException {
        if (!invoiceRepository.existsById(id)) {
            throw new NoSuchElementException("Invoice doesn't exist");
        }
        invoiceRepository.deleteById(id);
    }

    private Predicate<Invoice> createPredicate(FilterParameters filterParameters) {
        Predicate<Invoice> predicate = Objects::nonNull;
        if (filterParameters.getAfterDate().isPresent()) {
            predicate = predicate.and(invoice -> invoice.getDate().isAfter(filterParameters.getAfterDate().get()));
        }
        if (filterParameters.getBeforeDate().isPresent()) {
            predicate = predicate.and(invoice -> invoice.getDate().isBefore(filterParameters.getBeforeDate().get()));
        }
        if (filterParameters.getSellerTaxId().isPresent()) {
            predicate = predicate.and(invoice -> invoice.getSeller().getTaxIdentificationNumber()
                    .equals(filterParameters.getSellerTaxId().get()));
        }
        if (filterParameters.getBuyerTaxId().isPresent()) {
            predicate = predicate.and(invoice -> invoice.getBuyer().getTaxIdentificationNumber()
                    .equals(filterParameters.getBuyerTaxId().get()));
        }
        return predicate;
    }
}

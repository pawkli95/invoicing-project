package pl.futurecollars.invoicing.db.invoices.jpa;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@Getter
@RequiredArgsConstructor
public class JpaDatabase implements Database<Invoice> {

    private final InvoiceRepository invoiceRepository;

    @Override
    public Invoice save(Invoice invoice) {
        Objects.requireNonNull(invoice, "Invoice cannot be null");
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getById(UUID id) throws NoSuchElementException {
        Objects.requireNonNull(id, "Id cannot be null");
        Optional<Invoice> optional = invoiceRepository.findById(id);
        return optional.orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice update(Invoice updatedInvoice) throws NoSuchElementException {
        Objects.requireNonNull(updatedInvoice, "Invoice cannot be null");
        if (invoiceRepository.existsById(updatedInvoice.getId())) {
            return invoiceRepository.save(updatedInvoice);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void delete(UUID id) throws NoSuchElementException {
        Objects.requireNonNull(id, "Id cannot be null");
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
        } else {
            throw new NoSuchElementException();
        }
    }
}



package pl.futurecollars.invoicing.db.invoices;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@Getter
@RequiredArgsConstructor
public class InvoiceDatabase implements Database<Invoice> {

    private final InvoiceRepository invoiceRepository;

    @Override
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getById(UUID id) throws NoSuchElementException {
        Optional<Invoice> optional = invoiceRepository.findById(id);
        return optional.orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice update(Invoice updatedInvoice) throws NoSuchElementException {
        if (invoiceRepository.existsById(updatedInvoice.getId())) {
            return invoiceRepository.save(updatedInvoice);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void delete(UUID id) throws NoSuchElementException {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
        } else {
            throw new NoSuchElementException();
        }
    }
}



package pl.futurecollars.invoicing.db.invoices;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.futurecollars.invoicing.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    @Override
    @Query("select distinct i from Invoice i left join fetch i.invoiceEntries left join fetch "
            + "i.seller left join fetch i.buyer")
    List<Invoice> findAll();

    boolean existsByNumber(String number);

    @Query("select distinct i from Invoice i left join fetch i.invoiceEntries left join fetch "
            + "i.seller left join fetch i.buyer where i.seller.taxIdentificationNumber = ?1")
    List<Invoice> findBySellerTaxId(String taxId);
}

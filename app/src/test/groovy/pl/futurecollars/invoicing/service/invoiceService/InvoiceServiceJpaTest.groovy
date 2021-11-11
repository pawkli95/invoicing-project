package pl.futurecollars.invoicing.service.invoiceService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice


@SpringBootTest
@ActiveProfiles("test")
class InvoiceServiceJpaTest extends InvoiceServiceIntegrationTest {

    @Autowired
    Database<Invoice> jpaDatabase

    @Override
    Database getDatabase() {
        return jpaDatabase
    }
}

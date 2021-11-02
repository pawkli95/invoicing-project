package pl.futurecollars.invoicing.service.invoiceService


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.invoices.file.FileBasedDatabase

@ActiveProfiles("fileTest")
@SpringBootTest
class InvoiceServiceFileBasedTest extends InvoiceServiceAbstractIntegrationTest {

    @Autowired
    Database fileBasedDatabase

    FileBasedDatabase getDatabase() {
        return fileBasedDatabase
    }
}

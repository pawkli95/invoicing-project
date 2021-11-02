package pl.futurecollars.invoicing.service.invoiceService

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.invoices.memory.InMemoryDatabase

class InvoiceServiceInMemoryTest extends InvoiceServiceAbstractIntegrationTest{

    Database getDatabase() {
        return new InMemoryDatabase();
    }
}

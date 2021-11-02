package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.DatabaseTest
import pl.futurecollars.invoicing.db.invoices.memory.InMemoryDatabase

class InMemoryDatabaseTest extends DatabaseTest{

    @Override
    Database getDatabase() {
        return new InMemoryDatabase();
    }
}

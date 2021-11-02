package pl.futurecollars.invoicing.db.invoices

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.DatabaseTest
import pl.futurecollars.invoicing.db.invoices.file.FileBasedDatabase

@ActiveProfiles("fileTest")
@SpringBootTest
class FileBasedDatabaseTest extends DatabaseTest {

    @Autowired
    FileBasedDatabase fileBasedDatabase;

    @Override
    Database getDatabase() {
        return fileBasedDatabase
    }
}


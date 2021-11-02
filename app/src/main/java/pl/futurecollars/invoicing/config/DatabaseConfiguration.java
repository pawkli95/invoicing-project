package pl.futurecollars.invoicing.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.companies.CompanyDatabase;
import pl.futurecollars.invoicing.db.invoices.file.FileBasedDatabase;
import pl.futurecollars.invoicing.db.companies.CompanyRepository;
import pl.futurecollars.invoicing.db.invoices.jpa.InvoiceRepository;
import pl.futurecollars.invoicing.db.invoices.jpa.JpaDatabase;
import pl.futurecollars.invoicing.db.invoices.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.db.invoices.mongo.MongoDatabase;
import pl.futurecollars.invoicing.db.invoices.mongo.MongoDbRepository;
import pl.futurecollars.invoicing.db.invoices.sql.SqlDatabase;
import pl.futurecollars.invoicing.db.invoices.sql.InvoiceRowMapper;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@EnableJpaRepositories(basePackages = {"pl.futurecollars.invoicing.db.invoices.jpa", "pl.futurecollars.invoicing.db.companies"})
@EnableMongoRepositories(basePackages = "pl.futurecollars.invoicing.db.invoices.mongo")
@Slf4j
@Configuration
public class DatabaseConfiguration {

    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    @Bean
    public Database<Invoice> getFileBasedDatabase(@Value("${invoicing-system.invoicesFile}") String invoicesFile,
                                         @Value("${invoicing-system.idsFile}") String idsFile) {
        FileService invoices = new FileService(invoicesFile);
        FileService ids = new FileService(idsFile);
        log.info("Created file database");
        return new FileBasedDatabase(new JsonService<Invoice>(), invoices, ids);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
    public Database<Invoice> getInMemoryDatabase() {
        log.info("Created inmemory database");
        return new InMemoryDatabase();
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "fileTest")
    public Database<Invoice> getTestFileBasedDatabase(@Value("${invoicing-system.invoicesFile}") String invoicesFile,
                                             @Value("${invoicing-system.idsFile}") String idsFile) {
        FileService invoices = new FileService(invoicesFile);
        FileService ids = new FileService(idsFile);
        log.info("Created test file database");
        return new FileBasedDatabase(new JsonService<Invoice>(), invoices, ids);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "sql")
    public Database<Invoice> getSqlDatabase(JdbcTemplate jdbcTemplate, InvoiceRowMapper invoiceRowMapper) {
        return new SqlDatabase(jdbcTemplate, invoiceRowMapper);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "jpa")
    public Database<Invoice> getJpaDatabase(InvoiceRepository invoiceRepository) {
        return new JpaDatabase(invoiceRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
    public Database<Invoice> getMongoDatabase(MongoDbRepository mongoDbRepository) {
        return new MongoDatabase(mongoDbRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "jpa")
    public Database<Company> getCompanyDatabase(CompanyRepository companyRepository) {
        return new CompanyDatabase(companyRepository);
    }

}

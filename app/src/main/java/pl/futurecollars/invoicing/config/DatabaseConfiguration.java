package pl.futurecollars.invoicing.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.companies.CompanyDatabase;
import pl.futurecollars.invoicing.db.companies.CompanyRepository;
import pl.futurecollars.invoicing.db.invoices.InvoiceDatabase;
import pl.futurecollars.invoicing.db.invoices.InvoiceRepository;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@EnableJpaRepositories(basePackages = {"pl.futurecollars.invoicing.db.invoices", "pl.futurecollars.invoicing.db.companies",
    "pl.futurecollars.invoicing.db.roles"})
@Slf4j
@Configuration
public class DatabaseConfiguration {

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "dev")
    public Database<Invoice> getInvoiceDatabase(InvoiceRepository invoiceRepository) {
        return new InvoiceDatabase(invoiceRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "dev")
    public Database<Company> getCompanyDatabase(CompanyRepository companyRepository) {
        return new CompanyDatabase(companyRepository);
    }

}

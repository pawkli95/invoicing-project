package pl.futurecollars.invoicing.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.futurecollars.invoicing.repository.CompanyRepository;
import pl.futurecollars.invoicing.model.Company;

@EnableJpaRepositories(basePackages = {"pl.futurecollars.invoicing.repository",
        "pl.futurecollars.invoicing.repository",
    "pl.futurecollars.invoicing.repository",
        "pl.futurecollars.invoicing.repository",
})
@Slf4j
@Configuration
public class DatabaseConfiguration {

    }



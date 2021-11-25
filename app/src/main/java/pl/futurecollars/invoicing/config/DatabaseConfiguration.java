package pl.futurecollars.invoicing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"pl.futurecollars.invoicing.repository", "pl.futurecollars.invoicing.repository",
    "pl.futurecollars.invoicing.repository", "pl.futurecollars.invoicing.repository"})

@Configuration
public class DatabaseConfiguration {

}



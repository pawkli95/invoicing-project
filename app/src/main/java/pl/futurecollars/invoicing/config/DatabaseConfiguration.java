package pl.futurecollars.invoicing.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {
        "pl.futurecollars.invoicing.repository",
        "pl.futurecollars.invoicing.repository",
        "pl.futurecollars.invoicing.repository",
        "pl.futurecollars.invoicing.repository",
})
@Slf4j
@Configuration
public class DatabaseConfiguration {

}



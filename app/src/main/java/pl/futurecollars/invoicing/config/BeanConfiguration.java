package pl.futurecollars.invoicing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.utils.FileService;

@Configuration
public class BeanConfiguration {

    @Bean
    public FileService jsonFileService() {
        return new FileService(FilePathConfig.JSON_FILE);
    }

    @Bean
    public FileService idsFileService() {
        return new FileService(FilePathConfig.IDS_FILE);
    }
}
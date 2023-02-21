package by.rusetskaya.tests.exchangerates.configuration;

import by.rusetskaya.tests.exchangerates.handlers.RestTemplateErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for link with other microservices
 */
@Configuration
public class RestTemplateConfig {
    /**
     * @return RestTemplate by RestTemplateBuilder.errorHandler(new RestTemplateErrorHandler()
     * we indicate handler of errors, implementation of ResponseErrorHandler
     */
    @Bean
    RestTemplate restTemplate() {
        RestTemplateBuilder restTemplateBuilder = new
                RestTemplateBuilder();
        RestTemplate build = restTemplateBuilder
                .errorHandler(new RestTemplateErrorHandler())
                .build();
        return build;
    }
}

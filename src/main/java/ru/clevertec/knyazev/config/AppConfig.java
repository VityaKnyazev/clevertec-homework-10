package ru.clevertec.knyazev.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.clevertec.knyazev.cache.AbstractCacheFactory;
import ru.clevertec.knyazev.cache.Cache;
import ru.clevertec.knyazev.cache.impl.DefaultCacheFactory;
import ru.clevertec.knyazev.data.ServiceDTO;
import ru.clevertec.knyazev.entity.Person;
import ru.clevertec.knyazev.pdf.PDFManager;
import ru.clevertec.knyazev.pdf.impl.ServiceCheckPDFManagerImpl;
import ru.clevertec.knyazev.util.YAMLParser;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Configuration
@ComponentScan(basePackages = {"ru.clevertec.knyazev.dao.impl",
        "ru.clevertec.knyazev.dao.proxy",
        "ru.clevertec.knyazev.mapper",
        "ru.clevertec.knyazev.service.impl"})
public class AppConfig {

    private static final String PROPERTY_FILE = "application.yaml";

    @Bean
    YAMLParser yamlParser() {
        return new YAMLParser(PROPERTY_FILE);
    }

    @Bean
    CacheProperties cacheProperties(YAMLParser yamlParser) {
        return CacheProperties.builder()
                .algorithm(yamlParser.getProperty("cache", "algorithm"))
                .size(Integer.valueOf(
                        yamlParser.getProperty("cache", "size")))
                .build();
    }

    @Bean
    DataSourceProperties dataSourceProperties(YAMLParser yamlParser) {
        return DataSourceProperties.builder()
                .driverClassName(yamlParser.getProperty("datasource", "driverClassName"))
                .jdbcUrl(yamlParser.getProperty("datasource", "jdbcUrl"))
                .username(yamlParser.getProperty("datasource", "username"))
                .password(yamlParser.getProperty("datasource", "password"))
                .maxPoolSize(Integer.parseInt(yamlParser.getProperty("datasource",
                                                                      "maxPoolSize")))
                .connectionTimeout(Long.parseLong(yamlParser.getProperty("datasource",
                                                                          "connectionTimeout")))
                .build();
    }

    @Bean
    PDFProperties pdfProperties(YAMLParser yamlParser) {
        return PDFProperties.builder()
                .pdfTemplatePath(yamlParser.getProperty("pdf", "templatePath"))
                .pdfPath(yamlParser.getProperty("pdf", "resultPath"))
                .pdfFontPath(yamlParser.getProperty("pdf", "documentFontPath"))
                .pdfFontEncoding(yamlParser.getProperty("pdf", "documentFontEncoding"))
                .build();
    }


    @Bean
    AbstractCacheFactory defaultCacheFactory(CacheProperties cacheProperties) {
        return new DefaultCacheFactory(cacheProperties.algorithm(), cacheProperties.size());
    }

    @Bean
    Cache<UUID, Person> personCache(AbstractCacheFactory defaultCacheFactory) {
        return defaultCacheFactory.initCache();
    }

    @Bean
    PDFManager<List<ServiceDTO>> serviceCheckPDFManagerImpl(PDFProperties pdfProperties) {
        return new ServiceCheckPDFManagerImpl(pdfProperties.pdfTemplatePath(),
                pdfProperties.pdfPath(),
                pdfProperties.pdfFontPath(),
                pdfProperties.pdfFontEncoding());
    }

    @Bean
    ValidatorFactory validatorFactory() {
        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
    }

    @Bean
    DataSource hikariDataSource(DataSourceProperties dataSourceProperties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dataSourceProperties.driverClassName());
        hikariConfig.setJdbcUrl(dataSourceProperties.jdbcUrl());
        hikariConfig.setUsername(dataSourceProperties.username());
        hikariConfig.setPassword(dataSourceProperties.password());
        hikariConfig.setMaximumPoolSize(dataSourceProperties.maxPoolSize());
        hikariConfig.setConnectionTimeout(dataSourceProperties.connectionTimeout());

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    JdbcTemplate jdbcTemplate(DataSource hikariDataSource) {
        return new JdbcTemplate(hikariDataSource);
    }
}

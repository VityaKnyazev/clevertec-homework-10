package ru.clevertec.knyazev.config;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.clevertec.knyazev.cache.AbstractCacheFactory;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.dao.ServiceDAO;
import ru.clevertec.knyazev.dao.impl.PersonDAOImpl;
import ru.clevertec.knyazev.dao.impl.ServiceDAOImpl;
import ru.clevertec.knyazev.dao.proxy.PersonDaoProxy;
import ru.clevertec.knyazev.mapper.PersonMapperImpl;
import ru.clevertec.knyazev.mapper.ServiceMapperImpl;
import ru.clevertec.knyazev.pdf.impl.ServiceCheckPDFManagerImpl;
import ru.clevertec.knyazev.service.GovernmentService;
import ru.clevertec.knyazev.service.PersonService;
import ru.clevertec.knyazev.service.impl.GovernmentServiceImpl;
import ru.clevertec.knyazev.service.impl.PersonServiceImpl;
import ru.clevertec.knyazev.util.YAMLParser;

@WebListener
public class AppContextListener implements ServletContextListener {
    public static final String PERSON_SERVICE_IMPL = "personServiceImpl";
    public static final String GOVERNMENT_SERVICE_IMPL = "governmentServiceImpl";
    public static final String PAGING_PROPERTIES = "pagingProperties";
    public static final String GSON = "gson";

    private final AppConfig appConfig;

    public AppContextListener() {
        appConfig = new AppConfig();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        Gson gson = appConfig.gson();

        YAMLParser yamlParser = appConfig.yamlParser();

        DataSourceProperties dataSourceProperties = appConfig.dataSourceProperties(yamlParser);
        PagingProperties pagingProperties = appConfig.pagingProperties(yamlParser);
        CacheProperties cacheProperties = appConfig.cacheProperties(yamlParser);
        PDFProperties serverPDFProperties = appConfig.serverPDFProperties(yamlParser);


        JdbcTemplate jdbcTemplate = appConfig.jdbcTemplate(
                appConfig.hikariDataSource(dataSourceProperties));

        PersonDAO personDAOImpl = new PersonDAOImpl(jdbcTemplate);
        ServiceDAO serviceDAOImpl = new ServiceDAOImpl(personDAOImpl, jdbcTemplate);


        AbstractCacheFactory defaultCacheFactory = appConfig.defaultCacheFactory(cacheProperties);

        PersonDaoProxy personDaoProxy = new PersonDaoProxy(personDAOImpl,
                appConfig.personCache(defaultCacheFactory));

        PersonService personServiceImpl =  new PersonServiceImpl(
                personDaoProxy,
                new PersonMapperImpl(),
                appConfig.validatorFactory());
        GovernmentService governmentServiceImpl = new GovernmentServiceImpl(serviceDAOImpl,
                new ServiceMapperImpl(),
                new ServiceCheckPDFManagerImpl(serverPDFProperties.pdfTemplatePath(),
                        serverPDFProperties.pdfPath(),
                        serverPDFProperties.pdfFontPath(),
                        serverPDFProperties.pdfFontEncoding()));

        servletContext.setAttribute(PERSON_SERVICE_IMPL, personServiceImpl);
        servletContext.setAttribute(GOVERNMENT_SERVICE_IMPL, governmentServiceImpl);
        servletContext.setAttribute(PAGING_PROPERTIES, pagingProperties);
        servletContext.setAttribute(GSON, gson);
    }
}

package contactassigment.contactlistapp.domain;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.*;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


public class ContactRepositoryCustomTest {

    @Autowired
    private ContactRepositoryCustom repository;

    @Before
    public void setup() {
        repository = new ContactRepositoryImpl();

        Map<String, String> properties = new HashMap<>();

        properties.put("javax.persistence.provider", "org.hibernate.jpa.HibernatePersistence");
        properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
        properties.put("hibernate.connection.username", "sa");
        properties.put("hibernate.connection.password" ,"");
        properties.put("hibernate.connection.driver_class","org.h2.Driver");
        properties.put("hibernate.connection.url", "jdbc:h2:mem:myDb" );
        properties.put("hibernate.dialect" ,"org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.hbm2ddl.auto","create-drop");


        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");


        EntityManagerFactory entityManagerFactory = new HibernatePersistenceProvider().createContainerEntityManagerFactory(createPersistenceUnitInfo(), properties);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ReflectionTestUtils.setField(repository, "em", entityManager);

    }

    private static PersistenceUnitInfo createPersistenceUnitInfo() {
        return new PersistenceUnitInfo() {
            @Override
            public String getPersistenceUnitName() {
                return "test-persistence-unit";
            }

            @Override
            public String getPersistenceProviderClassName() {
                return "org.hibernate.jpa.HibernatePersistenceProvider";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return null;
            }

            @Override
            public List<String> getMappingFileNames() {
                return Collections.emptyList();
            }

            @Override
            public List<URL> getJarFileUrls() {
                return Collections.emptyList();
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return null;
            }

            @Override
            public List<String> getManagedClassNames() {
                return Arrays.asList(
                        "contactassigment.contactlistapp.domain.Contact",
                        "contactassigment.contactlistapp.domain.Organisation");
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public Properties getProperties() {
                return new Properties();
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }


            @Override
            public void addTransformer(ClassTransformer transformer) {

            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };
    }

    @DisplayName("Test the custom contact repository returns no contact for no matching criteria ")
    @Test
    public void return_no_contacts_for_no_match() {
        List<Contact> contacts = repository.searchByNamesFetchOrganisation("John", "", "");
        assertThat(contacts).isEmpty();
    }

    @DisplayName("Test the custom contact repository returns contacts for matching last name ")
    @Test
    public void return_contacts_forMatchingLastName() {
        List<Contact> contacts = repository.searchByNamesFetchOrganisation("", "B*", "");
        assertThat(contacts).isNotEmpty();
    }

    @DisplayName("TTest the custom contact repository returns contacts for matching organisation name ")
    @Test
    public void return_contacts_forMatchingOrganisationName() {
        List<Contact> contacts = repository.searchByNamesFetchOrganisation("", "", "CRP AUSTRALIA PTY LTD");
        assertThat(contacts).isNotEmpty();
    }
}


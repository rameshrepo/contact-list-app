package contactassigment.contactlistapp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    OrganisationRepository organisationRepository;

    @MockBean
    TimedAspect timedAspect;

    @MockBean
    MeterRegistry registry;

    @DisplayName("Test to check it returns all contacts ")
    @Test
    public void find_all_contacts() {
        Iterable<Contact> contacts = contactRepository.findAll();
        assertThat(contacts).isNotEmpty();
    }

    @DisplayName("Test to check the organisation is returned for contacts  ")
    @Test
    public void find_all_contacts_with_organisation() {
        Iterable<Contact> contacts = contactRepository.findAllFetchOrganisation();
        assertThat(contacts).isNotEmpty();
        contacts.forEach(contact -> assertThat(contact.getOrganisation()).isNotNull());
    }

    @DisplayName("Test to check the contact and organisation is returned for an existing id  ")
    @Test
    public void find_contact_for_a_given_id_with_organisation() {
        Contact contact = contactRepository.findByIdFetchOrganisation(1);
        assertThat(contact).isNotNull();
        assertThat(contact.getOrganisation()).isNotNull();
        assertThat(contact.getOrganisation().getId()).isEqualTo(1);
    }

}

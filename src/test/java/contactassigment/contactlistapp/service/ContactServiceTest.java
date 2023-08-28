package contactassigment.contactlistapp.service;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.domain.ContactRepository;
import contactassigment.contactlistapp.domain.Organisation;
import contactassigment.contactlistapp.domain.OrganisationRepository;
import contactassigment.contactlistapp.dto.ContactDTO;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private OrganisationRepository organisationRepo;

    @InjectMocks
    private ContactService contactService;

    private final List<Contact> list = new ArrayList<>();

    private final Organisation organisation = new Organisation();


    @BeforeEach
    public void setup(){
        contactRepository = Mockito.mock(ContactRepository.class);
        organisationRepo = Mockito.mock(OrganisationRepository.class);
        contactService = new ContactServiceImpl(contactRepository, organisationRepo);

        organisation.setId(1);
        organisation.setName("Test");
        organisation.setAbn(1234567890L);

        Date currentDate = new Date();

        Contact contact1 = new Contact();
        contact1.setFirstName("John");
        contact1.setLastName("Smart");
        contact1.setId(1);
        contact1.setOrganisation(organisation);
        contact1.setCreatedAt(currentDate);

        Contact contact2 = new Contact();
        contact2.setFirstName("Ken");
        contact2.setLastName("Wood");
        contact2.setOrganisation(organisation);
        contact2.setCreatedAt(currentDate);

        list.addAll(Arrays.asList(contact1, contact2));
    }

    @DisplayName("JUnit test for getAllContact")
    @Test
    public void givenContactList_whenGetAllContacts_thenReturnContactList(){
        // given - precondition or setup

        ContactSearchCriteriaDTO contactSearchCriteriaDTO = new ContactSearchCriteriaDTO();
        contactSearchCriteriaDTO.setFirstName("");
        contactSearchCriteriaDTO.setLastName("");
        contactSearchCriteriaDTO.setOrganisationName("");

        given(contactRepository.searchByNamesFetchOrganisation(contactSearchCriteriaDTO.getFirstName(),
                contactSearchCriteriaDTO.getLastName(), contactSearchCriteriaDTO.getOrganisationName()))
                .willReturn(list);

        // when -  action or the behaviour that we are going test
        List<ContactDTO> contactList = contactService.listByCriteriaFetchOrganisation(contactSearchCriteriaDTO);

        // then - verify the output
        assertThat(contactList).isNotNull();
        assertThat(contactList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for getAllContact for matching criteria")
    @Test
    public void givenContactList_whenSearchContact_thenReturnContactList(){
        // given - precondition or setup

        ContactSearchCriteriaDTO contactSearchCriteriaDTO = new ContactSearchCriteriaDTO();
        contactSearchCriteriaDTO.setFirstName("John");
        contactSearchCriteriaDTO.setLastName("");
        contactSearchCriteriaDTO.setOrganisationName("");

        List<Contact> searchList = new ArrayList<>();
        searchList.add(list.get(0));
        given(contactRepository.searchByNamesFetchOrganisation(contactSearchCriteriaDTO.getFirstName(),
                contactSearchCriteriaDTO.getLastName(), contactSearchCriteriaDTO.getOrganisationName()))
                .willReturn(searchList);


        // when -  action or the behaviour that we are going test
        List<ContactDTO> contactList = contactService.listByCriteriaFetchOrganisation(contactSearchCriteriaDTO);

        // then - verify the output
        assertThat(contactList).isNotNull();
        assertThat(contactList.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for getContactForGivenId")
    @Test
    public void givenContactList_whenGetContactForExistingId_thenReturnContact(){
        // given - precondition or setup

        int id = 1;
        Contact filteredContact = list.get(0);
        given(contactRepository.findByIdFetchOrganisation(id))
                .willReturn(filteredContact);

        // when -  action or the behaviour that we are going test
        ContactDTO contact = contactService.findByIdFetchOrganisation(id);

        // then - verify the output
        assertThat(contact).isNotNull();
        assertThat(contact.getId()).isEqualTo(1);
    }

    @DisplayName("JUnit test for getContactForGivenId that is not in Contact list")
    @Test
    public void givenContactList_whenGetContactForNonExistingId_thenReturnNull(){
        // given - precondition or setup

        int id = 3;
        given(contactRepository.findByIdFetchOrganisation(id))
                .willReturn(null);

        // when -  action or the behaviour that we are going test
        ContactDTO contact = contactService.findByIdFetchOrganisation(id);

        // then - verify the output
        assertThat(contact).isNull();
    }

    @DisplayName("JUnit test to persist the contact for an existing contact in the contact list")
    @Test
    public void givenContactList_whenContactExists_thenUpdateContact(){
        // given - precondition or setup

        int id = 1;
        Contact filteredContact = list.get(0);
        given(contactRepository.findByIdFetchOrganisation(id))
                .willReturn(filteredContact);

        given(organisationRepo.findById(id)).willReturn(java.util.Optional.of(organisation));

        given(contactRepository.save(any())).willReturn(filteredContact);

        ContactDTO updateContactDTO = ContactDTO.createBy(list.get(0));
        // when -  action or the behaviour that we are going test
        ContactDTO contact = contactService.updateByDTO(updateContactDTO);

        // then - verify the output
        assertThat(contact).isNotNull();
        assertThat(contact.getId()).isEqualTo(1);
    }

    @DisplayName("JUnit test to throw exception when contact doesn't exist in the contact list")
    @Test
    public void givenContactList_whenContactDoesntExists_thenThrowException(){
        // given - precondition or setup

        int id = 1;
        given(contactRepository.findByIdFetchOrganisation(id))
                .willReturn(null);

        ContactDTO updateContactDTO = ContactDTO.createBy(list.get(0));
        // when -  action or the behaviour that we are going test


        PersistenceException thrown = Assertions.assertThrows(EntityNotFoundException.class, () -> contactService.updateByDTO(updateContactDTO));
        // then - verify the output
        Assertions.assertEquals("Unable to find Entity: contactassigment.contactlistapp.domain.Contact with id: 1", thrown.getMessage());
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @DisplayName("JUnit test to when organisation doesn't exist in the contact list save with no organisation")
    @Test
    public void givenContactList_whenOrganisationDoesntExists_InContact_thenSave(){
        // given - precondition or setup
        int id = 1;
        Contact persistedContact = list.get(0);
        organisation.setId(-1);
        given(contactRepository.findByIdFetchOrganisation(id)).willReturn(persistedContact);
        given(contactRepository.save(persistedContact)).willReturn(persistedContact);

        // when -  action or the behaviour that we are going test
        ContactDTO updateContactDTO = ContactDTO.createBy(list.get(0));
        contactService.updateByDTO(updateContactDTO);

        // then - verify the output
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @DisplayName("JUnit test to when organisation doesn't exist in the Organisation throws Exception")
    @Test
    public void givenContactList_whenOrganisationDoesntExists_InOrganisation_thenThrowsException(){

        // given - precondition or setup

        int id = 1;
        Contact persistedContact = list.get(0);
        organisation.setId(3);
        given(contactRepository.findByIdFetchOrganisation(id)).willReturn(persistedContact);
        given(organisationRepo.findById(3)).willReturn(Optional.empty());

        ContactDTO updateContactDTO = ContactDTO.createBy(list.get(0));
        // when -  action or the behaviour that we are going test
        PersistenceException thrown = Assertions.assertThrows(EntityNotFoundException.class, () -> contactService.updateByDTO(updateContactDTO));

        // then - verify the output
        Assertions.assertEquals("Unable to find Entity: contactassigment.contactlistapp.domain.Organisation with id: 3", thrown.getMessage());
        verify(contactRepository, never()).save(any(Contact.class));
    }
}
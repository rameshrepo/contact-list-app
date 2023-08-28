package contactassigment.contactlistapp.web;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.domain.Organisation;
import contactassigment.contactlistapp.dto.ContactDTO;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
import contactassigment.contactlistapp.dto.OrganisationDTO;
import contactassigment.contactlistapp.service.ContactService;
import contactassigment.contactlistapp.service.OrganisationService;
import contactassigment.contactlistapp.web.validators.ContactDTOValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactControllerTest {

    @InjectMocks
    ContactController contactController;

    @Mock
    ContactService contactService;

    @Mock
    OrganisationService organisationService;

    @Mock
    ContactDTOValidator contactDTOValidator;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Model model;

    List<Contact> list = new ArrayList<>();
    List<ContactDTO> contactList = new ArrayList<>();
    List<OrganisationDTO> organisationList = new ArrayList<>();

    ContactSearchCriteriaDTO contactSearchCriteriaDTO = new ContactSearchCriteriaDTO();

    @BeforeEach
    public void setup(){
        Organisation organisation = new Organisation();
        organisation.setId(1);
        organisation.setName("Test");
        organisation.setAbn(1234567890L);

        List<Organisation> orgList = new ArrayList<>();
        orgList.add(organisation);
        organisationList = OrganisationDTO.createListBy(orgList);

        Date currentDate = new Date();

        Contact contact1 = new Contact();
        contact1.setId(1);
        contact1.setFirstName("John");
        contact1.setLastName("Smart");
        contact1.setOrganisation(organisation);
        contact1.setCreatedAt(currentDate);

        Contact contact2 = new Contact();
        contact1.setId(2);
        contact2.setFirstName("Ken");
        contact2.setLastName("Wood");
        contact2.setOrganisation(organisation);
        contact2.setCreatedAt(currentDate);

        list.addAll(Arrays.asList(contact1, contact2));
        contactList = ContactDTO.createListBy(list);

    }

    @DisplayName("Junit test to check no data returned when search text is all empty ")
    @Test
    public void testReturnNoContactForNoSearchCriteria()
    {
        // given
        contactSearchCriteriaDTO.setFirstName("");
        contactSearchCriteriaDTO.setLastName("");
        contactSearchCriteriaDTO.setOrganisationName("");

        // when
        String returnValue = contactController.listContacts(contactSearchCriteriaDTO, model);

        // then
        assertEquals("/contact/list", returnValue);
    }

    @DisplayName("Junit test to check data returned for given search criteria")
    @Test
    public void testReturnContactForSearchCriteria()
    {
        // given
        contactSearchCriteriaDTO.setFirstName("John");
        contactSearchCriteriaDTO.setLastName("Smart");
        contactSearchCriteriaDTO.setOrganisationName("Test");

        when(contactService.listByCriteriaFetchOrganisation(contactSearchCriteriaDTO)).thenReturn(contactList);

        // when
        String returnValue = contactController.listContacts(contactSearchCriteriaDTO, model);

        // then
        assertEquals("/contact/list", returnValue);
    }

    @DisplayName("Junit test to view is returned if only one contact is returned")
    @Test
    public void testReturnViewForSingleContact()
    {
        // given
        contactSearchCriteriaDTO.setFirstName("John");
        contactSearchCriteriaDTO.setLastName("Smart");
        contactSearchCriteriaDTO.setOrganisationName("Test");

        contactList.remove(0);
        when(contactService.listByCriteriaFetchOrganisation(contactSearchCriteriaDTO)).thenReturn(contactList);

        // when
        String returnValue = contactController.listContacts(contactSearchCriteriaDTO, model);

        // then
        assertEquals("/contact/view", returnValue);
    }

    @DisplayName("Junit test to check no data found for unmatched data")
    @Test
    public void testReturnViewForNoDataFound()
    {
        // given
        contactSearchCriteriaDTO.setFirstName("John");
        contactSearchCriteriaDTO.setLastName("Smart");
        contactSearchCriteriaDTO.setOrganisationName("Test");
        when(contactService.listByCriteriaFetchOrganisation(contactSearchCriteriaDTO)).thenReturn(null);

        // when
        String returnValue = contactController.listContacts(contactSearchCriteriaDTO, model);

        // then
        assertEquals("/contact/list", returnValue);
    }

    @DisplayName("Junit test to check contact is returned for a given id")
    @Test
    public void testContactReturnedForGivenId()
    {
        // given
        int id = 1;
        when(contactService.findByIdFetchOrganisation(id)).thenReturn(contactList.get(0));

        // when
        String returnValue = contactController.view(id, model);

        // then
        assertEquals("/contact/view", returnValue);
    }

    @DisplayName("Junit test to check edit is returned for selected id")
    @Test
    public void testEditReturnedForGivenId()
    {
        // given
        int id = 1;
        when(contactService.findByIdFetchOrganisation(id)).thenReturn(contactList.get(0));
        when(organisationService.listAll()).thenReturn(organisationList);

        // when
        String returnValue = contactController.edit(id, model);

        // then
        assertEquals("/contact/edit", returnValue);
    }

    @DisplayName("Junit test to check update returns updated contact")
    @Test
    public void testSuccessfulUpdateReturnsContact()
    {
        // given
        ContactDTO contact = contactList.get(0);

        BindingResult errors = new BeanPropertyBindingResult(contact, "contactDTO");
        doNothing().when(contactDTOValidator).validate(contact, errors);
        when(contactService.updateByDTO((contact))).thenReturn(contact);

        // when
        String returnValue = contactController.updateContact(contact, errors, model, redirectAttributes );

        // then
        String view = "redirect:/contacts/" + contact.getId();
        assertEquals(view, returnValue);
    }

    @DisplayName("Junit test to check edit returns failed update")
    @Test
    public void testFailedUpdateReturnsEditPage()
    {
        // given
        ContactDTO contact = contactList.get(0);

        BindingResult errors = new BeanPropertyBindingResult(contact, "contactDTO");
        errors.rejectValue("firstName", "name.too.long");
        doNothing().when(contactDTOValidator).validate(contact, errors);

        // when
        String returnValue = contactController.updateContact(contact, errors, model, redirectAttributes );

        // then
        assertEquals("/contact/edit", returnValue);
    }
}
package contactassigment.contactlistapp.web;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.domain.ContactRepository;
import contactassigment.contactlistapp.domain.Organisation;
import contactassigment.contactlistapp.dto.ContactDTO;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
import contactassigment.contactlistapp.service.ContactServiceImpl;
import contactassigment.contactlistapp.service.OrganisationServiceImpl;
import contactassigment.contactlistapp.web.validators.ContactDTOValidator;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactServiceImpl contactService;

    @Autowired
    private OrganisationServiceImpl organisationService;

    @Autowired
    private ContactDTOValidator contactDTOValidator;

    @Autowired
    private TimedAspect timedAspect;

    @Autowired
    private MeterRegistry registry;

    @Autowired
    private ContactRepository contactRepository;

    @DisplayName("Test to return no data for no matching criteria")
    @Test
    public void testGetMethod() throws Exception {

        // given - precondition or setup
        ContactSearchCriteriaDTO contactSearchCriteriaDTO = new ContactSearchCriteriaDTO();
        contactSearchCriteriaDTO.setFirstName("test");
        contactSearchCriteriaDTO.setLastName("test");
        contactSearchCriteriaDTO.setOrganisationName("test");

        //when and then
        mockMvc.perform(MockMvcRequestBuilders.get("/contacts").flashAttr("searchCriteria",contactSearchCriteriaDTO))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/jsp/contact/list.jsp"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("invalidSearch"));
    }

    @DisplayName("Test to return view and data for matching criteria for only one contact")
    @Test
    public void testGetMethodReturnOneContact() throws Exception {

        // given - precondition or setup
        ContactSearchCriteriaDTO contactSearchCriteriaDTO = new ContactSearchCriteriaDTO();
        contactSearchCriteriaDTO.setFirstName("Sophie");
        contactSearchCriteriaDTO.setLastName("");
        contactSearchCriteriaDTO.setOrganisationName("");

        //when and then
        mockMvc.perform(MockMvcRequestBuilders.get("/contacts").flashAttr("searchCriteria",contactSearchCriteriaDTO))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/jsp/contact/view.jsp"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
                .andExpect(MockMvcResultMatchers.model().attribute("contact", hasProperty("firstName", equalTo("Sophie"))));
    }

    @DisplayName("Test to return list and data for matching criteria for more than one contact")
    @Test
    public void testGetMethodReturnMultipleContacts() throws Exception {

        // given - precondition or setup
        ContactSearchCriteriaDTO contactSearchCriteriaDTO = new ContactSearchCriteriaDTO();
        contactSearchCriteriaDTO.setFirstName("");
        contactSearchCriteriaDTO.setLastName("B");
        contactSearchCriteriaDTO.setOrganisationName("");

        //when and then
        mockMvc.perform(MockMvcRequestBuilders.get("/contacts").flashAttr("searchCriteria",contactSearchCriteriaDTO))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/jsp/contact/list.jsp"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("contacts"));
    }

    @DisplayName("Test to return view and data for the given id")
    @Test
    public void testGetMethodReturnContactForAGivenId() throws Exception {

        // given - precondition or setup
        Integer id = 1;

        //when and then
        mockMvc.perform(MockMvcRequestBuilders.get("/contacts/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/jsp/contact/view.jsp"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
                .andExpect(MockMvcResultMatchers.model().attribute("contact", hasProperty("id", equalTo(1))));
    }

    @DisplayName("Test to return edit screen for the given id")
    @Test
    public void testGetMethodReturnEditContactForAGivenId() throws Exception {

        // given - precondition or setup
        Integer id = 1;

        //when and then
        mockMvc.perform(MockMvcRequestBuilders.get("/contacts/{id}/edit", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/jsp/contact/edit.jsp"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
                .andExpect(MockMvcResultMatchers.model().attribute("contact", hasProperty("id", equalTo(1))));
    }

    @DisplayName("Test to return edit if update has errors")
    @Test
    public void testPostMethod() throws Exception {

        // given - precondition or setup
        Organisation organisation = new Organisation();
        organisation.setId(3);
        organisation.setName("THE AUSTRALIAN");
        organisation.setAbn(33333333333L);

        Date currentDate = new Date();

        Contact contact = new Contact();
        contact.setId(8);
        contact.setFirstName("Nick");
        contact.setLastName("NameTooLongMoreThanThirtyCharacters");
        contact.setOrganisation(organisation);
        contact.setCreatedAt(currentDate);

        ContactDTO contactDTO = ContactDTO.createBy(contact);

        //when and then
        mockMvc.perform(MockMvcRequestBuilders.post("/contacts").flashAttr("contact",contactDTO))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/jsp/contact/edit.jsp"))
                .andExpect(MockMvcResultMatchers.model().hasErrors());
    }

    @DisplayName("Test to return list if update has successful")
    @Test
    public void testPostMethodUpdatesContact() throws Exception {

        // given - precondition or setup
        Organisation organisation = new Organisation();
        organisation.setId(3);
        organisation.setName("THE AUSTRALIAN");
        organisation.setAbn(33333333333L);

        Date currentDate = new Date();

        Contact contact = new Contact();
        Integer contactId = 8;
        contact.setId(contactId);
        contact.setFirstName("Nick");
        contact.setLastName("Jack");
        contact.setOrganisation(organisation);
        contact.setCreatedAt(currentDate);

        ContactDTO contactDTO = ContactDTO.createBy(contact);

        //when and then
        mockMvc.perform(MockMvcRequestBuilders.post("/contacts").flashAttr("contact",contactDTO))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/contacts/"+contactId));
    }
}

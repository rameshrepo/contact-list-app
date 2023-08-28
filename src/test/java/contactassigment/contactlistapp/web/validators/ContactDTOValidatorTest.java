package contactassigment.contactlistapp.web.validators;

import contactassigment.contactlistapp.dto.Constants;
import contactassigment.contactlistapp.dto.ContactDTO;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.*;

import static org.junit.Assert.*;

public class ContactDTOValidatorTest {

    @DisplayName("Test Validation logic for Invalid Contact DTO")
    @Test
    public void testValidateInValidContactDTO() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setFirstName("Big First Name which is longer than 30 characters");
        contactDTO.setLastName("Peter last name which is longer than 30 characters");
        Errors errors = new BeanPropertyBindingResult(contactDTO, "contactDTO");
        (new ContactDTOValidator()).validate(contactDTO, errors);
        assertNotNull(errors.getFieldError("firstName"));
        assertNotNull(errors.getFieldError("lastName"));
        assertTrue(new ContactDTOValidator().supports(ContactDTO.class));

        ContactDTO contactDTOWithNoValueSet = new ContactDTO();
        contactDTOWithNoValueSet.setLastName(Constants.EMPTY_STRING);
        contactDTOWithNoValueSet.setFirstName(Constants.EMPTY_STRING);
        errors = new BeanPropertyBindingResult(contactDTOWithNoValueSet, "contactDTO");
        (new ContactDTOValidator()).validate(contactDTOWithNoValueSet, errors);
        assertNotNull(errors.getFieldError("firstName"));
        assertNotNull(errors.getFieldError("lastName"));

    }

    @DisplayName("Test Validation logic for Valid Contact DTO")
    @Test
    public void testValidateValidContactDTO() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setFirstName("John");
        contactDTO.setLastName("Peter");
        Errors errors = new BeanPropertyBindingResult(contactDTO, "contactDTO");
        (new ContactDTOValidator()).validate(contactDTO, errors);
        assertNull(errors.getFieldError("firstName"));
        assertNull(errors.getFieldError("lastName"));
    }

}

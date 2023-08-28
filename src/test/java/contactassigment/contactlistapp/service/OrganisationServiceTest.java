package contactassigment.contactlistapp.service;

import contactassigment.contactlistapp.domain.ContactRepository;
import contactassigment.contactlistapp.domain.Organisation;
import contactassigment.contactlistapp.domain.OrganisationRepository;
import contactassigment.contactlistapp.dto.OrganisationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class OrganisationServiceTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @InjectMocks
    private OrganisationService organisationService;

    @BeforeEach
    public void setup() {
        organisationRepository = Mockito.mock(OrganisationRepository.class);
        organisationService = new OrganisationServiceImpl(organisationRepository);
    }

    @DisplayName("JUnit test for getAllOrganisation")
    @Test
    public void givenContactList_whenGetAllContacts_thenReturnContactList(){

        //given
        Organisation organisation1 = new Organisation();
        organisation1.setId(1);
        organisation1.setName("Test");
        organisation1.setAbn(1234567890L);

        Organisation organisation2 = new Organisation();
        organisation2.setId(2);
        organisation2.setName("Test2");
        organisation2.setAbn(1234567899L);

        List<Organisation> organisationList = new ArrayList<>();
        organisationList.add(organisation1);
        organisationList.add(organisation2);

        given(organisationRepository.findAll()).willReturn(organisationList);

        //when
        List<OrganisationDTO> orgList = organisationService.listAll();

        //then
        assertThat(orgList).isNotNull();
        assertThat(orgList.size()).isEqualTo(2);
    }
}
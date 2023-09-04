package contactassigment.contactlistapp.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.domain.Organisation;

public class ContactDTO
{

  public static ContactDTO createBy(Contact contact)
  {
    return new ContactDTO(contact);
  }

  public static List<ContactDTO> createListBy(List<Contact> contacts)
  {
    List<ContactDTO> contactDTOs = new ArrayList<>(contacts.size());
    for (Contact c : contacts)
    {
      contactDTOs.add(ContactDTO.createBy(c));
    }
    return contactDTOs;
  }

  private Integer id;

  private String firstName;

  private String lastName;

  private Date createdAt;

  private OrganisationDTO organisation;

  public ContactDTO()
  {
  }

  public ContactDTO(Contact contact)
  {
    setId(contact.getId());
    setFirstName(contact.getFirstName());
    setLastName(contact.getLastName());
    setCreatedAt(contact.getCreatedAt());
    Organisation org = contact.getOrganisation();
    if (org != null)
    {
      setOrganisation(new OrganisationDTO(contact.getOrganisation()));
    }
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getFirstName()
  {
    return this.firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  //used by jsp
  @SuppressWarnings("unused")
  public Date getCreatedAt() {return createdAt;}

  public OrganisationDTO getOrganisation()
  {
    return organisation;
  }

  public void setOrganisation(OrganisationDTO organisation)
  {
    this.organisation = organisation;
  }

  //used by jsp
  @SuppressWarnings("unused")
  public Long getOrganisationAbn()
  {
    OrganisationDTO org = getOrganisation();
    if (org != null)
    {
      return org.getAbn();
    }
    else
    {
      return 0L;
    }
  }

  //used by jsp
  @SuppressWarnings("unused")
  public String getOrganisationName()
  {
    OrganisationDTO org = getOrganisation();
    if (org != null)
    {
      return org.getName();
    }
    else
    {
      return Constants.EMPTY_STRING;
    }
  }

  //used by jsp
  @SuppressWarnings("unused")
  public String getOrganisationNameWithAbn()
  {
    OrganisationDTO org = getOrganisation();
    if (org != null)
    {
      return org.getFormattedNameWithAbn();
    }
    else
    {
      return Constants.EMPTY_STRING;
    }
  }
}

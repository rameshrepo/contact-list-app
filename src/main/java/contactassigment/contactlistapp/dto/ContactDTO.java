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
    List<ContactDTO> contactDTOs = new ArrayList<ContactDTO>(contacts.size());
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

  public Date getCreatedAt() {return createdAt;}

  public OrganisationDTO getOrganisation()
  {
    return organisation;
  }

  public void setOrganisation(OrganisationDTO organisation)
  {
    this.organisation = organisation;
  }

  public String getOrganisationInfo()
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

  public Long getOrganisationAbn()
  {
    OrganisationDTO org = getOrganisation();
    if (org != null)
    {
      return org.getAbn();
    }
    else
    {
      return new Long(0);
    }
  }

  public String getOrganisationAbnFormatted() {
    OrganisationDTO org = getOrganisation();
    if (org != null)
    {
      int groupSize = 3;
      String separator = " ";
      String unformatted = org.getAbn().toString();
      StringBuilder result = new StringBuilder(unformatted);

      int i = groupSize;
      while (i < unformatted.length()) {
        result.insert(unformatted.length() - i, separator);
        i += groupSize;
      }

      return result.toString();
    }
    else
    {
      return Constants.EMPTY_STRING;
    }

  }

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
}

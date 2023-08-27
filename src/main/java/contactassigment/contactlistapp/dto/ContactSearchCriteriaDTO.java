package contactassigment.contactlistapp.dto;

public class ContactSearchCriteriaDTO
{

  private String firstName = Constants.EMPTY_STRING;
  private String organisationName = Constants.EMPTY_STRING;

  private String lastName = Constants.EMPTY_STRING;

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String name)
  {
    this.firstName = name;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public String getOrganisationName()
  {
    return organisationName;
  }

  public void setOrganisationName(String organisationName)
  {
    this.organisationName = organisationName;
  }
}

package contactassigment.contactlistapp.dto;

import java.util.ArrayList;
import java.util.List;

import contactassigment.contactlistapp.domain.Organisation;

public class OrganisationDTO
{

  public static OrganisationDTO createBy(Organisation organisation)
  {
    return new OrganisationDTO(organisation);
  }

  public static List<OrganisationDTO> createListBy(List<Organisation> organisations)
  {
    List<OrganisationDTO> organisationDTOs = new ArrayList<>(organisations.size());
    for (Organisation o : organisations)
    {
      organisationDTOs.add(OrganisationDTO.createBy(o));
    }
    return organisationDTOs;
  }

  private Integer id;
  private String name;
  private long abn;

  //used by jsp
  @SuppressWarnings("unused")
  public OrganisationDTO()
  {
  }

  public OrganisationDTO(Organisation organisation)
  {
    setId(organisation.getId());
    setName(organisation.getName());
    setAbn(organisation.getAbn());
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Long getAbn() {
    return abn;
  }

  public void setAbn(long abn) {
    this.abn = abn;
  }

  public String getFormattedNameWithAbn() {
    return this.getName() + " (" + getFormattedAbn() + ") ";
  }

  private String getFormattedAbn() {
    int groupSize = 3;
    String separator = " ";
    String unformatted = this.getAbn().toString();
    StringBuilder result = new StringBuilder(unformatted);

    int i = groupSize;
    while (i < unformatted.length()) {
      result.insert(unformatted.length() - i, separator);
      i += groupSize;
    }
    return result.toString();
  }
}

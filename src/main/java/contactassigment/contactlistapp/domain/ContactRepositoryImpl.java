package contactassigment.contactlistapp.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.OracleCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class ContactRepositoryImpl implements ContactRepositoryCustom
{
  Logger logger = LoggerFactory.getLogger(ContactRepositoryImpl.class);

  @PersistenceContext
  EntityManager em;

  @Override
  public List<Contact> searchByNamesFetchOrganisation(String firstName, String lastName, String organisationName)
  {
    StringBuilder sbuilder = new StringBuilder("SELECT c FROM Contact c LEFT JOIN FETCH c.organisation o ");
    Map<String, String> parameters = new HashMap<>();
    Map<String, String> conditions = new HashMap<>();
    if (StringUtils.hasText(firstName))
    {
      parameters.put("firstNamePattern", firstName.replaceAll("\\*","%") + "%");
      conditions.put("firstNamePattern", "AND lower(c.firstName) like lower(:firstNamePattern) ");
    }
    if (StringUtils.hasText(lastName))
    {
      parameters.put("lastNamePattern", lastName.replaceAll("\\*","%") +"%");
      conditions.put("lastNamePattern", "AND lower(c.lastName) like lower(:lastNamePattern) ");
    }
    if (StringUtils.hasText(organisationName))
    {
      parameters.put("organisationNamePattern", organisationName.replaceAll("\\*","%") +"%");
      conditions.put("organisationNamePattern", "AND lower(o.name) like lower(:organisationNamePattern) ");
    }
    if (conditions.size() >0) {
      String baseQuery = conditions.values().stream().collect(Collectors.joining(" "));
      sbuilder.append(baseQuery);
    }
    String queryHQL = sbuilder.toString().replaceFirst("AND", "WHERE").trim();
    Long numberofRows = getRowCount(conditions, parameters);
    System.out.println("Number of Rows :" + numberofRows);
    System.out.println(queryHQL);
    javax.persistence.Query q = em.createQuery(queryHQL);
    for (Map.Entry<String,String> parameter: parameters.entrySet()) {
      q.setParameter(parameter.getKey(), parameter.getValue());
    }
    return q.getResultList();
  }

  public Long getRowCount(Map<String, String> conditions, Map<String, String> parameters) {
    StringBuilder queryBuilder = new StringBuilder("SELECT count(c.id) FROM Contact c LEFT JOIN c.organisation o ");
    if (conditions.size() >0)  {
      String baseQuery = conditions.values().stream().collect(Collectors.joining(" "));
      queryBuilder.append(baseQuery);
    }
    String rowCountQueryHQL = queryBuilder.toString().replaceFirst("AND", "WHERE").trim();
    System.out.println(rowCountQueryHQL);
    javax.persistence.Query q = em.createQuery(rowCountQueryHQL);
    for (Map.Entry<String,String> parameter: parameters.entrySet()) {
      q.setParameter(parameter.getKey(), parameter.getValue());
    }
    return (Long)q.getSingleResult();
    //return 1L;
  }

}

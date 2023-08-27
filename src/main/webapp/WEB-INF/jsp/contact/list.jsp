<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Contacts" context="${pageContext.servletContext.contextPath}">
    <jsp:body>
        <form:form method="get" modelAttribute="searchCriteria" commonName="searchCriteria" cssClass="form-horizontal">
            <div class="form-group">
                <label for="firstName" class="col-md-3 control-label">FirstName</label>
                <div class="col-sm-6">
                    <form:input path="firstName" type="text" cssClass="form-control" maxlength="30" id="firstName" placeholder="Enter first name to search for here...." />
                </div>
                <div class="col-sm-3"></div>
            </div>

            <div class="form-group">
                <label for="lastName" class="col-md-3 control-label">LastName</label>
                <div class="col-sm-6">
                    <form:input path="lastName" type="text" cssClass="form-control" maxlength="30" id="lastName" placeholder="Enter last name to search for here...." />
                </div>
                <div class="col-sm-3"></div>
            </div>

            <div class="form-group">
                <label for="organisationName" class="col-md-3 control-label">Organisation Name</label>
                <div class="col-sm-6">
                    <form:input path="organisationName" type="text" cssClass="form-control" id="organisationName" placeholder="Organisation Name" />
                </div>
                <div class="col-sm-3"></div>
            </div>

            <div class="form-group">
                <div class="col-sm-3"></div>
                <div class="col-sm-6" style="text-align:center">
                    <button type="submit" class="btn btn-primary">Search</button>
                </div>
                <div class="col-sm-3"></div>
            </div>
        </form:form>

        <c:choose>
            <c:when test="${not empty contacts}">
                <br>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Organisation</th>
                        <th>Date Created</th>
                        <th class="text-center">Action</th>
                    <tr>
                    </thead>
                    <c:forEach var="contact" items="${contacts}">
                        <tr class="vert-align">
                            <td>${contact.firstName} ${contact.lastName}</td>
                            <td>${contact.organisationInfo} (${contact.organisationAbnFormatted})</td>
                            <fmt:formatDate value="${contact.createdAt}" pattern="dd/MM/yyyy" var="createDate" />
                            <td>${createDate}</td>
                            <td class="text-center">
                                <a href="<c:url value="/contacts/${contact.id}"/>" class="btn btn-sm btn-info" style="padding: 0px 0px; width: 40px;">View</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:when test="${not invalidSearch && empty contacts}">
                <br>
                <table class="table">
                    <tr>
                        <td width="100%" class="text-center">
                            <h4>No result found.</h4>
                        <td>
                    </tr>
                </table>
            </c:when>
        </c:choose>
    </jsp:body>
</t:layout>
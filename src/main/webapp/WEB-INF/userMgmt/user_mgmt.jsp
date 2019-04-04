<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        User Management
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/dashboard.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>
        <div class="menuitem">
            <a href="<c:url value="/app/projekte/list/"/>">Liste der Projekte</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/projekte/projekt/new/"/>">Projekt anlegen</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/projekte/abteilungen/"/>">Abteilungen bearbeiten</a>
        </div>
    </jsp:attribute>
    <jsp:attribute name="content">
        <div class="container">
            <form method="post" class="stacked">
                <div class="column">
                    <%-- CSRF-Token --%>
                    <input type="hidden" name="csrf_token" value="${csrf_token}">
                    
                    <%-- Eingabefelder --%>
                    <label for="update_username">
                        Benutzername:
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="update_username" value="${pageContext.request.userPrincipal.name}" placeholder="${pageContext.request.userPrincipal.name}" readonly>
                    </div>
                    
                    <label for="update_vorname">
                        Vorname:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="update_vorname" value="${update_form.values["update_vorname"][0]}" placeholder="${vorname}">
                    </div>
                    
                    <label for="update_nachname">
                        Nachname:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="update_nachname" value="${update_form.values["update_nachname"][0]}" placeholder="${nachname}">
                    </div>

                    <label for="update_password1">
                        Altes Passwort:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="password" name="update_password1" value="${update_form.values["update_password1"][0]}">
                    </div>

                    <label for="update_password2">
                        Neues Passwort:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="password" name="update_password2" value="${update_form.values["update_password2"][0]}">
                    </div>
                    
                    <%-- Fehlermeldungen --%>
                    <c:if test="${!empty update_form.errors}">
                        <ul class="errors">
                            <c:forEach items="${update_form.errors}" var="error">
                                <li>${error}</li>
                                </c:forEach>
                        </ul>
                    </c:if>
                
                    <%-- Button zum Abschicken --%>
                    <div class="side-by-side">
                        <button type="submit">
                            Nutzerdaten aktualisieren
                        </button>
                    </div>
                </div>              
            </form>
        </div>
    </jsp:attribute>
</template:base>
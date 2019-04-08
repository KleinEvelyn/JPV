<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        <c:choose>
            <c:when test="${edit}">
                Projekt bearbeiten
            </c:when>
            <c:otherwise>
                Projekt anlegen
            </c:otherwise>
        </c:choose>
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/projekt_edit.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/app/projekte/list/"/>">Liste der Projekte</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <div class="column">
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                <label for="projekt_owner">Eigentümer:</label>
                <div class="side-by-side">
                    <input type="text" name="projekt_owner" value="${pageContext.request.userPrincipal.name}" readonly="readonly">
                </div>

                <label for="projekt_abteilung">Abteilung:</label>
                <div class="side-by-side">
                    <select name="projekt_abteilung">
                        <option value="">Keine Abteilung</option>

                        <c:forEach items="${abteilungen}" var="abteilung">
                            <option value="${abteilung.id}" ${projekt_form.values["projekt_abteilung"][0] == abteilung.id.toString() ? 'selected' : ''}>
                                <c:out value="${abteilung.kuerzel}" />
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="projekt_due_date">
                    Fällig am:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="projekt_due_date" value="${projekt_form.values["projekt_due_date"][0]}" placeholder="${dueDate}">
                    <input type="text" name="projekt_due_time" value="${projekt_form.values["projekt_due_time"][0]}" placeholder="${dueTime}">
                </div>

                <label for="projekt_status">
                    Status:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side margin">
                    <select name="projekt_status">
                        <c:forEach items="${statuses}" var="status">
                            <option value="${status}" ${projekt_form.values["projekt_status"][0] == status ? 'selected' : ''}>
                                <c:out value="${status.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="projekt_short_text">
                    Bezeichnung:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="projekt_short_text" value="${projekt_form.values["projekt_short_text"][0]}" placeholder="${bezeichnung}">
                </div>

                <label for="projekt_long_text">
                    Beschreibung:
                </label>
                <div class="side-by-side">
                    <textarea name="projekt_long_text" placeholder="${beschreibung}"><c:out value="${projekt_form.values['projekt_long_text'][0]}"/></textarea>
                </div>

                <%-- Fehlermeldungen --%>
                <c:if test="${!empty projekt_form.errors}">
                    <ul class="errors">
                        <c:forEach items="${projekt_form.errors}" var="error">
                            <li>${error}</li>
                        </c:forEach>
                    </ul>
                </c:if>
                
                <%-- Button zum Abschicken --%>
                <div class="side-by-side">
                    <button class="icon-pencil" type="submit" name="action" value="save">
                        Speichern
                    </button>

                    <c:if test="${edit}">
                        <button class="icon-trash" type="submit" name="action" value="delete">
                            Löschen
                        </button>
                    </c:if>
                </div>
            </div>
        </form>
            
        <script>
            // Workaround um die Values (Date, Time, Bezeichnung und Beschreibung) zu fÃ¼llen, sorry 4 bad coding ;-) 
            let datePlaceholder = document.getElementsByName("projekt_due_date")[0].placeholder;
            let yy = datePlaceholder.substring(0,4);
            let mm = datePlaceholder.substring(5,7);
            let dd = datePlaceholder.substring(8,10);            
            document.getElementsByName("projekt_due_date")[0].value = dd +"." +mm +"." +yy;

            let timePlaceholder = document.getElementsByName("projekt_due_time")[0].placeholder;
            document.getElementsByName("projekt_due_time")[0].value = timePlaceholder;
            
            let bezPlaceholder = document.getElementsByName("projekt_short_text")[0].placeholder;
            document.getElementsByName("projekt_short_text")[0].value = bezPlaceholder;
            
            let besPlaceholder = document.getElementsByName("projekt_long_text")[0].placeholder;
            document.getElementsByName("projekt_long_text")[0].value = besPlaceholder;
        </script>  
    </jsp:attribute>
</template:base>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Abteilungen bearbeiten
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/abteilung_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/tasks/list/"/>">Liste der Projekte</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <%-- CSRF-Token --%>
            <input type="hidden" name="csrf_token" value="${csrf_token}">

            <%-- Feld zum Anlegen einer neuen Kategorie --%>
            <div class="column margin">
                <h2>Abteilung anlegen und bearbeiten</h2>
                <label for="j_username">Name</label>
                <input type="text" name="name" value="${abteilungen_form.values["name"][0]}">
                
                <label for="j_username">Kürzel</label>
                <input type="text" name="kuerzel" value="${abteilungen_form.values["kuerzel"][0]}">
                
                <label for="j_username">Beschreibung</label>
                <input type="text" name="beschreibung" value="${abteilungen_form.values["beschreibung"][0]}">

                <button type="submit" name="action" value="create">
                    Anlegen
                </button>

                <%-- Fehlermeldungen --%>
                <c:if test="${!empty abteilungen_form.errors}">
                    <ul class="errors margin">
                        <c:forEach items="${abteilungen_form.errors}" var="error">
                            <li>${error}</li>
                            </c:forEach>
                    </ul>
                </c:if>

                <%-- Vorhandene Kategorien --%>
                <c:choose>
                    <c:when test="${empty abteilungen}">
                        <p>
                            Es sind noch keine Abteilungen vorhanden.
                        </p>
                    </c:when>
                    <c:otherwise>
                        <div>
                            <div class="margin">
                                <c:forEach items="${abteilungen}" var="abteilung">
                                    <input type="checkbox" name="abteilung" id="${'abteilung-'.concat(abteilung.id)}" value="${abteilung.id}" />
                                    <label for="${'abteilung-'.concat(abteilung.id)}">
                                        <c:out value="${abteilung.name}"/>
                                    </label>
                                    <br />
                                </c:forEach>
                            </div>

                            <button type="submit" name="action" value="delete" class="icon-trash">
                                Markierte löschen
                            </button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </form>
    </jsp:attribute>
</template:base>
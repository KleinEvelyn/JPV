<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Liste der Aufgaben
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/projekt_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/projekte/projekt/new/"/>">Aufgabe anlegen</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/projekte/abteilungen/"/>">Kategorien bearbeiten</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <%-- Suchfilter --%>
        <form method="GET" class="horizontal" id="search">
            <input type="text" name="search_text" value="${param.search_text}" placeholder="Beschreibung"/>

            <select name="search_abteilung">
                <option value="">Alle Kategorien</option>

                <c:forEach items="${abteilungen}" var="abteilung">
                    <option value="${abteilung.id}" ${param.search_abteilung == abteilung.id ? 'selected' : ''}>
                        <c:out value="${abteilung.name}" />
                    </option>
                </c:forEach>
            </select>

            <select name="search_status">
                <option value="">Alle Stati</option>

                <c:forEach items="${statuses}" var="status">
                    <option value="${status}" ${param.search_status == status ? 'selected' : ''}>
                        <c:out value="${status.label}"/>
                    </option>
                </c:forEach>
            </select>

            <button class="icon-search" type="submit">
                Suchen
            </button>
        </form>

        <%-- Gefundene Aufgaben --%>
        <c:choose>
            <c:when test="${empty projekte}">
                <p>
                    Es wurden keine Aufgaben gefunden. 🐈
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="dhbw.jpv.common.web.WebUtils"/>
                
                <table>
                    <thead>
                        <tr>
                            <th>Bezeichnung</th>
                            <th>Kategorie</th>
                            <th>Eigentümer</th>
                            <th>Status</th>
                            <th>Fällig am</th>
                        </tr>
                    </thead>
                    <c:forEach items="${projekte}" var="projekt">
                        <tr>
                            <td>
                                <a href="<c:url value="/app/projekte/projekt/${projekt.id}/"/>">
                                    <c:out value="${projekt.shortText}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${projekt.abteilung.name}"/>
                            </td>
                            <td>
                                <c:out value="${projekt.owner.username}"/>
                            </td>
                            <td>
                                <c:out value="${projekt.status.label}"/>
                            </td>
                            <td>
                                <c:out value="${utils.formatDate(projekt.dueDate)}"/>
                                <c:out value="${utils.formatTime(projekt.dueTime)}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>
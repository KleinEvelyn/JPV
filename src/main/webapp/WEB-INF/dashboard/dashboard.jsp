<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Dashboard
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/dashboard.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/projekte/list/"/>">Liste der Projekte</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/projekte/projekt/new/"/>">Projekt anlegen</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/projekte/abteilungen/"/>">Abteilungen bearbeiten</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/app/userMgmt/"/>">Benutzer bearbeiten</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/app/rest/abteilung"/>">REST Abteilung</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/app/rest/projekt"/>">REST Projekt</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <c:choose>
            <c:when test="${empty sections}">
                <p>
                    Es wurden keine Dashboard-Kacheln gefunden.
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="dhbw.jpv.common.web.WebUtils"/>

                <c:forEach items="${sections}" var="section">
                    <div id="dashHeadline">
                        <c:out value="${section.label}"/>
                    </div>

                    <c:forEach items="${section.tiles}" var="tile">
                        <div class="tile ${tile.cssClass}">
                            <a href="<c:url value="${tile.href}"/>">
                                <div class="content">
                                    <div class="label">
                                        <c:out value="${tile.label}"/>
                                    </div>
                                    <div class="icon icon-${tile.icon}"></div>
                                    <div class="amount">
                                        <c:choose>
                                            <c:when test="${tile.showDecimals}">
                                                <c:out value="${utils.formatDouble(tile.amount)}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${utils.formatInteger(tile.amount)}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>
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
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script> 
        <script type="text/javascript" src="<c:url value="/js/piechart.js"/>"></script> 
        <script type="text/javascript" src="<c:url value="/js/AbteilungResource.js"/>"></script> 
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
                <div class="column rest">
                    <%-- CSRF-Token --%>
                    <input type="hidden" name="csrf_token" value="${csrf_token}">
                    
                    <%-- REST Webservice --%>
                    <h1>REST Webservice // Projekt-Overview</h1>
                    <div id="piechart"></div>
                    <div id="abteilungen"></div>
                    <input id="search" type="text" />
                    <button id="btn" type="button">Test</button>
                </div>              
            </form>
        </div>
        <script>
            abteilungResource = new AbteilungResource();
            let reloadAbteilungen = async () => {
                let response = await abteilungResource.findAbteilungen(searchTerm);

                if ("exception" in response) {
                    alert('[${response.exception}]: ${response.message}')
                } else {
                    let abteilungenElement = document.getElementById("abteilungen");
                    abteilungenElement.innerHTML = "";

                    response.forEach(abteilung => {
                        let abteilungElement = document.createElement("div");
                        abteilungElement.classList.add("abteilung");
                        abteilungenElement.appendChild(abteilungElement);
                        abteilungElement.innerHTML = '<b>'+abteilung.name+'</b> <br/>'
                            + '<span class="label">Abteilung:</span>'+abteilung.name+'<br/>'
                            + '<span class="label">Kürzel:</span>'+abteilung.kuerzel+'<br/>'
                            + '<span class="label">Beschreibung:</span>'+abteilung.beschreibung+'<br/>';
                    });
                }
            };
            
            window.addEventListener("load", () => reloadAbteilungen());
            let btn = document.getElementById("btn");
            let searchTerm = document.getElementById("search").value;

            btn.addEventListener("click", () => reloadAbteilungen());
        </script>
    </jsp:attribute>
</template:base>
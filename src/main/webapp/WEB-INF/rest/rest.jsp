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
                    <textarea id="search" rows="1" placeholder="Suche nach Abteilungen"></textarea>
                    <table >
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Kürzel</th>
                                <th>Beschreibung</th>
                            </tr>
                        </thead>
                        <tbody id="abteilungen"></tbody>
                    </table>
                </div>              
            </form>
        </div>
        <script>
            abteilungResource = new AbteilungResource();
            let reloadAbteilungen = async (searchTerm) => {
                let response = await abteilungResource.findAbteilungen(searchTerm);

                if ("exception" in response) {
                    alert('[${response.exception}]: ${response.message}')
                } else {
                    let abteilungenElement = document.getElementById("abteilungen");
                    abteilungenElement.innerHTML = "";

                    response.forEach(abteilung => {
                        let abteilungElement = document.createElement("tr");
                        abteilungenElement.appendChild(abteilungElement);
                        abteilungElement.innerHTML = '<td>'+abteilung.name+'</td>' 
                                + '<td>'+abteilung.kuerzel+'</td>'
                                + '<td>'+abteilung.beschreibung+'</td>';
                    
                    });
                }
            };
            let inputField = document.getElementById("search");
            inputField.onkeyup = () => {
                let searchTerm = inputField.value;
                console.log(searchTerm);
                reloadAbteilungen(searchTerm);
            }
            
            window.addEventListener("load", () => reloadAbteilungen(""));
        </script>
    </jsp:attribute>
</template:base>
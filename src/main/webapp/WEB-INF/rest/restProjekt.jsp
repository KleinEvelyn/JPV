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
        <script type="text/javascript" src="<c:url value="/js/ProjektResource.js"/>"></script> 
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
                    <h1>REST Webservice</h1>
                    <h3>Suche nach Name des Projekts (Volltextsuche)</h3>
                    <textarea id="search" rows="1" placeholder="Suche nach Abteilungen"></textarea>
                    <table >
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Abteilung</th>
                                <th>Projectowner</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody id="projekte"></tbody>
                    </table>
                </div>              
            </form>
        </div>
        <script>
            projektResource = new ProjektResource();
            let reloadProjekte = async (searchTerm) => {
                let response = await projektResource.findProjekte(searchTerm);

                if ("exception" in response) {
                    alert('[${response.exception}]: ${response.message}')
                } else {
                    let projekteElement = document.getElementById("projekte");
                    projekteElement.innerHTML = "";

                    response.forEach(projekt => {
                        let projektElement = document.createElement("tr");
                        projekteElement.appendChild(projektElement);
                        projektElement.innerHTML = '<td>'+projekt.shortText+'</td>' 
                                + '<td>'+projekt.abteilung.kuerzel+'</td>'
                                + '<td>'+projekt.owner.username+'</td>'
                                + '<td>'+projekt.status+'</td>';
                    
                    });
                }
            };
            let inputField = document.getElementById("search");
            inputField.onkeyup = () => {
                let searchTerm = inputField.value;
                console.log(searchTerm);
                reloadProjekte(searchTerm);
            }
            
            window.addEventListener("load", () => reloadProjekte(""));
        </script>
    </jsp:attribute>
</template:base>
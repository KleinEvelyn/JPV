/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbw.jpv.projekte.web;

import dhbw.jpv.common.web.FormValues;
import dhbw.jpv.projekte.ejb.AbteilungBean;
import dhbw.jpv.projekte.ejb.ProjektBean;
import dhbw.jpv.common.ejb.ValidationBean;
import dhbw.jpv.projekte.jpa.Abteilung;
import dhbw.jpv.projekte.jpa.Projekt;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anzeigen und Bearbeiten der Kategorien. Die Seite besitzt ein
 * Formular, mit dem ein neue Kategorie angelegt werden kann, sowie eine Liste,
 * die zum Löschen der Kategorien verwendet werden kann.
 */
@WebServlet(urlPatterns = {"/app/projekte/abteilungen/"})
public class AbteilungListServlet extends HttpServlet {

    @EJB
    AbteilungBean abteilungBean;

    @EJB
    ProjektBean projektBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Alle vorhandenen Kategorien ermitteln
        request.setAttribute("abteilungen", this.abteilungBean.findAllSorted());

        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/projekte/abteilung_list.jsp");
        dispatcher.forward(request, response);

        // Alte Formulardaten aus der Session entfernen
        HttpSession session = request.getSession();
        session.removeAttribute("abteilungen_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen        
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "create":
                this.createAbteilung(request, response);
                break;
            case "delete":
                this.deleteAbteilungen(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neue Kategorie anlegen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void createAbteilung(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        String name = request.getParameter("name");
        String kuerzel = request.getParameter("kuerzel");
        String beschreibung = request.getParameter("beschreibung");
        
        Abteilung abteilung = new Abteilung(name, kuerzel, beschreibung);
        List<String> errors = this.validationBean.validate(abteilung);

        // Neue Kategorie anlegen
        if (errors.isEmpty()) {
            this.abteilungBean.saveNew(abteilung);
        }

        // Browser auffordern, die Seite neuzuladen
        if (!errors.isEmpty()) {
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("abteilungen_form", formValues);
        }

        response.sendRedirect(request.getRequestURI());
    }

    /**
     * Aufgerufen in doPost(): Markierte Kategorien löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteAbteilungen(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Markierte Abteilungs IDs auslesen
        String[] projektIds = request.getParameterValues("projekt");

        if (projektIds == null) {
            projektIds = new String[0];
        }

        // Abteilungen löschen
        for (String projektId : projektIds) {
            // Zu löschende Abteilung ermitteln
            Abteilung abteilung;

            try {
                abteilung = this.abteilungBean.findById(Long.parseLong(projektId));
            } catch (NumberFormatException ex) {
                continue;
            }

            if (abteilung == null) {
                continue;
            }

            // Bei allen betroffenen Projekten, den Bezug zur Abteilung aufheben
            List<Projekt> projekte = abteilung.getProjekte();

            if (projekte != null) {
                projekte.forEach((Projekt projekt) -> {
                    projekt.setAbteilung(null);
                    this.projektBean.update(projekt);
                });
            }

            // Und weg damit
            this.abteilungBean.delete(abteilung);
        }

        // Browser auffordern, die Seite neuzuladen
        response.sendRedirect(request.getRequestURI());
    }

}

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
@WebServlet(urlPatterns = {"/app/rest/abteilung"})
public class RESTAbteilungServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/rest/restAbteilung.jsp");
        dispatcher.forward(request, response);

    }
}

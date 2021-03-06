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

import dhbw.jpv.projekte.ejb.AbteilungBean;
import dhbw.jpv.projekte.ejb.ProjektBean;
import dhbw.jpv.projekte.jpa.Abteilung;
import dhbw.jpv.projekte.jpa.Projekt;
import dhbw.jpv.projekte.jpa.ProjektStatus;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet für die tabellarische Auflisten der Aufgaben.
 */
@WebServlet(urlPatterns = {"/app/projekte/list/"})
public class ProjektListServlet extends HttpServlet {

    @EJB
    private AbteilungBean categoryBean;
    
    @EJB
    private ProjektBean projektBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("abteilungen", this.categoryBean.findAllSorted());
        request.setAttribute("statuses", ProjektStatus.values());

        // Suchparameter aus der URL auslesen
        String searchText = request.getParameter("search_text");
        String searchCategory = request.getParameter("search_category");
        String searchStatus = request.getParameter("search_status");

        // Anzuzeigende Aufgaben suchen
        Abteilung category = null;
        ProjektStatus status = null;

        if (searchCategory != null) {
            try {
                category = this.categoryBean.findById(Long.parseLong(searchCategory));
            } catch (NumberFormatException ex) {
                category = null;
            }
        }

        if (searchStatus != null) {
            try {
                status = ProjektStatus.valueOf(searchStatus);
            } catch (IllegalArgumentException ex) {
                status = null;
            }

        }

        List<Projekt> projekte = this.projektBean.search(searchText, category, status);
        request.setAttribute("projekte", projekte);

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/projekte/projekt_list.jsp").forward(request, response);
    }
}

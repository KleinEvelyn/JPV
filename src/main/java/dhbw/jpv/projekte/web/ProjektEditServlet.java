package dhbw.jpv.projekte.web;

import dhbw.jpv.common.web.WebUtils;
import dhbw.jpv.common.web.FormValues;
import dhbw.jpv.projekte.ejb.AbteilungBean;
import dhbw.jpv.projekte.ejb.ProjektBean;
import dhbw.jpv.common.ejb.UserBean;
import dhbw.jpv.common.ejb.ValidationBean;
import dhbw.jpv.projekte.jpa.Projekt;
import dhbw.jpv.projekte.jpa.ProjektStatus;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/projekte/projekt/*")
public class ProjektEditServlet extends HttpServlet {

    @EJB
    ProjektBean projektBean;

    @EJB
    AbteilungBean abteilungBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("abteilungen", this.abteilungBean.findAllSorted());
        request.setAttribute("statuses", ProjektStatus.values());

        // Zu bearbeitende Aufgabe einlesen
        HttpSession session = request.getSession();

        Projekt projekt = this.getRequestedProjekt(request);
        request.setAttribute("edit", projekt.getId() != 0);
                                
        if (session.getAttribute("abteilung_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("abteilung_form", this.createProjektForm(projekt));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/projekte/projekt_edit.jsp").forward(request, response);
        
        session.removeAttribute("abteilung_form");
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
            case "save":
                this.saveProjekt(request, response);
                break;
            case "delete":
                this.deleteProjekt(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neue oder vorhandene Aufgabe speichern
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void saveProjekt(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String projektAbteilung = request.getParameter("projekt_abteilung");
        String projektDueDate = request.getParameter("projekt_due_date");
        String projektDueTime = request.getParameter("projekt_due_time");
        String projektStatus = request.getParameter("projekt_status");
        String projektShortText = request.getParameter("projekt_short_text");
        String projektLongText = request.getParameter("projekt_long_text");

        Projekt projekt = this.getRequestedProjekt(request);

        if (projektAbteilung != null && !projektAbteilung.trim().isEmpty()) {
            try {
                projekt.setAbteilung(this.abteilungBean.findById(Long.parseLong(projektAbteilung)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        Date dueDate = WebUtils.parseDate(projektDueDate);
        Time dueTime = WebUtils.parseTime(projektDueTime);

        if (dueDate != null) {
            projekt.setDueDate(dueDate);
        } else {
            errors.add("Das Datum muss dem Format dd.mm.yyyy entsprechen.");
        }

        if (dueTime != null) {
            projekt.setDueTime(dueTime);
        } else {
            errors.add("Die Uhrzeit muss dem Format hh:mm:ss entsprechen.");
        }

        try {
            projekt.setStatus(ProjektStatus.valueOf(projektStatus));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Status ist nicht vorhanden.");
        }

        projekt.setShortText(projektShortText);
        projekt.setLongText(projektLongText);

        this.validationBean.validate(projekt, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.projektBean.update(projekt);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/projekte/list/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("projekt_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    /**
     * Aufgerufen in doPost: Vorhandene Aufgabe löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteProjekt(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Projekt projekt = this.getRequestedProjekt(request);
        this.projektBean.delete(projekt);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/projekte/list/"));
    }

    /**
     * Zu bearbeitende Aufgabe aus der URL ermitteln und zurückgeben. Gibt
     * entweder einen vorhandenen Datensatz oder ein neues, leeres Objekt
     * zurück.
     *
     * @param request HTTP-Anfrage
     * @return Zu bearbeitende Aufgabe
     */
    private Projekt getRequestedProjekt(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Projekt projekt = new Projekt();
        projekt.setOwner(this.userBean.getCurrentUser());
        projekt.setDueDate(new Date(System.currentTimeMillis()));
        projekt.setDueTime(new Time(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String projektId = request.getPathInfo();

        if (projektId == null) {
            projektId = "";
        }

        projektId = projektId.substring(1);

        if (projektId.endsWith("/")) {
            projektId = projektId.substring(0, projektId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            projekt = this.projektBean.findById(Long.parseLong(projektId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return projekt;
    }

    /**
     * Neues FormValues-Objekt erzeugen und mit den Daten eines aus der
     * Datenbank eingelesenen Datensatzes füllen. Dadurch müssen in der JSP
     * keine hässlichen Fallunterscheidungen gemacht werden, ob die Werte im
     * Formular aus der Entity oder aus einer vorherigen Formulareingabe
     * stammen.
     *
     * @param projekt Die zu bearbeitende Aufgabe
     * @return Neues, gefülltes FormValues-Objekt
     */
    private FormValues createProjektForm(Projekt projekt) {
        Map<String, String[]> values = new HashMap<>();

        values.put("projekt_owner", new String[]{
            projekt.getOwner().getUsername()
        });

        if (projekt.getAbteilung() != null) {
            values.put("projekt_abteilung", new String[]{
                "" + projekt.getAbteilung().getId()
            });
        }

        values.put("projekt_due_date", new String[]{
            WebUtils.formatDate(projekt.getDueDate())
        });

        values.put("projekt_due_time", new String[]{
            WebUtils.formatTime(projekt.getDueTime())
        });

        values.put("projekt_status", new String[]{
            projekt.getStatus().toString()
        });

        values.put("projekt_short_text", new String[]{
            projekt.getShortText()
        });

        values.put("projekt_long_text", new String[]{
            projekt.getLongText()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}

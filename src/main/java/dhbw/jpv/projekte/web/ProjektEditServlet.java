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
@WebServlet(urlPatterns = "/app/tasks/task/*")
public class ProjektEditServlet extends HttpServlet {

    @EJB
    ProjektBean taskBean;

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
        request.setAttribute("categories", this.abteilungBean.findAllSorted());
        request.setAttribute("statuses", ProjektStatus.values());

        // Zu bearbeitende Aufgabe einlesen
        HttpSession session = request.getSession();

        Projekt task = this.getRequestedTask(request);
        request.setAttribute("edit", task.getId() != 0);
                                
        if (session.getAttribute("task_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("task_form", this.createTaskForm(task));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/tasks/task_edit.jsp").forward(request, response);
        
        session.removeAttribute("task_form");
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
                this.saveTask(request, response);
                break;
            case "delete":
                this.deleteTask(request, response);
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
    private void saveTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String projektAbteilung = request.getParameter("projekt_abteilung");
        String taskDueDate = request.getParameter("task_due_date");
        String taskDueTime = request.getParameter("task_due_time");
        String taskStatus = request.getParameter("task_status");
        String taskShortText = request.getParameter("task_short_text");
        String taskLongText = request.getParameter("task_long_text");

        Projekt task = this.getRequestedTask(request);

        if (projektAbteilung != null && !projektAbteilung.trim().isEmpty()) {
            try {
                task.setAbteilung(this.abteilungBean.findById(Long.parseLong(projektAbteilung)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        Date dueDate = WebUtils.parseDate(taskDueDate);
        Time dueTime = WebUtils.parseTime(taskDueTime);

        if (dueDate != null) {
            task.setDueDate(dueDate);
        } else {
            errors.add("Das Datum muss dem Format dd.mm.yyyy entsprechen.");
        }

        if (dueTime != null) {
            task.setDueTime(dueTime);
        } else {
            errors.add("Die Uhrzeit muss dem Format hh:mm:ss entsprechen.");
        }

        try {
            task.setStatus(ProjektStatus.valueOf(taskStatus));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Status ist nicht vorhanden.");
        }

        task.setShortText(taskShortText);
        task.setLongText(taskLongText);

        this.validationBean.validate(task, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.taskBean.update(task);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/list/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("task_form", formValues);

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
    private void deleteTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Projekt task = this.getRequestedTask(request);
        this.taskBean.delete(task);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/list/"));
    }

    /**
     * Zu bearbeitende Aufgabe aus der URL ermitteln und zurückgeben. Gibt
     * entweder einen vorhandenen Datensatz oder ein neues, leeres Objekt
     * zurück.
     *
     * @param request HTTP-Anfrage
     * @return Zu bearbeitende Aufgabe
     */
    private Projekt getRequestedTask(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Projekt task = new Projekt();
        task.setOwner(this.userBean.getCurrentUser());
        task.setDueDate(new Date(System.currentTimeMillis()));
        task.setDueTime(new Time(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String taskId = request.getPathInfo();

        if (taskId == null) {
            taskId = "";
        }

        taskId = taskId.substring(1);

        if (taskId.endsWith("/")) {
            taskId = taskId.substring(0, taskId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            task = this.taskBean.findById(Long.parseLong(taskId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return task;
    }

    /**
     * Neues FormValues-Objekt erzeugen und mit den Daten eines aus der
     * Datenbank eingelesenen Datensatzes füllen. Dadurch müssen in der JSP
     * keine hässlichen Fallunterscheidungen gemacht werden, ob die Werte im
     * Formular aus der Entity oder aus einer vorherigen Formulareingabe
     * stammen.
     *
     * @param task Die zu bearbeitende Aufgabe
     * @return Neues, gefülltes FormValues-Objekt
     */
    private FormValues createTaskForm(Projekt task) {
        Map<String, String[]> values = new HashMap<>();

        values.put("task_owner", new String[]{
            task.getOwner().getUsername()
        });

        if (task.getAbteilung() != null) {
            values.put("projekt_abteilung", new String[]{
                "" + task.getAbteilung().getId()
            });
        }

        values.put("task_due_date", new String[]{
            WebUtils.formatDate(task.getDueDate())
        });

        values.put("task_due_time", new String[]{
            WebUtils.formatTime(task.getDueTime())
        });

        values.put("task_status", new String[]{
            task.getStatus().toString()
        });

        values.put("task_short_text", new String[]{
            task.getShortText()
        });

        values.put("task_long_text", new String[]{
            task.getLongText()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
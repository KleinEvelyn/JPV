package dhbw.jpv.common.web;

import dhbw.jpv.common.ejb.UserBean;
import dhbw.jpv.common.ejb.ValidationBean;
import dhbw.jpv.common.jpa.User;
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
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/userMgmt/")
public class UserEditServlet extends HttpServlet {
    
    @EJB
    ValidationBean validationBean;
    
    @EJB
    UserBean userBean;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        request.setAttribute("vorname", userBean.getCurrentUser().getVorname());
        request.setAttribute("nachname", userBean.getCurrentUser().getNachname());
        
        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/userMgmt/user_mgmt.jsp");
        dispatcher.forward(request, response);
        
        // Alte Fehlermeldung zurücksetzen
        request.getSession().removeAttribute("update_form");
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Formulareingaben auslesen        
        String username = request.getParameter("update_username");
        String vorname = request.getParameter("update_vorname");
        String nachname = request.getParameter("update_nachname");
        String oldPassword = request.getParameter("update_password1");
        String newPassword1 = request.getParameter("update_password2");
        String newPassword2 = request.getParameter("update_password3");
        
        // Eingaben prüfen
        User tempUser = new User(username, vorname, nachname, oldPassword);
        List<String> errors = this.validationBean.validate(tempUser);
        //this.validationBean.validate(tempUser.getPassword(), errors);
        
        if (newPassword1 == null && newPassword2 == null && !newPassword1.equals(newPassword2)) {
            errors.add("Die beiden neuen Passwörter stimmen nicht überein.");
        }
        
        // Passwort aktualisieren
        if (errors.isEmpty() && newPassword1 != null && !newPassword1.trim().isEmpty()) {
            try {
                User newUser = userBean.getCurrentUser();
                this.userBean.changePassword(newUser, oldPassword, newPassword1.trim());
                this.userBean.update(newUser);
            } catch (UserBean.InvalidCredentialsException ex) {
                errors.add(ex.getMessage());
            }
        }
        
        // Redirect 
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            User newUser = userBean.getCurrentUser();
            newUser.setVorname(vorname);
            newUser.setNachname(nachname);
            this.userBean.update(newUser);
            
            response.sendRedirect(WebUtils.appUrl(request, "/app/userMgmt/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("update_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }
}

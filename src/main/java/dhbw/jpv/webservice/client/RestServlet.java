package dhbw.jpv.webservice.client;

import dhbw.jpv.common.web.*;
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
@WebServlet(urlPatterns = "/app/rest/")
public class RestServlet extends HttpServlet {
    
    @EJB
    ValidationBean validationBean;
    
    @EJB
    UserBean userBean;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/rest/rest.jsp");
        dispatcher.forward(request, response);       
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        /** TBD **/
    }
}

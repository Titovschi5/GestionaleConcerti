package servlet;

import dao.DaoFactory;
import dao.model.UtenteDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/conferma")
public class ConfermaAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            request.setAttribute("errorMessage", "Token non valido.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        UtenteDao utenteDao = DaoFactory.getDaoFactory().getUtenteDao();
        boolean attivato = utenteDao.attivaUtente(token);

        if (attivato) {
            request.setAttribute("message", "Account attivato con successo! Ora puoi accedere.");
        } else {
            request.setAttribute("errorMessage", "Token non valido o scaduto. Richiedi un nuovo link di conferma.");
        }

        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
}
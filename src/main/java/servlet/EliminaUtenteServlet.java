package servlet;

import dao.DaoFactory;
import dao.model.UtenteDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Utente;

import java.io.IOException;
import java.util.List;

@WebServlet("/eliminaUtente")
public class EliminaUtenteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long utenteId = Long.parseLong(request.getParameter("id"));

        UtenteDao utenteDao = DaoFactory.getDaoFactory().getUtenteDao();
        Utente utente = utenteDao.findById(utenteId);

        if (utente != null) {
            utenteDao.delete(utenteId);
        }

        // Ricarica la lista aggiornata degli utenti
        List<Utente> listaUtenti = utenteDao.getAll();
        request.getSession().setAttribute("listaUtenti", listaUtenti);

        request.getRequestDispatcher("/WEB-INF/jsp/dashboardAdmin.jsp").forward(request, response);
    }
}
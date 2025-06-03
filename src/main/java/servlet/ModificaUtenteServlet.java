package servlet;

import dao.DaoFactory;
import dao.model.UtenteDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Utente;

import java.io.IOException;
import java.util.List;

@WebServlet("/modificaUtente")
public class ModificaUtenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long utenteId = Long.parseLong(request.getParameter("id"));
        UtenteDao utenteDao = DaoFactory.getDaoFactory().getUtenteDao();
        Utente utente = utenteDao.findById(utenteId);

        request.setAttribute("utente", utente);
        request.getRequestDispatcher("/WEB-INF/jsp/modificaUtente.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String ruolo = request.getParameter("ruolo");

        UtenteDao utenteDao = DaoFactory.getDaoFactory().getUtenteDao();
        Utente utente = utenteDao.findById(id);
        if (utente != null) {
            utente.setUsername(username);
            utente.setEmail(email);
            utente.setRuolo(ruolo);
            utenteDao.update(utente);

            // Ricarica la lista aggiornata degli utenti
            List<Utente> listaUtenti = utenteDao.getAll();
            HttpSession session = request.getSession();
            session.setAttribute("listaUtenti", listaUtenti);
        }

        request.getRequestDispatcher("/WEB-INF/jsp/dashboardAdmin.jsp").forward(request, response);
    }
}
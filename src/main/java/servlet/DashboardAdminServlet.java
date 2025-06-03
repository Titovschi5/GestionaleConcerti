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

@WebServlet("/dashboard")
public class DashboardAdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UtenteDao utenteDao = DaoFactory.getDaoFactory().getUtenteDao();
        List<Utente> listaUtenti = utenteDao.getAll();
        request.setAttribute("listaUtenti", listaUtenti);

        request.getRequestDispatcher("/WEB-INF/jsp/dashboardAdmin.jsp").forward(request, response);
    }
}
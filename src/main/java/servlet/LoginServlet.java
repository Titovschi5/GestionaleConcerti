package servlet;

import dao.DaoFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Evento;
import model.Utente;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Utente utente = DaoFactory.getDaoFactory().getUtenteDao().login(email, password);

        if (utente != null) {
            // Verifica se l'account è attivato
            if (!utente.isAttivato()) {
                request.setAttribute("msg", "Account non attivato. Controlla la tua email per il link di conferma.");
                request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", utente);

            if ("admin".equalsIgnoreCase(utente.getRuolo())) {
                List<Evento> listaEventi = DaoFactory.getDaoFactory().getEventoDao().getEventiByOrganizzatore(utente);

                // Creazione della mappa partecipanti
                Map<Long, List<Utente>> mappaPartecipanti = new HashMap<>();
                for (Evento evento : listaEventi) {
                    List<Utente> partecipanti = DaoFactory.getDaoFactory().getIscrizioneDao().getPartecipantiEvento(evento.getId());
                    mappaPartecipanti.put(evento.getId(), partecipanti);
                }

                List<Utente> listaUtenti = DaoFactory.getDaoFactory().getUtenteDao().getAll();

                //HttpSession session1 = request.getSession();
                session.setAttribute("user", utente);
                session.setAttribute("listaEventi", listaEventi);
                session.setAttribute("listaUtenti", listaUtenti);
                session.setAttribute("mappaPartecipanti", mappaPartecipanti);

                request.getRequestDispatcher("WEB-INF/jsp/dashboardAdmin.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/homeutente");
            }
        } else {
            request.setAttribute("msg", "Email o password errati");
            request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
}
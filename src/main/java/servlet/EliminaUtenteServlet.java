package servlet;

import dao.DaoFactory;
import dao.model.UtenteDao;
import dao.model.EventoDao;
import dao.model.IscrizioneDao;
import model.Evento;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Utente;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

        EventoDao eventoDao = DaoFactory.getDaoFactory().getEventoDao();
        IscrizioneDao iscrizioneDao = DaoFactory.getDaoFactory().getIscrizioneDao();
        jakarta.servlet.http.HttpSession session = request.getSession(false);

        if (session != null) {
            Utente organizzatore = (Utente) session.getAttribute("user");
            if (organizzatore != null) {
                List<Evento> listaEventi = eventoDao.getEventiByOrganizzatore(organizzatore);
                for (Evento ev : listaEventi) {
                    List<Utente> partecipanti = iscrizioneDao.getPartecipantiEvento(ev.getId());
                    ev.setPartecipanti(partecipanti);
                }
                session.setAttribute("listaEventi", listaEventi);
                request.setAttribute("listaEventi", listaEventi);
                // Add mappaPartecipanti
                Map<Long, List<Utente>> mappaPartecipanti = new HashMap<>();
                for (Evento ev : listaEventi) {
                    List<Utente> iscritti = iscrizioneDao.getPartecipantiEvento(ev.getId());
                    mappaPartecipanti.put(ev.getId(), iscritti);
                }
                session.setAttribute("mappaPartecipanti", mappaPartecipanti);
                request.setAttribute("mappaPartecipanti", mappaPartecipanti);
            }
        }

        request.getRequestDispatcher("/WEB-INF/jsp/dashboardAdmin.jsp").forward(request, response);
    }
}
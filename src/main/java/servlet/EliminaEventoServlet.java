package servlet;

import dao.DaoFactory;
import dao.model.EventoDao;
import dao.model.IscrizioneDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Evento;
import model.Utente;

import java.io.IOException;
import java.util.List;

@WebServlet("/eliminaEvento")
public class EliminaEventoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long eventoId = Long.parseLong(request.getParameter("eventoId"));
        Utente organizzatore = (Utente) request.getSession().getAttribute("user");

        if (organizzatore == null) {
            // Reindirizza alla pagina di login se l'utente non è in sessione
            response.sendRedirect("login");
            return;
        }

        EventoDao eventoDao = DaoFactory.getDaoFactory().getEventoDao();
        IscrizioneDao iscrizioneDao = DaoFactory.getDaoFactory().getIscrizioneDao();
        Evento evento = eventoDao.findById(eventoId);

        if (evento != null && evento.getOrganizzatore().getId().equals(organizzatore.getId())) {
            try {
                // Prima eliminare tutte le iscrizioni associate a questo evento
                iscrizioneDao.rimuoviIscrizioniPerEvento(eventoId);

                // Poi eliminare l'evento
                eventoDao.remove(eventoId);

                // Aggiungi messaggio di successo
                request.setAttribute("successMessage", "Evento eliminato con successo");
            } catch (Exception e) {
                // In caso di errore, gestisci l'eccezione
                request.setAttribute("errorMessage", "Errore durante l'eliminazione dell'evento: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            request.setAttribute("errorMessage", "Non hai il permesso di eliminare questo evento");
        }

        // Ricarica la lista aggiornata degli eventi
        List<Evento> listaEventi = eventoDao.getEventiByOrganizzatore(organizzatore);

        // Aggiungi i partecipanti a ciascun evento e aggiorna i posti disponibili
        for (Evento ev : listaEventi) {
            List<Utente> iscritti = iscrizioneDao.getPartecipantiEvento(ev.getId());
            ev.setPartecipanti(iscritti);

            // Aggiorna i posti disponibili
            long numIscritti = iscrizioneDao.countIscrittiPerEvento(ev);
            ev.setPostiDisponibili(ev.getCapacita() - (int) numIscritti);
        }

        // Aggiorna la lista nella sessione e nella request
        request.getSession().setAttribute("listaEventi", listaEventi);
        request.setAttribute("listaEventi", listaEventi);

        // Mappa dei partecipanti per ogni evento
        java.util.Map<Long, java.util.List<Utente>> mappaPartecipanti = new java.util.HashMap<>();
        for (Evento ev : listaEventi) {
            java.util.List<Utente> iscritti = iscrizioneDao.getPartecipantiEvento(ev.getId());
            mappaPartecipanti.put(ev.getId(), iscritti);
        }
        request.getSession().setAttribute("mappaPartecipanti", mappaPartecipanti);
        request.setAttribute("mappaPartecipanti", mappaPartecipanti);

        request.getRequestDispatcher("/WEB-INF/jsp/dashboardAdmin.jsp").forward(request, response);
    }
}
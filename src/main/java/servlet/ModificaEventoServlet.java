package servlet;

import dao.DaoFactory;
import dao.model.CategoriaDao;
import dao.model.EventoDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Categoria;
import model.Evento;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/modificaEvento")
public class ModificaEventoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long eventoId = Long.parseLong(request.getParameter("id"));
        EventoDao eventoDao = DaoFactory.getDaoFactory().getEventoDao();
        CategoriaDao categoriaDao = DaoFactory.getDaoFactory().getCategoriaDao();

        Evento evento = eventoDao.findById(eventoId);
        List<Categoria> categorie = categoriaDao.getAllCategorie();

        request.setAttribute("evento", evento);
        request.setAttribute("categorie", categorie);
        request.getRequestDispatcher("/WEB-INF/jsp/modificaEvento.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long eventoId = Long.parseLong(request.getParameter("id"));
        String nome = request.getParameter("nome");
        String descrizione = request.getParameter("descrizione");
        String luogo = request.getParameter("luogo");
        LocalDate data = LocalDate.parse(request.getParameter("data"));
        LocalTime ora = LocalTime.parse(request.getParameter("ora"));

        EventoDao eventoDao = DaoFactory.getDaoFactory().getEventoDao();
        CategoriaDao categoriaDao = DaoFactory.getDaoFactory().getCategoriaDao();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventoDateTime = LocalDateTime.of(data, ora);
        if (eventoDateTime.isBefore(now.plusHours(24))) {
            request.setAttribute("erroreData", "L'evento deve iniziare almeno 24 ore dopo il momento della modifica.");
            request.setAttribute("evento", eventoDao.findById(eventoId));
            request.setAttribute("categorie", categoriaDao.getAllCategorie());
            request.getRequestDispatcher("/WEB-INF/jsp/modificaEvento.jsp").forward(request, response);
            return;
        }
        int capacita = Integer.parseInt(request.getParameter("capacita"));
        Long categoriaId = Long.parseLong(request.getParameter("categoriaId"));

        Evento evento = eventoDao.findById(eventoId);
        Categoria categoria = categoriaDao.findById(categoriaId);

        evento.setNome(nome);
        evento.setDescrizione(descrizione);
        evento.setLuogo(luogo);
        evento.setData(data);
        evento.setOra(ora);
        evento.setCapacita(capacita);
        evento.setCategoria(categoria);

        eventoDao.update(evento);

        // Aggiorna la lista degli eventi nella sessione
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Recupera l'organizzatore dalla sessione
            model.Utente organizzatore = (model.Utente) session.getAttribute("user");

            // Ricarica la lista degli eventi aggiornata
            List<Evento> listaEventi = eventoDao.getEventiByOrganizzatore(organizzatore);

            // Aggiungi i partecipanti a ciascun evento
            dao.model.IscrizioneDao iscrizioneDao = DaoFactory.getDaoFactory().getIscrizioneDao();
            for (Evento ev : listaEventi) {
                List<model.Utente> iscritti = iscrizioneDao.getPartecipantiEvento(ev.getId());
                ev.setPartecipanti(iscritti);
            }

            // Aggiorna la lista nella sessione e nella request
            session.setAttribute("listaEventi", listaEventi);
            request.setAttribute("listaEventi", listaEventi);

            // Carica la mappa dei partecipanti per ogni evento
            java.util.Map<Long, java.util.List<model.Utente>> mappaPartecipanti = new java.util.HashMap<>();
            for (Evento ev : listaEventi) {
                java.util.List<model.Utente> iscritti = iscrizioneDao.getPartecipantiEvento(ev.getId());
                mappaPartecipanti.put(ev.getId(), iscritti);
            }
            session.setAttribute("mappaPartecipanti", mappaPartecipanti);
            request.setAttribute("mappaPartecipanti", mappaPartecipanti);
        }

        request.getRequestDispatcher("/WEB-INF/jsp/dashboardAdmin.jsp").forward(request, response);
    }
}
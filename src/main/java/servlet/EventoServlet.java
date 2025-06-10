package servlet;

import dao.DaoFactory;
import dao.model.EventoDao;
import dao.model.IscrizioneDao;

import java.util.Map;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Categoria;
import model.Evento;
import model.Utente;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/creaEvento")
public class EventoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private EventoDao eventoDao;

    @Override
    public void init() throws ServletException {
        eventoDao = DaoFactory.getDaoFactory().getEventoDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Categoria> categorie = DaoFactory.getDaoFactory().getCategoriaDao().findAll();
        request.setAttribute("categorie", categorie);
        request.getRequestDispatcher("/WEB-INF/jsp/creaEvento.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String data = request.getParameter("data");
        String ora = request.getParameter("ora");
        String luogo = request.getParameter("luogo");
        int capacita = Integer.parseInt(request.getParameter("capacita"));
        String descrizione = request.getParameter("descrizione");

        Long categoriaId = null;
        Categoria categoria = null;
        try {
            categoriaId = Long.parseLong(request.getParameter("categoria"));
            categoria = DaoFactory.getDaoFactory().getCategoriaDao().findById(categoriaId);
        } catch (NumberFormatException e) {

        }

        HttpSession session = request.getSession(false);
        Utente organizzatore = (Utente) session.getAttribute("user");

        LocalDate dataEvento = LocalDate.parse(data);
        LocalTime oraEvento = LocalTime.parse(ora);

        // Verifica che la data e ora dell'evento siano almeno 24 ore dopo il momento attuale
        LocalDate oggi = LocalDate.now();
        LocalTime oraAttuale = LocalTime.now();
        LocalDate dataOraLimite = oggi.plusDays(1);

        if (dataEvento.isBefore(dataOraLimite) || (dataEvento.isEqual(dataOraLimite) && oraEvento.isBefore(oraAttuale))) {
            request.setAttribute("msg", "La data e ora dell'evento devono essere almeno 24 ore dopo l'orario attuale.");
            List<Categoria> categorie = DaoFactory.getDaoFactory().getCategoriaDao().findAll();
            request.setAttribute("categorie", categorie);
            request.getRequestDispatcher("/WEB-INF/jsp/creaEvento.jsp").forward(request, response);
            return;
        }

        Evento evento = new Evento();
        evento.setNome(nome);
        evento.setData(dataEvento);
        evento.setOra(oraEvento);
        evento.setLuogo(luogo);
        evento.setDescrizione(descrizione);
        evento.setCapacita(capacita);
        evento.setOrganizzatore(organizzatore);
        evento.setCategoria(categoria);

        boolean created = eventoDao.create(evento);

        if (created) {
            List<Evento> listaEventi = DaoFactory.getDaoFactory().getEventoDao().getEventiByOrganizzatore(organizzatore);
            IscrizioneDao iscrizioneDao = DaoFactory.getDaoFactory().getIscrizioneDao();
            for (Evento ev : listaEventi) {
                List<Utente> iscritti = iscrizioneDao.getPartecipantiEvento(ev.getId());
                ev.setPartecipanti(iscritti);
            }
            request.setAttribute("listaEventi", listaEventi);
            session.setAttribute("listaEventi", listaEventi);

            Map<Long, List<Utente>> mappaPartecipanti = new HashMap<>();
            for (Evento ev : listaEventi) {
                List<Utente> iscritti = iscrizioneDao.getPartecipantiEvento(ev.getId());
                mappaPartecipanti.put(ev.getId(), iscritti);
            }
            request.setAttribute("mappaPartecipanti", mappaPartecipanti);
            session.setAttribute("mappaPartecipanti", mappaPartecipanti);

            request.getRequestDispatcher("/WEB-INF/jsp/dashboardAdmin.jsp").forward(request, response);
        } else {
            request.setAttribute("msg", "Errore nella creazione dell'evento");
            request.getRequestDispatcher("/WEB-INF/jsp/creaEvento.jsp").forward(request, response);
        }
    }
}
package servlet;

import dao.DaoFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Utente;
import utils.EmailUtil;
import utils.TokenUtil;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/jsp/registrazione.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confermaPassword = request.getParameter("confermaPassword");

        // Validazione dei dati
        if (username == null || email == null || password == null ||
                username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("msg", "Tutti i campi sono obbligatori");
            request.getRequestDispatcher("WEB-INF/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        // Verifica che le password coincidano
        if (confermaPassword != null && !password.equals(confermaPassword)) {
            request.setAttribute("msg", "Le password non coincidono");
            request.getRequestDispatcher("WEB-INF/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        Utente nuovoUtente = new Utente();
        nuovoUtente.setUsername(username);
        nuovoUtente.setEmail(email);
        nuovoUtente.setPassword(password);

        // Imposta il ruolo in base alla presenza di altri utenti nel DB
        if (DaoFactory.getDaoFactory().getUtenteDao().countUtenti() == 0) {
            nuovoUtente.setRuolo("admin");
        } else {
            nuovoUtente.setRuolo("utente");
        }

        // Configurazione per la conferma della registrazione
        nuovoUtente.setAttivato(false);
        String token = TokenUtil.generaToken();
        nuovoUtente.setTokenConferma(token);
        nuovoUtente.setDataCreazioneToken(LocalDateTime.now());

        boolean isRegistrato = DaoFactory.getDaoFactory().getUtenteDao().register(nuovoUtente);

        if (isRegistrato) {
            try {
                // Preparo il link di conferma per l'email
                String appUrl = request.getScheme() + "://" + request.getServerName();
                if (request.getServerPort() != 80 && request.getServerPort() != 443) {
                    appUrl += ":" + request.getServerPort();
                }
                appUrl += request.getContextPath();
                String linkConferma = appUrl + "/conferma?token=" + token;

                // Invio l'email di conferma
                boolean emailInviata = EmailUtil.inviaEmailConferma(email, username, linkConferma);

                if (emailInviata) {
                    request.setAttribute("msg", "Registrazione completata! Ti abbiamo inviato un'email di conferma. Controlla la tua casella di posta.");
                } else {
                    request.setAttribute("msg", "Registrazione completata! Non è stato possibile inviare l'email di conferma. Contatta l'assistenza.");
                }
            } catch (Exception e) {
                System.err.println("Errore nell'invio dell'email: " + e.getMessage());
                request.setAttribute("msg", "Registrazione completata, ma si è verificato un errore nell'invio dell'email di conferma.");
            }

            request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
        } else {
            request.setAttribute("msg", "Errore nella registrazione. Email o username già esistenti?");
            request.getRequestDispatcher("WEB-INF/jsp/registrazione.jsp").forward(request, response);
        }
    }
}
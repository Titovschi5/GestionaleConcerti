package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Evento;
import service.GoogleCalendarService;
import dao.jpa.JpaEventoDao;

import java.io.IOException;
import java.io.PrintWriter;
import com.google.api.client.auth.oauth2.TokenResponseException;

@WebServlet("/addToGoogleCalendar")
public class GoogleCalendarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String idEventoStr = request.getParameter("id");

        if (idEventoStr == null || idEventoStr.isEmpty()) {
            out.write("{\"success\": false, \"message\": \"ID evento mancante\"}");
            return;
        }

        try {
            Long idEvento = Long.parseLong(idEventoStr);
            JpaEventoDao eventoDao = JpaEventoDao.getInstance();
            Evento evento = eventoDao.findById(idEvento);

            if (evento == null) {
                out.write("{\"success\": false, \"message\": \"Evento non trovato\"}");
                return;
            }

            // First clear tokens if previous authentication failed
            if (request.getParameter("retry") != null) {
                GoogleCalendarService.clearTokens();
            }

            String calendarLink = GoogleCalendarService.addEventToGoogleCalendar(evento);

            // Escape quotes in JSON
            calendarLink = calendarLink.replace("\"", "\\\"");

            // Return JSON response with success and calendar link
            out.write("{\"success\": true, \"calendarLink\": \"" + calendarLink + "\"}");

        } catch (NumberFormatException e) {
            out.write("{\"success\": false, \"message\": \"ID evento non valido\"}");
            e.printStackTrace();
        } catch (TokenResponseException e) {
            // Handle token authorization errors specifically
            System.err.println("OAuth2 token error: " + e.getMessage());

            // Clear tokens to force new authentication on next attempt
            GoogleCalendarService.clearTokens();

            out.write("{\"success\": false, \"message\": \"Errore di autenticazione con Google. Riprova.\", \"authError\": true}");
            e.printStackTrace();
        } catch (java.net.BindException | java.nio.channels.AlreadyBoundException e) {
            // Handle "Address already in use" error
            System.err.println("Port binding error: " + e.getMessage());
            out.write("{\"success\": false, \"message\": \"Errore: porta in uso. Attendere qualche minuto e riprovare.\"}");
            e.printStackTrace();
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg == null) {
                errorMsg = e.getClass().getName();
            } else {
                // Escape quotes for JSON
                errorMsg = errorMsg.replace("\"", "\\\"");
            }

            // Check if the exception contains "Address already in use"
            if (errorMsg.contains("Address already in use") ||
                    (e.getCause() != null && e.getCause().getMessage() != null &&
                            e.getCause().getMessage().contains("Address already in use"))) {
                out.write("{\"success\": false, \"message\": \"Errore: porta in uso. Attendere qualche minuto e riprovare.\"}");
            } else {
                out.write("{\"success\": false, \"message\": \"Errore: " + errorMsg + "\"}");
            }
            e.printStackTrace();
        }
    }
}
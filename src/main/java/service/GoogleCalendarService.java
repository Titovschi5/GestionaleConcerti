package service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import model.Evento;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class GoogleCalendarService {
    private static final String APPLICATION_NAME = "Google Calendar API Java";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Calendar service;

    public GoogleCalendarService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets
        InputStream in = GoogleCalendarService.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new FileNotFoundException("Resource not found: credentials.json");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singletonList(CalendarScopes.CALENDAR))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8080)
                .setCallbackPath("/oauth2callback")
                .build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static String addEventToGoogleCalendar(Evento evento) throws IOException, GeneralSecurityException {
        GoogleCalendarService calendarService = new GoogleCalendarService();

        // Create a Google Calendar Event
        Event calendarEvent = new Event()
                .setSummary(evento.getNome())
                .setDescription(evento.getDescrizione());

        // Format date and time correctly
        LocalDate date = evento.getData();
        LocalTime time = evento.getOra();

        // Format the time properly
        String formattedTime = time.format(TIME_FORMATTER);

        // Create the full ISO datetime string
        String startDateTimeStr = date.toString() + "T" + formattedTime + "+02:00";
        DateTime startDateTime = new DateTime(startDateTimeStr);

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Europe/Rome");
        calendarEvent.setStart(start);

        // End time (1 hour after start)
        LocalTime endTime = time.plusHours(1);
        String formattedEndTime = endTime.format(TIME_FORMATTER);

        String endDateTimeStr = date.toString() + "T" + formattedEndTime + "+02:00";
        DateTime endDateTime = new DateTime(endDateTimeStr);

        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Europe/Rome");
        calendarEvent.setEnd(end);

        // Set location if available
        if (evento.getLuogo() != null && !evento.getLuogo().isEmpty()) {
            calendarEvent.setLocation(evento.getLuogo());
        }

        // Add the event to the primary calendar
        String calendarId = "primary";
        Event createdEvent = calendarService.service.events().insert(calendarId, calendarEvent).execute();

        // Return the Google Calendar event link
        return createdEvent.getHtmlLink();
    }


    public static void clearTokens() {
        try {
            java.io.File tokensDir = new java.io.File(TOKENS_DIRECTORY_PATH);
            if (tokensDir.exists() && tokensDir.isDirectory()) {
                java.io.File[] files = tokensDir.listFiles();
                if (files != null) {
                    for (java.io.File file : files) {
                        boolean deleted = file.delete();
                        System.out.println(deleted ?
                                "Token file deleted: " + file.getName() :
                                "Failed to delete token file: " + file.getName());
                    }
                }
                System.out.println("OAuth tokens have been cleared successfully");
            } else {
                System.out.println("Tokens directory not found: " + TOKENS_DIRECTORY_PATH);
            }
        } catch (Exception e) {
            System.err.println("Error while clearing OAuth tokens: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
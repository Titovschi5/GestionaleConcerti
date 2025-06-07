package utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {
    private static final String EMAIL_USERNAME = "panepastaetwitch@gmail.com";
    private static final String EMAIL_PASSWORD = "rayo nmmx smdx qpkb";

    public static boolean inviaEmailConferma(String emailDestinatario, String username, String linkConferma) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.debug", "false"); // Per il debug
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinatario));
            message.setSubject("Conferma Registrazione");

            String contenutoEmail =
                    "Ciao " + username + ",<br><br>" +
                            "Grazie per esserti registrato sul nostro sito. Per completare la registrazione, " +
                            "conferma il tuo account cliccando sul link seguente:<br><br>" +
                            "<a href='" + linkConferma + "'>Conferma la tua email</a><br><br>" +
                            "Se non sei stato tu a registrarti, ignora questa email.<br><br>" +
                            "Cordiali saluti,<br>Il Team";

            message.setContent(contenutoEmail, "text/html; charset=utf-8");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
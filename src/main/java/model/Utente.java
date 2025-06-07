package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utente")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String ruolo;

    private boolean attivato = false;

    @Column(name = "token_conferma")
    private String tokenConferma;

    @Column(name = "data_creazione_token")
    private LocalDateTime dataCreazioneToken;

    // Costruttori esistenti
    public Utente() {
    }

    public Utente(Long id, String username, String email, String password, String ruolo) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    // Aggiungi getter e setter per i nuovi campi
    public boolean isAttivato() {
        return attivato;
    }

    public void setAttivato(boolean attivato) {
        this.attivato = attivato;
    }

    public String getTokenConferma() {
        return tokenConferma;
    }

    public void setTokenConferma(String tokenConferma) {
        this.tokenConferma = tokenConferma;
    }

    public LocalDateTime getDataCreazioneToken() {
        return dataCreazioneToken;
    }

    public void setDataCreazioneToken(LocalDateTime dataCreazioneToken) {
        this.dataCreazioneToken = dataCreazioneToken;
    }

    // Getter e setter esistenti
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
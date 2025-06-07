package dao.model;

import model.Utente;

import java.util.List;

public interface UtenteDao {
    boolean register(Utente nuovoUtente);
    Utente login(String email, String password);
    int countUtenti();
    List<Utente> getAll();
    Utente findById(Long id);
    public boolean update(Utente utente);
    public boolean delete(Long id);
    boolean attivaUtente(String token);
    Utente findByToken(String token);
    boolean aggiornaToken(Utente utente, String token);

}

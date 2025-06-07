package dao.jpa;

import dao.model.IscrizioneDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Evento;
import model.Iscrizione;
import model.Utente;

import java.util.List;
import java.util.ArrayList;

public class JpaIscrizioneDao implements IscrizioneDao {

    private static final JpaIscrizioneDao instance = new JpaIscrizioneDao();

    private JpaIscrizioneDao() {

    }

    public static JpaIscrizioneDao getInstance() {
        return instance;
    }

    @Override
    public boolean createIscrizione(Evento evento, Utente utente) {
        EntityManager em = JpaDaoFactory.getManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            // Ricarica le entità dal database per assicurarsi che siano managed
            Evento managedEvento = em.find(Evento.class, evento.getId());
            Utente managedUtente = em.find(Utente.class, utente.getId());

            if (managedEvento == null || managedUtente == null) {
                throw new Exception("Evento o utente non trovati nel database");
            }

            Iscrizione iscrizione = new Iscrizione(managedUtente, managedEvento);
            em.persist(iscrizione);

            tx.commit();
            return true;
        } catch (Exception e) {
            System.err.println("Errore in createIscrizione: " + e.getMessage());
            e.printStackTrace();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> getEventiIscritti(Utente utente) {
        EntityManager em = JpaDaoFactory.getManager();
        try {
            TypedQuery<Evento> query = em.createQuery(
                    "SELECT i.evento FROM Iscrizione i WHERE i.utente = :utente", Evento.class);
            query.setParameter("utente", utente);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Errore in getEventiIscritti: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean isGiaIscritto(Evento evento, Utente utente) {
        EntityManager em = JpaDaoFactory.getManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(i) FROM Iscrizione i WHERE i.evento = :evento AND i.utente = :utente", Long.class);
            query.setParameter("evento", evento);
            query.setParameter("utente", utente);
            long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            System.err.println("Errore in isGiaIscritto: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean deleteIscrizione(Evento evento, Utente utente) {
        EntityManager em = JpaDaoFactory.getManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            int deletedCount = em.createQuery(
                            "DELETE FROM Iscrizione i WHERE i.evento = :evento AND i.utente = :utente")
                    .setParameter("evento", evento)
                    .setParameter("utente", utente)
                    .executeUpdate();

            tx.commit();
            return deletedCount > 0;
        } catch (Exception e) {
            System.err.println("Errore in deleteIscrizione: " + e.getMessage());
            e.printStackTrace();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    public long countIscrittiPerEvento(Evento evento) {
        EntityManager em = JpaDaoFactory.getManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(i) FROM Iscrizione i WHERE i.evento = :evento", Long.class);
            query.setParameter("evento", evento);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Utente> getPartecipantiEvento(Long eventoId) {
        EntityManager em = JpaDaoFactory.getManager();
        try {
            TypedQuery<Utente> query = em.createQuery(
                    "SELECT i.utente FROM Iscrizione i WHERE i.evento.id = :eventoId", Utente.class);
            query.setParameter("eventoId", eventoId);
            return query.getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void rimuoviIscrizioniPerEvento(Long eventoId) {
        EntityManager em = JpaDaoFactory.getManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.createQuery("DELETE FROM Iscrizione i WHERE i.evento.id = :eventoId")
                .setParameter("eventoId", eventoId)
                .executeUpdate();
        tx.commit();
    }

}
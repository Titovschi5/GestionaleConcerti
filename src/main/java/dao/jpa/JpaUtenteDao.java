package dao.jpa;

import dao.model.UtenteDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import model.Utente;

import java.util.List;

public class JpaUtenteDao implements UtenteDao {

    private static JpaUtenteDao instance = new JpaUtenteDao();

    private JpaUtenteDao() {
    }

    public static JpaUtenteDao getInstance() {
        return instance;
    }

    @Override
    public boolean register(Utente nuovoUtente) {
        EntityManager em = JpaDaoFactory.getManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(nuovoUtente);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(Utente utente) {
        EntityManager em = JpaDaoFactory.getManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(utente);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Utente login(String email, String password) {
        EntityManager em = JpaDaoFactory.getManager();
        try {
            Query query = em.createQuery("SELECT u FROM Utente u WHERE u.email = :email AND u.password = :password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            return (Utente) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public int countUtenti() {
        EntityManager em = JpaDaoFactory.getManager();
        try {
            Query query = em.createQuery("SELECT COUNT(u) FROM Utente u");
            Long count = (Long) query.getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Utente> getAll() {
        EntityManager em = JpaDaoFactory.getManager();
        try {
            return em.createQuery("SELECT u FROM Utente u", Utente.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Utente findById(Long id) {
        EntityManager em = JpaDaoFactory.getManager();
        try {
            return em.find(Utente.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Long id) {
        EntityManager em = JpaDaoFactory.getManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Utente utente = em.find(Utente.class, id);
            if (utente != null) {
                // Elimina tutte le iscrizioni legate all'utente
                Query deleteIscrizioni = em.createQuery("DELETE FROM Iscrizione i WHERE i.utente.id = :utenteId");
                deleteIscrizioni.setParameter("utenteId", id);
                deleteIscrizioni.executeUpdate();

                // Ora elimina l'utente
                em.remove(utente);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}
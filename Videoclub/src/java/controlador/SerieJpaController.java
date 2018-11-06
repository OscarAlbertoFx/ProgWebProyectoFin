
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.PreexistingEntityException;
import controlador.exceptions.RollbackFailureException;
import entidad.Serie;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class SerieJpaController implements Serializable {

    public SerieJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Serie serie) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            em.persist(serie);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSerie(serie.getIdSerie()) != null) {
                throw new PreexistingEntityException("Serie " + serie + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Serie serie) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            serie = em.merge(serie);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = serie.getIdSerie();
                if (findSerie(id) == null) {
                    throw new NonexistentEntityException("The serie with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Serie serie;
            try {
                serie = em.getReference(Serie.class, id);
                serie.getIdSerie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The serie with id " + id + " no longer exists.", enfe);
            }
            em.remove(serie);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Serie> findSerieEntities() {
        return findSerieEntities(true, -1, -1);
    }

    public List<Serie> findSerieEntities(int maxResults, int firstResult) {
        return findSerieEntities(false, maxResults, firstResult);
    }

    private List<Serie> findSerieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Serie.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Serie findSerie(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Serie.class, id);
        } finally {
            em.close();
        }
    }

    public int getSerieCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Serie> rt = cq.from(Serie.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

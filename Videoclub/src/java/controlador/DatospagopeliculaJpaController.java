
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Comprapelicula;
import entidad.Datospagopelicula;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.UserTransaction;

public class DatospagopeliculaJpaController implements Serializable {

    public DatospagopeliculaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Datospagopelicula datospagopelicula) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Comprapelicula idCompraPelicula = datospagopelicula.getIdCompraPelicula();
            if (idCompraPelicula != null) {
                idCompraPelicula = em.getReference(idCompraPelicula.getClass(), idCompraPelicula.getIdCompra());
                datospagopelicula.setIdCompraPelicula(idCompraPelicula);
            }
            em.persist(datospagopelicula);
            if (idCompraPelicula != null) {
                idCompraPelicula.getDatospagopeliculaList().add(datospagopelicula);
                idCompraPelicula = em.merge(idCompraPelicula);
            }
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

    public void edit(Datospagopelicula datospagopelicula) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Datospagopelicula persistentDatospagopelicula = em.find(Datospagopelicula.class, datospagopelicula.getIdDatosPagoPelicula());
            Comprapelicula idCompraPeliculaOld = persistentDatospagopelicula.getIdCompraPelicula();
            Comprapelicula idCompraPeliculaNew = datospagopelicula.getIdCompraPelicula();
            if (idCompraPeliculaNew != null) {
                idCompraPeliculaNew = em.getReference(idCompraPeliculaNew.getClass(), idCompraPeliculaNew.getIdCompra());
                datospagopelicula.setIdCompraPelicula(idCompraPeliculaNew);
            }
            datospagopelicula = em.merge(datospagopelicula);
            if (idCompraPeliculaOld != null && !idCompraPeliculaOld.equals(idCompraPeliculaNew)) {
                idCompraPeliculaOld.getDatospagopeliculaList().remove(datospagopelicula);
                idCompraPeliculaOld = em.merge(idCompraPeliculaOld);
            }
            if (idCompraPeliculaNew != null && !idCompraPeliculaNew.equals(idCompraPeliculaOld)) {
                idCompraPeliculaNew.getDatospagopeliculaList().add(datospagopelicula);
                idCompraPeliculaNew = em.merge(idCompraPeliculaNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = datospagopelicula.getIdDatosPagoPelicula();
                if (findDatospagopelicula(id) == null) {
                    throw new NonexistentEntityException("The datospagopelicula with id " + id + " no longer exists.");
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
            Datospagopelicula datospagopelicula;
            try {
                datospagopelicula = em.getReference(Datospagopelicula.class, id);
                datospagopelicula.getIdDatosPagoPelicula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The datospagopelicula with id " + id + " no longer exists.", enfe);
            }
            Comprapelicula idCompraPelicula = datospagopelicula.getIdCompraPelicula();
            if (idCompraPelicula != null) {
                idCompraPelicula.getDatospagopeliculaList().remove(datospagopelicula);
                idCompraPelicula = em.merge(idCompraPelicula);
            }
            em.remove(datospagopelicula);
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

    public List<Datospagopelicula> findDatospagopeliculaEntities() {
        return findDatospagopeliculaEntities(true, -1, -1);
    }

    public List<Datospagopelicula> findDatospagopeliculaEntities(int maxResults, int firstResult) {
        return findDatospagopeliculaEntities(false, maxResults, firstResult);
    }

    private List<Datospagopelicula> findDatospagopeliculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Datospagopelicula.class));
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

    public Datospagopelicula findDatospagopelicula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Datospagopelicula.class, id);
        } finally {
            em.close();
        }
    }

    public int getDatospagopeliculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Datospagopelicula> rt = cq.from(Datospagopelicula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}


package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Compraserie;
import entidad.Datospagoserie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.UserTransaction;

public class DatospagoserieJpaController implements Serializable {

    public DatospagoserieJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Datospagoserie datospagoserie) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Compraserie idCompraSerie = datospagoserie.getIdCompraSerie();
            if (idCompraSerie != null) {
                idCompraSerie = em.getReference(idCompraSerie.getClass(), idCompraSerie.getIdCompraSerie());
                datospagoserie.setIdCompraSerie(idCompraSerie);
            }
            em.persist(datospagoserie);
            if (idCompraSerie != null) {
                idCompraSerie.getDatospagoserieList().add(datospagoserie);
                idCompraSerie = em.merge(idCompraSerie);
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

    public void edit(Datospagoserie datospagoserie) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Datospagoserie persistentDatospagoserie = em.find(Datospagoserie.class, datospagoserie.getIdDatosPagoSerie());
            Compraserie idCompraSerieOld = persistentDatospagoserie.getIdCompraSerie();
            Compraserie idCompraSerieNew = datospagoserie.getIdCompraSerie();
            if (idCompraSerieNew != null) {
                idCompraSerieNew = em.getReference(idCompraSerieNew.getClass(), idCompraSerieNew.getIdCompraSerie());
                datospagoserie.setIdCompraSerie(idCompraSerieNew);
            }
            datospagoserie = em.merge(datospagoserie);
            if (idCompraSerieOld != null && !idCompraSerieOld.equals(idCompraSerieNew)) {
                idCompraSerieOld.getDatospagoserieList().remove(datospagoserie);
                idCompraSerieOld = em.merge(idCompraSerieOld);
            }
            if (idCompraSerieNew != null && !idCompraSerieNew.equals(idCompraSerieOld)) {
                idCompraSerieNew.getDatospagoserieList().add(datospagoserie);
                idCompraSerieNew = em.merge(idCompraSerieNew);
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
                Integer id = datospagoserie.getIdDatosPagoSerie();
                if (findDatospagoserie(id) == null) {
                    throw new NonexistentEntityException("The datospagoserie with id " + id + " no longer exists.");
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
            Datospagoserie datospagoserie;
            try {
                datospagoserie = em.getReference(Datospagoserie.class, id);
                datospagoserie.getIdDatosPagoSerie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The datospagoserie with id " + id + " no longer exists.", enfe);
            }
            Compraserie idCompraSerie = datospagoserie.getIdCompraSerie();
            if (idCompraSerie != null) {
                idCompraSerie.getDatospagoserieList().remove(datospagoserie);
                idCompraSerie = em.merge(idCompraSerie);
            }
            em.remove(datospagoserie);
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

    public List<Datospagoserie> findDatospagoserieEntities() {
        return findDatospagoserieEntities(true, -1, -1);
    }

    public List<Datospagoserie> findDatospagoserieEntities(int maxResults, int firstResult) {
        return findDatospagoserieEntities(false, maxResults, firstResult);
    }

    private List<Datospagoserie> findDatospagoserieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Datospagoserie.class));
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

    public Datospagoserie findDatospagoserie(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Datospagoserie.class, id);
        } finally {
            em.close();
        }
    }

    public int getDatospagoserieCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Datospagoserie> rt = cq.from(Datospagoserie.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

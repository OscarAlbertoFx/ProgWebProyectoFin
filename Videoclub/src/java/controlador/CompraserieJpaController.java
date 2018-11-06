/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import entidad.Compraserie;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Usuario;
import entidad.Detallecompraserie;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.UserTransaction;

/**
 *
 * @author aaron
 */
public class CompraserieJpaController implements Serializable {

    public CompraserieJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compraserie compraserie) throws RollbackFailureException, Exception {
        if (compraserie.getDetallecompraserieList() == null) {
            compraserie.setDetallecompraserieList(new ArrayList<Detallecompraserie>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Usuario idUsuario = compraserie.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                compraserie.setIdUsuario(idUsuario);
            }
            List<Detallecompraserie> attachedDetallecompraserieList = new ArrayList<Detallecompraserie>();
            for (Detallecompraserie detallecompraserieListDetallecompraserieToAttach : compraserie.getDetallecompraserieList()) {
                detallecompraserieListDetallecompraserieToAttach = em.getReference(detallecompraserieListDetallecompraserieToAttach.getClass(), detallecompraserieListDetallecompraserieToAttach.getIdDetalleCompraSerie());
                attachedDetallecompraserieList.add(detallecompraserieListDetallecompraserieToAttach);
            }
            compraserie.setDetallecompraserieList(attachedDetallecompraserieList);
            em.persist(compraserie);
            if (idUsuario != null) {
                idUsuario.getCompraserieList().add(compraserie);
                idUsuario = em.merge(idUsuario);
            }
            for (Detallecompraserie detallecompraserieListDetallecompraserie : compraserie.getDetallecompraserieList()) {
                Compraserie oldIdCompraSerieOfDetallecompraserieListDetallecompraserie = detallecompraserieListDetallecompraserie.getIdCompraSerie();
                detallecompraserieListDetallecompraserie.setIdCompraSerie(compraserie);
                detallecompraserieListDetallecompraserie = em.merge(detallecompraserieListDetallecompraserie);
                if (oldIdCompraSerieOfDetallecompraserieListDetallecompraserie != null) {
                    oldIdCompraSerieOfDetallecompraserieListDetallecompraserie.getDetallecompraserieList().remove(detallecompraserieListDetallecompraserie);
                    oldIdCompraSerieOfDetallecompraserieListDetallecompraserie = em.merge(oldIdCompraSerieOfDetallecompraserieListDetallecompraserie);
                }
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

    public void edit(Compraserie compraserie) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();            
            Compraserie persistentCompraserie = em.find(Compraserie.class, compraserie.getIdCompraSerie());
            Usuario idUsuarioOld = persistentCompraserie.getIdUsuario();
            Usuario idUsuarioNew = compraserie.getIdUsuario();
            List<Detallecompraserie> detallecompraserieListOld = persistentCompraserie.getDetallecompraserieList();
            List<Detallecompraserie> detallecompraserieListNew = compraserie.getDetallecompraserieList();
            List<String> illegalOrphanMessages = null;
            for (Detallecompraserie detallecompraserieListOldDetallecompraserie : detallecompraserieListOld) {
                if (!detallecompraserieListNew.contains(detallecompraserieListOldDetallecompraserie)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallecompraserie " + detallecompraserieListOldDetallecompraserie + " since its idCompraSerie field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                compraserie.setIdUsuario(idUsuarioNew);
            }
            List<Detallecompraserie> attachedDetallecompraserieListNew = new ArrayList<Detallecompraserie>();
            for (Detallecompraserie detallecompraserieListNewDetallecompraserieToAttach : detallecompraserieListNew) {
                detallecompraserieListNewDetallecompraserieToAttach = em.getReference(detallecompraserieListNewDetallecompraserieToAttach.getClass(), detallecompraserieListNewDetallecompraserieToAttach.getIdDetalleCompraSerie());
                attachedDetallecompraserieListNew.add(detallecompraserieListNewDetallecompraserieToAttach);
            }
            detallecompraserieListNew = attachedDetallecompraserieListNew;
            compraserie.setDetallecompraserieList(detallecompraserieListNew);
            compraserie = em.merge(compraserie);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getCompraserieList().remove(compraserie);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getCompraserieList().add(compraserie);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (Detallecompraserie detallecompraserieListNewDetallecompraserie : detallecompraserieListNew) {
                if (!detallecompraserieListOld.contains(detallecompraserieListNewDetallecompraserie)) {
                    Compraserie oldIdCompraSerieOfDetallecompraserieListNewDetallecompraserie = detallecompraserieListNewDetallecompraserie.getIdCompraSerie();
                    detallecompraserieListNewDetallecompraserie.setIdCompraSerie(compraserie);
                    detallecompraserieListNewDetallecompraserie = em.merge(detallecompraserieListNewDetallecompraserie);
                    if (oldIdCompraSerieOfDetallecompraserieListNewDetallecompraserie != null && !oldIdCompraSerieOfDetallecompraserieListNewDetallecompraserie.equals(compraserie)) {
                        oldIdCompraSerieOfDetallecompraserieListNewDetallecompraserie.getDetallecompraserieList().remove(detallecompraserieListNewDetallecompraserie);
                        oldIdCompraSerieOfDetallecompraserieListNewDetallecompraserie = em.merge(oldIdCompraSerieOfDetallecompraserieListNewDetallecompraserie);
                    }
                }
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
                Integer id = compraserie.getIdCompraSerie();
                if (findCompraserie(id) == null) {
                    throw new NonexistentEntityException("The compraserie with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Compraserie compraserie;
            try {
                compraserie = em.getReference(Compraserie.class, id);
                compraserie.getIdCompraSerie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compraserie with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallecompraserie> detallecompraserieListOrphanCheck = compraserie.getDetallecompraserieList();
            for (Detallecompraserie detallecompraserieListOrphanCheckDetallecompraserie : detallecompraserieListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Compraserie (" + compraserie + ") cannot be destroyed since the Detallecompraserie " + detallecompraserieListOrphanCheckDetallecompraserie + " in its detallecompraserieList field has a non-nullable idCompraSerie field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario idUsuario = compraserie.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getCompraserieList().remove(compraserie);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(compraserie);
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

    public List<Compraserie> findCompraserieEntities() {
        return findCompraserieEntities(true, -1, -1);
    }

    public List<Compraserie> findCompraserieEntities(int maxResults, int firstResult) {
        return findCompraserieEntities(false, maxResults, firstResult);
    }

    private List<Compraserie> findCompraserieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compraserie.class));
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

    public Compraserie findCompraserie(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compraserie.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompraserieCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compraserie> rt = cq.from(Compraserie.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

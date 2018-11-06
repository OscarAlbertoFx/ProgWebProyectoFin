/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import entidad.Comprapelicula;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Detallecomprapelicula;
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
public class ComprapeliculaJpaController implements Serializable {

    public ComprapeliculaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comprapelicula comprapelicula) throws RollbackFailureException, Exception {
        if (comprapelicula.getDetallecomprapeliculaList() == null) {
            comprapelicula.setDetallecomprapeliculaList(new ArrayList<Detallecomprapelicula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            List<Detallecomprapelicula> attachedDetallecomprapeliculaList = new ArrayList<Detallecomprapelicula>();
            for (Detallecomprapelicula detallecomprapeliculaListDetallecomprapeliculaToAttach : comprapelicula.getDetallecomprapeliculaList()) {
                detallecomprapeliculaListDetallecomprapeliculaToAttach = em.getReference(detallecomprapeliculaListDetallecomprapeliculaToAttach.getClass(), detallecomprapeliculaListDetallecomprapeliculaToAttach.getIdDetalleCompra());
                attachedDetallecomprapeliculaList.add(detallecomprapeliculaListDetallecomprapeliculaToAttach);
            }
            comprapelicula.setDetallecomprapeliculaList(attachedDetallecomprapeliculaList);
            em.persist(comprapelicula);
            for (Detallecomprapelicula detallecomprapeliculaListDetallecomprapelicula : comprapelicula.getDetallecomprapeliculaList()) {
                Comprapelicula oldIdCompraOfDetallecomprapeliculaListDetallecomprapelicula = detallecomprapeliculaListDetallecomprapelicula.getIdCompra();
                detallecomprapeliculaListDetallecomprapelicula.setIdCompra(comprapelicula);
                detallecomprapeliculaListDetallecomprapelicula = em.merge(detallecomprapeliculaListDetallecomprapelicula);
                if (oldIdCompraOfDetallecomprapeliculaListDetallecomprapelicula != null) {
                    oldIdCompraOfDetallecomprapeliculaListDetallecomprapelicula.getDetallecomprapeliculaList().remove(detallecomprapeliculaListDetallecomprapelicula);
                    oldIdCompraOfDetallecomprapeliculaListDetallecomprapelicula = em.merge(oldIdCompraOfDetallecomprapeliculaListDetallecomprapelicula);
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

    public void edit(Comprapelicula comprapelicula) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Comprapelicula persistentComprapelicula = em.find(Comprapelicula.class, comprapelicula.getIdCompra());
            List<Detallecomprapelicula> detallecomprapeliculaListOld = persistentComprapelicula.getDetallecomprapeliculaList();
            List<Detallecomprapelicula> detallecomprapeliculaListNew = comprapelicula.getDetallecomprapeliculaList();
            List<String> illegalOrphanMessages = null;
            for (Detallecomprapelicula detallecomprapeliculaListOldDetallecomprapelicula : detallecomprapeliculaListOld) {
                if (!detallecomprapeliculaListNew.contains(detallecomprapeliculaListOldDetallecomprapelicula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallecomprapelicula " + detallecomprapeliculaListOldDetallecomprapelicula + " since its idCompra field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Detallecomprapelicula> attachedDetallecomprapeliculaListNew = new ArrayList<Detallecomprapelicula>();
            for (Detallecomprapelicula detallecomprapeliculaListNewDetallecomprapeliculaToAttach : detallecomprapeliculaListNew) {
                detallecomprapeliculaListNewDetallecomprapeliculaToAttach = em.getReference(detallecomprapeliculaListNewDetallecomprapeliculaToAttach.getClass(), detallecomprapeliculaListNewDetallecomprapeliculaToAttach.getIdDetalleCompra());
                attachedDetallecomprapeliculaListNew.add(detallecomprapeliculaListNewDetallecomprapeliculaToAttach);
            }
            detallecomprapeliculaListNew = attachedDetallecomprapeliculaListNew;
            comprapelicula.setDetallecomprapeliculaList(detallecomprapeliculaListNew);
            comprapelicula = em.merge(comprapelicula);
            for (Detallecomprapelicula detallecomprapeliculaListNewDetallecomprapelicula : detallecomprapeliculaListNew) {
                if (!detallecomprapeliculaListOld.contains(detallecomprapeliculaListNewDetallecomprapelicula)) {
                    Comprapelicula oldIdCompraOfDetallecomprapeliculaListNewDetallecomprapelicula = detallecomprapeliculaListNewDetallecomprapelicula.getIdCompra();
                    detallecomprapeliculaListNewDetallecomprapelicula.setIdCompra(comprapelicula);
                    detallecomprapeliculaListNewDetallecomprapelicula = em.merge(detallecomprapeliculaListNewDetallecomprapelicula);
                    if (oldIdCompraOfDetallecomprapeliculaListNewDetallecomprapelicula != null && !oldIdCompraOfDetallecomprapeliculaListNewDetallecomprapelicula.equals(comprapelicula)) {
                        oldIdCompraOfDetallecomprapeliculaListNewDetallecomprapelicula.getDetallecomprapeliculaList().remove(detallecomprapeliculaListNewDetallecomprapelicula);
                        oldIdCompraOfDetallecomprapeliculaListNewDetallecomprapelicula = em.merge(oldIdCompraOfDetallecomprapeliculaListNewDetallecomprapelicula);
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
                Integer id = comprapelicula.getIdCompra();
                if (findComprapelicula(id) == null) {
                    throw new NonexistentEntityException("The comprapelicula with id " + id + " no longer exists.");
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
            Comprapelicula comprapelicula;
            try {
                comprapelicula = em.getReference(Comprapelicula.class, id);
                comprapelicula.getIdCompra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comprapelicula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallecomprapelicula> detallecomprapeliculaListOrphanCheck = comprapelicula.getDetallecomprapeliculaList();
            for (Detallecomprapelicula detallecomprapeliculaListOrphanCheckDetallecomprapelicula : detallecomprapeliculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comprapelicula (" + comprapelicula + ") cannot be destroyed since the Detallecomprapelicula " + detallecomprapeliculaListOrphanCheckDetallecomprapelicula + " in its detallecomprapeliculaList field has a non-nullable idCompra field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(comprapelicula);
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

    public List<Comprapelicula> findComprapeliculaEntities() {
        return findComprapeliculaEntities(true, -1, -1);
    }

    public List<Comprapelicula> findComprapeliculaEntities(int maxResults, int firstResult) {
        return findComprapeliculaEntities(false, maxResults, firstResult);
    }

    private List<Comprapelicula> findComprapeliculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comprapelicula.class));
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

    public Comprapelicula findComprapelicula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comprapelicula.class, id);
        } finally {
            em.close();
        }
    }

    public int getComprapeliculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comprapelicula> rt = cq.from(Comprapelicula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

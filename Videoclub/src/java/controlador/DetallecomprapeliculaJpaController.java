/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Comprapelicula;
import entidad.Detallecomprapelicula;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.UserTransaction;

/**
 *
 * @author aaron
 */
public class DetallecomprapeliculaJpaController implements Serializable {

    public DetallecomprapeliculaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallecomprapelicula detallecomprapelicula) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Comprapelicula idCompra = detallecomprapelicula.getIdCompra();
            if (idCompra != null) {
                idCompra = em.getReference(idCompra.getClass(), idCompra.getIdCompra());
                detallecomprapelicula.setIdCompra(idCompra);
            }
            em.persist(detallecomprapelicula);
            if (idCompra != null) {
                idCompra.getDetallecomprapeliculaList().add(detallecomprapelicula);
                idCompra = em.merge(idCompra);
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

    public void edit(Detallecomprapelicula detallecomprapelicula) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallecomprapelicula persistentDetallecomprapelicula = em.find(Detallecomprapelicula.class, detallecomprapelicula.getIdDetalleCompra());
            Comprapelicula idCompraOld = persistentDetallecomprapelicula.getIdCompra();
            Comprapelicula idCompraNew = detallecomprapelicula.getIdCompra();
            if (idCompraNew != null) {
                idCompraNew = em.getReference(idCompraNew.getClass(), idCompraNew.getIdCompra());
                detallecomprapelicula.setIdCompra(idCompraNew);
            }
            detallecomprapelicula = em.merge(detallecomprapelicula);
            if (idCompraOld != null && !idCompraOld.equals(idCompraNew)) {
                idCompraOld.getDetallecomprapeliculaList().remove(detallecomprapelicula);
                idCompraOld = em.merge(idCompraOld);
            }
            if (idCompraNew != null && !idCompraNew.equals(idCompraOld)) {
                idCompraNew.getDetallecomprapeliculaList().add(detallecomprapelicula);
                idCompraNew = em.merge(idCompraNew);
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
                Integer id = detallecomprapelicula.getIdDetalleCompra();
                if (findDetallecomprapelicula(id) == null) {
                    throw new NonexistentEntityException("The detallecomprapelicula with id " + id + " no longer exists.");
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
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Detallecomprapelicula detallecomprapelicula;
            try {
                detallecomprapelicula = em.getReference(Detallecomprapelicula.class, id);
                detallecomprapelicula.getIdDetalleCompra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallecomprapelicula with id " + id + " no longer exists.", enfe);
            }
            Comprapelicula idCompra = detallecomprapelicula.getIdCompra();
            if (idCompra != null) {
                idCompra.getDetallecomprapeliculaList().remove(detallecomprapelicula);
                idCompra = em.merge(idCompra);
            }
            em.remove(detallecomprapelicula);
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

    public List<Detallecomprapelicula> findDetallecomprapeliculaEntities() {
        return findDetallecomprapeliculaEntities(true, -1, -1);
    }

    public List<Detallecomprapelicula> findDetallecomprapeliculaEntities(int maxResults, int firstResult) {
        return findDetallecomprapeliculaEntities(false, maxResults, firstResult);
    }

    private List<Detallecomprapelicula> findDetallecomprapeliculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallecomprapelicula.class));
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

    public Detallecomprapelicula findDetallecomprapelicula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallecomprapelicula.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallecomprapeliculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallecomprapelicula> rt = cq.from(Detallecomprapelicula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Detallecomprapelicula> obtenerCompra(Comprapelicula idCompra) {
        List<Detallecomprapelicula> compras;
        EntityManager em = getEntityManager();
        System.out.println("Buscando deatlles de compra por idCOmpra : " + idCompra);
        Query consulta = em.createNamedQuery("Detallecomprapelicula.findAll");
        compras = consulta.getResultList();
        for (int i = 0; i < compras.size(); i++) {
            System.out.println(compras.get(i));
        }
        for (int i = 0; i < compras.size(); i++) {
            if (!compras.get(i).getIdCompra().equals(idCompra)) {
                System.out.println("Borre " + compras.get(i));
                compras.remove(i);
                i--;
            }
        }
        return compras;
    }

}

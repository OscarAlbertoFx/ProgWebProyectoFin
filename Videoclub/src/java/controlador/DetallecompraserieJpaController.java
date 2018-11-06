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
import entidad.Compraserie;
import entidad.Detallecompraserie;
import entidad.Serie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *
 * @author aaron
 */
public class DetallecompraserieJpaController implements Serializable {

    public DetallecompraserieJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallecompraserie detallecompraserie) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Compraserie idCompraSerie = detallecompraserie.getIdCompraSerie();
            if (idCompraSerie != null) {
                idCompraSerie = em.getReference(idCompraSerie.getClass(), idCompraSerie.getIdCompraSerie());
                detallecompraserie.setIdCompraSerie(idCompraSerie);
            }
            Serie idSerie = detallecompraserie.getIdSerie();
            if (idSerie != null) {
                idSerie = em.getReference(idSerie.getClass(), idSerie.getIdSerie());
                detallecompraserie.setIdSerie(idSerie);
            }
            em.persist(detallecompraserie);
            if (idCompraSerie != null) {
                idCompraSerie.getDetallecompraserieList().add(detallecompraserie);
                idCompraSerie = em.merge(idCompraSerie);
            }
            if (idSerie != null) {
                idSerie.getDetallecompraserieList().add(detallecompraserie);
                idSerie = em.merge(idSerie);
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

    public void edit(Detallecompraserie detallecompraserie) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Detallecompraserie persistentDetallecompraserie = em.find(Detallecompraserie.class, detallecompraserie.getIdDetalleCompraSerie());
            Compraserie idCompraSerieOld = persistentDetallecompraserie.getIdCompraSerie();
            Compraserie idCompraSerieNew = detallecompraserie.getIdCompraSerie();
            Serie idSerieOld = persistentDetallecompraserie.getIdSerie();
            Serie idSerieNew = detallecompraserie.getIdSerie();
            if (idCompraSerieNew != null) {
                idCompraSerieNew = em.getReference(idCompraSerieNew.getClass(), idCompraSerieNew.getIdCompraSerie());
                detallecompraserie.setIdCompraSerie(idCompraSerieNew);
            }
            if (idSerieNew != null) {
                idSerieNew = em.getReference(idSerieNew.getClass(), idSerieNew.getIdSerie());
                detallecompraserie.setIdSerie(idSerieNew);
            }
            detallecompraserie = em.merge(detallecompraserie);
            if (idCompraSerieOld != null && !idCompraSerieOld.equals(idCompraSerieNew)) {
                idCompraSerieOld.getDetallecompraserieList().remove(detallecompraserie);
                idCompraSerieOld = em.merge(idCompraSerieOld);
            }
            if (idCompraSerieNew != null && !idCompraSerieNew.equals(idCompraSerieOld)) {
                idCompraSerieNew.getDetallecompraserieList().add(detallecompraserie);
                idCompraSerieNew = em.merge(idCompraSerieNew);
            }
            if (idSerieOld != null && !idSerieOld.equals(idSerieNew)) {
                idSerieOld.getDetallecompraserieList().remove(detallecompraserie);
                idSerieOld = em.merge(idSerieOld);
            }
            if (idSerieNew != null && !idSerieNew.equals(idSerieOld)) {
                idSerieNew.getDetallecompraserieList().add(detallecompraserie);
                idSerieNew = em.merge(idSerieNew);
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
                Integer id = detallecompraserie.getIdDetalleCompraSerie();
                if (findDetallecompraserie(id) == null) {
                    throw new NonexistentEntityException("The detallecompraserie with id " + id + " no longer exists.");
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
            Detallecompraserie detallecompraserie;
            try {
                detallecompraserie = em.getReference(Detallecompraserie.class, id);
                detallecompraserie.getIdDetalleCompraSerie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallecompraserie with id " + id + " no longer exists.", enfe);
            }
            Compraserie idCompraSerie = detallecompraserie.getIdCompraSerie();
            if (idCompraSerie != null) {
                idCompraSerie.getDetallecompraserieList().remove(detallecompraserie);
                idCompraSerie = em.merge(idCompraSerie);
            }
            Serie idSerie = detallecompraserie.getIdSerie();
            if (idSerie != null) {
                idSerie.getDetallecompraserieList().remove(detallecompraserie);
                idSerie = em.merge(idSerie);
            }
            em.remove(detallecompraserie);
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

    public List<Detallecompraserie> findDetallecompraserieEntities() {
        return findDetallecompraserieEntities(true, -1, -1);
    }

    public List<Detallecompraserie> findDetallecompraserieEntities(int maxResults, int firstResult) {
        return findDetallecompraserieEntities(false, maxResults, firstResult);
    }

    private List<Detallecompraserie> findDetallecompraserieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallecompraserie.class));
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

    public Detallecompraserie findDetallecompraserie(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallecompraserie.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallecompraserieCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallecompraserie> rt = cq.from(Detallecompraserie.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Detallecompraserie> obtenerCompra(Compraserie idCompra) {
        List<Detallecompraserie> compras;
        EntityManager em = getEntityManager();
        System.out.println("Buscando deatlles de compra por idCOmpra : " + idCompra);
        Query consulta = em.createNamedQuery("Detallecompraserie.findAll");
        compras = consulta.getResultList();
        for (int i = 0; i < compras.size(); i++) {
            System.out.println(compras.get(i));
        }
        for (int i = 0; i < compras.size(); i++) {
            if (!compras.get(i).getIdCompraSerie().equals(idCompra)) {
                System.out.println("Borre " + compras.get(i));
                compras.remove(i);
                i--;
            }
        }
        return compras;
    }

}

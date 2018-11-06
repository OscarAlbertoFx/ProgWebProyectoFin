/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.PreexistingEntityException;
import controlador.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Categoria;
import entidad.Pelicula;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.UserTransaction;

/**
 *
 * @author aaron
 */
public class PeliculaJpaController implements Serializable {

    public PeliculaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pelicula pelicula) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            em.persist(pelicula);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPelicula(pelicula.getIdPelicula()) != null) {
                throw new PreexistingEntityException("Pelicula " + pelicula + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public void edit(Pelicula pelicula) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Pelicula persistentPelicula = em.find(Pelicula.class, pelicula.getIdPelicula());
            Categoria idCategoriaOld = persistentPelicula.getIdCategoria();
            Categoria idCategoriaNew = pelicula.getIdCategoria();
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                pelicula.setIdCategoria(idCategoriaNew);
            }
            pelicula = em.merge(pelicula);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getPeliculaList().remove(pelicula);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getPeliculaList().add(pelicula);
                idCategoriaNew = em.merge(idCategoriaNew);
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
                Integer id = pelicula.getIdPelicula();
                if (findPelicula(id) == null) {
                    throw new NonexistentEntityException("The pelicula with id " + id + " no longer exists.");
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
            Pelicula pelicula;
            try {
                pelicula = em.getReference(Pelicula.class, id);
                pelicula.getIdPelicula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pelicula with id " + id + " no longer exists.", enfe);
            }
            Categoria idCategoria = pelicula.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getPeliculaList().remove(pelicula);
                idCategoria = em.merge(idCategoria);
            }
            em.remove(pelicula);
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

    public List<Pelicula> findPeliculaEntities() {
        return findPeliculaEntities(true, -1, -1);
    }

    public List<Pelicula> findPeliculaEntities(int maxResults, int firstResult) {
        return findPeliculaEntities(false, maxResults, firstResult);
    }

    private List<Pelicula> findPeliculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pelicula.class));
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

    public Pelicula findPelicula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pelicula.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeliculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pelicula> rt = cq.from(Pelicula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

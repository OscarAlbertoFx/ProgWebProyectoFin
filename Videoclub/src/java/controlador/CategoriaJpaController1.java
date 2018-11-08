/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import entidad.Categoria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Pelicula;
import java.util.ArrayList;
import java.util.List;
import entidad.Serie;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author jair_
 */
public class CategoriaJpaController1 implements Serializable
{

    public CategoriaJpaController1(UserTransaction utx, EntityManagerFactory emf)
    {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Categoria categoria) throws RollbackFailureException, Exception
    {
        if (categoria.getPeliculaList() == null)
        {
            categoria.setPeliculaList(new ArrayList<Pelicula>());
        }
        if (categoria.getSerieList() == null)
        {
            categoria.setSerieList(new ArrayList<Serie>());
        }
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            List<Pelicula> attachedPeliculaList = new ArrayList<Pelicula>();
            for (Pelicula peliculaListPeliculaToAttach : categoria.getPeliculaList())
            {
                peliculaListPeliculaToAttach = em.getReference(peliculaListPeliculaToAttach.getClass(), peliculaListPeliculaToAttach.getIdPelicula());
                attachedPeliculaList.add(peliculaListPeliculaToAttach);
            }
            categoria.setPeliculaList(attachedPeliculaList);
            List<Serie> attachedSerieList = new ArrayList<Serie>();
            for (Serie serieListSerieToAttach : categoria.getSerieList())
            {
                serieListSerieToAttach = em.getReference(serieListSerieToAttach.getClass(), serieListSerieToAttach.getIdSerie());
                attachedSerieList.add(serieListSerieToAttach);
            }
            categoria.setSerieList(attachedSerieList);
            em.persist(categoria);
            for (Pelicula peliculaListPelicula : categoria.getPeliculaList())
            {
                Categoria oldIdCategoriaOfPeliculaListPelicula = peliculaListPelicula.getIdCategoria();
                peliculaListPelicula.setIdCategoria(categoria);
                peliculaListPelicula = em.merge(peliculaListPelicula);
                if (oldIdCategoriaOfPeliculaListPelicula != null)
                {
                    oldIdCategoriaOfPeliculaListPelicula.getPeliculaList().remove(peliculaListPelicula);
                    oldIdCategoriaOfPeliculaListPelicula = em.merge(oldIdCategoriaOfPeliculaListPelicula);
                }
            }
            for (Serie serieListSerie : categoria.getSerieList())
            {
                Categoria oldIdCategoriaOfSerieListSerie = serieListSerie.getIdCategoria();
                serieListSerie.setIdCategoria(categoria);
                serieListSerie = em.merge(serieListSerie);
                if (oldIdCategoriaOfSerieListSerie != null)
                {
                    oldIdCategoriaOfSerieListSerie.getSerieList().remove(serieListSerie);
                    oldIdCategoriaOfSerieListSerie = em.merge(oldIdCategoriaOfSerieListSerie);
                }
            }
            utx.commit();
        } catch (Exception ex)
        {
            try
            {
                utx.rollback();
            } catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public void edit(Categoria categoria) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception
    {
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getIdCategoria());
            List<Pelicula> peliculaListOld = persistentCategoria.getPeliculaList();
            List<Pelicula> peliculaListNew = categoria.getPeliculaList();
            List<Serie> serieListOld = persistentCategoria.getSerieList();
            List<Serie> serieListNew = categoria.getSerieList();
            List<String> illegalOrphanMessages = null;
            for (Pelicula peliculaListOldPelicula : peliculaListOld)
            {
                if (!peliculaListNew.contains(peliculaListOldPelicula))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pelicula " + peliculaListOldPelicula + " since its idCategoria field is not nullable.");
                }
            }
            for (Serie serieListOldSerie : serieListOld)
            {
                if (!serieListNew.contains(serieListOldSerie))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Serie " + serieListOldSerie + " since its idCategoria field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Pelicula> attachedPeliculaListNew = new ArrayList<Pelicula>();
            for (Pelicula peliculaListNewPeliculaToAttach : peliculaListNew)
            {
                peliculaListNewPeliculaToAttach = em.getReference(peliculaListNewPeliculaToAttach.getClass(), peliculaListNewPeliculaToAttach.getIdPelicula());
                attachedPeliculaListNew.add(peliculaListNewPeliculaToAttach);
            }
            peliculaListNew = attachedPeliculaListNew;
            categoria.setPeliculaList(peliculaListNew);
            List<Serie> attachedSerieListNew = new ArrayList<Serie>();
            for (Serie serieListNewSerieToAttach : serieListNew)
            {
                serieListNewSerieToAttach = em.getReference(serieListNewSerieToAttach.getClass(), serieListNewSerieToAttach.getIdSerie());
                attachedSerieListNew.add(serieListNewSerieToAttach);
            }
            serieListNew = attachedSerieListNew;
            categoria.setSerieList(serieListNew);
            categoria = em.merge(categoria);
            for (Pelicula peliculaListNewPelicula : peliculaListNew)
            {
                if (!peliculaListOld.contains(peliculaListNewPelicula))
                {
                    Categoria oldIdCategoriaOfPeliculaListNewPelicula = peliculaListNewPelicula.getIdCategoria();
                    peliculaListNewPelicula.setIdCategoria(categoria);
                    peliculaListNewPelicula = em.merge(peliculaListNewPelicula);
                    if (oldIdCategoriaOfPeliculaListNewPelicula != null && !oldIdCategoriaOfPeliculaListNewPelicula.equals(categoria))
                    {
                        oldIdCategoriaOfPeliculaListNewPelicula.getPeliculaList().remove(peliculaListNewPelicula);
                        oldIdCategoriaOfPeliculaListNewPelicula = em.merge(oldIdCategoriaOfPeliculaListNewPelicula);
                    }
                }
            }
            for (Serie serieListNewSerie : serieListNew)
            {
                if (!serieListOld.contains(serieListNewSerie))
                {
                    Categoria oldIdCategoriaOfSerieListNewSerie = serieListNewSerie.getIdCategoria();
                    serieListNewSerie.setIdCategoria(categoria);
                    serieListNewSerie = em.merge(serieListNewSerie);
                    if (oldIdCategoriaOfSerieListNewSerie != null && !oldIdCategoriaOfSerieListNewSerie.equals(categoria))
                    {
                        oldIdCategoriaOfSerieListNewSerie.getSerieList().remove(serieListNewSerie);
                        oldIdCategoriaOfSerieListNewSerie = em.merge(oldIdCategoriaOfSerieListNewSerie);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex)
        {
            try
            {
                utx.rollback();
            } catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = categoria.getIdCategoria();
                if (findCategoria(id) == null)
                {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception
    {
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            Categoria categoria;
            try
            {
                categoria = em.getReference(Categoria.class, id);
                categoria.getIdCategoria();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pelicula> peliculaListOrphanCheck = categoria.getPeliculaList();
            for (Pelicula peliculaListOrphanCheckPelicula : peliculaListOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Pelicula " + peliculaListOrphanCheckPelicula + " in its peliculaList field has a non-nullable idCategoria field.");
            }
            List<Serie> serieListOrphanCheck = categoria.getSerieList();
            for (Serie serieListOrphanCheckSerie : serieListOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Serie " + serieListOrphanCheckSerie + " in its serieList field has a non-nullable idCategoria field.");
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(categoria);
            utx.commit();
        } catch (Exception ex)
        {
            try
            {
                utx.rollback();
            } catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public List<Categoria> findCategoriaEntities()
    {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult)
    {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
            Query q = em.createQuery(cq);
            if (!all)
            {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally
        {
            em.close();
        }
    }

    public Categoria findCategoria(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Categoria.class, id);
        } finally
        {
            em.close();
        }
    }

    public int getCategoriaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}

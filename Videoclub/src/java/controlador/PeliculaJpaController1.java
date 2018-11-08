/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Categoria;
import entidad.Detallecomprapelicula;
import java.util.ArrayList;
import java.util.List;
import entidad.Detallerenta;
import entidad.Pelicula;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author jair_
 */
public class PeliculaJpaController1 implements Serializable
{

    public PeliculaJpaController1(UserTransaction utx, EntityManagerFactory emf)
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

    public void create(Pelicula pelicula) throws RollbackFailureException, Exception
    {
        if (pelicula.getDetallecomprapeliculaList() == null)
        {
            pelicula.setDetallecomprapeliculaList(new ArrayList<Detallecomprapelicula>());
        }
        if (pelicula.getDetallerentaCollection() == null)
        {
            pelicula.setDetallerentaCollection(new ArrayList<Detallerenta>());
        }
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            Categoria idCategoria = pelicula.getIdCategoria();
            if (idCategoria != null)
            {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                pelicula.setIdCategoria(idCategoria);
            }
            List<Detallecomprapelicula> attachedDetallecomprapeliculaList = new ArrayList<Detallecomprapelicula>();
            for (Detallecomprapelicula detallecomprapeliculaListDetallecomprapeliculaToAttach : pelicula.getDetallecomprapeliculaList())
            {
                detallecomprapeliculaListDetallecomprapeliculaToAttach = em.getReference(detallecomprapeliculaListDetallecomprapeliculaToAttach.getClass(), detallecomprapeliculaListDetallecomprapeliculaToAttach.getIdDetalleCompra());
                attachedDetallecomprapeliculaList.add(detallecomprapeliculaListDetallecomprapeliculaToAttach);
            }
            pelicula.setDetallecomprapeliculaList(attachedDetallecomprapeliculaList);
            Collection<Detallerenta> attachedDetallerentaCollection = new ArrayList<Detallerenta>();
            for (Detallerenta detallerentaCollectionDetallerentaToAttach : pelicula.getDetallerentaCollection())
            {
                detallerentaCollectionDetallerentaToAttach = em.getReference(detallerentaCollectionDetallerentaToAttach.getClass(), detallerentaCollectionDetallerentaToAttach.getIdDetalleRenta());
                attachedDetallerentaCollection.add(detallerentaCollectionDetallerentaToAttach);
            }
            pelicula.setDetallerentaCollection(attachedDetallerentaCollection);
            em.persist(pelicula);
            if (idCategoria != null)
            {
                idCategoria.getPeliculaList().add(pelicula);
                idCategoria = em.merge(idCategoria);
            }
            for (Detallecomprapelicula detallecomprapeliculaListDetallecomprapelicula : pelicula.getDetallecomprapeliculaList())
            {
                Pelicula oldIdPeliculaOfDetallecomprapeliculaListDetallecomprapelicula = detallecomprapeliculaListDetallecomprapelicula.getIdPelicula();
                detallecomprapeliculaListDetallecomprapelicula.setIdPelicula(pelicula);
                detallecomprapeliculaListDetallecomprapelicula = em.merge(detallecomprapeliculaListDetallecomprapelicula);
                if (oldIdPeliculaOfDetallecomprapeliculaListDetallecomprapelicula != null)
                {
                    oldIdPeliculaOfDetallecomprapeliculaListDetallecomprapelicula.getDetallecomprapeliculaList().remove(detallecomprapeliculaListDetallecomprapelicula);
                    oldIdPeliculaOfDetallecomprapeliculaListDetallecomprapelicula = em.merge(oldIdPeliculaOfDetallecomprapeliculaListDetallecomprapelicula);
                }
            }
            for (Detallerenta detallerentaCollectionDetallerenta : pelicula.getDetallerentaCollection())
            {
                Pelicula oldIdPeliculaOfDetallerentaCollectionDetallerenta = detallerentaCollectionDetallerenta.getIdPelicula();
                detallerentaCollectionDetallerenta.setIdPelicula(pelicula);
                detallerentaCollectionDetallerenta = em.merge(detallerentaCollectionDetallerenta);
                if (oldIdPeliculaOfDetallerentaCollectionDetallerenta != null)
                {
                    oldIdPeliculaOfDetallerentaCollectionDetallerenta.getDetallerentaCollection().remove(detallerentaCollectionDetallerenta);
                    oldIdPeliculaOfDetallerentaCollectionDetallerenta = em.merge(oldIdPeliculaOfDetallerentaCollectionDetallerenta);
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

    public void edit(Pelicula pelicula) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception
    {
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            Pelicula persistentPelicula = em.find(Pelicula.class, pelicula.getIdPelicula());
            Categoria idCategoriaOld = persistentPelicula.getIdCategoria();
            Categoria idCategoriaNew = pelicula.getIdCategoria();
            List<Detallecomprapelicula> detallecomprapeliculaListOld = persistentPelicula.getDetallecomprapeliculaList();
            List<Detallecomprapelicula> detallecomprapeliculaListNew = pelicula.getDetallecomprapeliculaList();
            Collection<Detallerenta> detallerentaCollectionOld = persistentPelicula.getDetallerentaCollection();
            Collection<Detallerenta> detallerentaCollectionNew = pelicula.getDetallerentaCollection();
            List<String> illegalOrphanMessages = null;
            for (Detallecomprapelicula detallecomprapeliculaListOldDetallecomprapelicula : detallecomprapeliculaListOld)
            {
                if (!detallecomprapeliculaListNew.contains(detallecomprapeliculaListOldDetallecomprapelicula))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallecomprapelicula " + detallecomprapeliculaListOldDetallecomprapelicula + " since its idPelicula field is not nullable.");
                }
            }
            for (Detallerenta detallerentaCollectionOldDetallerenta : detallerentaCollectionOld)
            {
                if (!detallerentaCollectionNew.contains(detallerentaCollectionOldDetallerenta))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallerenta " + detallerentaCollectionOldDetallerenta + " since its idPelicula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null)
            {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                pelicula.setIdCategoria(idCategoriaNew);
            }
            List<Detallecomprapelicula> attachedDetallecomprapeliculaListNew = new ArrayList<Detallecomprapelicula>();
            for (Detallecomprapelicula detallecomprapeliculaListNewDetallecomprapeliculaToAttach : detallecomprapeliculaListNew)
            {
                detallecomprapeliculaListNewDetallecomprapeliculaToAttach = em.getReference(detallecomprapeliculaListNewDetallecomprapeliculaToAttach.getClass(), detallecomprapeliculaListNewDetallecomprapeliculaToAttach.getIdDetalleCompra());
                attachedDetallecomprapeliculaListNew.add(detallecomprapeliculaListNewDetallecomprapeliculaToAttach);
            }
            detallecomprapeliculaListNew = attachedDetallecomprapeliculaListNew;
            pelicula.setDetallecomprapeliculaList(detallecomprapeliculaListNew);
            Collection<Detallerenta> attachedDetallerentaCollectionNew = new ArrayList<Detallerenta>();
            for (Detallerenta detallerentaCollectionNewDetallerentaToAttach : detallerentaCollectionNew)
            {
                detallerentaCollectionNewDetallerentaToAttach = em.getReference(detallerentaCollectionNewDetallerentaToAttach.getClass(), detallerentaCollectionNewDetallerentaToAttach.getIdDetalleRenta());
                attachedDetallerentaCollectionNew.add(detallerentaCollectionNewDetallerentaToAttach);
            }
            detallerentaCollectionNew = attachedDetallerentaCollectionNew;
            pelicula.setDetallerentaCollection(detallerentaCollectionNew);
            pelicula = em.merge(pelicula);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew))
            {
                idCategoriaOld.getPeliculaList().remove(pelicula);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld))
            {
                idCategoriaNew.getPeliculaList().add(pelicula);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            for (Detallecomprapelicula detallecomprapeliculaListNewDetallecomprapelicula : detallecomprapeliculaListNew)
            {
                if (!detallecomprapeliculaListOld.contains(detallecomprapeliculaListNewDetallecomprapelicula))
                {
                    Pelicula oldIdPeliculaOfDetallecomprapeliculaListNewDetallecomprapelicula = detallecomprapeliculaListNewDetallecomprapelicula.getIdPelicula();
                    detallecomprapeliculaListNewDetallecomprapelicula.setIdPelicula(pelicula);
                    detallecomprapeliculaListNewDetallecomprapelicula = em.merge(detallecomprapeliculaListNewDetallecomprapelicula);
                    if (oldIdPeliculaOfDetallecomprapeliculaListNewDetallecomprapelicula != null && !oldIdPeliculaOfDetallecomprapeliculaListNewDetallecomprapelicula.equals(pelicula))
                    {
                        oldIdPeliculaOfDetallecomprapeliculaListNewDetallecomprapelicula.getDetallecomprapeliculaList().remove(detallecomprapeliculaListNewDetallecomprapelicula);
                        oldIdPeliculaOfDetallecomprapeliculaListNewDetallecomprapelicula = em.merge(oldIdPeliculaOfDetallecomprapeliculaListNewDetallecomprapelicula);
                    }
                }
            }
            for (Detallerenta detallerentaCollectionNewDetallerenta : detallerentaCollectionNew)
            {
                if (!detallerentaCollectionOld.contains(detallerentaCollectionNewDetallerenta))
                {
                    Pelicula oldIdPeliculaOfDetallerentaCollectionNewDetallerenta = detallerentaCollectionNewDetallerenta.getIdPelicula();
                    detallerentaCollectionNewDetallerenta.setIdPelicula(pelicula);
                    detallerentaCollectionNewDetallerenta = em.merge(detallerentaCollectionNewDetallerenta);
                    if (oldIdPeliculaOfDetallerentaCollectionNewDetallerenta != null && !oldIdPeliculaOfDetallerentaCollectionNewDetallerenta.equals(pelicula))
                    {
                        oldIdPeliculaOfDetallerentaCollectionNewDetallerenta.getDetallerentaCollection().remove(detallerentaCollectionNewDetallerenta);
                        oldIdPeliculaOfDetallerentaCollectionNewDetallerenta = em.merge(oldIdPeliculaOfDetallerentaCollectionNewDetallerenta);
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
                Integer id = pelicula.getIdPelicula();
                if (findPelicula(id) == null)
                {
                    throw new NonexistentEntityException("The pelicula with id " + id + " no longer exists.");
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
            Pelicula pelicula;
            try
            {
                pelicula = em.getReference(Pelicula.class, id);
                pelicula.getIdPelicula();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The pelicula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallecomprapelicula> detallecomprapeliculaListOrphanCheck = pelicula.getDetallecomprapeliculaList();
            for (Detallecomprapelicula detallecomprapeliculaListOrphanCheckDetallecomprapelicula : detallecomprapeliculaListOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pelicula (" + pelicula + ") cannot be destroyed since the Detallecomprapelicula " + detallecomprapeliculaListOrphanCheckDetallecomprapelicula + " in its detallecomprapeliculaList field has a non-nullable idPelicula field.");
            }
            Collection<Detallerenta> detallerentaCollectionOrphanCheck = pelicula.getDetallerentaCollection();
            for (Detallerenta detallerentaCollectionOrphanCheckDetallerenta : detallerentaCollectionOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pelicula (" + pelicula + ") cannot be destroyed since the Detallerenta " + detallerentaCollectionOrphanCheckDetallerenta + " in its detallerentaCollection field has a non-nullable idPelicula field.");
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idCategoria = pelicula.getIdCategoria();
            if (idCategoria != null)
            {
                idCategoria.getPeliculaList().remove(pelicula);
                idCategoria = em.merge(idCategoria);
            }
            em.remove(pelicula);
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

    public List<Pelicula> findPeliculaEntities()
    {
        return findPeliculaEntities(true, -1, -1);
    }

    public List<Pelicula> findPeliculaEntities(int maxResults, int firstResult)
    {
        return findPeliculaEntities(false, maxResults, firstResult);
    }

    private List<Pelicula> findPeliculaEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pelicula.class));
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

    public Pelicula findPelicula(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Pelicula.class, id);
        } finally
        {
            em.close();
        }
    }

    public int getPeliculaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pelicula> rt = cq.from(Pelicula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}

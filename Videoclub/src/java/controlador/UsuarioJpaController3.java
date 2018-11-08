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
import entidad.Comprapelicula;
import java.util.ArrayList;
import java.util.List;
import entidad.Compraserie;
import entidad.Rentapelicula;
import entidad.Usuario;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author jair_
 */
public class UsuarioJpaController3 implements Serializable
{

    public UsuarioJpaController3(UserTransaction utx, EntityManagerFactory emf)
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

    public void create(Usuario usuario) throws RollbackFailureException, Exception
    {
        if (usuario.getComprapeliculaList() == null)
        {
            usuario.setComprapeliculaList(new ArrayList<Comprapelicula>());
        }
        if (usuario.getCompraserieList() == null)
        {
            usuario.setCompraserieList(new ArrayList<Compraserie>());
        }
        if (usuario.getRentapeliculaCollection() == null)
        {
            usuario.setRentapeliculaCollection(new ArrayList<Rentapelicula>());
        }
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            List<Comprapelicula> attachedComprapeliculaList = new ArrayList<Comprapelicula>();
            for (Comprapelicula comprapeliculaListComprapeliculaToAttach : usuario.getComprapeliculaList())
            {
                comprapeliculaListComprapeliculaToAttach = em.getReference(comprapeliculaListComprapeliculaToAttach.getClass(), comprapeliculaListComprapeliculaToAttach.getIdCompra());
                attachedComprapeliculaList.add(comprapeliculaListComprapeliculaToAttach);
            }
            usuario.setComprapeliculaList(attachedComprapeliculaList);
            List<Compraserie> attachedCompraserieList = new ArrayList<Compraserie>();
            for (Compraserie compraserieListCompraserieToAttach : usuario.getCompraserieList())
            {
                compraserieListCompraserieToAttach = em.getReference(compraserieListCompraserieToAttach.getClass(), compraserieListCompraserieToAttach.getIdCompraSerie());
                attachedCompraserieList.add(compraserieListCompraserieToAttach);
            }
            usuario.setCompraserieList(attachedCompraserieList);
            Collection<Rentapelicula> attachedRentapeliculaCollection = new ArrayList<Rentapelicula>();
            for (Rentapelicula rentapeliculaCollectionRentapeliculaToAttach : usuario.getRentapeliculaCollection())
            {
                rentapeliculaCollectionRentapeliculaToAttach = em.getReference(rentapeliculaCollectionRentapeliculaToAttach.getClass(), rentapeliculaCollectionRentapeliculaToAttach.getIdRenta());
                attachedRentapeliculaCollection.add(rentapeliculaCollectionRentapeliculaToAttach);
            }
            usuario.setRentapeliculaCollection(attachedRentapeliculaCollection);
            em.persist(usuario);
            for (Comprapelicula comprapeliculaListComprapelicula : usuario.getComprapeliculaList())
            {
                Usuario oldIdUsuarioOfComprapeliculaListComprapelicula = comprapeliculaListComprapelicula.getIdUsuario();
                comprapeliculaListComprapelicula.setIdUsuario(usuario);
                comprapeliculaListComprapelicula = em.merge(comprapeliculaListComprapelicula);
                if (oldIdUsuarioOfComprapeliculaListComprapelicula != null)
                {
                    oldIdUsuarioOfComprapeliculaListComprapelicula.getComprapeliculaList().remove(comprapeliculaListComprapelicula);
                    oldIdUsuarioOfComprapeliculaListComprapelicula = em.merge(oldIdUsuarioOfComprapeliculaListComprapelicula);
                }
            }
            for (Compraserie compraserieListCompraserie : usuario.getCompraserieList())
            {
                Usuario oldIdUsuarioOfCompraserieListCompraserie = compraserieListCompraserie.getIdUsuario();
                compraserieListCompraserie.setIdUsuario(usuario);
                compraserieListCompraserie = em.merge(compraserieListCompraserie);
                if (oldIdUsuarioOfCompraserieListCompraserie != null)
                {
                    oldIdUsuarioOfCompraserieListCompraserie.getCompraserieList().remove(compraserieListCompraserie);
                    oldIdUsuarioOfCompraserieListCompraserie = em.merge(oldIdUsuarioOfCompraserieListCompraserie);
                }
            }
            for (Rentapelicula rentapeliculaCollectionRentapelicula : usuario.getRentapeliculaCollection())
            {
                Usuario oldIdUsuarioOfRentapeliculaCollectionRentapelicula = rentapeliculaCollectionRentapelicula.getIdUsuario();
                rentapeliculaCollectionRentapelicula.setIdUsuario(usuario);
                rentapeliculaCollectionRentapelicula = em.merge(rentapeliculaCollectionRentapelicula);
                if (oldIdUsuarioOfRentapeliculaCollectionRentapelicula != null)
                {
                    oldIdUsuarioOfRentapeliculaCollectionRentapelicula.getRentapeliculaCollection().remove(rentapeliculaCollectionRentapelicula);
                    oldIdUsuarioOfRentapeliculaCollectionRentapelicula = em.merge(oldIdUsuarioOfRentapeliculaCollectionRentapelicula);
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

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception
    {
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            List<Comprapelicula> comprapeliculaListOld = persistentUsuario.getComprapeliculaList();
            List<Comprapelicula> comprapeliculaListNew = usuario.getComprapeliculaList();
            List<Compraserie> compraserieListOld = persistentUsuario.getCompraserieList();
            List<Compraserie> compraserieListNew = usuario.getCompraserieList();
            Collection<Rentapelicula> rentapeliculaCollectionOld = persistentUsuario.getRentapeliculaCollection();
            Collection<Rentapelicula> rentapeliculaCollectionNew = usuario.getRentapeliculaCollection();
            List<String> illegalOrphanMessages = null;
            for (Comprapelicula comprapeliculaListOldComprapelicula : comprapeliculaListOld)
            {
                if (!comprapeliculaListNew.contains(comprapeliculaListOldComprapelicula))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comprapelicula " + comprapeliculaListOldComprapelicula + " since its idUsuario field is not nullable.");
                }
            }
            for (Compraserie compraserieListOldCompraserie : compraserieListOld)
            {
                if (!compraserieListNew.contains(compraserieListOldCompraserie))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compraserie " + compraserieListOldCompraserie + " since its idUsuario field is not nullable.");
                }
            }
            for (Rentapelicula rentapeliculaCollectionOldRentapelicula : rentapeliculaCollectionOld)
            {
                if (!rentapeliculaCollectionNew.contains(rentapeliculaCollectionOldRentapelicula))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rentapelicula " + rentapeliculaCollectionOldRentapelicula + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Comprapelicula> attachedComprapeliculaListNew = new ArrayList<Comprapelicula>();
            for (Comprapelicula comprapeliculaListNewComprapeliculaToAttach : comprapeliculaListNew)
            {
                comprapeliculaListNewComprapeliculaToAttach = em.getReference(comprapeliculaListNewComprapeliculaToAttach.getClass(), comprapeliculaListNewComprapeliculaToAttach.getIdCompra());
                attachedComprapeliculaListNew.add(comprapeliculaListNewComprapeliculaToAttach);
            }
            comprapeliculaListNew = attachedComprapeliculaListNew;
            usuario.setComprapeliculaList(comprapeliculaListNew);
            List<Compraserie> attachedCompraserieListNew = new ArrayList<Compraserie>();
            for (Compraserie compraserieListNewCompraserieToAttach : compraserieListNew)
            {
                compraserieListNewCompraserieToAttach = em.getReference(compraserieListNewCompraserieToAttach.getClass(), compraserieListNewCompraserieToAttach.getIdCompraSerie());
                attachedCompraserieListNew.add(compraserieListNewCompraserieToAttach);
            }
            compraserieListNew = attachedCompraserieListNew;
            usuario.setCompraserieList(compraserieListNew);
            Collection<Rentapelicula> attachedRentapeliculaCollectionNew = new ArrayList<Rentapelicula>();
            for (Rentapelicula rentapeliculaCollectionNewRentapeliculaToAttach : rentapeliculaCollectionNew)
            {
                rentapeliculaCollectionNewRentapeliculaToAttach = em.getReference(rentapeliculaCollectionNewRentapeliculaToAttach.getClass(), rentapeliculaCollectionNewRentapeliculaToAttach.getIdRenta());
                attachedRentapeliculaCollectionNew.add(rentapeliculaCollectionNewRentapeliculaToAttach);
            }
            rentapeliculaCollectionNew = attachedRentapeliculaCollectionNew;
            usuario.setRentapeliculaCollection(rentapeliculaCollectionNew);
            usuario = em.merge(usuario);
            for (Comprapelicula comprapeliculaListNewComprapelicula : comprapeliculaListNew)
            {
                if (!comprapeliculaListOld.contains(comprapeliculaListNewComprapelicula))
                {
                    Usuario oldIdUsuarioOfComprapeliculaListNewComprapelicula = comprapeliculaListNewComprapelicula.getIdUsuario();
                    comprapeliculaListNewComprapelicula.setIdUsuario(usuario);
                    comprapeliculaListNewComprapelicula = em.merge(comprapeliculaListNewComprapelicula);
                    if (oldIdUsuarioOfComprapeliculaListNewComprapelicula != null && !oldIdUsuarioOfComprapeliculaListNewComprapelicula.equals(usuario))
                    {
                        oldIdUsuarioOfComprapeliculaListNewComprapelicula.getComprapeliculaList().remove(comprapeliculaListNewComprapelicula);
                        oldIdUsuarioOfComprapeliculaListNewComprapelicula = em.merge(oldIdUsuarioOfComprapeliculaListNewComprapelicula);
                    }
                }
            }
            for (Compraserie compraserieListNewCompraserie : compraserieListNew)
            {
                if (!compraserieListOld.contains(compraserieListNewCompraserie))
                {
                    Usuario oldIdUsuarioOfCompraserieListNewCompraserie = compraserieListNewCompraserie.getIdUsuario();
                    compraserieListNewCompraserie.setIdUsuario(usuario);
                    compraserieListNewCompraserie = em.merge(compraserieListNewCompraserie);
                    if (oldIdUsuarioOfCompraserieListNewCompraserie != null && !oldIdUsuarioOfCompraserieListNewCompraserie.equals(usuario))
                    {
                        oldIdUsuarioOfCompraserieListNewCompraserie.getCompraserieList().remove(compraserieListNewCompraserie);
                        oldIdUsuarioOfCompraserieListNewCompraserie = em.merge(oldIdUsuarioOfCompraserieListNewCompraserie);
                    }
                }
            }
            for (Rentapelicula rentapeliculaCollectionNewRentapelicula : rentapeliculaCollectionNew)
            {
                if (!rentapeliculaCollectionOld.contains(rentapeliculaCollectionNewRentapelicula))
                {
                    Usuario oldIdUsuarioOfRentapeliculaCollectionNewRentapelicula = rentapeliculaCollectionNewRentapelicula.getIdUsuario();
                    rentapeliculaCollectionNewRentapelicula.setIdUsuario(usuario);
                    rentapeliculaCollectionNewRentapelicula = em.merge(rentapeliculaCollectionNewRentapelicula);
                    if (oldIdUsuarioOfRentapeliculaCollectionNewRentapelicula != null && !oldIdUsuarioOfRentapeliculaCollectionNewRentapelicula.equals(usuario))
                    {
                        oldIdUsuarioOfRentapeliculaCollectionNewRentapelicula.getRentapeliculaCollection().remove(rentapeliculaCollectionNewRentapelicula);
                        oldIdUsuarioOfRentapeliculaCollectionNewRentapelicula = em.merge(oldIdUsuarioOfRentapeliculaCollectionNewRentapelicula);
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
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null)
                {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try
            {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Comprapelicula> comprapeliculaListOrphanCheck = usuario.getComprapeliculaList();
            for (Comprapelicula comprapeliculaListOrphanCheckComprapelicula : comprapeliculaListOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Comprapelicula " + comprapeliculaListOrphanCheckComprapelicula + " in its comprapeliculaList field has a non-nullable idUsuario field.");
            }
            List<Compraserie> compraserieListOrphanCheck = usuario.getCompraserieList();
            for (Compraserie compraserieListOrphanCheckCompraserie : compraserieListOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Compraserie " + compraserieListOrphanCheckCompraserie + " in its compraserieList field has a non-nullable idUsuario field.");
            }
            Collection<Rentapelicula> rentapeliculaCollectionOrphanCheck = usuario.getRentapeliculaCollection();
            for (Rentapelicula rentapeliculaCollectionOrphanCheckRentapelicula : rentapeliculaCollectionOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Rentapelicula " + rentapeliculaCollectionOrphanCheckRentapelicula + " in its rentapeliculaCollection field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities()
    {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult)
    {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Usuario.class, id);
        } finally
        {
            em.close();
        }
    }

    public int getUsuarioCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.PreexistingEntityException;
import controlador.exceptions.RollbackFailureException;
import entidad.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author tigre
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws Exception {
        EntityManager em = null;
        try {

            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            em.persist(usuario);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsuario(usuario.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
 

    public void edit(Usuario usuario) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            usuario = em.merge(usuario);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public Usuario findCuates(String correo) {
        Usuario prb = new Usuario();
        EntityManager eme;

        eme = getEntityManager();
        utx = eme.getTransaction();
        utx.begin();

        TypedQuery<Usuario> q = eme.createNamedQuery("SELECT u FROM Usuario u WHERE u.correo = :'" + correo + "'", Usuario.class);
        utx.commit();
        q.setParameter("username", correo);
        prb.setCorreo(q.getSingleResult().getCorreo());
        prb.setNombre(q.getSingleResult().getNombre());
        prb.setApellidoP(q.getSingleResult().getApellidoP());
        prb.setApellidoM(q.getSingleResult().getApellidoM());
        prb.setIdUsuario(q.getSingleResult().getIdUsuario());
        prb.setContraseña(q.getSingleResult().getContraseña());
        prb.setCalle(q.getSingleResult().getCalle());
        prb.setCp(q.getSingleResult().getCp());
        prb.setNoExt(q.getSingleResult().getNoExt());
        prb.setNoInt(q.getSingleResult().getNoInt());
        prb.setTelefonoFijo(q.getSingleResult().getTelefonoFijo());

        try {
            return prb;
        } finally {
            eme.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Usuario findByCorreo(String login){
        Usuario user = null;
        List<Usuario> users;
        EntityManager em = getEntityManager();
        System.out.println("Buscando usuario por email jpacontroller");
        Query consulta = em.createNamedQuery("Usuario.findByCorreo");
        consulta.setParameter("correo", login);
        users=consulta.getResultList();
        if (!users.isEmpty()) {
            System.out.println("Lo encontro");
            System.out.println("users.get[0]: "+users.get(0));
            user = users.get(0);
        }
        return user;
    }
}

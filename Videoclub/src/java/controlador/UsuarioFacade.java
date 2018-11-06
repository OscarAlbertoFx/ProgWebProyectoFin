/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.RollbackFailureException;
import entidad.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
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
@Stateless
public class UsuarioFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("VideoclubPU");
    private UserTransaction utx;
    private UsuarioJpaController userJpa = new UsuarioJpaController(emf);

    public void crearUsuario(Usuario usuario) {
        try {
            userJpa.create(usuario);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarUsuario(Usuario usuario) {
        try {
            userJpa.edit(usuario);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Usuario buscarPorcorreo(String Correo) {
        Usuario user;
        user = userJpa.findByCorreo(Correo);
        return user;
    }

    public boolean buscarUsuario(String correo, String contraseña) {
        Usuario userPojo;
        boolean valido = false;

        userPojo = new Usuario();

        userPojo = userJpa.findByCorreo(correo);

        System.out.println("Usuario hallado ");
        if (userPojo != null) {
            valido = validarUsuario(userPojo, contraseña);
            if (valido) {
                System.out.println("Es valido");
                return true;
            } else {
                System.out.println("No es valido");
                return false;
            }
        }
        return false;
    }

    public boolean validarUsuario(Usuario user, String pwd) {
        String actLogin, actPwd;
        actPwd = user.getContraseña();
        if (actPwd.equals(pwd)) {
            return true;
        } else {
            return false;
        }
    }

}

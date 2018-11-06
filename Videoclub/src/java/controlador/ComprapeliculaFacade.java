
package controlador;

import entidad.Comprapelicula;
import entidad.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

@Stateless
public class ComprapeliculaFacade{

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("VideoclubPU");
    private UserTransaction utx;
    private ComprapeliculaJpaController compraJpa = new ComprapeliculaJpaController(emf);
    private UsuarioJpaController userJpa = new UsuarioJpaController(emf);
    
    public ComprapeliculaFacade() {
        
    }
    
    public Comprapelicula crearCompra(CompraPeliculaPojo compra, String correo_user){
        Comprapelicula compraP = null;
        try {
            compraP = new Comprapelicula();
            compraP.setIdCompra(compra.getIdCompra());
            compraP.setIdUsuario(buscarPorcorreo(correo_user));
            compraP.setTotalCompra(compra.getTotal_compra());
            compraP.setFechaCompra(compra.getFecha_compra());
            compraP.setFechaEntrega(compra.getFecha_entrega());
            compraJpa.create(compraP);
            return compraP;
        } catch (Exception ex) {
            Logger.getLogger(ComprapeliculaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return compraP;
    }
    
    public Usuario buscarPorcorreo(String correo_user) {
        Usuario user;
        user = userJpa.findByCorreo(correo_user);
        return user;
    }
    
    public void update(Comprapelicula compra) throws Exception{
        compraJpa.edit(compra);
    }
    
    public void remove(int idCompraP) throws Exception{
        compraJpa.destroy(idCompraP);
    }
}

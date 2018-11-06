package controlador;

import entidad.Compraserie;
import entidad.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

@Stateless
public class CompraserieFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("VideoclubPU");
    private UserTransaction utx;
    private CompraserieJpaController compraSerieJpa = new CompraserieJpaController(emf);
    private UsuarioJpaController userJpa = new UsuarioJpaController(emf);

    public CompraserieFacade() {

    }

    public Compraserie crearCompra(CompraSeriePojo compra, String correo_user) {
        Compraserie compraS = null;
        try {
            compraS = new Compraserie();
            compraS.setIdCompraSerie(compra.getIdCompraSerie());
            compraS.setIdUsuario(buscarPorcorreo(correo_user));
            compraS.setTotalCompra(compra.getTotal_compra());
            compraS.setFechaCompra(compra.getFecha_compra());
            compraS.setFechaEntrega(compra.getFecha_entrega());
            compraSerieJpa.create(compraS);
            return compraS;
        } catch (Exception ex) {
            Logger.getLogger(ComprapeliculaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return compraS;
    }
    
    public Usuario buscarPorcorreo(String correo_user) {
        Usuario user;
        user = userJpa.findByCorreo(correo_user);
        return user;
    }
    
    public void update(Compraserie compra) throws Exception{
        compraSerieJpa.edit(compra);
    }
    
    public void remove(int idCompraSerie) throws Exception{
        compraSerieJpa.destroy(idCompraSerie);
    }

}

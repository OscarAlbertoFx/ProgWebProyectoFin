
package controlador;

import entidad.Comprapelicula;
import entidad.Detallecomprapelicula;
import entidad.Pelicula;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

@Stateless
public class DetallecomprapeliculaFacade{

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("VideoclubPU");
    private UserTransaction utx;
    private DetallecomprapeliculaJpaController detalleJpa = new DetallecomprapeliculaJpaController(emf);
    private ComprapeliculaJpaController compraJpa = new ComprapeliculaJpaController(emf);
    private PeliculaJpaController peliJpa = new PeliculaJpaController(emf);
    
    public DetallecomprapeliculaFacade() {
        
    }
    
    public Comprapelicula getCompra(int idCompra){
        return compraJpa.findComprapelicula(idCompra);
    }
    
    public Pelicula getPelicula(int idPelicula){
        return peliJpa.findPelicula(idPelicula);
    }
    
    public void create(DetalleCompraPojo compra){
        try {
            Detallecomprapelicula c = new Detallecomprapelicula();
            c.setIdCompra(compra.getIdCompra());
            c.setIdDetalleCompra(compra.getIdDetalleCompra());
            c.setIdPelicula(compra.getIdPelicula());
            c.setSubtotal(compra.getIdPelicula().getPrecioCompra());
            detalleJpa.create(c);
        } catch (Exception ex) {
            Logger.getLogger(DetallecomprapeliculaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Detallecomprapelicula> filtrar(Comprapelicula idCompra){
        return detalleJpa.obtenerCompra(idCompra);
    }
    
    public void remove(Comprapelicula idCompra) throws Exception{
        List<Detallecomprapelicula> registros = detalleJpa.obtenerCompra(idCompra);
        for (int i = 0; i < registros.size(); i++) {
            detalleJpa.destroy(registros.get(i).getIdDetalleCompra());
        }
    }
}

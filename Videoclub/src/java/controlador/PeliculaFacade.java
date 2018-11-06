
package controlador;

import controlador.exceptions.RollbackFailureException;
import entidad.Pelicula;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

@Stateless
public class PeliculaFacade{

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("VideoclubPU");
    private UserTransaction utx;
    private PeliculaJpaController peliJpa = new PeliculaJpaController(emf);
    
    public PeliculaFacade() {
        
    }
    
    public PeliculaPojo buscarPelicula(int id) {
        PeliculaPojo peliculaPojo = new PeliculaPojo();
        boolean valido = false;
        Pelicula pelicula;
        pelicula = new Pelicula();
        
        pelicula = peliJpa.findPelicula(id);
        
        System.out.println("Pelicula hallada " + pelicula);
        peliculaPojo.setIdPelicula(pelicula.getIdPelicula());
        peliculaPojo.setSinopsis(pelicula.getSinopsis());
        peliculaPojo.setTitulo(pelicula.getTitulo());
        peliculaPojo.setClasificacion(pelicula.getClasificacion());
        peliculaPojo.setRating(pelicula.getRating());
        peliculaPojo.setDirector(pelicula.getDirector());
        peliculaPojo.setIdCategoria(pelicula.getIdCategoria());
        peliculaPojo.setPrecio_compra(pelicula.getPrecioCompra());
        peliculaPojo.setPrecio_renta(pelicula.getPrecioRenta());
        peliculaPojo.setAño(pelicula.getAño());
        peliculaPojo.setDuracion(pelicula.getDuracion());
        peliculaPojo.setCantidad_almacen(pelicula.getCantidadAlmacen());
        peliculaPojo.setCantidad_rentas(pelicula.getCantidadRenta());
        return peliculaPojo;
    }
        public void crearPelicula(Pelicula pelicula) {
        try {
            peliJpa.create(pelicula);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}


package controlador;

import entidad.Compraserie;
import entidad.Detallecomprapelicula;
import entidad.Detallecompraserie;
import entidad.Serie;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

@Stateless
public class DetallecompraserieFacade{

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("VideoclubPU");
    private UserTransaction utx;
    private DetallecompraserieJpaController detalleSerieJpa = new DetallecompraserieJpaController(emf);
    private CompraserieJpaController compraJpa = new CompraserieJpaController(emf);
    private SerieJpaController serieJpa = new SerieJpaController(emf);

    public DetallecompraserieFacade() {
        
    }

    public Compraserie getCompra(int idCompra){
        return compraJpa.findCompraserie(idCompra);
    }
    
    public Serie getSerie(int idSerie){
        return serieJpa.findSerie(idSerie);
    }
    
    public void create(DetalleCompraSeriePojo compra){
        try {
            Detallecompraserie c = new Detallecompraserie();
            c.setIdCompraSerie(compra.getIdCompraSerie());
            c.setIdDetalleCompraSerie(compra.getIdDetalleCompraSerie());
            c.setIdSerie(compra.getIdSerie());
            c.setSubtotal(compra.getIdSerie().getPrecio());
            detalleSerieJpa.create(c);
        } catch (Exception ex) {
            Logger.getLogger(DetallecomprapeliculaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Detallecompraserie> filtrar(Compraserie idCompra){
        return detalleSerieJpa.obtenerCompra(idCompra);
    }
    
    public void remove(Compraserie idCompra) throws Exception{
        List<Detallecompraserie> registros = detalleSerieJpa.obtenerCompra(idCompra);
        for (int i = 0; i < registros.size(); i++) {
            detalleSerieJpa.destroy(registros.get(i).getIdDetalleCompraSerie());
        }
    }
    
}

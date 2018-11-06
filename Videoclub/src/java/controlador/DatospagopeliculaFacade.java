
package controlador;

import entidad.Datospagopelicula;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

@Stateless
public class DatospagopeliculaFacade{

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("VideoclubPU");
    private UserTransaction utx;
    private DatospagopeliculaJpaController datosJpa = new DatospagopeliculaJpaController(emf);

    public DatospagopeliculaFacade() {
        
    }
    
    public void create(DatosPagoPeliculaPojo datos) throws Exception{
        System.out.println("Paso por qui");
        Datospagopelicula dP = new Datospagopelicula();
        dP.setContraseña(datos.getContraseña());
        dP.setCvv(datos.getCvv());
        dP.setFechaCaducidad(datos.getFecha_caducidad());
        dP.setIdCompraPelicula(datos.getIdCompraPelicula());
        dP.setIdDatosPagoPelicula(datos.getIdDatosPagoPelicula());
        dP.setTarjeta(datos.getTarjeta());
        datosJpa.create(dP);
    }
}

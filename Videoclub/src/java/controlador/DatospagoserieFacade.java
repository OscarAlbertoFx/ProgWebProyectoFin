
package controlador;

import entidad.Compraserie;
import entidad.Datospagoserie;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Stateless
public class DatospagoserieFacade{

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("VideoclubPU");
    private DatospagoserieJpaController datosJpa = new DatospagoserieJpaController(emf);

    public DatospagoserieFacade() {
        
    }
    
    public void create(DatosPagoPeliculaPojo datos,Compraserie cs) throws Exception{
        System.out.println("Paso por qui");
        Datospagoserie dP = new Datospagoserie();
        dP.setContraseña(datos.getContraseña());
        dP.setCvv(datos.getCvv());
        dP.setFechaCaducidad(datos.getFecha_caducidad());
        dP.setIdCompraSerie(cs);
        dP.setIdDatosPagoSerie(0);
        dP.setTarjeta(datos.getTarjeta());
        datosJpa.create(dP);
    }
    
}

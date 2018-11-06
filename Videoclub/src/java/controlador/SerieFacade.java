/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.RollbackFailureException;
import entidad.Serie;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author aaron
 */
@Stateless
public class SerieFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("VideoclubPU");
    private UserTransaction utx;
    private SerieJpaController serieJpa = new SerieJpaController(emf);

    public SerieFacade() {

    }

    public SeriePojo buscarSerie(int id) {
        SeriePojo seriePojo = new SeriePojo();
        boolean valido = false;
        Serie serie = new Serie();

        serie = serieJpa.findSerie(id);

        System.out.println("Serie hallada " + serie);
        seriePojo.setIdCategoria(serie.getIdCategoria());
        seriePojo.setIdSerie(serie.getIdSerie());
        seriePojo.setNumero_temporadas(serie.getNumeroTemporadas());
        seriePojo.setPrecio(serie.getPrecio());
        seriePojo.setRating(serie.getRating());
        seriePojo.setSinopsis(serie.getSinopsis());
        seriePojo.setTitulo(serie.getTitulo());
        return seriePojo;
    }

    public void crearSerie(Serie seie) {
        try {
            serieJpa.create(seie);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

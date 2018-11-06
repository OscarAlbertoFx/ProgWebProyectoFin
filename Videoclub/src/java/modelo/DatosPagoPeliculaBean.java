package modelo;

import controlador.DatosPagoPeliculaPojo;
import controlador.DatospagopeliculaFacade;
import controlador.DatospagoserieFacade;
import controlador.DetallecomprapeliculaFacade;
import controlador.DetallecompraserieFacade;
import controlador.UsuarioFacade;
import entidad.Comprapelicula;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named(value = "datosPagoPeliculaBean")
@RequestScoped
public class DatosPagoPeliculaBean {

    private int idDatosPagoPelicula;
    private Comprapelicula idCompraPelicula;
    private String tarjeta;
    private String fecha_caducidad;
    private String cvv;
    private String contraseña;

    private UsuarioFacade usuarioFacade;
    private DatosPagoPeliculaPojo datosPojo;
    private DetallecomprapeliculaFacade compraFacade;
    private DatospagopeliculaFacade datosFacade;
    private DatospagoserieFacade datosSerieFacade;
    private DetallecompraserieFacade compraSerieFacade;

    public DatosPagoPeliculaBean() {

    }

    public int getIdDatosPagoPelicula() {
        return idDatosPagoPelicula;
    }

    public void setIdDatosPagoPelicula(int idDatosPagoPelicula) {
        this.idDatosPagoPelicula = idDatosPagoPelicula;
    }

    public Comprapelicula getIdCompraPelicula() {
        return idCompraPelicula;
    }

    public void setIdCompraPelicula(Comprapelicula idCompraPelicula) {
        this.idCompraPelicula = idCompraPelicula;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getFecha_caducidad() {
        return fecha_caducidad;
    }

    public void setFecha_caducidad(String fecha_caducidad) {
        this.fecha_caducidad = fecha_caducidad;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void submit(String correo) throws Exception {
        usuarioFacade = new UsuarioFacade();
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("Entro al sumbit");
        UserLog neededBean = (UserLog) context.getApplication().createValueBinding("#{userLog}").getValue(context);
        System.out.println(neededBean.getCorreo());;
        System.out.println(getContraseña());
        if (!usuarioFacade.buscarUsuario(neededBean.getCorreo(), getContraseña())) {
            System.out.println("No coincide");
        } else {
            System.out.println("----------------------La contraseña coincide");
            datosPojo = new DatosPagoPeliculaPojo();
            compraFacade = new DetallecomprapeliculaFacade();
            compraSerieFacade = new DetallecompraserieFacade();
            datosPojo.setContraseña(contraseña);
            datosPojo.setCvv(cvv);
            Date fecha = null;
            try {
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
                fecha = formatoDelTexto.parse(fecha_caducidad);
            } catch (Exception e) {
                fecha = null;
            }
            datosPojo.setFecha_caducidad(fecha);
            datosPojo.setIdDatosPagoPelicula(idDatosPagoPelicula);
            System.out.println(compraFacade.getCompra(neededBean.getIdCompra()) + "Este es el problema---------XDXD");
            datosPojo.setIdCompraPelicula(compraFacade.getCompra(neededBean.getIdCompra()));
            datosPojo.setTarjeta(tarjeta);
            datosFacade = new DatospagopeliculaFacade();
            datosSerieFacade = new DatospagoserieFacade();
            if (neededBean.getIdCompra() != 0) {
                datosFacade.create(datosPojo);
                neededBean.setIdCompra(0);
            }
            if (neededBean.getIdCompraSerie() != 0) {
                datosSerieFacade.create(datosPojo, compraSerieFacade.getCompra(neededBean.getIdCompraSerie()));
                neededBean.setIdCompraSerie(0);
            }
            System.out.println("------------------------Cree Datos");
            try {
                context.getExternalContext().redirect("/Videoclub/faces/view/Historial-Usuario.xhtml");
            } catch (Exception e) {
                System.out.println("Me voy al carajo, no funciona esta redireccion");
            }
        }
    }

    public void regresar() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("/Videoclub/faces/view/carrito.xhtml");
        } catch (Exception e) {
            System.out.println("Me voy al carajo, no funciona esta redireccion");
        }
    }

}

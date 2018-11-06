
package controlador;

import entidad.Comprapelicula;
import java.util.Date;

public class DatosPagoPeliculaPojo {
    
    private int idDatosPagoPelicula;
    private Comprapelicula idCompraPelicula;
    private String tarjeta;
    private Date fecha_caducidad;
    private String cvv;
    private String contraseña;
    
    
    public DatosPagoPeliculaPojo() {
        
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

    public Date getFecha_caducidad() {
        return fecha_caducidad;
    }

    public void setFecha_caducidad(Date fecha_caducidad) {
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
    
}

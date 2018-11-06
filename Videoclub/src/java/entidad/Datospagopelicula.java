/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "datospagopelicula")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Datospagopelicula.findAll", query = "SELECT d FROM Datospagopelicula d")
    , @NamedQuery(name = "Datospagopelicula.findByIdDatosPagoPelicula", query = "SELECT d FROM Datospagopelicula d WHERE d.idDatosPagoPelicula = :idDatosPagoPelicula")
    , @NamedQuery(name = "Datospagopelicula.findByTarjeta", query = "SELECT d FROM Datospagopelicula d WHERE d.tarjeta = :tarjeta")
    , @NamedQuery(name = "Datospagopelicula.findByFechaCaducidad", query = "SELECT d FROM Datospagopelicula d WHERE d.fechaCaducidad = :fechaCaducidad")
    , @NamedQuery(name = "Datospagopelicula.findByCvv", query = "SELECT d FROM Datospagopelicula d WHERE d.cvv = :cvv")
    , @NamedQuery(name = "Datospagopelicula.findByContrase\u00f1a", query = "SELECT d FROM Datospagopelicula d WHERE d.contrase\u00f1a = :contrase\u00f1a")})
public class Datospagopelicula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDatosPagoPelicula")
    private Integer idDatosPagoPelicula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "tarjeta")
    private String tarjeta;
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "cvv")
    private String cvv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "contrase\u00f1a")
    private String contraseña;
    @JoinColumn(name = "idCompraPelicula", referencedColumnName = "idCompra")
    @ManyToOne(optional = false)
    private Comprapelicula idCompraPelicula;

    public Datospagopelicula() {
    }

    public Datospagopelicula(Integer idDatosPagoPelicula) {
        this.idDatosPagoPelicula = idDatosPagoPelicula;
    }

    public Datospagopelicula(Integer idDatosPagoPelicula, String tarjeta, String cvv, String contraseña) {
        this.idDatosPagoPelicula = idDatosPagoPelicula;
        this.tarjeta = tarjeta;
        this.cvv = cvv;
        this.contraseña = contraseña;
    }

    public Integer getIdDatosPagoPelicula() {
        return idDatosPagoPelicula;
    }

    public void setIdDatosPagoPelicula(Integer idDatosPagoPelicula) {
        this.idDatosPagoPelicula = idDatosPagoPelicula;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
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

    public Comprapelicula getIdCompraPelicula() {
        return idCompraPelicula;
    }

    public void setIdCompraPelicula(Comprapelicula idCompraPelicula) {
        this.idCompraPelicula = idCompraPelicula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDatosPagoPelicula != null ? idDatosPagoPelicula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Datospagopelicula)) {
            return false;
        }
        Datospagopelicula other = (Datospagopelicula) object;
        if ((this.idDatosPagoPelicula == null && other.idDatosPagoPelicula != null) || (this.idDatosPagoPelicula != null && !this.idDatosPagoPelicula.equals(other.idDatosPagoPelicula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Datospagopelicula[ idDatosPagoPelicula=" + idDatosPagoPelicula + " ]";
    }
    
}

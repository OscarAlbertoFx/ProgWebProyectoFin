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
@Table(name = "datospagoserie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Datospagoserie.findAll", query = "SELECT d FROM Datospagoserie d")
    , @NamedQuery(name = "Datospagoserie.findByIdDatosPagoSerie", query = "SELECT d FROM Datospagoserie d WHERE d.idDatosPagoSerie = :idDatosPagoSerie")
    , @NamedQuery(name = "Datospagoserie.findByTarjeta", query = "SELECT d FROM Datospagoserie d WHERE d.tarjeta = :tarjeta")
    , @NamedQuery(name = "Datospagoserie.findByFechaCaducidad", query = "SELECT d FROM Datospagoserie d WHERE d.fechaCaducidad = :fechaCaducidad")
    , @NamedQuery(name = "Datospagoserie.findByContrase\u00f1a", query = "SELECT d FROM Datospagoserie d WHERE d.contrase\u00f1a = :contrase\u00f1a")
    , @NamedQuery(name = "Datospagoserie.findByCvv", query = "SELECT d FROM Datospagoserie d WHERE d.cvv = :cvv")})
public class Datospagoserie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDatosPagoSerie")
    private Integer idDatosPagoSerie;
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
    @Column(name = "contrase\u00f1a")
    private String contraseña;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "cvv")
    private String cvv;
    @JoinColumn(name = "idCompraSerie", referencedColumnName = "idCompraSerie")
    @ManyToOne(optional = false)
    private Compraserie idCompraSerie;

    public Datospagoserie() {
    }

    public Datospagoserie(Integer idDatosPagoSerie) {
        this.idDatosPagoSerie = idDatosPagoSerie;
    }

    public Datospagoserie(Integer idDatosPagoSerie, String tarjeta, String contraseña, String cvv) {
        this.idDatosPagoSerie = idDatosPagoSerie;
        this.tarjeta = tarjeta;
        this.contraseña = contraseña;
        this.cvv = cvv;
    }

    public Integer getIdDatosPagoSerie() {
        return idDatosPagoSerie;
    }

    public void setIdDatosPagoSerie(Integer idDatosPagoSerie) {
        this.idDatosPagoSerie = idDatosPagoSerie;
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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Compraserie getIdCompraSerie() {
        return idCompraSerie;
    }

    public void setIdCompraSerie(Compraserie idCompraSerie) {
        this.idCompraSerie = idCompraSerie;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDatosPagoSerie != null ? idDatosPagoSerie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Datospagoserie)) {
            return false;
        }
        Datospagoserie other = (Datospagoserie) object;
        if ((this.idDatosPagoSerie == null && other.idDatosPagoSerie != null) || (this.idDatosPagoSerie != null && !this.idDatosPagoSerie.equals(other.idDatosPagoSerie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Datospagoserie[ idDatosPagoSerie=" + idDatosPagoSerie + " ]";
    }
    
}

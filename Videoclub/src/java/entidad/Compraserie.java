/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "compraserie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Compraserie.findAll", query = "SELECT c FROM Compraserie c")
    , @NamedQuery(name = "Compraserie.findByIdCompraSerie", query = "SELECT c FROM Compraserie c WHERE c.idCompraSerie = :idCompraSerie")
    , @NamedQuery(name = "Compraserie.findByTotalCompra", query = "SELECT c FROM Compraserie c WHERE c.totalCompra = :totalCompra")
    , @NamedQuery(name = "Compraserie.findByFechaCompra", query = "SELECT c FROM Compraserie c WHERE c.fechaCompra = :fechaCompra")
    , @NamedQuery(name = "Compraserie.findByFechaEntrega", query = "SELECT c FROM Compraserie c WHERE c.fechaEntrega = :fechaEntrega")})
public class Compraserie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCompraSerie")
    private Integer idCompraSerie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_compra")
    private double totalCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_compra")
    @Temporal(TemporalType.DATE)
    private Date fechaCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompraSerie")
    private List<Datospagoserie> datospagoserieList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompraSerie")
    private List<Detallecompraserie> detallecompraserieList;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public Compraserie() {
    }

    public Compraserie(Integer idCompraSerie) {
        this.idCompraSerie = idCompraSerie;
    }

    public Compraserie(Integer idCompraSerie, double totalCompra, Date fechaCompra, Date fechaEntrega) {
        this.idCompraSerie = idCompraSerie;
        this.totalCompra = totalCompra;
        this.fechaCompra = fechaCompra;
        this.fechaEntrega = fechaEntrega;
    }

    public Integer getIdCompraSerie() {
        return idCompraSerie;
    }

    public void setIdCompraSerie(Integer idCompraSerie) {
        this.idCompraSerie = idCompraSerie;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    @XmlTransient
    public List<Datospagoserie> getDatospagoserieList() {
        return datospagoserieList;
    }

    public void setDatospagoserieList(List<Datospagoserie> datospagoserieList) {
        this.datospagoserieList = datospagoserieList;
    }

    @XmlTransient
    public List<Detallecompraserie> getDetallecompraserieList() {
        return detallecompraserieList;
    }

    public void setDetallecompraserieList(List<Detallecompraserie> detallecompraserieList) {
        this.detallecompraserieList = detallecompraserieList;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompraSerie != null ? idCompraSerie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compraserie)) {
            return false;
        }
        Compraserie other = (Compraserie) object;
        if ((this.idCompraSerie == null && other.idCompraSerie != null) || (this.idCompraSerie != null && !this.idCompraSerie.equals(other.idCompraSerie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Compraserie[ idCompraSerie=" + idCompraSerie + " ]";
    }
    
}

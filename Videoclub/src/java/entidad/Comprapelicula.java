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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "comprapelicula")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comprapelicula.findAll", query = "SELECT c FROM Comprapelicula c")
    , @NamedQuery(name = "Comprapelicula.findByIdCompra", query = "SELECT c FROM Comprapelicula c WHERE c.idCompra = :idCompra")
    , @NamedQuery(name = "Comprapelicula.findByTotalCompra", query = "SELECT c FROM Comprapelicula c WHERE c.totalCompra = :totalCompra")
    , @NamedQuery(name = "Comprapelicula.findByFechaCompra", query = "SELECT c FROM Comprapelicula c WHERE c.fechaCompra = :fechaCompra")
    , @NamedQuery(name = "Comprapelicula.findByFechaEntrega", query = "SELECT c FROM Comprapelicula c WHERE c.fechaEntrega = :fechaEntrega")})
public class Comprapelicula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCompra")
    private Integer idCompra;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_compra")
    private Double totalCompra;
    @Column(name = "fecha_compra")
    @Temporal(TemporalType.DATE)
    private Date fechaCompra;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompra")
    private List<Detallecomprapelicula> detallecomprapeliculaList;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompraPelicula")
    private List<Datospagopelicula> datospagopeliculaList;

    public Comprapelicula() {
    }

    public Comprapelicula(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(Double totalCompra) {
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
    public List<Detallecomprapelicula> getDetallecomprapeliculaList() {
        return detallecomprapeliculaList;
    }

    public void setDetallecomprapeliculaList(List<Detallecomprapelicula> detallecomprapeliculaList) {
        this.detallecomprapeliculaList = detallecomprapeliculaList;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @XmlTransient
    public List<Datospagopelicula> getDatospagopeliculaList() {
        return datospagopeliculaList;
    }

    public void setDatospagopeliculaList(List<Datospagopelicula> datospagopeliculaList) {
        this.datospagopeliculaList = datospagopeliculaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompra != null ? idCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comprapelicula)) {
            return false;
        }
        Comprapelicula other = (Comprapelicula) object;
        if ((this.idCompra == null && other.idCompra != null) || (this.idCompra != null && !this.idCompra.equals(other.idCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Comprapelicula[ idCompra=" + idCompra + " ]";
    }
    
}

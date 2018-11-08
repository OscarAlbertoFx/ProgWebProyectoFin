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
 * @author jair_
 */
@Entity
@Table(name = "rentapelicula")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Rentapelicula.findAll", query = "SELECT r FROM Rentapelicula r"),
    @NamedQuery(name = "Rentapelicula.findByIdRenta", query = "SELECT r FROM Rentapelicula r WHERE r.idRenta = :idRenta"),
    @NamedQuery(name = "Rentapelicula.findByFechaInicio", query = "SELECT r FROM Rentapelicula r WHERE r.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Rentapelicula.findByFechaTermino", query = "SELECT r FROM Rentapelicula r WHERE r.fechaTermino = :fechaTermino"),
    @NamedQuery(name = "Rentapelicula.findByTotalRenta", query = "SELECT r FROM Rentapelicula r WHERE r.totalRenta = :totalRenta")
})
public class Rentapelicula implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idRenta")
    private Integer idRenta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaInicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaTermino")
    @Temporal(TemporalType.DATE)
    private Date fechaTermino;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalRenta")
    private int totalRenta;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRenta")
    private List<Detallerenta> detallerentaList;

    public Rentapelicula()
    {
    }

    public Rentapelicula(Integer idRenta)
    {
        this.idRenta = idRenta;
    }

    public Rentapelicula(Integer idRenta, Date fechaInicio, Date fechaTermino, int totalRenta)
    {
        this.idRenta = idRenta;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.totalRenta = totalRenta;
    }

    public Integer getIdRenta()
    {
        return idRenta;
    }

    public void setIdRenta(Integer idRenta)
    {
        this.idRenta = idRenta;
    }

    public Date getFechaInicio()
    {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio)
    {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaTermino()
    {
        return fechaTermino;
    }

    public void setFechaTermino(Date fechaTermino)
    {
        this.fechaTermino = fechaTermino;
    }

    public int getTotalRenta()
    {
        return totalRenta;
    }

    public void setTotalRenta(int totalRenta)
    {
        this.totalRenta = totalRenta;
    }

    public Usuario getIdUsuario()
    {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario)
    {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (idRenta != null ? idRenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rentapelicula))
        {
            return false;
        }
        Rentapelicula other = (Rentapelicula) object;
        if ((this.idRenta == null && other.idRenta != null) || (this.idRenta != null && !this.idRenta.equals(other.idRenta)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entidad.Rentapelicula[ idRenta=" + idRenta + " ]";
    }
    
    @XmlTransient
    public List<Detallerenta> getDetallerentaList()
    {
        return detallerentaList;
    }
    
    public void setDetallerentaList(List<Detallerenta> detallerentaList)
    {
        this.detallerentaList = detallerentaList;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jair_
 */
@Entity
@Table(name = "detallerenta")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Detallerenta.findAll", query = "SELECT d FROM Detallerenta d"),
    @NamedQuery(name = "Detallerenta.findByIdDetalleRenta", query = "SELECT d FROM Detallerenta d WHERE d.idDetalleRenta = :idDetalleRenta"),
    @NamedQuery(name = "Detallerenta.findBySubtotal", query = "SELECT d FROM Detallerenta d WHERE d.subtotal = :subtotal")
})
public class Detallerenta implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalleRenta")
    private Integer idDetalleRenta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subtotal")
    private double subtotal;
    @JoinColumn(name = "idRenta", referencedColumnName = "idRenta")
    @ManyToOne(optional = false)
    private Rentapelicula idRenta;
    @JoinColumn(name = "idPelicula", referencedColumnName = "idPelicula")
    @ManyToOne(optional = false)
    private Pelicula idPelicula;

    public Detallerenta()
    {
    }

    public Detallerenta(Integer idDetalleRenta)
    {
        this.idDetalleRenta = idDetalleRenta;
    }

    public Detallerenta(Integer idDetalleRenta, double subtotal)
    {
        this.idDetalleRenta = idDetalleRenta;
        this.subtotal = subtotal;
    }

    public Integer getIdDetalleRenta()
    {
        return idDetalleRenta;
    }

    public void setIdDetalleRenta(Integer idDetalleRenta)
    {
        this.idDetalleRenta = idDetalleRenta;
    }

    public double getSubtotal()
    {
        return subtotal;
    }

    public void setSubtotal(double subtotal)
    {
        this.subtotal = subtotal;
    }

    public Rentapelicula getIdRenta()
    {
        return idRenta;
    }

    public void setIdRenta(Rentapelicula idRenta)
    {
        this.idRenta = idRenta;
    }

    public Pelicula getIdPelicula()
    {
        return idPelicula;
    }

    public void setIdPelicula(Pelicula idPelicula)
    {
        this.idPelicula = idPelicula;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (idDetalleRenta != null ? idDetalleRenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallerenta))
        {
            return false;
        }
        Detallerenta other = (Detallerenta) object;
        if ((this.idDetalleRenta == null && other.idDetalleRenta != null) || (this.idDetalleRenta != null && !this.idDetalleRenta.equals(other.idDetalleRenta)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entidad.Detallerenta[ idDetalleRenta=" + idDetalleRenta + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "pelicula")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pelicula.findAll", query = "SELECT p FROM Pelicula p")
    , @NamedQuery(name = "Pelicula.findByIdPelicula", query = "SELECT p FROM Pelicula p WHERE p.idPelicula = :idPelicula")
    , @NamedQuery(name = "Pelicula.findByTitulo", query = "SELECT p FROM Pelicula p WHERE p.titulo = :titulo")
    , @NamedQuery(name = "Pelicula.findBySinopsis", query = "SELECT p FROM Pelicula p WHERE p.sinopsis = :sinopsis")
    , @NamedQuery(name = "Pelicula.findByA\u00f1o", query = "SELECT p FROM Pelicula p WHERE p.a\u00f1o = :a\u00f1o")
    , @NamedQuery(name = "Pelicula.findByDuracion", query = "SELECT p FROM Pelicula p WHERE p.duracion = :duracion")
    , @NamedQuery(name = "Pelicula.findByClasificacion", query = "SELECT p FROM Pelicula p WHERE p.clasificacion = :clasificacion")
    , @NamedQuery(name = "Pelicula.findByPrecioCompra", query = "SELECT p FROM Pelicula p WHERE p.precioCompra = :precioCompra")
    , @NamedQuery(name = "Pelicula.findByPrecioRenta", query = "SELECT p FROM Pelicula p WHERE p.precioRenta = :precioRenta")
    , @NamedQuery(name = "Pelicula.findByCantidadAlmacen", query = "SELECT p FROM Pelicula p WHERE p.cantidadAlmacen = :cantidadAlmacen")
    , @NamedQuery(name = "Pelicula.findByCantidadRenta", query = "SELECT p FROM Pelicula p WHERE p.cantidadRenta = :cantidadRenta")
    , @NamedQuery(name = "Pelicula.findByDirector", query = "SELECT p FROM Pelicula p WHERE p.director = :director")
    , @NamedQuery(name = "Pelicula.findByRating", query = "SELECT p FROM Pelicula p WHERE p.rating = :rating")
    , @NamedQuery(name = "Pelicula.findByFechaAgregada", query = "SELECT p FROM Pelicula p WHERE p.fechaAgregada = :fechaAgregada")})
public class Pelicula implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPelicula")
    private Collection<Detallerenta> detallerentaCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPelicula")
    private Integer idPelicula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sinopsis")
    private String sinopsis;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "a\u00f1o")
    private String año;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "duracion")
    private String duracion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "clasificacion")
    private String clasificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_compra")
    private double precioCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_renta")
    private double precioRenta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_almacen")
    private int cantidadAlmacen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_renta")
    private int cantidadRenta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "director")
    private String director;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rating")
    private double rating;
    @Column(name = "fecha_agregada")
    @Temporal(TemporalType.DATE)
    private Date fechaAgregada;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPelicula")
    private List<Detallecomprapelicula> detallecomprapeliculaList;
    @JoinColumn(name = "idCategoria", referencedColumnName = "idCategoria")
    @ManyToOne(optional = false)
    private Categoria idCategoria;

    public Pelicula() {
    }

    public Pelicula(Integer idPelicula) {
        this.idPelicula = idPelicula;
    }

    public Pelicula(Integer idPelicula, String titulo, String sinopsis, String año, String duracion, String clasificacion, double precioCompra, double precioRenta, int cantidadAlmacen, int cantidadRenta, String director, double rating) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.año = año;
        this.duracion = duracion;
        this.clasificacion = clasificacion;
        this.precioCompra = precioCompra;
        this.precioRenta = precioRenta;
        this.cantidadAlmacen = cantidadAlmacen;
        this.cantidadRenta = cantidadRenta;
        this.director = director;
        this.rating = rating;
    }

    public Integer getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(Integer idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioRenta() {
        return precioRenta;
    }

    public void setPrecioRenta(double precioRenta) {
        this.precioRenta = precioRenta;
    }

    public int getCantidadAlmacen() {
        return cantidadAlmacen;
    }

    public void setCantidadAlmacen(int cantidadAlmacen) {
        this.cantidadAlmacen = cantidadAlmacen;
    }

    public int getCantidadRenta() {
        return cantidadRenta;
    }

    public void setCantidadRenta(int cantidadRenta) {
        this.cantidadRenta = cantidadRenta;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Date getFechaAgregada() {
        return fechaAgregada;
    }

    public void setFechaAgregada(Date fechaAgregada) {
        this.fechaAgregada = fechaAgregada;
    }

    @XmlTransient
    public List<Detallecomprapelicula> getDetallecomprapeliculaList() {
        return detallecomprapeliculaList;
    }

    public void setDetallecomprapeliculaList(List<Detallecomprapelicula> detallecomprapeliculaList) {
        this.detallecomprapeliculaList = detallecomprapeliculaList;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPelicula != null ? idPelicula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pelicula)) {
            return false;
        }
        Pelicula other = (Pelicula) object;
        if ((this.idPelicula == null && other.idPelicula != null) || (this.idPelicula != null && !this.idPelicula.equals(other.idPelicula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Pelicula[ idPelicula=" + idPelicula + " ]";
    }

    @XmlTransient
    public Collection<Detallerenta> getDetallerentaCollection()
    {
        return detallerentaCollection;
    }

    public void setDetallerentaCollection(Collection<Detallerenta> detallerentaCollection)
    {
        this.detallerentaCollection = detallerentaCollection;
    }
    
}

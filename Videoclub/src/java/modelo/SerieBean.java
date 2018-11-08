package modelo;

import entidad.Categoria;
import javax.inject.Named;
import controlador.SerieFacade;
import controlador.SeriePojo;
import entidad.Pelicula;
import entidad.Serie;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "serieBean")
@RequestScoped
public class SerieBean {

    private int idSerie;
    private Categoria idCategoria;
    private String titulo;
    private String sinopsis;
    private double precio;
    private double rating;
    private int numero_temporadas;
    private String categoria_nombre;

    private SerieFacade serieFacade;
    private SeriePojo seriePojo;
    private SerieFacade SerieFacade = new SerieFacade();
    private FacesContext fc = FacesContext.getCurrentInstance();
    private ExternalContext ec = fc.getExternalContext();
    private Serie serie = new Serie();

    public SerieBean() {

    }

    public int getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(int idSerie) {
        this.idSerie = idSerie;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumero_temporadas() {
        return numero_temporadas;
    }

    public void setNumero_temporadas(int numero_temporadas) {
        this.numero_temporadas = numero_temporadas;
    }

    public String getCategoria_nombre() {
        return categoria_nombre;
    }

    public void setCategoria_nombre(String categoria_nombre) {
        this.categoria_nombre = categoria_nombre;
    }

    public void buscar() {
        
       SeriePojo serieP= SerieFacade.buscarSerie(idSerie);
       if(serieP !=null){
           setTitulo(serieP.getTitulo());
           setSinopsis(serieP.getSinopsis());
           setRating(serieP.getRating());
           setPrecio(serieP.getPrecio());
           setNumero_temporadas(serieP.getNumero_temporadas());
           setIdCategoria(serieP.getIdCategoria());
           
       
       }
       else{
           System.out.println("No se ha hallado");}
  
    }
  
  
public void actualizar(){
if (titulo.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir el titiulo"));
        } else if (sinopsis.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir la sinopsis"));
        } else if (categoria_nombre.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir el nobre de la categria"));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            Serie serie = new Serie();
            serie.setTitulo(titulo);
            serie.setIdSerie(idSerie);
            serie.setSinopsis(sinopsis);
            serie.setPrecio(precio);
            serie.setRating(rating);
            serie.setNumeroTemporadas(numero_temporadas);
            SerieFacade.actualizar(serie);
            System.out.println("yeahhhhhhhhhhhhhhhhhhhhhh");
            context.addMessage("", new FacesMessage("Se registro correctamente"));
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Registro Existoso", "Advertencia"));
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("/Videoclub/faces/view/CrearSerie.xhtml");
            } catch (Exception e) {
                System.out.println("Me voy al carajo, no funciona esta redireccion");
            }
        }

}
    public void alta() {

        if (titulo.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir el titiulo"));
        } else if (sinopsis.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir la sinopsis"));
        } else if (categoria_nombre.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir el nobre de la categria"));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            Serie serie = new Serie();
            serie.setTitulo(titulo);
            serie.setIdSerie(idSerie);
            serie.setSinopsis(sinopsis);
            serie.setPrecio(precio);
            serie.setRating(rating);
            serie.setNumeroTemporadas(numero_temporadas);
            SerieFacade.crearSerie(serie);
            System.out.println("yeahhhhhhhhhhhhhhhhhhhhhh");
            context.addMessage("", new FacesMessage("Se registro correctamente"));
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Registro Existoso", "Advertencia"));
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("/Videoclub/faces/view/CrearSerie.xhtml");
            } catch (Exception e) {
                System.out.println("Me voy al carajo, no funciona esta redireccion");
            }
        }
    }
}

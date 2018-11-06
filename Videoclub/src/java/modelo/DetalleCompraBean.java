package modelo;

import controlador.ComprapeliculaFacade;
import controlador.DetallecomprapeliculaFacade;
import controlador.DetallecompraserieFacade;
import entidad.Comprapelicula;
import entidad.Detallecomprapelicula;
import entidad.Pelicula;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "detalleCompraBean")
@RequestScoped
public class DetalleCompraBean {

    private int idDetalleCompra;
    private Comprapelicula idCompra;
    private Pelicula idPelicula;
    private double subtotal;

    private List<Detallecomprapelicula> valores;

    private DetallecomprapeliculaFacade detalleFacade;
    private DetallecompraserieFacade detalleSerieFacade;
    private ComprapeliculaFacade compraFacade;
    private double total_pago;

    public DetalleCompraBean() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UserLog neededBean = (UserLog) facesContext.getApplication().createValueBinding("#{userLog}").getValue(facesContext);
        detalleFacade = new DetallecomprapeliculaFacade();
        detalleSerieFacade = new DetallecompraserieFacade();
        this.valores = (detalleFacade.filtrar(detalleFacade.getCompra(neededBean.getIdCompra())));
        compraFacade = new ComprapeliculaFacade();
        double aux = 0;
        try {
            aux = detalleSerieFacade.getCompra(neededBean.getIdCompraSerie()).getTotalCompra();
        } catch (NullPointerException e) {
            aux = 0;
        }
        double aux2 = 0;
        try {
            aux2 = detalleFacade.getCompra(neededBean.getIdCompra()).getTotalCompra();
        } catch (Exception e) {
            aux2 = 0;
        }
        this.total_pago = (double) Math.round((aux2 + aux) * 100d) / 100d;
    }

    public int getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(int idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Comprapelicula getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Comprapelicula idCompra) {
        this.idCompra = idCompra;
    }

    public Pelicula getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(Pelicula idPelicula) {
        this.idPelicula = idPelicula;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public List<Detallecomprapelicula> getValores() {
        return valores;
    }

    public void setValores(List<Detallecomprapelicula> valores) {
        this.valores = valores;
    }

    public double getTotal_pago() {
        return total_pago;
    }

    public void setTotal_pago(double total_pago) {
        this.total_pago = total_pago;
    }

}

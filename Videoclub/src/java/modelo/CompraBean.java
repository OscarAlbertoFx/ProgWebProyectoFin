package modelo;

import controlador.CompraPeliculaPojo;
import controlador.ComprapeliculaFacade;
import controlador.CompraserieFacade;
import controlador.DetalleCompraPojo;
import controlador.DetallecomprapeliculaFacade;
import controlador.DetallecompraserieFacade;
import entidad.Comprapelicula;
import entidad.Usuario;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@Named(value = "compraBean")
@RequestScoped
public class CompraBean {

    private int idCompra;
    private Usuario idUsuario;
    private double total_compra;
    private Date fecha_compra;
    private Date fecha_entrega;

    private ComprapeliculaFacade compraFacade;
    private DetallecomprapeliculaFacade detalleFacade;
    private CompraserieFacade compraSerieFacade;
    private DetallecompraserieFacade detalleSerieFacade;

    public CompraBean() {

    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getTotal_compra() {
        return total_compra;
    }

    public void setTotal_compra(double total_compra) {
        this.total_compra = total_compra;
    }

    public Date getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(Date fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public Date getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(Date fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public String crearCompra(String correo) {
        if (!correo.equals("")) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UserLog neededBean = (UserLog) facesContext.getApplication().createValueBinding("#{userLog}").getValue(facesContext);
            if (neededBean.getIdCompra() == 0) {
                Calendar fecha = Calendar.getInstance();
                Date now = fecha.getTime();
                fecha.add(Calendar.DAY_OF_YEAR, 30);
                Date agregado = fecha.getTime();
                FacesContext context = FacesContext.getCurrentInstance();
                CompraPeliculaPojo compra = new CompraPeliculaPojo();
                compra.setIdCompra(idCompra);
                compra.setIdUsuario(idUsuario);
                compra.setFecha_compra(now);
                compra.setFecha_entrega(agregado);
                compra.setTotal_compra(0);
                compraFacade = new ComprapeliculaFacade();
                Comprapelicula compraN = compraFacade.crearCompra(compra, correo);
                setIdCompra(compraN.getIdCompra());
                setIdUsuario(compraN.getIdUsuario());
                setTotal_compra(compraN.getTotalCompra());
                setFecha_compra(compraN.getFechaCompra());
                setFecha_entrega(compraN.getFechaEntrega());
                neededBean.setIdCompra(compraN.getIdCompra());
                System.out.println(neededBean.getCorreo());
            } else {
                setIdCompra(neededBean.getIdCompra());
            }
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String txtProperty = request.getParameter("myForm:movie");
            int idPeli = Integer.parseInt(txtProperty);
            agregarCarrito(idPeli);
            return "carrito";
        }
        return "Login";
    }

    public void agregarCarrito(int idPeli) {
        try {
            System.out.println("LA pelicula es-----------------" + idPeli);
            DetalleCompraPojo detalle = new DetalleCompraPojo();
            detalleFacade = new DetallecomprapeliculaFacade();
            detalle.setIdDetalleCompra(idCompra);
            detalle.setIdCompra(detalleFacade.getCompra(idCompra));
            detalle.setIdPelicula(detalleFacade.getPelicula(idPeli));
            detalle.setSubtotal(detalleFacade.getPelicula(idPeli).getPrecioCompra());
            detalleFacade.create(detalle);
            Comprapelicula editable = detalleFacade.getCompra(idCompra);
            editable.setTotalCompra((double) Math.round((editable.getTotalCompra() + detalle.getSubtotal()) * 100d) / 100d);
            System.out.println(editable.getTotalCompra() + "Total de compraXDXDXDXD");
            compraFacade = new ComprapeliculaFacade();
            compraFacade.update(editable);
        } catch (Exception ex) {
            Logger.getLogger(CompraBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String vaciar(String correo) throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UserLog neededBean = (UserLog) facesContext.getApplication().createValueBinding("#{userLog}").getValue(facesContext);
        if (neededBean.getIdCompra() == 0 && neededBean.getIdCompra() == 0) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No tienes nada en el carrito", "Advertencia"));
        } else {
            compraFacade = new ComprapeliculaFacade();
            detalleFacade = new DetallecomprapeliculaFacade();
            if (neededBean.getIdCompra() != 0) {
                detalleFacade.remove(detalleFacade.getCompra(neededBean.getIdCompra()));
                compraFacade.remove(neededBean.getIdCompra());
            }
            detalleSerieFacade = new DetallecompraserieFacade();
            if (neededBean.getIdCompraSerie() != 0) {
                detalleSerieFacade.remove(detalleSerieFacade.getCompra(neededBean.getIdCompraSerie()));
                compraSerieFacade = new CompraserieFacade();
                compraSerieFacade.remove(neededBean.getIdCompraSerie());
            }
            neededBean.setIdCompra(0);
            neededBean.setIdCompraSerie(0);
        }
        return "Home";
    }

    public void confirmar() {
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/Videoclub/faces/view/confirmar.xhtml");
        } catch (Exception e) {
            System.out.println("Me voy al carajo, no funciona esta redireccion");
        }
    }
}

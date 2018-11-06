/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador.UsuarioFacade;
import entidad.Usuario;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author tigre
 */
@Named(value = "bean")
@RequestScoped
public class UsuarioBean {

    private int idUsuario;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String calle;
    private String contraseña;
    private String correo;
    private String no_int;
    private String no_ext;
    private String cp;
    private String celular;
    private String telefono_fijo;

    private UsuarioFacade usuarioFacade = new UsuarioFacade();
    private FacesContext fc = FacesContext.getCurrentInstance();
    private ExternalContext ec = fc.getExternalContext();
    private Usuario usuario = new Usuario();

    public UsuarioBean() {

    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNo_int() {
        return no_int;
    }

    public void setNo_int(String no_int) {
        this.no_int = no_int;
    }

    public String getNo_ext() {
        return no_ext;
    }

    public void setNo_ext(String no_ext) {
        this.no_ext = no_ext;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefono_fijo() {
        return telefono_fijo;
    }

    public void setTelefono_fijo(String telefono_fijo) {
        this.telefono_fijo = telefono_fijo;
    }

    public void alta() {

        if (nombre.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir tu nombre"));
        } else if (contraseña.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir tu contraseña"));
        } else if (apellidoP.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir tu ap"));
        } else if (apellidoM.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir tu am"));
        } else if (correo.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir tu correo"));
        } else if (calle.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir tu pass"));
        } else if (no_int.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir tu numero interior"));
        } else if (no_ext.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir tu numero exteior"));
        } else if (cp.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir tu codigo postal"));
        } else if (celular.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir tu celular"));
        } else if (telefono_fijo.equals("")) {
            fc.addMessage("", new FacesMessage("Te falta escribir tu telefono fijo"));
        } else {
            Usuario user = new Usuario();
            FacesContext context = FacesContext.getCurrentInstance();
            user.setCp(cp);
            user.setContraseña(contraseña);
            user.setApellidoM(apellidoM);
            user.setApellidoP(apellidoP);
            user.setCalle(calle);
            user.setCelular(celular);
            user.setIdUsuario(idUsuario);
            user.setNoExt(no_ext);
            user.setNoInt(no_int);
            user.setCorreo(correo);
            user.setIdUsuario(idUsuario);
            user.setNombre(nombre);
            user.setTelefonoFijo(telefono_fijo);
            usuarioFacade.crearUsuario(user);      
            System.out.println("yeahhhhhhhhhhhhhhhhhhhhhh");
            context.addMessage("", new FacesMessage("Se registro correctamente"));
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Registro Existoso", "Advertencia"));
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("/Videoclub/faces/view/Login.xhtml");
            } catch (Exception e) {
                System.out.println("Me voy al carajo, no funciona esta redireccion");
            }
        }
    }

    public void editar(String correo) {
        Usuario usuario = usuarioFacade.buscarPorcorreo(correo);
        FacesContext context = FacesContext.getCurrentInstance();
        usuario.setCalle(calle);
        usuario.setCelular(celular);
        usuario.setContraseña(contraseña);
        usuario.setCp(cp);
        usuario.setNoExt(no_ext);
        usuario.setNoInt(no_int);
        usuario.setTelefonoFijo(telefono_fijo);
        usuarioFacade.editarUsuario(usuario);
        System.out.println("aqui esta modificandoxD");
        context.addMessage("", new FacesMessage("Se edito correctamente"));
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Registro Existoso", "Advertencia"));
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/Videoclub/faces/view/Home.xhtml");
        } catch (Exception e) {
            System.out.println("Me voy al carajo, no funciona esta redireccion");
        }

    }

    public String buscaBycorreo() {
        Usuario usuarios;
        usuarios = usuarioFacade.buscarPorcorreo(correo);
        if (usuarios != null) {
            this.idUsuario = usuarios.getIdUsuario();
            this.nombre = usuarios.getNombre();
            this.contraseña = usuarios.getContraseña();
            this.apellidoP = usuarios.getApellidoP();
            this.apellidoM = usuarios.getApellidoM();
            this.correo = usuarios.getCorreo();
            this.calle = usuarios.getCalle();
            this.no_int = usuarios.getNoInt();
            this.no_ext = usuarios.getNoExt();
            this.cp = usuarios.getCp();
            this.celular = usuarios.getCelular();
            this.telefono_fijo = usuarios.getTelefonoFijo();
            System.out.println("Aqui esta" + correo);
            return "Resultado";

        }
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage("", new FacesMessage("No existe un compa con ese correo"));
        return null;
    }

    public void submit() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (correo.isEmpty() || contraseña.isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Los datos no son validos", "Error"));
        } else {
            usuarioFacade = new UsuarioFacade();
            if (usuarioFacade.buscarUsuario(correo, contraseña)) {
                try {
                    FacesContext contex = FacesContext.getCurrentInstance();
                    contex.getExternalContext().redirect("/Videoclub/faces/view/Home.xhtml");
                } catch (Exception e) {
                    System.out.println("Me voy al carajo, no funciona esta redireccion");
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario no hallado", "Advertencia"));
            }
        }
    }

}

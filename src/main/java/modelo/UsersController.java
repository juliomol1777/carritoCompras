package modelo;

import datos.dao.ClienteFacadeLocal;
import datos.entidades.Cliente;
import datos.entidades.Users;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import datos.dao.UsersFacadeLocal;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.util.JsfUtil;

/**
 *
 * @author usuario
 */
@Named("usersController")
@SessionScoped
public class UsersController implements Serializable {

    @Inject
    private UsersFacadeLocal ejbFacade;
    @Inject
    private ClienteFacadeLocal clienteFacade;
    private Users selected;
    private List<Users> items = null;
    private Cliente cliente;
    private String usuario;
    private String password;
    private String nombres;
    private String email;
    private String dni;
    private String direccion;

    public UsersController() {
        }
    
    
    
//    public String doLogin() {
//        selected = ejbFacade.buscarUsuario(usuario, password);
//        String redireccion = null;
//        if (selected != null) {
//            cliente = clienteFacade.find(selected.getUid());
//            redireccion = "compras";
//        } else {
//            setUsuario("");
//            setPassword("");
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Login incorrecto"));
//            redireccion = "login";
//        }
//        return redireccion;
//    }
//
//    public String doDesLogin() {
//        setNombres("");
//        setPassword("");
//        cliente = null;
//        return "carrito";
//    }

    public String doRegistro() {
        cliente = new Cliente(dni, nombres, direccion, password);
        cliente.setEmail(email);
        boolean valido = validarEmail();
        String redireccion = null;
        try {
            if (valido) {
                clienteFacade.create(cliente);
                int uid = cliente.getIdCliente();
                usuario = cliente.getEmail();
                selected = new Users(uid, usuario, password);
                ejbFacade.create(selected);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Registro exitoso"));
                usuario = "";
                password = "";
                redireccion = "login";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Registro incorrecto"));
                redireccion = "registro";
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", e.getMessage()));
            redireccion = "registro";
        }
        return redireccion;
    }

    private boolean validarEmail() {
        boolean valido = false;
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            valido = true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Email no valido"));
        }
        return valido;
    }
    
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UsersFacadeLocal getFacade() {
        return ejbFacade;
    }

    public Users prepareCreate() {
        selected = new Users();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(JsfUtil.PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UsersCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsersUpdated"));
    }

    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UsersDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Users> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Users getUsers(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Users> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Users> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Users.class)
    public static class UsersControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsersController controller = (UsersController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usersController");
            return controller.getUsers(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Users) {
                Users o = (Users) object;
                return getStringKey(o.getUid());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Users.class.getName()});
                return null;
            }
        }

    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Users getSelected() {
        return selected;
    }

    public void setSelected(Users selected) {
        this.selected = selected;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    
}

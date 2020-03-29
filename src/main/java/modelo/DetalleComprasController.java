package modelo;

import datos.entidades.DetalleCompras;
import modelo.util.JsfUtil;
import modelo.util.JsfUtil.PersistAction;
import datos.dao.DetalleComprasFacadeLocal;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("detalleComprasController")
@SessionScoped
public class DetalleComprasController implements Serializable {

    @Inject
    private DetalleComprasFacadeLocal ejbFacade;
    private List<DetalleCompras> items = null;
    private DetalleCompras selected;

    public DetalleComprasController() {
    }

    public DetalleCompras getSelected() {
        return selected;
    }

    public void setSelected(DetalleCompras selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getDetalleComprasPK().setIdProducto(selected.getProducto().getIdProducto());
        selected.getDetalleComprasPK().setIdCompras(selected.getCompras().getIdCompras());
    }

    protected void initializeEmbeddableKey() {
        selected.setDetalleComprasPK(new datos.entidades.DetalleComprasPK());
    }

    private DetalleComprasFacadeLocal getFacade() {
        return ejbFacade;
    }

    public DetalleCompras prepareCreate() {
        selected = new DetalleCompras();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DetalleComprasCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DetalleComprasUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DetalleComprasDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<DetalleCompras> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
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

    public DetalleCompras getDetalleCompras(datos.entidades.DetalleComprasPK id) {
        return getFacade().find(id);
    }

    public List<DetalleCompras> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<DetalleCompras> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = DetalleCompras.class)
    public static class DetalleComprasControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DetalleComprasController controller = (DetalleComprasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "detalleComprasController");
            return controller.getDetalleCompras(getKey(value));
        }

        datos.entidades.DetalleComprasPK getKey(String value) {
            datos.entidades.DetalleComprasPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new datos.entidades.DetalleComprasPK();
            key.setIdDetalle(Integer.parseInt(values[0]));
            key.setIdProducto(Integer.parseInt(values[1]));
            key.setIdCompras(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(datos.entidades.DetalleComprasPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdDetalle());
            sb.append(SEPARATOR);
            sb.append(value.getIdProducto());
            sb.append(SEPARATOR);
            sb.append(value.getIdCompras());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof DetalleCompras) {
                DetalleCompras o = (DetalleCompras) object;
                return getStringKey(o.getDetalleComprasPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DetalleCompras.class.getName()});
                return null;
            }
        }

    }

}

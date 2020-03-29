package modelo;

import datos.entidades.Producto;
import carritoCompras.util.JsfUtil;
import carritoCompras.util.JsfUtil.PersistAction;
import datos.dao.ProductoFacadeLocal;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("productoController")
@SessionScoped
public class ProductoController implements Serializable {

    @Inject
    private ProductoFacadeLocal ejbFacade;
    private List<Producto> items = null;
    private Producto selected;
    private UploadedFile file;
    private String imagenProducto;

    public ProductoController() {
    }

    public String getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(String imagenProducto) {
        this.imagenProducto = imagenProducto;
    }
    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public Producto getSelected() {
        return selected;
    }

    public void setSelected(Producto selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProductoFacadeLocal getFacade() {
        return ejbFacade;
    }

    public Producto prepareCreate() {
        selected = new Producto();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProductoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProductoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProductoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Producto> getItems() {
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

    public Producto getProducto(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Producto> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Producto> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Producto.class)
    public static class ProductoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductoController controller = (ProductoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productoController");
            return controller.getProducto(getKey(value));
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
            if (object instanceof Producto) {
                Producto o = (Producto) object;
                return getStringKey(o.getIdProducto());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Producto.class.getName()});
                return null;
            }
        }

    }
    
    /**
    * Copia una imagen en una carpeta path: C:\Program Files\glassfish5\glassfish\domains\domain5\var\webapp\ upload .. 
    * que esta en el servidor. se agrega una entrada property al archivo glassfish-web, en la base de datos se guarda 
    * el nombre de la foto.. foto.jpg, y en el xhtml se agrega value="/upload/#{prod.foto}" para renderizar la foto.
    * @author usuario
    */
    
    public void handleFileUpload(FileUploadEvent event){
        FacesContext facesContext= FacesContext.getCurrentInstance();
        ExternalContext  externalContext= facesContext.getExternalContext();
        HttpServletResponse response= (HttpServletResponse) externalContext.getResponse();
        System.out.println("path: "+ externalContext.getRealPath("/upload/"));
        System.out.println("file solo: "+ event.getFile().getFileName());
        
        File result= new File(externalContext.getRealPath("/upload/")+ File.separator+ event.getFile().getFileName());
        System.out.println("Final file: "+ result.getName());
        String ruta= result.getPath();
        System.out.println(ruta);
        String nombreImagen=result.getName();
        selected.setFoto(nombreImagen);
        try {
            FileOutputStream fileOutputStream= new FileOutputStream(result);
            InputStream inputStream= event.getFile().getInputstream();
            byte[] buffer= new byte[1024];
            int length=0;
            while((length =inputStream.read(buffer))!=-1){
                fileOutputStream.write(buffer,0,length);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            FacesMessage message= new FacesMessage("Exito "+event.getFile().getFileName()+ " fue subido");
            FacesContext.getCurrentInstance().addMessage(null, message); 
        } catch (Exception e) {
            FacesMessage message= new FacesMessage("Error");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}

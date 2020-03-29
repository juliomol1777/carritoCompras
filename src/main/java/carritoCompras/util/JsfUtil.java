package carritoCompras.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

public class JsfUtil {
    

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static String getPath() {
        try {
            ServletContext ctx= (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            return ctx.getRealPath("/");
        } catch (Exception e) {
            addErrorMessage("getPath() " + e.getLocalizedMessage());
        }
        return null;
    }
    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }
    public static enum PersistAction {

        CREATE,
        DELETE,
        UPDATE
    }
    public static String guardarBlobEnArchivoTemporal( byte[] bytes, String nombreArchivo){
        String ubicacionImagen=null;
        ServletContext ctx= (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path= ctx.getRealPath("")+ File.separatorChar+ "resources"+ File.separatorChar+ "img"+ File.separatorChar+ 
                "tmp"+ File.separatorChar+ nombreArchivo;
        File f=null;
        InputStream in=null;
        try {
            f= new File(path);
            in= new ByteArrayInputStream(bytes);
            FileOutputStream out= new FileOutputStream(f.getAbsolutePath());
            int c=0;
            while((c= in.read())>=0){
                out.write(c);
            }
            out.flush();
            out.close();
            ubicacionImagen= "../../resources/img/tmp" + nombreArchivo;
        } catch (Exception e) {
            System.err.print("No se pudo cargar la imagen");
        }
        return ubicacionImagen;
    }
}

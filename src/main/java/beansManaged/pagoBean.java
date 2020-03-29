/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansManaged;

import datos.dao.ClienteFacadeLocal;
import datos.dao.ComprasFacadeLocal;
import datos.dao.DetalleComprasFacadeLocal;
import datos.dao.PagoFacadeLocal;
import datos.dao.ProductoFacadeLocal;
import datos.dao.UsersFacadeLocal;
import datos.entidades.Carrito;
import datos.entidades.Cliente;
import datos.entidades.Compras;
import datos.entidades.DetalleCompras;
import datos.entidades.Pago;
import datos.entidades.Producto;
import datos.entidades.Users;
import datos.entidades.UsuarioGrupo;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author usuario
 */
@Named(value = "pagoBean")
@SessionScoped
public class pagoBean implements Serializable {

    @Inject
    private carritoBean carritoB;
    @Inject
    private comprasBean comprasBean;
    @Inject
    private ClienteFacadeLocal clienteFacade;
    @Inject
    private UsersFacadeLocal userFacade;
    @Inject
    private ComprasFacadeLocal comprasB;
    @Inject
    private PagoFacadeLocal ejbFacade;
    @Inject
    private DetalleComprasFacadeLocal detalleComprasFacade;
    @Inject
    private ProductoFacadeLocal prodFacade;
    private Pago selected;
    private Compras compra;
    private Cliente cliente;
    private Producto producto;
    private Users user;
    private UsuarioGrupo usuarioG;
    private DetalleCompras detalleCompra;
    private int idDetalle;
    private List<Carrito> listaCompra;
    private boolean pagoExitoso= false;
    private int pagoAcreditado=-1;
    private String usuario;
    private String password;
    private double monto;
    
    public pagoBean() {
    }
    
    @PostConstruct
    public void init() {
        listaCompra = carritoB.getListaProdElegidos();
    }
    
    public String doLogin() {
        user = userFacade.buscarUsuario(usuario, password);
        String redireccion = null;
        if (user != null) {
            if(userFacade.rolesAutorizados(user.getUid())){
                redireccion = "/CrudCarrito/index.xhtml";
            }else{
                cliente = clienteFacade.find(user.getUid());
                redireccion = "compras";
            } 
        }
        else {
            setUsuario("");
            setPassword("");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Login incorrecto"));
            redireccion = "login";
        }
        return redireccion;
    }
    
    public String logoutAdmi() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/../compras.xhtml?faces-redirect=true";
    }

    public String doDesLogin() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        setUsuario("");
        setPassword("");
        cliente = null;
        return "/compras.xhtml?faces-redirect=true";
    }
    
    public void preparePago() {
        monto = carritoB.precioTotalCompra();
        
        if (cliente!=null && monto>0 && pagoAcreditado<0) {
            try {
                selected = new Pago();
                selected.setMonto(monto);
                ejbFacade.create(selected);
                for (Carrito car : listaCompra) {
                    producto = car.getProducto();
                    int stock = producto.getStock();
                    producto.setStock(stock - 1);
                    prodFacade.edit(producto);
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", e.getMessage()));
            }
            try {
                compra = new Compras();
                compra.setIdCliente(cliente);
                compra.setIdPago(selected);
                compra.setEstado("cancelado");
                compra.setFechaCompras(fecha());
                compra.setMonto(selected.getMonto());
                comprasB.create(compra);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Transaccion exitosa"));
            
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Error al realizar la Transaccion"));
            }
           pagoExitoso = true; 
           pagoAcreditado++;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Debe iniciar sesion y agregar Productos al carrito"));
        }
    }
    
    public String comprar() {
        if (pagoExitoso) {
            try {
                detalleCompra = new DetalleCompras();
                detalleCompra.setDetalleComprasPK(new datos.entidades.DetalleComprasPK());
                    
                for (Carrito detalle : listaCompra) {
                    detalleCompra.getDetalleComprasPK().setIdProducto(detalle.getProducto().getIdProducto());
                    detalleCompra.getDetalleComprasPK().setIdCompras(compra.getIdCompras());
                    detalleCompra.setPrecioCompra(detalle.getSubtotal());
                    detalleCompra.setCantidad(detalle.getCantidaProductos());
                    detalleComprasFacade.create(detalleCompra);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Compra exitosa"));
                pagoExitoso=false;
                pagoAcreditado=-1;
                comprasBean.getListaProdElegidos().clear();
                comprasBean.setNumeroProdElegidos(0);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", e.getMessage()));
            }
            return "resumenCompra";
        } 
        else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Debe iniciar sesion y/o agregar Productos al carrito"));
        return null;
        }   
    }
    
    public String fecha() {
        String fecha;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        fecha = sdf.format(calendar.getTime());
        return fecha;
    }

    public Pago getSelected() {
        return selected;
    }

    public void setSelected(Pago selected) {
        this.selected = selected;
    }

    public Compras getCompra() {
        return compra;
    }

    public void setCompra(Compras compra) {
        this.compra = compra;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Carrito> getListaCompra() {
        return listaCompra;
    }

    public void setListaCompra(List<Carrito> listaCompra) {
        this.listaCompra = listaCompra;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }
    
}

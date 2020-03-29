/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansManaged;

import datos.entidades.Carrito;
import datos.entidades.Producto;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import modelo.ProductoController;

/**
 *
 * @author usuario
 */
@Named("comprasBean")
@SessionScoped
public class comprasBean implements Serializable {

    @Inject
    private ProductoController productoFacade;
    private Producto producto;
    private List<Producto> lista;
    private Carrito carrito;
    private List<Carrito> listaProdElegidos= new ArrayList<>();
    private int numeroProdElegidos = 0;
    private int cantidadIndividual;

    public comprasBean() {
    }

    @PostConstruct
    public void init() {
        lista = productoFacade.getItems();
    }
    
    public List<Carrito> prepareCarrito(){
        if(listaProdElegidos==null){
            listaProdElegidos = new ArrayList<>();
        }
        return listaProdElegidos;
    }

    public void actionListenerAgregarProductos(Producto p) {        
        int posicion = -1;
        int stock = p.getStock();
        //prepareCarrito();
        if (stock>0 && numeroProdElegidos< stock ) {
            numeroProdElegidos += 1;
            for (Carrito car : listaProdElegidos) {
                if (car.getProducto().getIdProducto().equals(p.getIdProducto())) {
                    posicion = listaProdElegidos.indexOf(car);
                    cantidadIndividual = car.getCantidaProductos() + 1;
                }
            }
            if (posicion >= 0 ) {
                listaProdElegidos.get(posicion).setCantidaProductos(cantidadIndividual);
                listaProdElegidos.get(posicion).setSubtotal(cantidadIndividual * p.getPrecio());

            } else {
                carrito = new Carrito();
                cantidadIndividual = 1;
                carrito.setProducto(p);
                carrito.setCantidaProductos(cantidadIndividual);
                double precio = cantidadIndividual * p.getPrecio();
                carrito.setSubtotal(precio);
                listaProdElegidos.add(carrito);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Producto sin stock"));
        }
    }
    
    public String irCarrito(){
        String redireccion=null;
        if(numeroProdElegidos>0){
            redireccion= "/carrito.xhtml?faces-redirect=true";
        }
        else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "aviso", "Debe agregar Productos al carrito"));
        }
        return redireccion;
    }
    
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<Producto> getLista() {
        return lista;
    }

    public void setLista(List<Producto> lista) {
        this.lista = lista;
    }

    public List<Carrito> getListaProdElegidos() {
        return listaProdElegidos;
    }

    public void setListaProdElegidos(List<Carrito> listaProdElegidos) {
        this.listaProdElegidos = listaProdElegidos;
    }

    public int getNumeroProdElegidos() {
        return numeroProdElegidos;
    }

    public void setNumeroProdElegidos(int numeroProdElegidos) {
        this.numeroProdElegidos = numeroProdElegidos;
    }

    public int getCantidadIndividual() {
        return cantidadIndividual;
    }

    public void setCantidadIndividual(int cantidadIndividual) {
        this.cantidadIndividual = cantidadIndividual;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

}

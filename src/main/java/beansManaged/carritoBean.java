package beansManaged;

import datos.entidades.Carrito;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Named("carritoBean")
@SessionScoped
public class carritoBean implements Serializable {

    @Inject
    private comprasBean comprasBeans;
    private List<Carrito> listaProdElegidos;
    private Carrito carrito;
    private double subtotal;
    private int numeroProdElegidos;

    public carritoBean() {
    }
    
    @PostConstruct
    public void init() {
        listaProdElegidos = comprasBeans.getListaProdElegidos();
    }

    public List<Carrito> getListaProdElegidos() {
        return listaProdElegidos;
    }

    public void setListaProdElegidos(List<Carrito> listaProdElegidos) {
        this.listaProdElegidos = listaProdElegidos;
    }

    public double precioTotalCompra() {
        subtotal = 0;
        for (Carrito car : listaProdElegidos) {
            subtotal += car.getSubtotal();
        }
        return subtotal;
    }

    public void eliminarProductoCarrito(Carrito carrito) {
        int id = -1;
        for (Carrito car : listaProdElegidos) {
            if (car.getProducto().getIdProducto().equals(carrito.getProducto().getIdProducto())) {
                id = listaProdElegidos.indexOf(car);
            }
        }
        int num = carrito.getCantidaProductos();
        numeroProdElegidos = comprasBeans.getNumeroProdElegidos();
        comprasBeans.setNumeroProdElegidos(numeroProdElegidos - 1);
        if (num > 1) {
            carrito.setCantidaProductos(num - 1);
            
            listaProdElegidos.get(id).setSubtotal((num - 1) * carrito.getProducto().getPrecio());
        } else {
            listaProdElegidos.remove(id);
            
        }

    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getNumeroProdElegidos() {
        return numeroProdElegidos;
    }

    public void setNumeroProdElegidos(int numeroProdElegidos) {
        this.numeroProdElegidos = numeroProdElegidos;
    }
    
}

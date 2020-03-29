
package datos.entidades;

import java.io.Serializable;

/**
 *
 * @author usuario
 */
public class Carrito implements Serializable{
    private static final long serialVersionUID = 1L;
    private int cantidaProductos;
    private double subtotal;
    private Producto producto;

    public Carrito() {
    }
    
    public Carrito(Producto producto, int cantidaProductos, double subtotal) {
        this.producto = producto;
        this.cantidaProductos = cantidaProductos;
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidaProductos() {
        return cantidaProductos;
    }

    public void setCantidaProductos(int cantidaProductos) {
        this.cantidaProductos = cantidaProductos;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
}

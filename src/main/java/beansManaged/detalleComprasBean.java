/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansManaged;

import datos.dao.DetalleComprasFacadeLocal;
import datos.entidades.Carrito;
import datos.entidades.Cliente;
import datos.entidades.Compras;
import datos.entidades.DetalleCompras;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import modelo.ClienteController;
import modelo.ComprasController;
import modelo.DetalleComprasController;
import modelo.PagoController;

/**
 *
 * @author usuario
 */
@Named(value = "detalleComprasBean")
@SessionScoped
public class detalleComprasBean implements Serializable {

    @Inject
    DetalleComprasController detalleComprasB;
    @Inject
    DetalleComprasFacadeLocal detalleComprasF;
    @Inject
    pagoBean pagoB;
    @Inject
    PagoController pagoCont;
    @Inject
    ClienteController clienteCont;
    @Inject
    ComprasController comprasCont;
    List<Carrito> lista;
    private DetalleCompras selected;
    private String nombre;
    private String direccion;
    private String nombreP;
    private double precioP;
    private int cantidadP;
    private int cantidad;
    private double monto;
    private String fecha;
    private int idDetalle;
    private Cliente cliente;
    private Compras compra;
    
    
    public detalleComprasBean() {
        
    }
    
    @PostConstruct
    public void init(){
       
    }
    
//    public List<Carrito> mostrarProductos(){
//        int id= pagoB.getIdDetalle();
//        selected= detalleComprasF.find(id);
//        for(Carrito car: lista){
//            car.setProducto(selected.getProducto());
//        }
//        return car;
//    }
            
    
    public Cliente mostrarDatosCliente(){ 
        cliente= new Cliente();
        int idCliente= pagoB.getCliente().getIdCliente();
        cliente.setNombres(clienteCont.getCliente(idCliente).getNombres());
        cliente.setDireccion(clienteCont.getCliente(idCliente).getDireccion());
        return cliente;
    }
    
    public Compras mostrarDatosCompra(){ 
        compra= new Compras();
        int idCompra= pagoB.getCompra().getIdCompras();
        compra.setMonto(comprasCont.getCompras(idCompra).getMonto());
        compra.setFechaCompras(comprasCont.getCompras(idCompra).getFechaCompras());
        return compra;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Compras getCompra() {
        return compra;
    }

    public void setCompra(Compras compra) {
        this.compra = compra;
    }

    public List<Carrito> getLista() {
        return lista;
    }

    public void setLista(List<Carrito> lista) {
        this.lista = lista;
    }
    
    
    
}

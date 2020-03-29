/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "detalle_compras", catalog = "bdcarritocompras", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleCompras.findAll", query = "SELECT d FROM DetalleCompras d"),
    @NamedQuery(name = "DetalleCompras.findByIdDetalle", query = "SELECT d FROM DetalleCompras d WHERE d.detalleComprasPK.idDetalle = :idDetalle"),
    @NamedQuery(name = "DetalleCompras.findByIdProducto", query = "SELECT d FROM DetalleCompras d WHERE d.detalleComprasPK.idProducto = :idProducto"),
    @NamedQuery(name = "DetalleCompras.findByIdCompras", query = "SELECT d FROM DetalleCompras d WHERE d.detalleComprasPK.idCompras = :idCompras"),
    @NamedQuery(name = "DetalleCompras.findByCantidad", query = "SELECT d FROM DetalleCompras d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "DetalleCompras.findByPrecioCompra", query = "SELECT d FROM DetalleCompras d WHERE d.precioCompra = :precioCompra")})
public class DetalleCompras implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetalleComprasPK detalleComprasPK;
    @Column(name = "Cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PrecioCompra", precision = 22)
    private Double precioCompra;
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;
    @JoinColumn(name = "idCompras", referencedColumnName = "idCompras", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Compras compras;

    public DetalleCompras() {
    }

    public DetalleCompras(DetalleComprasPK detalleComprasPK, Integer cantidad, Double precioCompra) {
        this.detalleComprasPK = detalleComprasPK;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
    }
    
    
    
    public DetalleCompras(DetalleComprasPK detalleComprasPK) {
        this.detalleComprasPK = detalleComprasPK;
    }

    public DetalleCompras( int idProducto, int idCompras) {
        this.detalleComprasPK = new DetalleComprasPK(idProducto, idCompras);
    }

    public DetalleComprasPK getDetalleComprasPK() {
        return detalleComprasPK;
    }

    public void setDetalleComprasPK(DetalleComprasPK detalleComprasPK) {
        this.detalleComprasPK = detalleComprasPK;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Compras getCompras() {
        return compras;
    }

    public void setCompras(Compras compras) {
        this.compras = compras;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleComprasPK != null ? detalleComprasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleCompras)) {
            return false;
        }
        DetalleCompras other = (DetalleCompras) object;
        if ((this.detalleComprasPK == null && other.detalleComprasPK != null) || (this.detalleComprasPK != null && !this.detalleComprasPK.equals(other.detalleComprasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "datos.entidades.DetalleCompras[ detalleComprasPK=" + detalleComprasPK + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author usuario
 */
@Embeddable
public class DetalleComprasPK implements Serializable {
    
    @Basic(optional = false)
    @Column(name = "idDetalle", nullable = false)
    private int idDetalle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idProducto", nullable = false)
    private int idProducto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCompras", nullable = false)
    private int idCompras;

    public DetalleComprasPK() {
    }

    public DetalleComprasPK(int idProducto, int idCompras) {
        this.idProducto = idProducto;
        this.idCompras = idCompras;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(int idCompras) {
        this.idCompras = idCompras;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idDetalle;
        hash += (int) idProducto;
        hash += (int) idCompras;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleComprasPK)) {
            return false;
        }
        DetalleComprasPK other = (DetalleComprasPK) object;
        if (this.idDetalle != other.idDetalle) {
            return false;
        }
        if (this.idProducto != other.idProducto) {
            return false;
        }
        if (this.idCompras != other.idCompras) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "datos.entidades.DetalleComprasPK[ idDetalle=" + idDetalle + ", idProducto=" + idProducto + ", idCompras=" + idCompras + " ]";
    }
    
}

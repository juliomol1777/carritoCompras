/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.dao;

import datos.entidades.DetalleCompras;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author usuario
 */
@Stateless
public class DetalleComprasFacade extends AbstractFacade<DetalleCompras> implements DetalleComprasFacadeLocal {

    @PersistenceContext(unitName = "com.codeM_carritoCompra_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleComprasFacade() {
        super(DetalleCompras.class);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.dao;

import datos.entidades.Users;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author usuario
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> implements UsersFacadeLocal {

    @PersistenceContext(unitName = "com.codeM_carritoCompra_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }

    @Override
    public Users buscarUsuario(String usuario, String password) {
        Users usu = null;
        Query query = em.createQuery("select u from Users u where u.usuario=:usuario AND u.password=:password");
        query.setParameter("usuario", usuario);
        query.setParameter("password", password);
        boolean hayUsuario = query.getResultList().isEmpty();
        if(!hayUsuario){
            usu = (Users) query.getSingleResult();
        }
        return usu;
    }
    
    @Override
    public boolean rolesAutorizados(int ugId){
        Query query = em.createQuery("SELECT u FROM UsuarioGrupo u WHERE u.ugId = :ugId");
        query.setParameter("ugId", ugId);
        boolean hayUsuario = query.getResultList().isEmpty();
        if(!hayUsuario){
            return true;
        }
        return false;
    }
}

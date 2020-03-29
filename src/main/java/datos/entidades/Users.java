/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "users", catalog = "bdcarritocompras", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUid", query = "SELECT u FROM Users u WHERE u.uid = :uid"),
    @NamedQuery(name = "Users.findByUsuario", query = "SELECT u FROM Users u WHERE u.usuario = :usuario"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Id
    @Column(name = "uid", nullable = false)
    private Integer uid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "usuario", nullable = false, length = 60)
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "password", nullable = false, length = 60)
    private String password;

    public Users() {
    }

    public Users(int uid, String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
        this.uid=uid;
    }

    public Users(Integer uid) {
        this.uid = uid;
    }

    public Users(Integer uid, String usuario, String password) {
        this.uid = uid;
        this.usuario = usuario;
        this.password = password;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uid != null ? uid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "datos.entidades.Users[ uid=" + uid + " ]";
    }
    
}

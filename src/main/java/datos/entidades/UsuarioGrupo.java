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
@Table(name = "usuario_grupo", catalog = "bdcarritocompras", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioGrupo.findAll", query = "SELECT u FROM UsuarioGrupo u"),
    @NamedQuery(name = "UsuarioGrupo.findByUsuarios", query = "SELECT u FROM UsuarioGrupo u WHERE u.usuarios = :usuarios"),
    @NamedQuery(name = "UsuarioGrupo.findByGrupo", query = "SELECT u FROM UsuarioGrupo u WHERE u.grupo = :grupo"),
    @NamedQuery(name = "UsuarioGrupo.findByUgId", query = "SELECT u FROM UsuarioGrupo u WHERE u.ugId = :ugId")})
public class UsuarioGrupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "usuarios")
    private String usuarios;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "grupo")
    private String grupo;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UgId")
    private Integer ugId;

    public UsuarioGrupo() {
    }

    public UsuarioGrupo(Integer ugId) {
        this.ugId = ugId;
    }

    public UsuarioGrupo(Integer ugId, String usuarios, String grupo) {
        this.ugId = ugId;
        this.usuarios = usuarios;
        this.grupo = grupo;
    }

    public String getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(String usuarios) {
        this.usuarios = usuarios;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public Integer getUgId() {
        return ugId;
    }

    public void setUgId(Integer ugId) {
        this.ugId = ugId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ugId != null ? ugId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioGrupo)) {
            return false;
        }
        UsuarioGrupo other = (UsuarioGrupo) object;
        if ((this.ugId == null && other.ugId != null) || (this.ugId != null && !this.ugId.equals(other.ugId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "datos.entidades.UsuarioGrupo[ ugId=" + ugId + " ]";
    }
    
}

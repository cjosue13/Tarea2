/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jose Pablo Bermudez
 */
@Entity
@Table(name = "HOR_ROL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r")
    , @NamedQuery(name = "Rol.findByRolId", query = "SELECT r FROM Rol r WHERE r.rolId = :rolId")
    , @NamedQuery(name = "Rol.findByRolNombre", query = "SELECT r FROM Rol r WHERE r.rolNombre = :rolNombre")
    , @NamedQuery(name = "Rol.findByRolHorariorotativo", query = "SELECT r FROM Rol r WHERE r.rolHorariorotativo = :rolHorariorotativo")
    , @NamedQuery(name = "Rol.findByRolVersion", query = "SELECT r FROM Rol r WHERE r.rolVersion = :rolVersion")})
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "ROL_SEC_GENERATOR", sequenceName = "HOR_SEC_ROL_01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROL_SEC_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ROL_ID")
    private Integer rolId;
    @Basic(optional = false)
    @Column(name = "ROL_NOMBRE")
    private String rolNombre;
    @Basic(optional = false)
    @Column(name = "ROL_HORARIOROTATIVO")
    private String rolHorariorotativo;
    @Basic(optional = false)
    @Column(name = "ROL_VERSION")
    private Integer rolVersion;
    @ManyToMany(mappedBy = "rolList", fetch = FetchType.LAZY)
    private List<Puesto> puestoList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "horRol", fetch = FetchType.LAZY)
    private Horario horario;

    public Rol() {
    }

    public Rol(Integer rolId) {
        this.rolId = rolId;
    }

    public Rol(RolDto rol){
        this.rolId = rol.getId();
        actualizarRol(rol);
    }
    
    public Rol(Integer rolId, String rolNombre, String rolHorariorotativo, Integer rolVersion) {
        this.rolId = rolId;
        this.rolNombre = rolNombre;
        this.rolHorariorotativo = rolHorariorotativo;
        this.rolVersion = rolVersion;
    }
    public void actualizarRol(RolDto rol) {
        this.rolId = rol.getId();
        this.rolNombre = rol.getNombreRol();
        this.rolHorariorotativo = rol.getHorarioRotativo();
        this.rolVersion = rol.getVersion();
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public String getRolHorariorotativo() {
        return rolHorariorotativo;
    }

    public void setRolHorariorotativo(String rolHorariorotativo) {
        this.rolHorariorotativo = rolHorariorotativo;
    }

    public Integer getRolVersion() {
        return rolVersion;
    }

    public void setRolVersion(Integer rolVersion) {
        this.rolVersion = rolVersion;
    }

    @XmlTransient
    public List<Puesto> getPuestoList() {
        return puestoList;
    }

    public void setPuestoList(List<Puesto> puestoList) {
        this.puestoList = puestoList;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolId != null ? rolId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.rolId == null && other.rolId != null) || (this.rolId != null && !this.rolId.equals(other.rolId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "horarios.model.Rol[ rolId=" + rolId + " ]";
    }
    
}

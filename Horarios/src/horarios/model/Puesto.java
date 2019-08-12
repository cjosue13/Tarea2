/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "HOR_PUESTOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Puesto.findAll", query = "SELECT p FROM Puesto p")
    , @NamedQuery(name = "Puesto.findByPueCodigo", query = "SELECT p FROM Puesto p WHERE p.pueCodigo = :pueCodigo")
    , @NamedQuery(name = "Puesto.findByPueNombrepuesto", query = "SELECT p FROM Puesto p WHERE p.pueNombrepuesto = :pueNombrepuesto")
    , @NamedQuery(name = "Puesto.findByPueDescripcion", query = "SELECT p FROM Puesto p WHERE p.pueDescripcion = :pueDescripcion")
    , @NamedQuery(name = "Puesto.findByPueVersion", query = "SELECT p FROM Puesto p WHERE p.pueVersion = :pueVersion")})
public class Puesto implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "PUESTOS_SEC_GENERATOR", sequenceName = "HOR_SEC_PUE_01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PUESTOS_SEC_GENERATOR")
    @Basic(optional = false)
    @Column(name = "PUE_CODIGO")
    private Integer pueCodigo;
    @Basic(optional = false)
    @Column(name = "PUE_NOMBREPUESTO")
    private String pueNombrepuesto;
    @Column(name = "PUE_DESCRIPCION")
    private String pueDescripcion;
    @Basic(optional = false)
    @Column(name = "PUE_VERSION")
    private Integer pueVersion;
    @JoinTable(name = "HOR_PUE_ROL", joinColumns = {
        @JoinColumn(name = "PUE_CODIGO", referencedColumnName = "PUE_CODIGO")}, inverseJoinColumns = {
        @JoinColumn(name = "ROL_ID", referencedColumnName = "ROL_ID")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Rol> rolList;
    @JoinColumn(name = "PUE_EMPLEADO", referencedColumnName = "EMP_FOLIO")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Empleado pueEmpleado;

    public Puesto() {
    }

    public Puesto(Integer pueCodigo) {
        this.pueCodigo = pueCodigo;
    }

    public Puesto(PuestoDto pues) {
        this.pueCodigo = pues.getId();
        actualizarPuesto(pues);
    }

    public Puesto(Integer pueCodigo, String pueNombrepuesto, Integer pueVersion) {
        this.pueCodigo = pueCodigo;
        this.pueNombrepuesto = pueNombrepuesto;
        this.pueVersion = pueVersion;
    }

    public void actualizarPuesto(PuestoDto pues) {
        this.pueCodigo = pues.getId();
        this.pueNombrepuesto = pues.getNombrePuesto();
        this.pueVersion = pues.getVersion();
        if (pues.getEmpleado() != null) {
            this.pueEmpleado = new Empleado(pues.getEmpleado());
        }
        else{
            this.pueEmpleado = null;
        }
        this.pueDescripcion = pues.getDescripcion();
    }

    public Integer getPueCodigo() {
        return pueCodigo;
    }

    public void setPueCodigo(Integer pueCodigo) {
        this.pueCodigo = pueCodigo;
    }

    public String getPueNombrepuesto() {
        return pueNombrepuesto;
    }

    public void setPueNombrepuesto(String pueNombrepuesto) {
        this.pueNombrepuesto = pueNombrepuesto;
    }

    public String getPueDescripcion() {
        return pueDescripcion;
    }

    public void setPueDescripcion(String pueDescripcion) {
        this.pueDescripcion = pueDescripcion;
    }

    public Integer getPueVersion() {
        return pueVersion;
    }

    public void setPueVersion(Integer pueVersion) {
        this.pueVersion = pueVersion;
    }

    public List<Rol> getRolList() {
        return rolList;
    }

    public void setRolList(List<Rol> rolList) {
        this.rolList = rolList;
    }

    public Empleado getPueEmpleado() {
        return pueEmpleado;
    }

    public void setPueEmpleado(Empleado pueEmpleado) {
        this.pueEmpleado = pueEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pueCodigo != null ? pueCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Puesto)) {
            return false;
        }
        Puesto other = (Puesto) object;
        return !((this.pueCodigo == null && other.pueCodigo != null) || (this.pueCodigo != null && !this.pueCodigo.equals(other.pueCodigo)));
    }

    @Override
    public String toString() {
        return "horarios.model.Puesto[ pueCodigo=" + pueCodigo + " ]";
    }

}

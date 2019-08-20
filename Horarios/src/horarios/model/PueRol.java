/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos Olivares
 */
@Entity
@Table(name = "HOR_PUE_ROL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PueRol.findAll", query = "SELECT p FROM PueRol p", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
    , @NamedQuery(name = "PueRol.findByHorPueId", query = "SELECT p FROM PueRol p WHERE p.horPueId = :horPueId", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
    , @NamedQuery(name = "PueRol.findByOrdenRotacion", query = "SELECT p FROM PueRol p WHERE p.ordenRotacion = :ordenRotacion", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))})
public class PueRol implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "PUESTO_ROL_SEC_GENERATOR", sequenceName = "HOR_SEC_PUE_ROL_01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PUESTO_ROL_SEC_GENERATOR")
    @Basic(optional = false)
    @Column(name = "HOR_PUE_ID")
    private Integer horPueId;
    @Basic(optional = false)
    @Column(name = "ORDEN_ROTACION")
    private Integer ordenRotacion;
    @JoinColumn(name = "PUE_CODIGO", referencedColumnName = "PUE_CODIGO")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Puesto pueCodigo;
    @JoinColumn(name = "ROL_ID", referencedColumnName = "ROL_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Rol rolId;

    public PueRol() {
    }

    public PueRol(PueRolDto pueRol) {
        this.horPueId = pueRol.getHorPueId();
        this.pueCodigo = new Puesto(pueRol.getPueCodigo());
        this.rolId = new Rol(pueRol.getRolId());
        actualizarPuesto(pueRol);
    }

    public void actualizarPuesto(PueRolDto pueRolDto) {
        this.ordenRotacion = pueRolDto.getOrdenRotacion();
    }

    public PueRol(Integer horPueId) {
        this.horPueId = horPueId;
    }

    public PueRol(Integer horPueId, Integer ordenRotacion) {
        this.horPueId = horPueId;
        this.ordenRotacion = ordenRotacion;
    }

    public Integer getHorPueId() {
        return horPueId;
    }

    public void setHorPueId(Integer horPueId) {
        this.horPueId = horPueId;
    }

    public Integer getOrdenRotacion() {
        return ordenRotacion;
    }

    public void setOrdenRotacion(Integer ordenRotacion) {
        this.ordenRotacion = ordenRotacion;
    }

    public Puesto getPueCodigo() {
        return pueCodigo;
    }

    public void setPueCodigo(Puesto pueCodigo) {
        this.pueCodigo = pueCodigo;
    }

    public Rol getRolId() {
        return rolId;
    }

    public void setRolId(Rol rolId) {
        this.rolId = rolId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (horPueId != null ? horPueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PueRol)) {
            return false;
        }
        PueRol other = (PueRol) object;
        if ((this.horPueId == null && other.horPueId != null) || (this.horPueId != null && !this.horPueId.equals(other.horPueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "horarios.model.PueRol[ horPueId=" + horPueId + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jose Pablo Bermudez
 */
@Entity
@Table(name = "HOR_HORARIOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Horario.findAll", query = "SELECT h FROM Horario h")
    , @NamedQuery(name = "Horario.findByHorId", query = "SELECT h FROM Horario h WHERE h.horId = :horId")
    , @NamedQuery(name = "Horario.findByHorFechainicio", query = "SELECT h FROM Horario h WHERE h.horFechainicio = :horFechainicio")
    , @NamedQuery(name = "Horario.findByHorHoraslibres", query = "SELECT h FROM Horario h WHERE h.horHoraslibressemanales = :horHoraslibressemanales")
    , @NamedQuery(name = "Horario.findByHorVersion", query = "SELECT h FROM Horario h WHERE h.horVersion = :horVersion")
    , @NamedQuery(name = "Horario.findByRol", query = "SELECT h FROM Horario h  WHERE h.horRol = :horRol")
})
public class Horario implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "HORARIO_SEC_GENERATOR", sequenceName = "HOR_SEC_HOR_01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HORARIO_SEC_GENERATOR")
    @Basic(optional = false)
    @Column(name = "HOR_ID")
    private Integer horId;
    @Basic(optional = false)
    @Column(name = "HOR_FECHAINICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horFechainicio;
    @Basic(optional = false)
    @Column(name = "HOR_VERSION")
    private Integer horVersion;
    @Basic(optional = false)
    @Column(name = "HOR_HORASLIBRESSEMANALES")
    private Integer horHoraslibressemanales;
    @Basic(optional = false)
    @Column(name = "HOR_ORDENROTACION")
    private Integer horOrdenrotacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "horHorario", fetch = FetchType.LAZY)
    private List<Dia> horDiaList;
    @JoinColumn(name = "HOR_ROL", referencedColumnName = "ROL_ID")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Rol horRol;

    public Horario() {
    }

    public Horario(Integer horId, Date horFechainicio, Integer horVersion, Integer horHoraslibressemanales, Integer horOrdenrotacion, Rol horRol) {
        this.horId = horId;
        this.horFechainicio = horFechainicio;
        this.horVersion = horVersion;
        this.horHoraslibressemanales = horHoraslibressemanales;
        this.horOrdenrotacion = horOrdenrotacion;
        this.horRol = horRol;
    }

    public Horario(HorarioDto hor) {
        this.horId = hor.getId();
        actualizarHorario(hor);
    }

    public void actualizarHorario(HorarioDto horario) {
        this.horId = horario.getId();
        this.horFechainicio = Date.from(horario.getFechaInicio().atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.horHoraslibressemanales = horario.getHorasLibras();
        this.horVersion = horario.getVersion();
        this.horOrdenrotacion = horario.getOrdenRotacion();
        this.horRol = new Rol(horario.getRol());
        this.horDiaList = new ArrayList<>();

    }

    public Integer getHorId() {
        return horId;
    }

    public void setHorId(Integer horId) {
        this.horId = horId;
    }

    public Date getHorFechainicio() {
        return horFechainicio;
    }

    public void setHorFechainicio(Date horFechainicio) {
        this.horFechainicio = horFechainicio;
    }

    public Integer getHorVersion() {
        return horVersion;
    }

    public void setHorVersion(Integer horVersion) {
        this.horVersion = horVersion;
    }

    public List<Dia> getHorDiaList() {
        return horDiaList;
    }

    public void setHorDiaList(List<Dia> horDiaList) {
        this.horDiaList = horDiaList;
    }

    public Rol getHorRol() {
        return horRol;
    }

    public void setHorRol(Rol horRol) {
        this.horRol = horRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (horId != null ? horId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Horario)) {
            return false;
        }
        Horario other = (Horario) object;
        return !((this.horId == null && other.horId != null) || (this.horId != null && !this.horId.equals(other.horId)));
    }

    public Integer getHorHoraslibressemanales() {
        return horHoraslibressemanales;
    }

    public void setHorHoraslibressemanales(Integer horHoraslibressemanales) {
        this.horHoraslibressemanales = horHoraslibressemanales;
    }

    public Integer getHorOrdenrotacion() {
        return horOrdenrotacion;
    }

    public void setHorOrdenrotacion(Integer horOrdenrotacion) {
        this.horOrdenrotacion = horOrdenrotacion;
    }

    @Override
    public String toString() {
        return "horarios.model.Horario[ horId=" + horId + " ]";
    }

}

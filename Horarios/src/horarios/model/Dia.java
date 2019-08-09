/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "HOR_DIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dia.findAll", query = "SELECT d FROM Dia d")
    , @NamedQuery(name = "Dia.findByDiaId", query = "SELECT d FROM Dia d WHERE d.diaId = :diaId")
    , @NamedQuery(name = "Dia.findByDiaNombre", query = "SELECT d FROM Dia d WHERE d.diaNombre = :diaNombre")
    , @NamedQuery(name = "Dia.findByDiaHorainicio", query = "SELECT d FROM Dia d WHERE d.diaHorainicio = :diaHorainicio")
    , @NamedQuery(name = "Dia.findByDiaHorasalida", query = "SELECT d FROM Dia d WHERE d.diaHorasalida = :diaHorasalida")
    , @NamedQuery(name = "Dia.findByDiaVersion", query = "SELECT d FROM Dia d WHERE d.diaVersion = :diaVersion")})
public class Dia implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)/0/if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "DIA_SEC_GENERATOR", sequenceName = "HOR_SEC_DIA_01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DIA_SEC_GENERATOR")
    @Basic(optional = false)
    @Column(name = "DIA_ID")
    private Integer diaId;
    @Basic(optional = false)
    @Column(name = "DIA_NOMBRE")
    private String diaNombre;
    @Basic(optional = false)
    @Column(name = "DIA_HORAINICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date diaHorainicio;
    @Basic(optional = false)
    @Column(name = "DIA_HORASALIDA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date diaHorasalida;
    @Basic(optional = false)
    @Column(name = "DIA_VERSION")
    private Integer diaVersion;
    @JoinColumn(name = "HOR_HORARIO", referencedColumnName = "HOR_ID")
    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    private Horario horHorario;

    public Dia() {
    }

    
    public Dia(DiaDto dia){
        this.diaId = dia.getDiaid();
        actualizarDia(dia);
    }
    
    public Dia(Integer diaId) {
        this.diaId = diaId;
    }

    public Dia(Integer diaId, String diaNombre, Date diaHorainicio, Date diaHorasalida, Integer diaVersion) {
        this.diaId = diaId;
        this.diaNombre = diaNombre;
        this.diaHorainicio = diaHorainicio;
        this.diaHorasalida = diaHorasalida;
        this.diaVersion = diaVersion;
    }

    public void actualizarDia(DiaDto dia){
        this.diaHorainicio = Date.from(dia.getHora_Inicio().atZone(ZoneId.systemDefault()).toInstant());
        this.diaHorasalida = Date.from(dia.getHora_Salida().atZone(ZoneId.systemDefault()).toInstant());
        this.diaId = dia.getDiaid();
        this.diaNombre = dia.getNombre();
        this.diaVersion = dia.getVersion();
        this.horHorario = new Horario(dia.getHorario());
        
    }
    
    public Integer getDiaId() {
        return diaId;
    }

    public void setDiaId(Integer diaId) {
        this.diaId = diaId;
    }

    public String getDiaNombre() {
        return diaNombre;
    }

    public void setDiaNombre(String diaNombre) {
        this.diaNombre = diaNombre;
    }

    public Date getDiaHorainicio() {
        return diaHorainicio;
    }

    public void setDiaHorainicio(Date diaHorainicio) {
        this.diaHorainicio = diaHorainicio;
    }

    public Date getDiaHorasalida() {
        return diaHorasalida;
    }

    public void setDiaHorasalida(Date diaHorasalida) {
        this.diaHorasalida = diaHorasalida;
    }

    public Integer getDiaVersion() {
        return diaVersion;
    }

    public void setDiaVersion(Integer diaVersion) {
        this.diaVersion = diaVersion;
    }

    public Horario getHorHorario() {
        return horHorario;
    }

    public void setHorHorario(Horario horHorario) {
        this.horHorario = horHorario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (diaId != null ? diaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dia)) {
            return false;
        }
        Dia other = (Dia) object;
        if ((this.diaId == null && other.diaId != null) || (this.diaId != null && !this.diaId.equals(other.diaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "horarios.model.Dia[ diaId=" + diaId + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jose Pablo Bermudez
 */
@Entity
@Table(name = "HOR_EMPLEADOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e",hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
    , @NamedQuery(name = "Empleado.findByEmpFolio", query = "SELECT e FROM Empleado e WHERE e.empFolio = :empFolio",hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
    , @NamedQuery(name = "Empleado.findByEmpNombre", query = "SELECT e FROM Empleado e WHERE e.empNombre = :empNombre",hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
    , @NamedQuery(name = "Empleado.findByEmpApellidos", query = "SELECT e FROM Empleado e WHERE e.empApellidos = :empApellidos",hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
    , @NamedQuery(name = "Empleado.findByEmpCedula", query = "SELECT e FROM Empleado e WHERE e.empCedula = :empCedula",hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
    , @NamedQuery(name = "Empleado.findByEmpCorreo", query = "SELECT e FROM Empleado e WHERE e.empCorreo = :empCorreo",hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
    , @NamedQuery(name = "Empleado.findByEmpCantidadhorastrabajadas", query = "SELECT e FROM Empleado e WHERE e.empCantidadhorastrabajadas = :empCantidadhorastrabajadas" ,hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
    , @NamedQuery(name = "Empleado.findByEmpVersion", query = "SELECT e FROM Empleado e WHERE e.empVersion = :empVersion" ,hints = @QueryHint(name = "eclipselink.refresh", value = "true"))})
public class Empleado implements Serializable {

    @Basic(optional = false)
    @Column(name = "EMP_CANTIDADHORASTRABAJADAS")
    private Integer empCantidadhorastrabajadas;
    @Basic(optional = false)
    @Column(name = "EMP_VERSION")
    private Integer empVersion;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "EMPLEADO_SEC_GENERATOR", sequenceName = "HOR_SEC_EMP_01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLEADO_SEC_GENERATOR")
    @Basic(optional = false)
    @Column(name = "EMP_FOLIO")
    private Integer empFolio;
    @Basic(optional = false)
    @Column(name = "EMP_NOMBRE")
    private String empNombre;
    @Basic(optional = false)
    @Column(name = "EMP_APELLIDOS")
    private String empApellidos;
    @Basic(optional = false)
    @Column(name = "EMP_CEDULA")
    private String empCedula;
    @Basic(optional = false)
    @Column(name = "EMP_CORREO")
    private String empCorreo;
    @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "pueEmpleado", fetch = FetchType.LAZY)
    private Puesto puesto;

    public Empleado() {
    }

    public Empleado(Integer empFolio) {
        this.empFolio = empFolio;
    }

    public Empleado(EmpleadoDto emp) {
        this.empFolio = emp.getId();
        actualizarEmpleado(emp);
    }

    public Empleado(Integer empFolio, String empNombre, String empApellidos, String empCedula, String empCorreo, Integer empCantidadhorastrabajadas, Integer empVersion) {
        this.empFolio = empFolio;
        this.empNombre = empNombre;
        this.empApellidos = empApellidos;
        this.empCedula = empCedula;
        this.empCorreo = empCorreo;
        this.empCantidadhorastrabajadas = empCantidadhorastrabajadas;
        this.empVersion = empVersion;
    }

    public void actualizarEmpleado(EmpleadoDto empleado) {
        this.empFolio = empleado.getId();
        this.empNombre = empleado.getNombre();
        this.empApellidos = empleado.getApellido();
        this.empCedula = empleado.getCedula();
        this.empCorreo = empleado.getCorreo();
        this.empCantidadhorastrabajadas = empleado.getCantidadHoras();
        this.empVersion = empleado.getVersion();
        //this.puesto = new Puesto(empleado.getPuesto());
    }

    public Integer getEmpFolio() {
        return empFolio;
    }

    public void setEmpFolio(Integer empFolio) {
        this.empFolio = empFolio;
    }

    public String getEmpNombre() {
        return empNombre;
    }

    public void setEmpNombre(String empNombre) {
        this.empNombre = empNombre;
    }

    public String getEmpApellidos() {
        return empApellidos;
    }

    public void setEmpApellidos(String empApellidos) {
        this.empApellidos = empApellidos;
    }

    public String getEmpCedula() {
        return empCedula;
    }

    public void setEmpCedula(String empCedula) {
        this.empCedula = empCedula;
    }

    public String getEmpCorreo() {
        return empCorreo;
    }

    public void setEmpCorreo(String empCorreo) {
        this.empCorreo = empCorreo;
    }

    public Integer getEmpCantidadhorastrabajadas() {
        return empCantidadhorastrabajadas;
    }

    public void setEmpCantidadhorastrabajadas(Integer empCantidadhorastrabajadas) {
        this.empCantidadhorastrabajadas = empCantidadhorastrabajadas;
    }

    public Integer getEmpVersion() {
        return empVersion;
    }

    public void setEmpVersion(Integer empVersion) {
        this.empVersion = empVersion;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empFolio != null ? empFolio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        return !((this.empFolio == null && other.empFolio != null) || (this.empFolio != null && !this.empFolio.equals(other.empFolio)));
    }

    @Override
    public String toString() {
        return "horarios.model.Empleado[ empFolio=" + empFolio + " ]";
    }



}

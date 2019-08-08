/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class PuestoDto {
    
    private String NombrePuesto,Descripcion;
    private Integer version;
    private EmpleadoDto empleado;
    private Integer id;

    public PuestoDto(String NombrePuesto, String Descripcion, Integer version, EmpleadoDto empleado, Integer id) {
        this.NombrePuesto = NombrePuesto;
        this.Descripcion = Descripcion;
        this.version = version;
        this.empleado = empleado;
        this.id = id;
    }
    public PuestoDto(Puesto puesto) {
        this.NombrePuesto = puesto.getPueNombrepuesto();
        this.Descripcion = puesto.getPueDescripcion();
        this.version = puesto.getPueVersion();
        this.empleado = new EmpleadoDto(puesto.getPueEmpleado());
        this.id = puesto.getPueCodigo();
    }

    public String getNombrePuesto() {
        return NombrePuesto;
    }

    public void setNombrePuesto(String NombrePuesto) {
        this.NombrePuesto = NombrePuesto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public EmpleadoDto getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDto empleado) {
        this.empleado = empleado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
  
    
}

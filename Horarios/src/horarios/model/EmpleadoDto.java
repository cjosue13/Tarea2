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
public class EmpleadoDto {
    
    private String Nombre;
    private String Apellido;
    private String Cedula;
    private String Correo;
    private Integer CantidadHoras;
    private Integer version;
    private Integer Id;

    public EmpleadoDto(String Nombre, String Apellido, String Cedula, String Correo, Integer CantidadHoras, Integer version, Integer Id) {
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Cedula = Cedula;
        this.Correo = Correo;
        this.CantidadHoras = CantidadHoras;
        this.version = version;
        this.Id = Id;
    }
    public EmpleadoDto(Empleado empleado) {
        this.Nombre = empleado.getEmpNombre();
        this.Apellido = empleado.getEmpApellidos();
        this.Cedula = empleado.getEmpCedula();
        this.Correo = empleado.getEmpCorreo();
        this.CantidadHoras = empleado.getEmpCantidadhorastrabajadas();
        this.version = empleado.getEmpVersion();
        this.Id = empleado.getEmpFolio();
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String Cedula) {
        this.Cedula = Cedula;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String Correo) {
        this.Correo = Correo;
    }

    public Integer getCantidadHoras() {
        return CantidadHoras;
    }

    public void setCantidadHoras(Integer CantidadHoras) {
        this.CantidadHoras = CantidadHoras;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    
    
    
}

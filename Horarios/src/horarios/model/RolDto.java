/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

import java.util.ArrayList;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class RolDto {

    private String NombreRol, HorarioRotativo;
    private Integer version;
    private Integer id;
    private ArrayList<PuestoDto> puestos;
    private HorarioDto horario;

    public RolDto(String NombreRol, String HorarioRotativo, Integer version, Integer id, HorarioDto horario) {
        this.NombreRol = NombreRol;
        this.HorarioRotativo = HorarioRotativo;
        this.version = version;
        this.id = id;
        this.horario = horario;
    }

    public RolDto(Rol rol) {
        this.HorarioRotativo = rol.getRolHorariorotativo();
        this.NombreRol = rol.getRolNombre();
        this.id = rol.getRolId();
        this.version = rol.getRolVersion();
    }

    public String getNombreRol() {
        return NombreRol;
    }

    public void setNombreRol(String NombreRol) {
        this.NombreRol = NombreRol;
    }

    public String getHorarioRotativo() {
        return HorarioRotativo;
    }

    public void setHorarioRotativo(String HorarioRotativo) {
        this.HorarioRotativo = HorarioRotativo;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public HorarioDto getHorario() {
        return horario;
    }

    public void setHorario(HorarioDto horario) {
        this.horario = horario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<PuestoDto> getPuestos() {
        if (puestos != null) {
            return puestos;
        } else {
            puestos = new ArrayList<>();
            return puestos;
        }

    }

    public void setPuestos(ArrayList<PuestoDto> puestos) {
        this.puestos = puestos;
    }

    @Override
    public String toString() {
        return "RolDto{" + "NombreRol=" + NombreRol + ", HorarioRotativo=" + HorarioRotativo + ", version=" + version + ", id=" + id + ", puestos=" + puestos + ", horario=" + horario + '}';
    }

}

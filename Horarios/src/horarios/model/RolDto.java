/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

import java.time.Instant;
import java.time.ZoneId;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class RolDto {
    
    String NombreRol,HorarioRotativo;
    Integer version;
    Integer id;

    public RolDto(String NombreRol, String HorarioRotativo, Integer version, Integer id) {
        this.NombreRol = NombreRol;
        this.HorarioRotativo = HorarioRotativo;
        this.version = version;
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
    
}

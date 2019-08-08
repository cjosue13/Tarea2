/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class HorarioDto {
    
    private LocalDate FechaInicio;
    private Integer HorasLibras;
    private Integer version;
    private Integer Id;
    private RolDto rol;
    private ArrayList <DiaDto> dias;


    public HorarioDto(LocalDate FechaInicio, Integer HorasLibras, Integer version, Integer Id, RolDto rol) {
        this.FechaInicio = FechaInicio;
        this.HorasLibras = HorasLibras;
        this.version = version;
        this.Id = Id;
        this.rol = rol;
    }

    public HorarioDto(Horario horario) {
        this.FechaInicio = Instant.ofEpochMilli(horario.getHorFechainicio().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        this.HorasLibras = horario.getHorHoraslibres();
        this.Id = horario.getHorId();
        this.rol = new RolDto(horario.getHorRol());
        this.version = horario.getHorVersion();
    }
    
    
    public LocalDate getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(LocalDate FechaInicio) {
        this.FechaInicio = FechaInicio;
    }

    public Integer getHorasLibras() {
        return HorasLibras;
    }

    public void setHorasLibras(Integer HorasLibras) {
        this.HorasLibras = HorasLibras;
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

    public RolDto getRol() {
        return rol;
    }

    public void setRol(RolDto rol) {
        this.rol = rol;
    }
    
    public ArrayList<DiaDto> getDias() {
        return dias;
    }

    public void setDias(ArrayList<DiaDto> dias) {
        this.dias = dias;
    }
    
    
}

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
    private Integer ordenRotacion;
    private ArrayList<DiaDto> dias;

    public HorarioDto(LocalDate FechaInicio, Integer HorasLibras, Integer version, Integer Id, RolDto rol, Integer ordenRotacion) {
        this.FechaInicio = FechaInicio;
        this.HorasLibras = HorasLibras;
        this.version = version;
        this.Id = Id;
        this.rol = rol;
        this.ordenRotacion = ordenRotacion;
    }

    public HorarioDto(LocalDate FechaInicio, Integer HorasLibras, Integer version, Integer Id, RolDto rol, Integer ordenRotacion, ArrayList<DiaDto> dias) {
        this.FechaInicio = FechaInicio;
        this.HorasLibras = HorasLibras;
        this.version = version;
        this.Id = Id;
        this.rol = rol;
        this.ordenRotacion = ordenRotacion;
        this.dias = dias;
    }

    

    public HorarioDto(Horario horario) {
        this.FechaInicio = Instant.ofEpochMilli(horario.getHorFechainicio().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        this.HorasLibras = horario.getHorHoraslibressemanales();
        this.Id = horario.getHorId();
        this.ordenRotacion = horario.getHorOrdenrotacion();
        this.rol = new RolDto(horario.getHorRol());
        this.version = horario.getHorVersion();
    }

    public HorarioDto() {
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
        if (dias != null) {
            return dias;
        } else {
            dias = new ArrayList<>();
            return dias;
        }
    }

    public void setDias(ArrayList<DiaDto> dias) {
        this.dias = dias;
    }

    public void calcularHorasLibres() {
        if (dias != null) {
            this.HorasLibras = dias.stream().mapToInt(x -> x.getCantHorasLibre()).sum();
        }
    }

    public Integer getOrdenRotacion() {
        return ordenRotacion;
    }

    public void setOrdenRotacion(Integer ordenRotacion) {
        this.ordenRotacion = ordenRotacion;
    }
    
    @Override
    public String toString() {
        return "HorarioDto{" + "FechaInicio=" + FechaInicio + ", HorasLibras=" + HorasLibras + ", version=" + version + ", Id=" + Id + ", rol=" + rol + ", dias=" + dias + '}';
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class DiaDto {
    
    private String nombre;
    private LocalDateTime Hora_Inicio;
    private LocalDateTime Hora_Salida;
    private Integer Diaid;
    private Integer version;
    private HorarioDto horario;
    private Integer cantHorasLibre;

    public DiaDto(String nombre, LocalDateTime Hora_Inicio, LocalDateTime Hora_Salida, Integer Diaid, Integer version, HorarioDto horario, Integer cantHorasLibre) {
        this.nombre = nombre;
        this.Hora_Inicio = Hora_Inicio;
        this.Hora_Salida = Hora_Salida;
        this.Diaid = Diaid;
        this.version = version;
        this.horario = horario;
        this.cantHorasLibre = cantHorasLibre;
    }
    
    

    
    
    public DiaDto(Dia dia) {
        this.nombre = dia.getDiaNombre();
        this.Hora_Inicio = LocalDateTime.ofInstant(dia.getDiaHorainicio().toInstant(),ZoneId.systemDefault());
        this.Hora_Salida = LocalDateTime.ofInstant(dia.getDiaHorasalida().toInstant(),ZoneId.systemDefault());
        this.Diaid = dia.getDiaId();
        this.cantHorasLibre = dia.getDiaCantidadhoraslibres();
        this.version = dia.getDiaVersion();
        this.horario = new HorarioDto(dia.getHorHorario());
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getHora_Inicio() {
        return Hora_Inicio;
    }

    public void setHora_Inicio(LocalDateTime Hora_Inicio) {
        this.Hora_Inicio = Hora_Inicio;
    }

    public LocalDateTime getHora_Salida() {
        return Hora_Salida;
    }

    public void setHora_Salida(LocalDateTime Hora_Salida) {
        this.Hora_Salida = Hora_Salida;
    }

    public Integer getCantHorasLibre() {
        return cantHorasLibre;
    }

    public void setCantHorasLibre(Integer cantHorasLibre) {
        this.cantHorasLibre = cantHorasLibre;
    }
    
    public Integer getDiaid() {
        return Diaid;
    }

    public void setDiaid(Integer Diaid) {
        this.Diaid = Diaid;
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

    @Override
    public String toString() {
        return "DiaDto{" + "nombre=" + nombre + ", Hora_Inicio=" + Hora_Inicio + ", Hora_Salida=" + Hora_Salida + ", Diaid=" + Diaid + ", version=" + version + ", horario=" + horario + ", cantHorasLibre=" + cantHorasLibre + '}';
    }
    
}

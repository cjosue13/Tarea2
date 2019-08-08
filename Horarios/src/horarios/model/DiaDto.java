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

    public DiaDto(String nombre, LocalDateTime Hora_Inicio, LocalDateTime Hora_Salida, Integer Diaid, Integer version, Integer Id, HorarioDto horario) {
        this.nombre = nombre;
        this.Hora_Inicio = Hora_Inicio;
        this.Hora_Salida = Hora_Salida;
        this.Diaid = Diaid;
        this.version = version;
        this.horario = horario;
    }

    public DiaDto(Dia dia) {
        this.nombre = dia.getDiaNombre();
        this.Hora_Inicio = LocalDateTime.ofInstant(dia.getDiaHorainicio().toInstant(),ZoneId.systemDefault());
        this.Hora_Salida = LocalDateTime.ofInstant(dia.getDiaHorasalida().toInstant(),ZoneId.systemDefault());
        this.Diaid = dia.getDiaId();
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
}

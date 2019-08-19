/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.model;

/**
 *
 * @author Carlos Olivares
 */
public class PueRolDto {
    private Integer horPueId;
    private Integer ordenRotacion;
    private PuestoDto pueCodigo;
    private RolDto rolId;

    public PueRolDto() {
    }
    
    public PueRolDto(Integer horPueId, Integer ordenRotacion, PuestoDto pueCodigo, RolDto rolId) {
        this.horPueId = horPueId;
        this.ordenRotacion = ordenRotacion;
        this.pueCodigo = pueCodigo;
        this.rolId = rolId;
    }
    public PueRolDto(PueRol pueRol) {
        this.horPueId = pueRol.getHorPueId();
        this.ordenRotacion = pueRol.getOrdenRotacion();
        this.pueCodigo = new PuestoDto(pueRol.getPueCodigo());
        this.rolId = new RolDto(pueRol.getRolId());
    }
    public Integer getHorPueId() {
        return horPueId;
    }

    public void setHorPueId(Integer horPueId) {
        this.horPueId = horPueId;
    }

    public Integer getOrdenRotacion() {
        return ordenRotacion;
    }

    public void setOrdenRotacion(Integer ordenRotacion) {
        this.ordenRotacion = ordenRotacion;
    }

    public PuestoDto getPueCodigo() {
        return pueCodigo;
    }

    public void setPueCodigo(PuestoDto pueCodigo) {
        this.pueCodigo = pueCodigo;
    }

    public RolDto getRolId() {
        return rolId;
    }

    public void setRolId(RolDto rolId) {
        this.rolId = rolId;
    }
    
    
}

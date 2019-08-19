/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.service;

import horarios.model.PueRol;
import horarios.model.PueRolDto;
import horarios.model.Puesto;
import horarios.model.PuestoDto;
import horarios.util.EntityManagerHelper;
import horarios.util.Respuesta;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Carlos Olivares
 */
public class PueRolService {
    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;
    
    public Respuesta guardarTablaRelacional(PueRolDto pueRolDto) {
        try {
            et = em.getTransaction();
            et.begin();
            PueRol pueRol;
            if (pueRolDto.getHorPueId() != null && pueRolDto.getHorPueId() > 0) {
                pueRol = em.find(PueRol.class, pueRolDto.getHorPueId());
                if (pueRol == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el Puesto a modificar.", "guardarPuesto NoResultException");
                }
                pueRol.actualizarPuesto(pueRolDto);
                pueRol = em.merge(pueRol);
            } else {
                pueRol = new PueRol(pueRolDto);
                em.persist(pueRol);
            }
            et.commit();
            return new Respuesta(true, "Guardado exitosamente", "", "pueRol", new PueRolDto(pueRol));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(PuestoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar el Puesto.", ex);
            return new Respuesta(false, "Ocurrio un error al guardar el Puesto.", "guardarPuesto " + ex.getMessage());
        }
    }
}

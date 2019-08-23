/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.service;

import horarios.model.PueRol;
import horarios.model.PueRolDto;
import horarios.util.EntityManagerHelper;
import horarios.util.Respuesta;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

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
            et.rollback();//devuelve el estado inicial
            Logger.getLogger(PuestoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar el Puesto.", ex);
            return new Respuesta(false, "Ocurrio un error al guardar el Puesto.", "guardarPuesto " + ex.getMessage());
        }
    }

    public Respuesta getRelaciones() {
        try {
            Query qryRelacion = em.createNamedQuery("PueRol.findAll", PueRol.class);
            ArrayList<PueRolDto> lista = new ArrayList();
            qryRelacion.getResultList().stream().forEach((pues) -> {
                lista.add(new PueRolDto((PueRol) pues));
            });
            return new Respuesta(true, "", "", "getRelaciones", lista);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No hay resultados", "getRelaciones " + ex.getMessage());
        } catch (Exception e) {
            Logger.getLogger(PueRolService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar la relacional", e);
            return new Respuesta(false, "Ocurrio un error al consultar la relacion", "getRelaciones " + e.getMessage());
        }
    }
    public Respuesta eliminarRelacion(Integer id) {
        try {
            et = em.getTransaction();
            et.begin();
            PueRol relacion;
            if (id != null && id > 0) {
                relacion = em.find(PueRol.class, id);
                if (relacion == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró la relacion a eliminar.", "eliminarRelacion NoResultException");
                }
                em.remove(relacion);
            } else {
                et.rollback();
                return new Respuesta(false, "Debe cargar la relacion a eliminar.", "eliminarRelacion NoResultException");
            }
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            et.rollback();
            if (ex.getCause() != null && ex.getCause().getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                return new Respuesta(false, "No se puede eliminar la relacion porque tiene relaciones con otros registros.", "eliminarRelacion " + ex.getMessage());
            }
            Logger.getLogger(RolService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar el Rol.", ex);
            return new Respuesta(false, "Ocurrio un error al eliminar la relacion.", "eliminarRelacion " + ex.getMessage());
        }
    }
}

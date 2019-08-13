/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.service;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import horarios.model.Dia;
import horarios.model.DiaDto;
import horarios.model.Dia;
import horarios.util.EntityManagerHelper;
import horarios.util.Respuesta;
import java.sql.SQLIntegrityConstraintViolationException;

public class DiaService {

    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta getUsuario(Integer ID) {
        try {
            Query qryUsuario = em.createNamedQuery("Dia.findByHosId", Dia.class);
            qryUsuario.setParameter("diaId", ID);
            
            DiaDto Dia = new DiaDto((Dia) qryUsuario.getSingleResult());

            return new Respuesta(true, "Encontrado exitosamente", "", "DiaID", Dia);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe un usuario con el código ingresado.", "getUsuario NoResultException");
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(DiaService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Dia.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el usuario.", "getUsuario NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(DiaService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Dia.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el usuario.", "getUsuario " + ex.getMessage());
        }
    }

    public Respuesta guardarDia(DiaDto diaDto) {
        try {
            et = em.getTransaction();
            et.begin();
            Dia Dia;
            if (diaDto.getDiaid() != null && diaDto.getDiaid() > 0) {
                Dia = em.find(Dia.class, diaDto.getDiaid());
                if (Dia == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el Dia a modificar.", "guardarDia NoResultException");
                }
                Dia.actualizarDia(diaDto);
                Dia = em.merge(Dia);
            } else {
                Dia = new Dia(diaDto);
                em.persist(Dia);
            }
            et.commit();
            return new Respuesta(true, "Guardado exitosamente", "", "Dia", new DiaDto(Dia));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(DiaService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar la Diaes.", ex);
            return new Respuesta(false, "Ocurrio un error al guardar la Diaes.", "guardarDiaes " + ex.getMessage());
        }
    }

    public Respuesta getDias() {
        try {
            Query qryDia = em.createNamedQuery("Dia.findAll", Dia.class);
            ArrayList<DiaDto> Dia = new ArrayList<>();

            qryDia.getResultList().stream().forEach((hos) -> {
                Dia.add(new DiaDto((Dia) hos));
            });

            return new Respuesta(true, "Dia obtenidas", "", "Dias", Dia);

        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe una Dia con el código ingresado.", "getDia NoResultException");
        } catch (Exception e) {
            et.rollback();
            Logger.getLogger(DiaService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar los Dia.", e);
            return new Respuesta(false, "Ocurrio un error al consultar los Dia.", "getDia " + e.getMessage());
        }
    }

    public Respuesta EliminarDia(Integer id) {
        try {
            et = em.getTransaction();
            et.begin();
            Dia Dia;
            if (id != null && id > 0) {
                Dia = em.find(Dia.class, id);
                if (Dia == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el Dia a eliminar.", "EliminarDia NoResultException");
                }
                em.remove(Dia);
            } else {
                et.rollback();
                return new Respuesta(false, "Debe cargar el Dia a eliminar.", "EliminarDia NoResultException");
            }
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            et.rollback();
            if (ex.getCause() != null && ex.getCause().getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                return new Respuesta(false, "No se puede eliminar el Dia porque tiene relaciones con otros registros.", "EliminarDia " + ex.getMessage());
            }
            Logger.getLogger(DiaService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar el Dia.", ex);
            return new Respuesta(false, "Ocurrio un error al eliminar el Dia.", "EliminarDia " + ex.getMessage());
        }
    }

}

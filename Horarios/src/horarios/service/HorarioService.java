/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.service;

import horarios.model.Dia;
import horarios.model.DiaDto;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import horarios.model.Horario;
import horarios.model.HorarioDto;
import horarios.util.EntityManagerHelper;
import horarios.util.Respuesta;

public class HorarioService {

    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta getHorario(Integer ID) {
        try {
            Query qryHorario = em.createNamedQuery("Horario.findByHorId", Horario.class);
            qryHorario.setParameter("horId", ID);

            HorarioDto Horario = new HorarioDto((Horario) qryHorario.getSingleResult());

            return new Respuesta(true, "Encontrado exitosamente", "", "HorarioID", Horario);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe un Horario con el código ingresado.", "getHorario NoResultException");
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(HorarioService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Horario.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Horario.", "getHorario NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(HorarioService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Horario.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Horario.", "getHorario " + ex.getMessage());
        }
    }

    public Respuesta guardarHorario(HorarioDto horarioDto) {
        try {
            et = em.getTransaction();
            et.begin();
            Horario Horario;
            if (horarioDto.getId() != null && horarioDto.getId() > 0) {
                Horario = em.find(Horario.class, horarioDto.getId());
                if (Horario == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el Horario a modificar.", "guardarHorario NoResultException");
                }
                Horario.actualizarHorario(horarioDto);
                Horario = em.merge(Horario);
            } else {
                Horario = new Horario(horarioDto);

                em.persist(Horario);
            }
            et.commit();
            return new Respuesta(true, "Guardado exitosamente", "", "Horario", new HorarioDto(Horario));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(HorarioService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar la Horario.", ex);
            return new Respuesta(false, "Ocurrio un error al guardar la Horario.", "guardarHorario " + ex.getMessage());
        }
    }

    public Respuesta getHorarios() {
        try {
            Query qryHos = em.createNamedQuery("Horario.findAll", Horario.class);
            ArrayList<HorarioDto> Horario = new ArrayList<>();

            qryHos.getResultList().stream().forEach((hor) -> {
                Horario.add(new HorarioDto((Horario) hor));
            });

            return new Respuesta(true, "Horarios obtenido", "", "Horarios", Horario);

        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe horarios", "getHorario NoResultException");
        } catch (Exception e) {
            et.rollback();
            Logger.getLogger(HorarioService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar los Horarios", e);
            return new Respuesta(false, "Ocurrio un error al consultar los Horarios", "getHorarios" + e.getMessage());
        }
    }

    public Respuesta getDias(Integer ID) {
        try {
            Query qryHorario = em.createNamedQuery("Horario.findByHorId", Horario.class);
            qryHorario.setParameter("horId", ID);
            ArrayList<DiaDto> dias = new ArrayList<>();
            for (Dia dia : ((Horario) qryHorario.getSingleResult()).getHorDiaList()) {
                dias.add(new DiaDto(dia));
            }

            return new Respuesta(true, "Dias de los horarios encontrados exitosamente", "", "getDias", dias);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe un Horario con el código ingresado.", "getHorario NoResultException");
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(HorarioService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Horario.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Horario.", "getHorario NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(HorarioService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Horario.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Horario.", "getHorario " + ex.getMessage());
        }
    }
}

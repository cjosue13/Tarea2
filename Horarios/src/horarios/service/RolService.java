/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.service;

import horarios.model.Dia;
import horarios.model.DiaDto;
import horarios.model.Horario;
import horarios.model.HorarioDto;
import horarios.model.Puesto;
import horarios.model.PuestoDto;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import horarios.model.Rol;
import horarios.model.RolDto;
import horarios.util.EntityManagerHelper;
import horarios.util.Respuesta;
import java.sql.SQLIntegrityConstraintViolationException;

public class RolService {

    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta getRol(Integer ID) {
        try {
            Query qryRol = em.createNamedQuery("Rol.findByRolId", Rol.class);
            qryRol.setParameter("rolId", ID);

            RolDto Rol = new RolDto((Rol) qryRol.getSingleResult());
            
            return new Respuesta(true, "Encontrado exitosamente", "", "RolID", Rol);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe un Rol con el código ingresado.", "getRol NoResultException");
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(RolService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Rol.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Rol.", "getRol NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(RolService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Rol.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Rol.", "getRol " + ex.getMessage());
        }
    }
    
     public Respuesta getHorario(RolDto rol) {
        try {
            Query qryHorario = em.createNamedQuery("Horario.findByRol", Horario.class);
          
            qryHorario.setParameter("horRol", new Rol(rol));
            Horario horario = em.find(Horario.class, ((Horario) qryHorario.getSingleResult()).getHorId());
            
            ArrayList <DiaDto> dias = new ArrayList<>();
            
            for(Dia dia :(horario).getHorDiaList()){
                dias.add(new DiaDto(dia));
            }
            
            HorarioDto horarioDto = new HorarioDto(horario);
            horarioDto.setDias(dias);

            return new Respuesta(true, "Encontrado exitosamente", "", "Horario", horarioDto);
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
    
    public Respuesta guardarRol(RolDto rolDto) {
        try {
            et = em.getTransaction();
            et.begin();
            Rol rol;
            
            if (rolDto.getId() != null && rolDto.getId() > 0) {
                rol = em.find(Rol.class, rolDto.getId());
                if (rol == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el Rol a modificar.", "guardarRol NoResultException");
                }

                rol.actualizarRol(rolDto);
                if (rolDto.getPuestos() != null && !rolDto.getPuestos().isEmpty()) {
                    for (PuestoDto pues : rolDto.getPuestos()) {
                        Puesto puesto = em.find(Puesto.class, pues.getId());
                        puesto.getRolList().add(rol);
                        rol.getPuestoList().add(puesto);
                        //Revisar lo del orden de rotacion
                        
                    }
                }
                
                rol = em.merge(rol);
            } else {
                rol = new Rol(rolDto);
                
                if (rolDto.getPuestos() != null && !rolDto.getPuestos().isEmpty()) {
                    for (PuestoDto pues : rolDto.getPuestos()) {
                        Puesto puesto = em.find(Puesto.class, pues.getId());
                        puesto.getRolList().add(rol);
                        rol.getPuestoList().add(puesto);
                    }
                }
                em.persist(rol);
            }
            et.commit();
            return new Respuesta(true, "Guardado exitosamente", "", "Rol", new RolDto(rol));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(RolService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar la Rol.", ex);
            return new Respuesta(false, "Ocurrio un error al guardar la Rol.", "guardarRol " + ex.getMessage());
        }
    }

    public Respuesta getRoles() {
        try {
            Query qryRol = em.createNamedQuery("Rol.findAll", Rol.class);
            ArrayList<RolDto> Rol = new ArrayList<>();

            qryRol.getResultList().stream().forEach((rol) -> {
                Rol.add(new RolDto((Rol) rol));
            });

            return new Respuesta(true, "Roles obtenidos", "", "Roles", Rol);

        } catch (NoResultException ex) {
            return new Respuesta(false, "No existen resultados.", "getRol NoResultException");
        } catch (Exception e) {
            et.rollback();
            Logger.getLogger(RolService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar los Rols.", e);
            return new Respuesta(false, "Ocurrio un error al consultar los Rol.", "getRol " + e.getMessage());
        }
    }

    public Respuesta EliminarRol(Integer id) {
        try {
            et = em.getTransaction();
            et.begin();
            Rol Rol;
            if (id != null && id > 0) {
                Rol = em.find(Rol.class, id);
                if (Rol == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el Rol a eliminar.", "EliminarRol NoResultException");
                }
                em.remove(Rol);
            } else {
                et.rollback();
                return new Respuesta(false, "Debe cargar el Rol a eliminar.", "EliminarRol NoResultException");
            }
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            et.rollback();
            if (ex.getCause() != null && ex.getCause().getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                return new Respuesta(false, "No se puede eliminar el Rol porque tiene relaciones con otros registros.", "EliminarRol " + ex.getMessage());
            }
            Logger.getLogger(RolService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar el Rol.", ex);
            return new Respuesta(false, "Ocurrio un error al eliminar el Rol.", "EliminarRol " + ex.getMessage());
        }
    }
}

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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import horarios.model.Puesto;
import horarios.model.PuestoDto;
import horarios.model.Rol;
import horarios.model.RolDto;
import horarios.util.EntityManagerHelper;
import horarios.util.Respuesta;
import java.sql.SQLIntegrityConstraintViolationException;

public class PuestoService {

    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;
    public Respuesta getPuesto(Integer ID) {
        try {
            Query qryPuesto = em.createNamedQuery("Puesto.findByPueCodigo", Puesto.class);
            qryPuesto.setParameter("pueCodigo", ID);
            
            PuestoDto Puesto = new PuestoDto((Puesto) qryPuesto.getSingleResult());
            
            return new Respuesta(true, "Encontrado exitosamente", "", "PuestoID", Puesto);
        }  catch (NoResultException ex) {
            return new Respuesta(false, "No existe un Puesto con el código ingresado.", "getPuesto NoResultException");
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(PuestoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Puesto.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Puesto.", "getPuesto NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(PuestoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Puesto.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Puesto.", "getPuesto " + ex.getMessage());
        }
    }  
    
    public Respuesta getRoles(Integer puestoID) {
        try {
            Query qryPuesto = em.createNamedQuery("Puesto.findByPueCodigo", Puesto.class);
            qryPuesto.setParameter("pueCodigo", puestoID);
            ArrayList <RolDto> roles = new ArrayList <>();
            for(Rol rol : ((Puesto) qryPuesto.getSingleResult()).getRolList()){
                RolDto rolDto = new RolDto(rol);
                System.out.println("Rol");
                ArrayList <DiaDto> dias = new ArrayList<>();
                HorarioDto horarioDto = new HorarioDto(rol.getHorario());
                for(Dia dia: rol.getHorario().getHorDiaList()){
                    System.out.println("Dia");
                    dias.add(new DiaDto(dia));
                }
                horarioDto.setDias(dias);
                rolDto.setHorario(horarioDto);
                roles.add(rolDto);
                //((Puesto) qryPuesto.getSingleResult()).getRolList()
            }
            
            
            
            PuestoDto puesto = new PuestoDto((Puesto) qryPuesto.getSingleResult());
            puesto.setRoles(roles);
            
            return new Respuesta(true, "Encontrado exitosamente", "", "roles", puesto);
        }  catch (NoResultException ex) {
            return new Respuesta(false, "No existe un Puesto con el código ingresado.", "getPuesto NoResultException");
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(PuestoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Puesto.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Puesto.", "getPuesto NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(PuestoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Puesto.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Puesto.", "getPuesto " + ex.getMessage());
        }
    }  
    
    public Respuesta guardarPuesto(PuestoDto PuestoDto) {
        try {
            et = em.getTransaction();
            et.begin();
            Puesto Puesto;
            if (PuestoDto.getId()!= null && PuestoDto.getId()> 0){
                Puesto = em.find(Puesto.class, PuestoDto.getId());
                if (Puesto == null){
                    et.rollback();
                    return new Respuesta(false, "No se encontró el Puesto a modificar.", "guardarPuesto NoResultException");
                }
                Puesto.actualizarPuesto(PuestoDto);
                Puesto = em.merge(Puesto);
            }else{
                Puesto = new Puesto(PuestoDto);
                em.persist(Puesto);
            }
            et.commit();
            return new Respuesta(true, "Guardado exitosamente", "", "Puesto", new PuestoDto(Puesto));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(PuestoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar la Puesto.", ex);
            return new Respuesta(false, "Ocurrio un error al guardar el Puesto.", "guardarPuesto " + ex.getMessage());
        }
    }
    
    public Respuesta getPuestos() {
        try {
            Query qryPues = em.createNamedQuery("Puesto.findAll", Puesto.class);
            ArrayList <PuestoDto> puestos = new ArrayList<>();
            
            qryPues.getResultList().stream().forEach((pues)->{
                puestos.add(new PuestoDto((Puesto)pues));
            });
            
            return new Respuesta(true, "Puestos obtenidos", "", "Puestos", puestos);
            
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existen Puestos", "getPuestos NoResultException");
        } catch (Exception e) {
            et.rollback();
            Logger.getLogger(PuestoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar los Puestos.", e);
            return new Respuesta(false, "Ocurrio un error al consultar los Puestos", "getPuestos " + e.getMessage());
        }
    }    
     public Respuesta EliminarPuesto(Integer id) {
        try {
            et = em.getTransaction();
            et.begin();
            Puesto Puesto;
            if(id != null && id >0){
                Puesto = em.find(Puesto.class, id);
                if(Puesto == null){
                    et.rollback();
                    return new Respuesta(false, "No se encontró el Puesto a eliminar.", "EliminarPuesto NoResultException");
                }
                em.remove(Puesto);
            }else{
                et.rollback();
                return new Respuesta(false, "Debe cargar el Puesto a eliminar.", "EliminarPuesto NoResultException");
            }
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            et.rollback();
            if (ex.getCause() != null && ex.getCause().getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                return new Respuesta(false, "No se puede eliminar el Puesto porque tiene relaciones con otros registros.", "EliminarPuesto " + ex.getMessage());
            }
            Logger.getLogger(PuestoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar el Puesto.", ex);
            return new Respuesta(false, "Ocurrio un error al eliminar el Puesto.", "EliminarPuesto " + ex.getMessage());
        }
    } 
  
}

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
import horarios.model.Empleado;
import horarios.model.EmpleadoDto;
import horarios.model.Puesto;
import horarios.model.PuestoDto;
import horarios.util.EntityManagerHelper;
import horarios.util.Respuesta;
import java.sql.SQLIntegrityConstraintViolationException;

public class EmpleadoService {

    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta getEmpleado(Integer ID) {
        try {
            Query qryEmpleado = em.createNamedQuery("Empleado.findByEmpFolio", Empleado.class);
            qryEmpleado.setParameter("empFolio", ID);

            EmpleadoDto Empleado = new EmpleadoDto((Empleado) qryEmpleado.getSingleResult());

            return new Respuesta(true, "Encontrado exitosamente", "", "EmpleadoID", Empleado);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe un Empleado con el código ingresado.", "getEmpleado NoResultException");
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(EmpleadoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Empleado.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Empleado.", "getEmpleado NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(EmpleadoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el Empleado.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el Empleado.", "getEmpleado " + ex.getMessage());
        }
    }

    public Respuesta guardarEmpleado(EmpleadoDto EmpleadoDto) {
        try {
            et = em.getTransaction();
            et.begin();
            Empleado Empleado;
            if (EmpleadoDto.getId() != null && EmpleadoDto.getId() > 0) {
                Empleado = em.find(Empleado.class, EmpleadoDto.getId());
                if (Empleado == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el Empleado a modificar.", "guardarEmpleado NoResultException");
                }
                Empleado.actualizarEmpleado(EmpleadoDto);
                Empleado = em.merge(Empleado);
            } else {
                Empleado = new Empleado(EmpleadoDto);
                em.persist(Empleado);
            }
            et.commit();
            return new Respuesta(true, "Guardado exitosamente", "", "Empleado", new EmpleadoDto(Empleado));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(EmpleadoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar la Empleado.", ex);
            return new Respuesta(false, "Ocurrio un error al guardar la Empleado.", "guardarEmpleado " + ex.getMessage());
        }
    }

    public Respuesta getEmpleados() {
        try {
            Query qryEmp = em.createNamedQuery("Empleado.findAll", Empleado.class);
            ArrayList<EmpleadoDto> empleados = new ArrayList<>();

            qryEmp.getResultList().stream().forEach((emp) -> {
                EmpleadoDto empleado = new EmpleadoDto((Empleado) emp);

                if (((Empleado) emp).getPuesto() != null) {
                    PuestoDto puesto = new PuestoDto(((Empleado) emp).getPuesto());
                    empleado.setPuesto(puesto);
                }
                empleados.add(empleado);
            });

            return new Respuesta(true, "Empleados obtenidos", "", "Empleados", empleados);

        } catch (NoResultException ex) {
            return new Respuesta(false, "No existen empleados", "getEmpleados NoResultException");
        } catch (Exception e) {
            et.rollback();
            Logger.getLogger(EmpleadoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar los Empleadoss", e);
            return new Respuesta(false, "Ocurrio un error al consultar los Empleados", "getEmpleados " + e.getMessage());
        }
    }

    public Respuesta eliminarEmpleado(Integer id) {
        try {
            et = em.getTransaction();
            et.begin();
            Empleado empleado;
            if (id != null && id > 0) {
                empleado = em.find(Empleado.class, id);
                if (empleado == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el empleado a eliminar.", "EliminarEmpleado NoResultException");
                }
                em.remove(empleado);
            } else {
                et.rollback();
                return new Respuesta(false, "Debe cargar el empleado a eliminar.", "EliminarEmpleado NoResultException");
            }
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            et.rollback();
            if (ex.getCause() != null && ex.getCause().getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                return new Respuesta(false, "No se puede eliminar el empleado porque tiene relaciones con otros registros.", "EliminarEmpleado " + ex.getMessage());
            }
            Logger.getLogger(EmpleadoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar el empleado.", ex);
            return new Respuesta(false, "Ocurrio un error al eliminar el empleado.", "EliminarEmpleado " + ex.getMessage());
        }
    }
}

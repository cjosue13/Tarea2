/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import horarios.model.HorarioDto;
import horarios.model.PueRolDto;
import horarios.model.PuestoDto;
import horarios.model.RolDto;
import horarios.service.PueRolService;
import horarios.service.PuestoService;
import horarios.service.RolService;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class AsignacionRolesController extends Controller {

    @FXML
    private Label Titulo;
    @FXML
    private TableView<PuestoDto> tablePuestos;
    @FXML
    private TableColumn<PuestoDto, String> COL_PUESTO_EMP;
    @FXML
    private TableColumn<PuestoDto, String> COL_NOMBRE_EMP;
    @FXML
    private TableColumn<PuestoDto, Number> COL_FOLIO_EMP;
    @FXML
    private JFXButton btnAsignarRol;
    @FXML
    private JFXTextField txtfolio;
    @FXML
    private JFXTextField txtNombre;
    @FXML
    private JFXTextField txtPuesto;
    @FXML
    private TableView<RolDto> TV_ROLES_NO_ROTATIVOS;
    @FXML
    private TableColumn<RolDto, String> COL_ROL_NO_ROTATIVO;
    @FXML
    private TableView<PueRolDto> TV_ROLES_ROTATIVOS;
    @FXML
    private TableColumn<PueRolDto, String> COL_ROL_ROTATIVO;
    @FXML
    private TableColumn<PueRolDto, Number> COL_ORDEN_ROL;
    @FXML
    private TableView<RolDto> TV_ASIGNAR_ROLES;
    @FXML
    private TableColumn<RolDto, String> COL_ASIGNAR_ROLES;
    @FXML
    private TableColumn<RolDto, String> COL_ASIGNAR_ROTATIVO;
    private PuestoDto puesto;
    private RolDto rol;
    private PuestoService puesService;
    private Respuesta respPues;
    private ArrayList<PuestoDto> puestos;
    private ObservableList itemsPuestos;
    private Mensaje ms;
    private RolService rolservice;
    private Respuesta respRol;
    private ArrayList<RolDto> roles;
    private ObservableList itemsRoles;
    private ArrayList<RolDto> rolesR;
    private ArrayList<RolDto> rolesNR;
    private ArrayList<PueRolDto> pueRoles;

    @Override
    public void initialize() {
        inicio();
    }

    private void inicio() {
        pueRoles = new ArrayList();
        btnAsignarRol.setCursor(Cursor.HAND);
        puesService = new PuestoService();
        ms = new Mensaje();
        respPues = puesService.getPuestos();
        puestos = ((ArrayList) respPues.getResultado("Puestos"));

        COL_NOMBRE_EMP.setCellValueFactory(value -> new SimpleStringProperty((value.getValue().getEmpleado() != null) ? value.getValue().
                getEmpleado().getNombre() + " " + value.getValue().getEmpleado().getApellido() : "Sin asignar"));
        COL_PUESTO_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombrePuesto()));
        COL_FOLIO_EMP.setCellValueFactory(value -> new SimpleIntegerProperty((value.getValue().getEmpleado() != null) ? value.getValue().
                getEmpleado().getId() : 0));
        itemsPuestos = FXCollections.observableArrayList(puestos);
        tablePuestos.setItems(itemsPuestos);

        rolservice = new RolService();
        respRol = rolservice.getRoles();
        roles = ((ArrayList) respRol.getResultado("Roles"));
        COL_ASIGNAR_ROLES.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombreRol()));
        COL_ASIGNAR_ROTATIVO.setCellValueFactory(value -> new SimpleStringProperty((value.getValue().getHorarioRotativo().equals("Y")) ? "Si" : "No"));
        itemsRoles = FXCollections.observableArrayList(roles);
        TV_ASIGNAR_ROLES.setItems(itemsRoles);
        roles.clear();
        /*
            Para cuando se seleccione el empleado
         */
        COL_ROL_NO_ROTATIVO.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombreRol()));

        COL_ORDEN_ROL.setCellValueFactory(value -> new SimpleIntegerProperty(value.getValue().getOrdenRotacion()));
        COL_ROL_ROTATIVO.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getRolId().getNombreRol()));
    }

    private boolean ValidarAsignacion() {
        if (tablePuestos.getSelectionModel() == null || TV_ASIGNAR_ROLES.getSelectionModel() == null) {
            return false;
        } else {
            return !(tablePuestos.getSelectionModel().getSelectedItem() == null
                    || TV_ASIGNAR_ROLES.getSelectionModel().getSelectedItem() == null);
        }
    }

    @FXML
    private void AsignarRol(ActionEvent event) {
        if (ValidarAsignacion()) { // si devuelve true entonces seleccionó bien
            if (puesto.getEmpleado() != null) {
                puesto.setRoles(roles);
                try {
                    /*
                     Si el puesto ya tiene un empleado asignado, debe enviar un correo con el horario asignado al rol
                     */
                    if (puesto.getEmpleado() != null) {

                    }

                    PueRolService prs = new PueRolService();
                    pueRoles.stream().forEach((pueRol) -> {
                        prs.guardarTablaRelacional(pueRol);
                    });
                    //respPues = puesService.guardarPuesto(puesto);
                    ms.showModal(Alert.AlertType.INFORMATION, "Informacion de guardado", this.getStage(), "Elementos guardados");
                    limpiarValores();

                } catch (Exception e) {
                    ms.showModal(Alert.AlertType.ERROR, "Informacion de guardado", this.getStage(), "Ocurrio un error al asignar un rol al empleado");
                }
            } else {
                ms.showModal(Alert.AlertType.WARNING, "Informacion de registro", this.getStage(), "El puesto no tiene asignado ningun empleado. Debes asignarle un empledo previamente");
            }
        } else {
            ms.showModal(Alert.AlertType.WARNING, "Informacion de registro", this.getStage(), "No has seleccionado alguno de los datos de la tabla.");
        }
    }

    @FXML
    private void DatosEmpleado(MouseEvent event) {
        if (tablePuestos.getSelectionModel() != null) {
            if (tablePuestos.getSelectionModel().getSelectedItem() != null) {
                puesto = tablePuestos.getSelectionModel().getSelectedItem();
                //Muestra los roles rotativos y no rotativos del empleado o puesto 
                rolesEmpleado(puesto);

                txtfolio.setText(String.valueOf((puesto.getEmpleado() != null) ? puesto.getEmpleado().getId() : 0));
                txtNombre.setText((puesto.getEmpleado() != null) ? puesto.getEmpleado().getNombre() + " "
                        + puesto.getEmpleado().getApellido() : " ");
                txtPuesto.setText(puesto.getNombrePuesto());
            }
        }
    }

    @FXML
    private void DatosRol(MouseEvent event) {
        if (TV_ASIGNAR_ROLES.getSelectionModel() != null) {
            if (TV_ASIGNAR_ROLES.getSelectionModel() != null) {
                if (tablePuestos.getSelectionModel() != null && tablePuestos.getSelectionModel().getSelectedItem() != null) {
                    //Tomo los rolesRotativos del empleado
                    pueRoles = new ArrayList(TV_ROLES_ROTATIVOS.getItems());
                    rolesR.clear();
                    pueRoles.stream().forEach(z -> {

                        rolesR.add(new RolDto(z.getRolId().getNombreRol(), z.getRolId().getHorarioRotativo(), z.getRolId().getVersion(), z.getRolId().getId(), z.getRolId().getHorario()));

                    });

                    // rolesR = new ArrayList(TV_ROLES_ROTATIVOS.getItems());
                    //tomo los rolesNORotativos del empleado
                    rolesNR = new ArrayList(TV_ROLES_NO_ROTATIVOS.getItems());
                    rol = TV_ASIGNAR_ROLES.getSelectionModel().getSelectedItem();

                    //Obtengo el horario de ese rol
                    respRol = rolservice.getHorario(rol);
                    rol.setHorario((HorarioDto) respRol.getResultado("Horario"));
                    //Si el horario es rotativo
                    if (rol.getHorarioRotativo().equals("Y")) {
                        if (!rolesR.isEmpty() && rolesR.stream().allMatch(x -> !x.getId().equals(rol.getId()))) {
                            //Si la lista esta vacia el orden inicia en 1
                            //rol.getHorario().setOrdenRotacion(rolesR.size() + 1);
                            rolesR.add(rol);

                            //Lleno la lista para que se almacene en el empleado
                            pueRoles.add(new PueRolDto(null, rolesR.size(), puesto, rol));
                            itemsRoles = FXCollections.observableArrayList(pueRoles);
                            TV_ROLES_ROTATIVOS.setItems(itemsRoles);
                            roles.add(rol);
                        } else {
                            //Si la lista esta vacia la seteamos al tableView
                            if (rolesR.isEmpty()) {
                                pueRoles.add(new PueRolDto(null, 1, puesto, rol));
                                rolesR.add(rol);
                                itemsRoles = FXCollections.observableArrayList(pueRoles);
                                TV_ROLES_ROTATIVOS.setItems(itemsRoles);
                                //Lleno la lista para que se almacene en el empleado
                                roles.add(rol);
                            } else {
                                ms.showModal(Alert.AlertType.WARNING, "Informacion de registro", this.getStage(), "Ya has agregado este rol al empleado");
                            }
                        }
                    } else {
                        if (!rolesNR.isEmpty() && rolesNR.stream().allMatch(x -> !x.getId().equals(rol.getId()))) {
                            rolesNR.add(rol);
                            pueRoles.add(new PueRolDto(null, 0, puesto, rol));
                            itemsRoles = FXCollections.observableArrayList(rolesNR);
                            TV_ROLES_NO_ROTATIVOS.setItems(itemsRoles);
                            //Lleno la lista para que se almacene en el empleado
                            roles.add(rol);
                        } else {
                            //Si la lista esta vacia la seteamos al tableView
                            if (rolesNR.isEmpty()) {
                                rolesNR.add(rol);
                                pueRoles.add(new PueRolDto(null, 0, puesto, rol));
                                itemsRoles = FXCollections.observableArrayList(rolesNR);
                                TV_ROLES_NO_ROTATIVOS.setItems(itemsRoles);
                                //Lleno la lista para que se almacene en el empleado
                                roles.add(rol);
                            } else {
                                ms.showModal(Alert.AlertType.WARNING, "Informacion de registro", this.getStage(), "Ya has agregado este rol al empleado");
                            }
                        }
                    }
                } else {
                    ms.showModal(Alert.AlertType.WARNING, "Informacion de registro", this.getStage(), "No has seleccionado un empleado para asignar roles");
                }
            }
        }
    }

    @FXML
    private void limpiarValores() {
        txtfolio.clear();
        txtNombre.clear();
        txtPuesto.clear();
        tablePuestos.getSelectionModel().clearSelection();
        TV_ASIGNAR_ROLES.getSelectionModel().clearSelection();
        TV_ROLES_NO_ROTATIVOS.getItems().clear();
        TV_ROLES_ROTATIVOS.getItems().clear();
        rol = null;
        puesto = null;
        respPues = puesService.getPuestos();
        roles.clear();
        puestos = ((ArrayList) respPues.getResultado("Puestos"));
        itemsPuestos = FXCollections.observableArrayList(puestos);
        tablePuestos.setItems(itemsPuestos);
        pueRoles.clear();
    }

    @FXML
    private void roles_no_rotativos(MouseEvent event) {
        System.out.println("ROLES NO ROTATIVOS");
    }

    @FXML
    private void roles_rotativos(MouseEvent event) {
        System.out.println("ROLES ROTATIVOS");
    }

    /*
        Muestra los roles rotativos y no rotativos del empleado
     */
    private void rolesEmpleado(PuestoDto puesto) {
        rolesR = new ArrayList<>();
        rolesNR = new ArrayList<>();

        puesto.getRoles().stream().forEach((rolDto) -> {
            if (rolDto.getHorarioRotativo().equals("N")) {
                rolesNR.add(rolDto);
            } else {
                rolesR.add(rolDto);
            }
        });

        ArrayList<PueRolDto> lista = new ArrayList();

        PueRolService puerolService = new PueRolService();

        pueRoles = (ArrayList) puerolService.getRelaciones().getResultado("getRelaciones");

        rolesR.stream().forEach(x -> {

            pueRoles.stream().forEach(y -> {

                if (y.getRolId().getId() == x.getId() && y.getPueCodigo().getId() == puesto.getId()) {
                    lista.add(y);
                }

            });

        });

        pueRoles.clear();

        itemsRoles = FXCollections.observableArrayList(lista);
        /*
        Ordena la lista de roles con respecto al número
         */
        //   System.out.println(lista.size());

        itemsRoles.sort((t, t1) -> {
            if (((PueRolDto) t).getOrdenRotacion() > ((PueRolDto) t1).getOrdenRotacion()) {
                return 1;
            } else {
                return 0;
            }
        });

        TV_ROLES_ROTATIVOS.setItems(itemsRoles);
        itemsRoles = FXCollections.observableArrayList(rolesNR);
        TV_ROLES_NO_ROTATIVOS.setItems(itemsRoles);
    }

    @FXML
    private void rotarRoles(ActionEvent event) {
        pueRoles = new ArrayList(TV_ROLES_ROTATIVOS.getItems());
        if (tablePuestos.getSelectionModel() != null && tablePuestos.getSelectionModel().getSelectedItem() != null && !pueRoles.isEmpty()) {

            if (pueRoles.size() > 1) {
                if (pueRoles.size() > 2) {
                    pueRoles.get(pueRoles.size() - 1).setOrdenRotacion(1);
                    pueRoles.get(0).setOrdenRotacion(2);
                    pueRoles.stream().forEach(x -> {
                        if (pueRoles.indexOf(x) != 0 && pueRoles.indexOf(x) != pueRoles.size() - 1) {
                            x.setOrdenRotacion(x.getOrdenRotacion() + 1);
                        }

                    });
                } else {
                    pueRoles.get(pueRoles.size() - 1).setOrdenRotacion(1);
                    pueRoles.get(0).setOrdenRotacion(2);
                }

                PueRolService prs = new PueRolService();
                pueRoles.stream().forEach((pueRol) -> {
                    prs.guardarTablaRelacional(pueRol);
                });
                TV_ROLES_ROTATIVOS.getItems().clear();
                itemsRoles = FXCollections.observableArrayList(pueRoles);
                itemsRoles.sort((t, t1) -> {
                    if (((PueRolDto) t).getOrdenRotacion() > ((PueRolDto) t1).getOrdenRotacion()) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
                TV_ROLES_ROTATIVOS.setItems(itemsRoles);
            }
        }
    }
}

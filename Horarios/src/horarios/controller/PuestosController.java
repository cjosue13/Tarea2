/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import horarios.model.EmpleadoDto;
import horarios.model.PuestoDto;
import horarios.service.EmpleadoService;
import horarios.service.PuestoService;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
public class PuestosController extends Controller {

    @FXML
    private Label Titulo;
    @FXML
    private JFXButton btnEditar1;
    @FXML
    private JFXButton btnEliminar;
    @FXML
    private JFXButton btnAgregar1;
    @FXML
    private JFXTextField txtNombre;
    @FXML
    private JFXTextArea txtDescripcion;
    @FXML
    private TableColumn<PuestoDto, String> COL_NOMBRE_PUES;
    @FXML
    private TableColumn<PuestoDto, String> COL_DESCRIPCION_PUES;
    @FXML
    private TableColumn<PuestoDto, Number> COL_CODIGO_PUES;
    @FXML
    private TableView<PuestoDto> tablePuesto;
    @FXML
    private TableView<EmpleadoDto> tableEmpleado;
    @FXML
    private TableColumn<EmpleadoDto, Number> COL_ID_EMP;
    @FXML
    private TableColumn<EmpleadoDto, String> COL_NOMBRE_EMP;
    @FXML
    private JFXTextField txtEmpleado;
    @FXML
    private TableColumn<PuestoDto, String> COL_EMPLEADO_PUE;
    private PuestoService puesService;
    private EmpleadoService empService;
    private Respuesta resp;
    private ArrayList<PuestoDto> puestos;
    private ArrayList<EmpleadoDto> empleados;
    private ObservableList itemsPues;
    private ObservableList itemsEmp;
    private Mensaje ms;
    private PuestoDto puesto;
    private EmpleadoDto empleado;

    @Override
    public void initialize() {
        inicio();
    }

    @FXML
    private void editar(ActionEvent event) {
        if (tablePuesto.getSelectionModel() != null) {
            if (tablePuesto.getSelectionModel().getSelectedItem() != null) {
                if (registroCorrecto()) {
                    String nombre = txtNombre.getText();
                    String descripcion = txtDescripcion.getText();
                    Integer id = puesto.getId();
                    Integer version = puesto.getVersion() + 1;

                    //Si el empleado ya tiene un puesto 
                    if (empleado != null && empleado.getPuesto() != null) {
                        //Si el puesto no es igual al que ya tiene
                        if (!empleado.getPuesto().getId().equals(puesto.getId())) {
                            //Si ya existe un rol con horario para el nuevo puesto asignado para el empleado
                            if (!puesto.getRoles().isEmpty()) {
                                //Deberia enviar un correo al nuevo empleado con el nuevo horario asignado

                            }
                            try {
                                //Tomo el puesto anterior que tenia el empleado y lo dejo como vacante libre
                                PuestoDto pues = empleado.getPuesto();
                                pues.setEmpleado(null);
                                puesService.guardarPuesto(pues);
                            } catch (Exception e) {

                            }
                        }
                    } else if (empleado == null) {
                        System.out.println("");
                        empleado = puesto.getEmpleado();
                    }

                    puesto = new PuestoDto(nombre, descripcion, version, empleado, id);

                    try {
                        resp = puesService.guardarPuesto(puesto);

                        ms.showModal(Alert.AlertType.INFORMATION, "Informacion de guardado", this.getStage(), resp.getMensaje());
                        limpiarValores();
                        puestos = (ArrayList) puesService.getPuestos().getResultado("Puestos");
                        tablePuesto.getItems().clear();
                        itemsPues = FXCollections.observableArrayList(puestos);
                        tablePuesto.setItems(itemsPues);

                    } catch (Exception e) {
                        ms.showModal(Alert.AlertType.ERROR, "Informacion de guardado", this.getStage(), "Hubo un error al momento de guardar el Puesto. "
                                + "Verifica que todos los datos esten llenados correctamente o que el empleado no tenga un puesto asignado");
                    }
                } else {
                    ms.showModal(Alert.AlertType.ERROR, "Informacion acerca del guardado", this.getStage(), "Existen datos erroneos en el registro, "
                            + "verifica que todos los datos esten llenos.");
                }
            } else {
                ms.showModal(Alert.AlertType.WARNING, "Información", this.getStage(), "Debes seleccionar el elemento a editar");
            }
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (tablePuesto.getSelectionModel() != null) {
            if (tablePuesto.getSelectionModel().getSelectedItem() != null) {
                puesService.EliminarPuesto(tablePuesto.getSelectionModel().getSelectedItem().getId());
                ms.showModal(Alert.AlertType.INFORMATION, "Información", this.getStage(), "Datos eliminados correctamente");
                resp = puesService.getPuestos();
                itemsPues.clear();
                puestos = (ArrayList) resp.getResultado("Puestos");
                itemsPues = FXCollections.observableArrayList(puestos);
                tablePuesto.setItems(itemsPues);
                limpiarValores();
            } else {
                ms.showModal(Alert.AlertType.WARNING, "Información", this.getStage(), "Debes seleccionar el elemento a eliminar");
            }
        }
    }

    @FXML
    private void agregar(ActionEvent event) {
        if (registroCorrecto()) {
            if (tablePuesto.getSelectionModel() != null) {
                if (tablePuesto.getSelectionModel().getSelectedItem() == null) {
                    if (empleado.getPuesto() == null) {
                        String nombre = txtNombre.getText();
                        String descripcion = txtDescripcion.getText();

                        puesto = new PuestoDto(nombre, descripcion, 1, empleado, null);
                        try {
                            resp = puesService.guardarPuesto(puesto);

                            ms.showModal(Alert.AlertType.INFORMATION, "Informacion de guardado", this.getStage(), resp.getMensaje());
                            limpiarValores();
                            puestos = (ArrayList) puesService.getPuestos().getResultado("Puestos");
                            tablePuesto.getItems().clear();
                            itemsPues = FXCollections.observableArrayList(puestos);
                            tablePuesto.setItems(itemsPues);

                        } catch (Exception e) {
                            ms.showModal(Alert.AlertType.ERROR, "Informacion de guardado", this.getStage(), "Hubo un error al momento de guardar el Puesto. "
                                    + "Verifica que todos los datos esten llenos correctamente o que el empleado no tenga un puesto asignado");
                        }
                    }else{
                         ms.showModal(Alert.AlertType.WARNING, "Informacion del registro", this.getStage(), "Este empleado ya tiene un puesto asignado,"
                                 + " si deseas asignarle un puesto diferente, debes seleccionarlo y luego editarlo.");
                    }
                } else {
                    ms.showModal(Alert.AlertType.WARNING, "Informacion acerca del guardado", this.getStage(), "Se ha elegido un puesto para editar, "
                            + "debes limpiar el registro.");
                }
            }

        } else {
            ms.showModal(Alert.AlertType.ERROR, "Informacion acerca del guardado", this.getStage(), "Existen datos erroneos en el registro, "
                    + "verifica que todos los datos esten llenos.");
        }
    }

    private boolean registroCorrecto() {
        return !txtNombre.getText().isEmpty() && !txtEmpleado.getText().isEmpty();
    }

    private void limpiarValores() {
        txtNombre.clear();
        txtDescripcion.clear();
        txtEmpleado.clear();
        empleado = null;
        tableEmpleado.getSelectionModel().clearSelection();
        tablePuesto.getSelectionModel().clearSelection();
    }

    public void inicio() {
        puesService = new PuestoService();
        empService = new EmpleadoService();

        ms = new Mensaje();
        resp = puesService.getPuestos();

        puestos = ((ArrayList<PuestoDto>) resp.getResultado("Puestos"));
        COL_NOMBRE_PUES.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombrePuesto()));
        COL_DESCRIPCION_PUES.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getDescripcion()));
        COL_CODIGO_PUES.setCellValueFactory(value -> new SimpleIntegerProperty(value.getValue().getId()));
        COL_EMPLEADO_PUE.setCellValueFactory(value -> new SimpleStringProperty((value.getValue().getEmpleado() != null)
                ? value.getValue().getEmpleado().getNombre() + " "
                + value.getValue().getEmpleado().getApellido() : " "));

        resp = empService.getEmpleados();
        empleados = (ArrayList) resp.getResultado("Empleados");

        COL_NOMBRE_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombre()+" "+
                value.getValue().getApellido()));
        COL_ID_EMP.setCellValueFactory(value -> new SimpleIntegerProperty(value.getValue().getId()));

        itemsPues = FXCollections.observableArrayList(puestos);
        itemsEmp = FXCollections.observableArrayList(empleados);
        tablePuesto.setItems(itemsPues);
        tableEmpleado.setItems(itemsEmp);
    }

    @FXML
    private void agregarPersona(MouseEvent event) {
        if (tableEmpleado.getSelectionModel() != null) {
            if (tableEmpleado.getSelectionModel().getSelectedItem() != null) {
                empleado = tableEmpleado.getSelectionModel().getSelectedItem();
                txtEmpleado.setText(empleado.getNombre() + " " + empleado.getApellido());
            }
        }
    }

    @FXML
    private void DatosPuestos(MouseEvent event) {
        if (tablePuesto.getSelectionModel() != null) {
            if (tablePuesto.getSelectionModel().getSelectedItem() != null) {
                puesto = tablePuesto.getSelectionModel().getSelectedItem();
                txtNombre.setText(puesto.getNombrePuesto());
                txtDescripcion.setText(puesto.getDescripcion());
                txtEmpleado.setText((puesto.getEmpleado() != null) ? puesto.getEmpleado().getNombre() + " " + puesto.getEmpleado().getApellido() : " ");
                tableEmpleado.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    private void limpiarRegistro(ActionEvent event) {
        limpiarValores();
    }
    
}

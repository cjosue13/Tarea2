/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import horarios.model.EmpleadoDto;
import horarios.service.EmpleadoService;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class EmpleadosController extends Controller {

    @FXML
    private TableView<EmpleadoDto> table;
    @FXML
    private TableColumn<EmpleadoDto, String> COL_NOMBRE_EMP;
    @FXML
    private TableColumn<EmpleadoDto, String> COL_APELLIDO_EMP;
    @FXML
    private TableColumn<EmpleadoDto, String> COL_CEDULA_EMP;
    @FXML
    private TableColumn<EmpleadoDto, String> COL_CORREO_EMP;
    @FXML
    private Label Titulo;
    @FXML
    private JFXButton btnEliminar;
    @FXML
    private JFXTextField txtNombre;
    @FXML
    private JFXButton btnEditar1;
    @FXML
    private JFXButton btnAgregar1;
    @FXML
    private JFXTextField txtApellidos;
    @FXML
    private JFXTextField txtCedula;
    @FXML
    private JFXTextField txtCorreo;

    private EmpleadoDto empleado;
    private EmpleadoService empService;
    private Respuesta resp;
    private ArrayList<EmpleadoDto> empleados;
    private ObservableList items;
    private Mensaje ms;

    @Override
    public void initialize() {
        inicio();
    }

    public void inicio() {
        empService = new EmpleadoService();
        ms = new Mensaje();
        resp = empService.getEmpleados();
        empleados = ((ArrayList<EmpleadoDto>) resp.getResultado("Empleados"));
        COL_NOMBRE_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombre()));
        COL_APELLIDO_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getApellido()));
        COL_CEDULA_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getCedula()));
        COL_CORREO_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getCorreo()));
        items = FXCollections.observableArrayList(empleados);
        table.setItems(items);
    }

    @FXML
    private void editar(ActionEvent event) {
        if (table.getSelectionModel() != null) {
            if (table.getSelectionModel().getSelectedItem() != null) {
                if (registroCorrecto()) {
                    Integer id = empleado.getId();
                    String nombre = txtNombre.getText();
                    String apellido = txtApellidos.getText();
                    String correo = txtCorreo.getText();
                    String cedula = txtCedula.getText();
                    Integer version = table.getSelectionModel().getSelectedItem().getVersion() + 1;

                    //Introducir la cantidad de horas trabajadas al empleado
                    empleado = new EmpleadoDto(nombre, apellido, cedula, correo, empleado.getCantidadHoras(), version, id);
                    try {
                        resp = empService.guardarEmpleado(empleado);
                        ms.showModal(AlertType.INFORMATION, "Informacion de Edición", this.getStage(), resp.getMensaje());
                        limpiarValores();
                        empleados = (ArrayList) empService.getEmpleados().getResultado("Empleados");
                        table.getItems().clear();
                        items = FXCollections.observableArrayList(empleados);
                        table.setItems(items);
                    } catch (Exception e) {
                        ms.showModal(AlertType.ERROR, "Informacion de guardado", this.getStage(), "Hubo un error al momento de guardar el empleado.");
                    }
                } else {
                    ms.showModal(AlertType.ERROR, "Informacion acerca del guardado", this.getStage(), "Existen datos erroneos en el registro, "
                            + "verifica que todos los datos esten llenos.");
                }
            } else {
                ms.showModal(Alert.AlertType.WARNING, "Información", this.getStage(), "Debes seleccionar el elemento a eliminar");
            }
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (table.getSelectionModel() != null) {
            if (table.getSelectionModel().getSelectedItem() != null) {

                empService.eliminarEmpleado(table.getSelectionModel().getSelectedItem().getId());
                ms.showModal(Alert.AlertType.INFORMATION, "Información", this.getStage(), "Datos Eliminados correctamente");

                Respuesta respuesta = empService.getEmpleados();
                items.clear();
                empleados = (ArrayList) respuesta.getResultado("Empleados");
                items = FXCollections.observableArrayList(empleados);
                table.setItems(items);
                limpiarValores();
            } else {
                ms.showModal(Alert.AlertType.WARNING, "Información", this.getStage(), "Debes seleccionar el elemento a eliminar");
            }
        }
    }

    @FXML
    private void agregar(ActionEvent event) {

        if (registroCorrecto()) {
            String nombre = txtNombre.getText();
            String apellido = txtApellidos.getText();
            String correo = txtCorreo.getText();
            String cedula = txtCedula.getText();

            empleado = new EmpleadoDto(nombre, apellido, cedula, correo, 0, 1, null);
            try {
                resp = empService.guardarEmpleado(empleado);
                ms.showModal(AlertType.INFORMATION, "Informacion de guardado", this.getStage(), resp.getMensaje());
                limpiarValores();
                empleados = (ArrayList) empService.getEmpleados().getResultado("Empleados");
                table.getItems().clear();
                items = FXCollections.observableArrayList(empleados);
                table.setItems(items);

            } catch (Exception e) {
                ms.showModal(AlertType.ERROR, "Informacion de guardado", this.getStage(), "Hubo un error al momento de guardar el empleado.");
            }

        } else {
            ms.showModal(AlertType.ERROR, "Informacion acerca del guardado", this.getStage(), "Existen datos erroneos en el registro, "
                    + "verifica que todos los datos esten llenos.");
        }

    }

    boolean registroCorrecto() {
        return !txtNombre.getText().isEmpty() && !txtApellidos.getText().isEmpty()
                && !txtCorreo.getText().isEmpty() && !txtCedula.getText().isEmpty();
    }

    void limpiarValores() {
        txtNombre.clear();
        txtApellidos.clear();
        txtCorreo.clear();
        txtCedula.clear();
        table.getSelectionModel().clearSelection();

    }

    @FXML
    private void DatosEmpleado(MouseEvent event) {
        if (table.getSelectionModel() != null) {
            if (table.getSelectionModel().getSelectedItem() != null) {
                empleado = table.getSelectionModel().getSelectedItem();
                txtApellidos.setText(empleado.getApellido());
                txtNombre.setText(empleado.getNombre());
                txtCorreo.setText(empleado.getCorreo());
                txtCedula.setText(empleado.getCedula());
            }
        }
    }

    @FXML
    private void limpiarRegistro(ActionEvent event) {
        limpiarValores();
    }

}

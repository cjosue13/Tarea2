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
    private TableColumn<EmpleadoDto, String> COL_APELLIDO_EMP;
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

        puesService = new PuestoService();
        empService = new EmpleadoService();

        ms = new Mensaje();
        resp = puesService.getPuestos();

        puestos = ((ArrayList<PuestoDto>) resp.getResultado("Puestos"));
        COL_NOMBRE_PUES.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombrePuesto()));
        COL_DESCRIPCION_PUES.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getDescripcion()));
        COL_CODIGO_PUES.setCellValueFactory(value -> new SimpleIntegerProperty(value.getValue().getId()));
        COL_EMPLEADO_PUE.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getEmpleado().getNombre() + " "
                + value.getValue().getEmpleado().getApellido()));

        resp = empService.getEmpleados();
        empleados = (ArrayList) resp.getResultado("Empleados");

        COL_NOMBRE_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombre()));
        COL_APELLIDO_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getApellido()));
        COL_ID_EMP.setCellValueFactory(value -> new SimpleIntegerProperty(value.getValue().getId()));

        itemsPues = FXCollections.observableArrayList(puestos);
        itemsEmp = FXCollections.observableArrayList(empleados);
        tablePuesto.setItems(itemsPues);
        tableEmpleado.setItems(itemsEmp);

    }

    @FXML
    private void editar(ActionEvent event) {
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (tablePuesto.getSelectionModel() != null) {
            if (tablePuesto.getSelectionModel().getSelectedItem() != null) {
                puesService.EliminarPuesto(tablePuesto.getSelectionModel().getSelectedItem().getId());
                ms.show(Alert.AlertType.INFORMATION, "Información", "Datos Eliminados correctamente");
                resp= puesService.getPuestos();
                itemsPues.clear();
                puestos = (ArrayList<PuestoDto>) resp.getResultado("Puestos");
                itemsPues = FXCollections.observableArrayList(puestos);
                tablePuesto.setItems(itemsPues);
            }
        } else {
            ms.show(Alert.AlertType.WARNING, "Información", "Debes seleccionar el elemento a eliminar");
        }
    }

    @FXML
    private void agregar(ActionEvent event) {
        if (registroCorrecto()) {
            String nombre = txtNombre.getText();
            String descripcion = txtDescripcion.getText();

            puesto = new PuestoDto(nombre, descripcion, 1, empleado, null);
            try {
                resp = puesService.guardarPuesto(puesto);

                ms.show(Alert.AlertType.INFORMATION, "Informacion de guardado", resp.getMensaje());
                limpiarValores();
                puestos = (ArrayList) puesService.getPuestos().getResultado("Puestos");
                tablePuesto.getItems().clear();
                itemsPues = FXCollections.observableArrayList(puestos);
                tablePuesto.setItems(itemsPues);

            } catch (Exception e) {
                ms.show(Alert.AlertType.ERROR, "Informacion de guardado", "Hubo un error al momento de guardar el hospital. "
                        + "Verifica que todos los datos esten llenados correctamente o que el empleado no tenga un puesto asignado");

            }

        } else {
            ms.show(Alert.AlertType.ERROR, "Informacion acerca del guardado", "Existen datos erroneos en el registro, "
                    + "verifica que todos los datos esten llenos.");
        }

    }

    boolean registroCorrecto() {
        if (!txtNombre.getText().isEmpty() && empleado != null) {
            return true;
        } else {
            return false;
        }
    }

    void limpiarValores() {
        txtNombre.clear();
        txtDescripcion.clear();
        txtEmpleado.clear();
        empleado = null;
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
}

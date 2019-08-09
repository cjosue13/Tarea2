/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import horarios.model.EmpleadoDto;
import horarios.model.PuestoDto;
import horarios.model.RolDto;
import horarios.service.EmpleadoService;
import horarios.service.PuestoService;
import horarios.service.RolService;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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
    private TableView<PuestoDto> table;
    private JFXTextField txtCorreo;
    private EmpleadoDto empleado;
    private PuestoService puesService;
    private Respuesta resp;
    private ArrayList<PuestoDto> EmpPuesto;
    private ObservableList items;
    private Mensaje ms;
    private RolService rolservice;
    private Respuesta resp2;
    private ArrayList<RolDto> roles;
    private ObservableList items2;
    private Mensaje ms2;
    private RolDto rol;
    @FXML
    private TableColumn<RolDto, String> COL_NOMBRE_ROL;
    @FXML
    private TableView<RolDto> table2;
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
    private JFXTextField txtRol;
    private PuestoDto puesto;
    private RolDto Rol;

    @Override
    public void initialize() {
        
        txtRol.clear();
        txtfolio.clear();
        txtNombre.clear();
        txtPuesto.clear();
        
        
        btnAsignarRol.setCursor(Cursor.HAND);
        puesService = new PuestoService();
        ms = new Mensaje();
        resp = puesService.getPuestos();
        EmpPuesto = ((ArrayList<PuestoDto>) resp.getResultado("Puestos"));
        COL_NOMBRE_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getEmpleado().getNombre() + " " + value.getValue().getEmpleado().getApellido()));
        COL_PUESTO_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombrePuesto()));
        COL_FOLIO_EMP.setCellValueFactory(value -> new SimpleIntegerProperty(value.getValue().getEmpleado().getId()));
        items = FXCollections.observableArrayList(EmpPuesto);
        table.setItems(items);

        rolservice = new RolService();
        ms2 = new Mensaje();
        resp2 = rolservice.getRoles();
        roles = ((ArrayList<RolDto>) resp2.getResultado("Roles"));
        COL_NOMBRE_ROL.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombreRol()));

        items2 = FXCollections.observableArrayList(roles);
        table2.setItems(items2);

    }

    public boolean ValidarAsignacion() {
        if (table.getSelectionModel() == null || table2.getSelectionModel() == null) {
            return false;
        } else {
            if (table.getSelectionModel().getSelectedItem() == null || table2.getSelectionModel().getSelectedItem() == null) {
                return false;
            } else {
                return true;
            }
        }
    }

    @FXML
    private void AsignarRol(ActionEvent event) {

        if (ValidarAsignacion()) { // si devuelve true entonces seleccionó bien

        }
    }

    @FXML
    private void DatosEmpleado(MouseEvent event) {
        if (table.getSelectionModel() != null) {
            if (table.getSelectionModel().getSelectedItem() != null) {
                puesto = table.getSelectionModel().getSelectedItem();
                txtfolio.setText(String.valueOf(puesto.getId()));
                txtNombre.setText(puesto.getEmpleado().getNombre() + " " + puesto.getEmpleado().getApellido());
                txtPuesto.setText(puesto.getNombrePuesto());
            }
        }
    }

    @FXML
    private void DatosRol(MouseEvent event) {
        if (table2.getSelectionModel() != null) {
            if (table2.getSelectionModel().getSelectedItem() != null) {
                Rol = table2.getSelectionModel().getSelectedItem();
                txtRol.setText(Rol.getNombreRol());
            }
        }

    }

}

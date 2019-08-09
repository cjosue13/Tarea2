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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TableColumn<RolDto, String> COL_NOMBRE_ROL;
    @FXML
    private TableView<RolDto> tableRoles;
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

    @Override
    public void initialize() {

       // limpiarValores();

        btnAsignarRol.setCursor(Cursor.HAND);
        puesService = new PuestoService();
        ms = new Mensaje();
        respPues = puesService.getPuestos();
        puestos = ((ArrayList) respPues.getResultado("Puestos"));

        COL_NOMBRE_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getEmpleado().getNombre() + " " + value.getValue().getEmpleado().getApellido()));
        COL_PUESTO_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombrePuesto()));
        COL_FOLIO_EMP.setCellValueFactory(value -> new SimpleIntegerProperty(value.getValue().getEmpleado().getId()));
        itemsPuestos = FXCollections.observableArrayList(puestos);
        tablePuestos.setItems(itemsPuestos);

        rolservice = new RolService();
        respRol = rolservice.getRoles();
        roles = ((ArrayList) respRol.getResultado("Roles"));
        COL_NOMBRE_ROL.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombreRol()));
        //Remueve los roles que ya tengan un 
        /*Stream <RolDto>streamRol  = roles.stream();
        streamRol.filter(x->x.getPuestos()!=null).forEach((t) -> {
            roles.remove(t);
        });*/
        
        itemsRoles = FXCollections.observableArrayList(roles);
        tableRoles.setItems(itemsRoles);

    }

    public boolean ValidarAsignacion() {
        if (tablePuestos.getSelectionModel() == null || tableRoles.getSelectionModel() == null) {
            return false;
        } else {
            return !(tablePuestos.getSelectionModel().getSelectedItem() == null
                    || tableRoles.getSelectionModel().getSelectedItem() == null);
        }
    }

    @FXML
    private void AsignarRol(ActionEvent event) {

        if (ValidarAsignacion()) { // si devuelve true entonces seleccionó bien
            rol.getPuestos().add(puesto);
            try {
                respPues = rolservice.guardarRol(rol);
                ms.show(Alert.AlertType.INFORMATION, "Informacion de guardado", respPues.getMensaje());
                limpiarValores();

            } catch (Exception e) {
                ms.show(Alert.AlertType.ERROR, "Informacion de guardado", "Ocurrio un error al asignar un rol al empleado");
            }

        } else {
            ms.show(Alert.AlertType.ERROR, "Informacion de registro", "No has seleccionado alguno de los datos de la tabla.");
        }
    }

    @FXML
    private void DatosEmpleado(MouseEvent event) {
        if (tablePuestos.getSelectionModel() != null) {
            if (tablePuestos.getSelectionModel().getSelectedItem() != null) {
                puesto = tablePuestos.getSelectionModel().getSelectedItem();
                txtfolio.setText(String.valueOf(puesto.getId()));
                txtNombre.setText(puesto.getEmpleado().getNombre() + " " + puesto.getEmpleado().getApellido());
                txtPuesto.setText(puesto.getNombrePuesto());
            }
        }
    }

    @FXML
    private void DatosRol(MouseEvent event) {
        if (tableRoles.getSelectionModel() != null) {
            if (tableRoles.getSelectionModel().getSelectedItem() != null) {
                rol = tableRoles.getSelectionModel().getSelectedItem();
                txtRol.setText(rol.getNombreRol());
            }
        }
    }

    void limpiarValores() {
        txtRol.clear();
        txtfolio.clear();
        txtNombre.clear();
        txtPuesto.clear();
        tablePuestos.getSelectionModel().clearSelection();
        tableRoles.getSelectionModel().clearSelection();
    }
}

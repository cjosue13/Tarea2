/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXTextField;
import horarios.model.EmpleadoDto;
import horarios.model.RolDto;
import horarios.service.EmpleadoService;
import horarios.service.RolService;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class AsignacionRolesController extends Controller {

    @FXML
    private Label Titulo;
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
    private JFXTextField txtCorreo;
    private EmpleadoDto empleado;
    private EmpleadoService empService;
    private Respuesta resp;
    private ArrayList<EmpleadoDto> empleados;
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
    
    @Override
    public void initialize() {

        
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
        
        
        
        rolservice = new RolService();
        ms2 = new Mensaje();
        resp2 = rolservice.getRoles();
        roles = ((ArrayList<RolDto>) resp2.getResultado("Roles"));
        COL_NOMBRE_ROL.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombreRol()));
        
      
        items2 = FXCollections.observableArrayList(roles);
        table2.setItems(items2);
        
    }
    
}


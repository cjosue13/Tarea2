/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXTextField;
import horarios.model.EmpleadoDto;
import horarios.model.PuestoDto;
import horarios.model.RolDto;
import horarios.service.EmpleadoService;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class HorariosController extends Controller {   

    @FXML
    private ScrollPane HorarioDiasNaturales;
    @FXML
    private AnchorPane Lunes;
    @FXML
    private AnchorPane Martes;
    @FXML
    private AnchorPane Miercoles;
    @FXML
    private AnchorPane Jueves;
    @FXML
    private AnchorPane Viernes;
    @FXML
    private AnchorPane Sabado;
    @FXML
    private AnchorPane Domingo;
    @FXML
    private TableView<?> listaEmpleados;
    @FXML
    private TableColumn<EmpleadoDto, String> COL_NOMBRE_EMP;
    @FXML
    private TableColumn<PuestoDto, String> COL_NOMBRE_PUE;
    @FXML
    private TableColumn<RolDto, String> COL_ROL_NOMBRE;
    @FXML
    private Label horasTrabajadas;
    @FXML
    private Label CantidadRoles;
    @FXML
    private Label Titulo;

    private JFXTextField txtCorreo;
    private EmpleadoDto empleado;
    private EmpleadoService empService;
    private Respuesta resp;
    private ArrayList<EmpleadoDto> empleados;
    private ObservableList items;
    private Mensaje ms;
    @Override
    public void initialize() {

        
        empService = new EmpleadoService();
        ms = new Mensaje();
        resp = empService.getEmpleados();
        empleados = ((ArrayList<EmpleadoDto>) resp.getResultado("Empleados"));
        COL_NOMBRE_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombre()));
        
        items = FXCollections.observableArrayList(empleados);
        listaEmpleados.setItems(items);
        
        
    }
    
}

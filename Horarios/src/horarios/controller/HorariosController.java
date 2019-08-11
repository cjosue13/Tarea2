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
import horarios.service.PuestoService;
import horarios.service.RolService;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.SimpleStyleableStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
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
    private TableView<PuestoDto> listaEmpleados;
    @FXML
    private TableColumn<PuestoDto, String> COL_NOMBRE_EMP;
    @FXML
    private TableColumn<PuestoDto, String> COL_NOMBRE_PUE;
    @FXML
    private Label horasTrabajadas;
    @FXML
    private Label CantidadRoles;
    @FXML
    private Label Titulo;

    private JFXTextField txtCorreo;
    private PuestoService puesService;
    private Respuesta resp;
    private ArrayList<PuestoDto> empleados;
    private ObservableList items;
    @FXML
    private TableView<RolDto> tableRol;
    private ArrayList<RolDto> roles;
    private ObservableList itemsRoles;
    private RolService rolService;
    private Respuesta RespuestaRol;
    @FXML
    private TableColumn<RolDto, String> COL_NOMBRE_ROL;
    @Override
    public void initialize() {
        puesService = new PuestoService();
        resp = puesService.getPuestos();
        empleados = ((ArrayList<PuestoDto>) resp.getResultado("Puestos"));
        COL_NOMBRE_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getEmpleado().getNombre()));
        COL_NOMBRE_PUE.setCellValueFactory(value-> new SimpleStringProperty(value.getValue().getNombrePuesto()));
        items = FXCollections.observableArrayList(empleados);
        listaEmpleados.setItems(items);   
        
        rolService = new RolService();
        RespuestaRol = rolService.getRoles();
        roles = ((ArrayList<RolDto>) RespuestaRol.getResultado("Roles"));
        COL_NOMBRE_ROL.setCellValueFactory(value-> new SimpleStringProperty(value.getValue().getNombreRol()));
        itemsRoles = FXCollections.observableArrayList(roles);
        //tableRol.setItems(itemsRoles);
    }

    @FXML
    private void SeleccionaEmpleado(MouseEvent event) {
        if(listaEmpleados.getSelectionModel()!=null){
            if(listaEmpleados.getSelectionModel().getSelectedItem()!=null){
                tableRol.getItems().clear();
                PuestoDto puestoDto = listaEmpleados.getSelectionModel().getSelectedItem();
                ArrayList<RolDto> lista = puestoDto.getRoles();
                resp = puesService.getRoles(puestoDto.getId());
                puestoDto = (PuestoDto)resp.getResultado("roles");
                itemsRoles = FXCollections.observableArrayList(puestoDto.getRoles());
                tableRol.setItems(itemsRoles);
            }       
        }
    }

    @FXML
    private void SeleccionaRol(MouseEvent event) {
    }
    
}

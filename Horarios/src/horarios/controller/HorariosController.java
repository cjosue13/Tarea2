/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXTextField;
import horarios.model.DiaDto;
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
    @FXML
    private Label lblHoraInicioLunes;
    @FXML
    private Label lblHoraFinalLunes;
    @FXML
    private Label lblHoraInicioMartes;
    @FXML
    private Label lblHoraFinalMartes;
    @FXML
    private Label lblHoraInicioMiercoles;
    @FXML
    private Label lblHoraFinalMiercoles;
    @FXML
    private Label lblHoraInicioJueves;
    @FXML
    private Label lblHoraFinalJueves;
    @FXML
    private Label lblHoraInicioViernes;
    @FXML
    private Label lblHoraFinalViernes;
    @FXML
    private Label lblHoraInicioSabado;
    @FXML
    private Label lblHoraFinalSabado;
    @FXML
    private Label lblHoraInicioDomingo;
    @FXML
    private Label lblHoraFinalDomingo;
    private boolean TLunes;
    private boolean TMartes;
    private boolean TMiercoles;
    private boolean TJueves;
    private boolean TViernes;
    private boolean TSabado;
    private boolean TDomingo;

    @Override
    public void initialize() {
        TLunes = false;
        TMartes = false;
        TMiercoles = false;
        TJueves = false;
        TViernes = false;
        TSabado = false;
        TDomingo = false;
        
        puesService = new PuestoService();
        resp = puesService.getPuestos();
        empleados = ((ArrayList<PuestoDto>) resp.getResultado("Puestos"));
        COL_NOMBRE_EMP.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getEmpleado().getNombre()));
        COL_NOMBRE_PUE.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombrePuesto()));
        items = FXCollections.observableArrayList(empleados);
        listaEmpleados.setItems(items);

        rolService = new RolService();
        RespuestaRol = rolService.getRoles();
        roles = ((ArrayList<RolDto>) RespuestaRol.getResultado("Roles"));
        COL_NOMBRE_ROL.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombreRol()));
        itemsRoles = FXCollections.observableArrayList(roles);

    }

    @FXML
    private void SeleccionaEmpleado(MouseEvent event) {
        if (listaEmpleados.getSelectionModel() != null) {
            if (listaEmpleados.getSelectionModel().getSelectedItem() != null) {
                tableRol.getItems().clear();
                PuestoDto puestoDto = listaEmpleados.getSelectionModel().getSelectedItem();
                resp = puesService.getRoles(puestoDto.getId());
                puestoDto = (PuestoDto) resp.getResultado("roles");
                itemsRoles = FXCollections.observableArrayList(puestoDto.getRoles());
                tableRol.setItems(itemsRoles);
            }
        }
    }

    @FXML
    private void SeleccionaRol(MouseEvent event) {
        if (tableRol.getSelectionModel() != null) {
            if (tableRol.getSelectionModel().getSelectedItem() != null) {
                RolDto roldto = tableRol.getSelectionModel().getSelectedItem();
                ArrayList<DiaDto> diadto = roldto.getHorario().getDias();
                diadto.stream().forEach(dia -> {
                    switch (dia.getNombre()) {
                        case "Lunes":
                            Lunes.setStyle("-fx-background-color: green");
                            lblHoraInicioLunes.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalLunes.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            TLunes = true;
                            break;
                        case "Martes":
                            Martes.setStyle("-fx-background-color: green");
                            lblHoraInicioMartes.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalMartes.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            TMartes = true;
                            break;
                        case "Miercoles":
                            Miercoles.setStyle("-fx-background-color: green");
                            lblHoraInicioMiercoles.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalMiercoles.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            TMiercoles = true;
                            break;
                        case "Jueves":
                            Jueves.setStyle("-fx-background-color: green");
                            lblHoraInicioJueves.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalJueves.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            TJueves = true;
                            break;
                        case "Viernes":
                            Viernes.setStyle("-fx-background-color: green");
                            lblHoraInicioViernes.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalViernes.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            TViernes = true;
                            break;
                        case "Sabado":
                            Sabado.setStyle("-fx-background-color: green");
                            lblHoraInicioSabado.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalSabado.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            TSabado = true;
                            break;
                        case "Domingo":
                            Domingo.setStyle("-fx-background-color: green");
                            lblHoraInicioDomingo.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalDomingo.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            TDomingo = true;
                            break;
                        default:
                            break;
                    }
                });
                if (!TLunes) {
                    Lunes.setStyle("-fx-background-color: black");
                    lblHoraInicioLunes.setText("");
                    lblHoraFinalLunes.setText("");

                }if (!TMartes) {
                    Martes.setStyle("-fx-background-color: black");
                    lblHoraInicioMartes.setText("");
                    lblHoraFinalMartes.setText("");

                }if (!TMiercoles) {
                    Miercoles.setStyle("-fx-background-color: black");
                    lblHoraInicioMiercoles.setText("");
                    lblHoraFinalMiercoles.setText("");

                }if (!TJueves) {
                    Jueves.setStyle("-fx-background-color: black");
                    lblHoraInicioJueves.setText("");
                    lblHoraFinalJueves.setText("");

                }if (!TViernes) {
                    Viernes.setStyle("-fx-background-color: black");
                    lblHoraInicioViernes.setText("");
                    lblHoraFinalViernes.setText("");
                }if (!TSabado) {
                    Sabado.setStyle("-fx-background-color: black");
                    lblHoraInicioSabado.setText("");
                    lblHoraFinalSabado.setText("");
                }if (!TDomingo) {
                    Domingo.setStyle("-fx-background-color: black");
                    lblHoraInicioDomingo.setText("");
                    lblHoraFinalDomingo.setText("");
                }
                TLunes = false;
                TMartes = false;
                TMiercoles = false;
                TJueves = false;
                TViernes = false;
                TSabado = false;
                TDomingo = false;
            }
        }
    }

}

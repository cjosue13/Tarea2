/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import horarios.model.DiaDto;
import horarios.model.EmpleadoDto;
import horarios.model.PuestoDto;
import horarios.model.RolDto;
import horarios.service.EmpleadoService;
import horarios.service.PuestoService;
import horarios.service.RolService;
import horarios.util.Excel;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.SimpleStyleableStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
import javax.mail.MessagingException;
import jxl.write.WriteException;

/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class HorariosController extends Controller {

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
    @FXML
    private TableView<RolDto> tableRol;
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
    @FXML
    private FlowPane flowPane;
    private PuestoService puesService;
    private Respuesta resp;
    private ArrayList<PuestoDto> empleados;
    private ObservableList items;
    private ArrayList<RolDto> roles;
    private ObservableList itemsRoles;
    private RolService rolService;
    private Respuesta RespuestaRol;
    @FXML
    private JFXButton btnCorreo;
    private Mensaje m = new Mensaje();
    @FXML
    private JFXProgressBar progressBar;
    private double progreso;
    private final Timeline timeProgress = new Timeline(new KeyFrame(Duration.ZERO, event -> correrBarWarning()), new KeyFrame(Duration.seconds(0.017)));
    private final Timeline time = new Timeline(new KeyFrame(Duration.ZERO, event -> correrBarInformation()), new KeyFrame(Duration.seconds(0.017)));
    private Excel correo = new Excel();
    int porcentaje;
    @FXML
    private Label lblPorcentaje;
    @FXML
    private JFXButton Exportar;

    @Override
    public void initialize() {
        inicio();
    }

    private void inicio() {

        puesService = new PuestoService();
        resp = puesService.getPuestos();
        empleados = ((ArrayList<PuestoDto>) resp.getResultado("Puestos"));
        COL_NOMBRE_EMP.setCellValueFactory(value -> new SimpleStringProperty((value.getValue().getEmpleado()!=null)?value.getValue().
                getEmpleado().getNombre():"Sin asignar"));
        COL_NOMBRE_PUE.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombrePuesto()));
        items = FXCollections.observableArrayList(empleados);
        listaEmpleados.setItems(items);

        COL_NOMBRE_ROL.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombreRol()));

        progressBar.setVisible(false);
    }

    @FXML
    private void SeleccionaEmpleado(MouseEvent event) {
        if (listaEmpleados.getSelectionModel() != null) {
            if (listaEmpleados.getSelectionModel().getSelectedItem() != null) {
                limpiarHorario();
                tableRol.getItems().clear();
                PuestoDto puestoDto = listaEmpleados.getSelectionModel().getSelectedItem();
                resp = puesService.getRoles(puestoDto.getId());
                puestoDto = (PuestoDto) resp.getResultado("roles");
                itemsRoles = FXCollections.observableArrayList(puestoDto.getRoles());
                CantidadRoles.setText(String.valueOf(puestoDto.getRoles().size()));
                tableRol.setItems(itemsRoles);
            }
        }
    }

    @FXML
    private void SeleccionaRol(MouseEvent event) {
        if (tableRol.getSelectionModel() != null) {
            if (tableRol.getSelectionModel().getSelectedItem() != null) {
                limpiarHorario();
                RolDto roldto = tableRol.getSelectionModel().getSelectedItem();
                ArrayList<DiaDto> diadto = roldto.getHorario().getDias();
                diadto.stream().forEach(dia -> {
                    switch (dia.getNombre()) {
                        case "Lunes":
                            Lunes.setId("buttonSelec");
                            lblHoraInicioLunes.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalLunes.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            break;
                        case "Martes":
                            Martes.setId("buttonSelec");
                            lblHoraInicioMartes.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalMartes.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            break;
                        case "Miercoles":
                            Miercoles.setId("buttonSelec");
                            lblHoraInicioMiercoles.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalMiercoles.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            break;
                        case "Jueves":
                            Jueves.setId("buttonSelec");
                            lblHoraInicioJueves.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalJueves.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            break;
                        case "Viernes":
                            Viernes.setId("buttonSelec");
                            lblHoraInicioViernes.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalViernes.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            break;
                        case "Sabado":
                            Sabado.setId("buttonSelec");
                            lblHoraInicioSabado.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalSabado.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            break;
                        case "Domingo":
                            Domingo.setId("buttonSelec");
                            lblHoraInicioDomingo.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            lblHoraFinalDomingo.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            break;
                        default:
                            break;
                    }
                });

            }
        }
    }

    public void limpiarHorario() {
        flowPane.getChildren().stream().forEach(node -> {
            ((AnchorPane) node).setId("button2");
            ((Label) ((AnchorPane) node).getChildren().get(3)).setText("");
            ((Label) ((AnchorPane) node).getChildren().get(4)).setText("");
        });
    }

    @FXML
    private void EnviarCorreo(ActionEvent event) {
        progressBar.setVisible(true);
        lblPorcentaje.setVisible(true);
        progressBar.setProgress(0);
        progreso = 0;
        if (listaEmpleados.getSelectionModel() != null) {
            if (listaEmpleados.getSelectionModel().getSelectedItem() != null) {
                time.setCycleCount(Timeline.INDEFINITE);
                correrBarInformation();
                time.play();
            } else {
                progressBar.setVisible(true);
                timeProgress.setCycleCount(Timeline.INDEFINITE);
                correrBarWarning();
                timeProgress.play();
            }
        } else {
            progressBar.setVisible(true);
            timeProgress.setCycleCount(Timeline.INDEFINITE);
            correrBarWarning();
            timeProgress.play();
        }
    }

    public void correrBarWarning() {
        progreso += 0.01;
        progressBar.setProgress(progreso);
        porcentaje = (int) (progreso * 100);
        lblPorcentaje.setText(String.valueOf(porcentaje) + "%");
        if (progreso > 0.9) {
            timeProgress.stop();
            progressBar.setVisible(false);
            lblPorcentaje.setVisible(false);
            m.showModal(Alert.AlertType.WARNING, "Envio de Correo", this.getStage(), "Debes seleccionar el empleado");
        }
    }

    public void correrBarInformation() {
        progreso += 0.01;
        progressBar.setProgress(progreso);
        porcentaje = (int) (progreso * 100);
        lblPorcentaje.setText(String.valueOf(porcentaje) + "%");
        if (progreso > 0.9) {
            time.stop();
            progressBar.setVisible(false);
            lblPorcentaje.setVisible(false);
            try {
                correo.SendMail(listaEmpleados.getSelectionModel().getSelectedItem().getEmpleado().getCorreo());
            } catch (MessagingException | IOException ex) {
                m.show(Alert.AlertType.ERROR, "Envio de Correo" ,"Ha ocurrido un error inesperado al enviar su correo");
            }
        }
    }

    @FXML
    private void Exportar(ActionEvent event) throws WriteException {
        
        Excel excel = new Excel();
        excel.GenerarReporte();
        
    }
}

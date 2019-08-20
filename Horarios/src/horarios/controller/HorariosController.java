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
import java.util.ConcurrentModificationException;
import java.util.Objects;
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
import javafx.scene.Cursor;
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
    static public String InicioLunes = "";
    @FXML
    private Label lblHoraFinalLunes;
    static public String FinalLunes = "";
    static public Integer HorasLibras = 0;
    @FXML
    private Label lblHoraInicioMartes;
    static public String InicioMartes = "";
    @FXML
    private Label lblHoraFinalMartes;
    static public String FinalMartes = "";
    @FXML
    private Label lblHoraInicioMiercoles;
    static public String InicioMiercoles = "";
    @FXML
    private Label lblHoraFinalMiercoles;
    static public String FinalMiercoles = "";
    @FXML
    private Label lblHoraInicioJueves;
    static public String InicioJueves = "";
    @FXML
    private Label lblHoraFinalJueves;
    static public String FinalJueves = "";
    @FXML
    private Label lblHoraInicioViernes;
    static public String InicioViernes = "";
    @FXML
    private Label lblHoraFinalViernes;
    static public String FinalViernes = "";
    @FXML
    private Label lblHoraInicioSabado;
    static public String InicioSabado = "";
    @FXML
    private Label lblHoraFinalSabado;
    static public String FinalSabado = "";
    @FXML
    private Label lblHoraInicioDomingo;
    static public String InicioDomingo = "";
    @FXML
    private Label lblHoraFinalDomingo;
    static public String FinalDomingo = "";
    static public String nombreX = "";
    static public Integer CantRol = 0;
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
    private Mensaje m = new Mensaje();
    @FXML
    private JFXProgressBar progressBar;
    private double progreso;
    private final Timeline timeProgress = new Timeline(new KeyFrame(Duration.ZERO, event -> correrBarWarning()), new KeyFrame(Duration.seconds(0.017)));
    private final Timeline time = new Timeline(new KeyFrame(Duration.ZERO, event -> correrBarInformation()), new KeyFrame(Duration.seconds(0.017)));
    private Excel correo = new Excel();
    private int porcentaje;
    @FXML
    private Label lblPorcentaje;
    @FXML
    private JFXButton Exportar;
    static public boolean RolSeleccion = false;
    private Integer HorasTotales = 0;
    private Integer MinutosTotales = 0;
    @FXML
    private JFXButton btnFiltroEmp;
    @FXML
    private JFXTextField txtFiltroEmp;
    @FXML
    private JFXTextField txtFiltroPuesto;
    @FXML
    private JFXButton btnFiltroPuesto;

    @Override
    public void initialize() {
        inicio();
    }

    private void inicio() {
        try {

            Exportar.setCursor(Cursor.HAND);
            RolSeleccion = false;
            puesService = new PuestoService();
            resp = puesService.getPuestos();
            empleados = ((ArrayList<PuestoDto>) resp.getResultado("Puestos"));

            COL_NOMBRE_EMP.setCellValueFactory(value -> new SimpleStringProperty((value.getValue().getEmpleado() != null) ? value.getValue().
                    getEmpleado().getNombre() + " " + value.getValue().getEmpleado().getApellido() : "Sin asignar"));
            COL_NOMBRE_PUE.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombrePuesto()));
            items = FXCollections.observableArrayList(empleados);
            listaEmpleados.setItems(items);

            COL_NOMBRE_ROL.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombreRol()));
            progressBar.setVisible(false);
        } catch (NullPointerException ex) {
        }
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
                CantRol = puestoDto.getRoles().size();
                if (puestoDto.getEmpleado() != null) {
                    nombreX = puestoDto.getEmpleado().getNombre();
                }

                tableRol.setItems(itemsRoles);
            }
        }
    }

    @FXML
    private void SeleccionaRol(MouseEvent event) {
        limpiarDias();
        HorasTotales = 0;
        MinutosTotales = 0;
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
                            InicioLunes = lblHoraInicioLunes.getText();
                            lblHoraFinalLunes.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            FinalLunes = lblHoraFinalLunes.getText();
                            HorasLibras = dia.getCantHorasLibre();
                            HorasTotales += (dia.getHora_Salida().toLocalTime().getHour() - dia.getHora_Inicio().toLocalTime().getHour());
                            MinutosTotales += (dia.getHora_Salida().toLocalTime().getMinute() - dia.getHora_Inicio().toLocalTime().getMinute());
                            HorasTotales -= (dia.getCantHorasLibre());
                            MinutosaHoras();
                            break;
                        case "Martes":
                            Martes.setId("buttonSelec");
                            lblHoraInicioMartes.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            InicioMartes = lblHoraInicioMartes.getText();
                            lblHoraFinalMartes.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            FinalMartes = lblHoraFinalMartes.getText();
                            HorasLibras = dia.getCantHorasLibre();
                            HorasTotales += (dia.getHora_Salida().toLocalTime().getHour() - dia.getHora_Inicio().toLocalTime().getHour());
                            MinutosTotales += (dia.getHora_Salida().toLocalTime().getMinute() - dia.getHora_Inicio().toLocalTime().getMinute());
                            HorasTotales -= (dia.getCantHorasLibre());
                            MinutosaHoras();
                            break;
                        case "Miercoles":
                            Miercoles.setId("buttonSelec");
                            lblHoraInicioMiercoles.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            InicioMiercoles = lblHoraInicioMiercoles.getText();
                            lblHoraFinalMiercoles.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            FinalMiercoles = lblHoraFinalMiercoles.getText();
                            HorasLibras = dia.getCantHorasLibre();
                            HorasTotales += (dia.getHora_Salida().toLocalTime().getHour() - dia.getHora_Inicio().toLocalTime().getHour());
                            MinutosTotales += (dia.getHora_Salida().toLocalTime().getMinute() - dia.getHora_Inicio().toLocalTime().getMinute());
                            HorasTotales -= (dia.getCantHorasLibre());
                            MinutosaHoras();
                            break;
                        case "Jueves":
                            Jueves.setId("buttonSelec");
                            lblHoraInicioJueves.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            InicioJueves = lblHoraInicioJueves.getText();
                            lblHoraFinalJueves.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            FinalJueves = lblHoraInicioJueves.getText();
                            HorasLibras = dia.getCantHorasLibre();
                            HorasTotales += (dia.getHora_Salida().toLocalTime().getHour() - dia.getHora_Inicio().toLocalTime().getHour());
                            MinutosTotales += (dia.getHora_Salida().toLocalTime().getMinute() - dia.getHora_Inicio().toLocalTime().getMinute());
                            HorasTotales -= (dia.getCantHorasLibre());
                            MinutosaHoras();
                            break;
                        case "Viernes":
                            Viernes.setId("buttonSelec");
                            lblHoraInicioViernes.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            InicioViernes = lblHoraInicioViernes.getText();
                            lblHoraFinalViernes.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            FinalViernes = lblHoraFinalViernes.getText();
                            HorasLibras = dia.getCantHorasLibre();
                            HorasTotales += (dia.getHora_Salida().toLocalTime().getHour() - dia.getHora_Inicio().toLocalTime().getHour());
                            MinutosTotales += (dia.getHora_Salida().toLocalTime().getMinute() - dia.getHora_Inicio().toLocalTime().getMinute());
                            HorasTotales -= (dia.getCantHorasLibre());
                            MinutosaHoras();
                            break;
                        case "Sabado":
                            Sabado.setId("buttonSelec");
                            lblHoraInicioSabado.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            InicioSabado = lblHoraInicioSabado.getText();
                            lblHoraFinalSabado.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            FinalSabado = lblHoraFinalSabado.getText();
                            HorasLibras = dia.getCantHorasLibre();
                            HorasTotales += (dia.getHora_Salida().toLocalTime().getHour() - dia.getHora_Inicio().toLocalTime().getHour());
                            MinutosTotales += (dia.getHora_Salida().toLocalTime().getMinute() - dia.getHora_Inicio().toLocalTime().getMinute());
                            HorasTotales -= (dia.getCantHorasLibre());
                            MinutosaHoras();
                            break;
                        case "Domingo":
                            Domingo.setId("buttonSelec");
                            lblHoraInicioDomingo.setText(String.valueOf(dia.getHora_Inicio().toLocalTime()));
                            InicioDomingo = lblHoraInicioDomingo.getText();
                            lblHoraFinalDomingo.setText(String.valueOf(dia.getHora_Salida().toLocalTime()));
                            FinalDomingo = lblHoraFinalDomingo.getText();
                            HorasLibras = dia.getCantHorasLibre();
                            HorasTotales += (dia.getHora_Salida().toLocalTime().getHour() - dia.getHora_Inicio().toLocalTime().getHour());
                            MinutosTotales += (dia.getHora_Salida().toLocalTime().getMinute() - dia.getHora_Inicio().toLocalTime().getMinute());
                            HorasTotales -= (dia.getCantHorasLibre());
                            MinutosaHoras();
                            break;
                        default:
                            break;
                    }
                });
                if (MinutosTotales < 10) {
                    horasTrabajadas.setText(String.valueOf(HorasTotales) + ":0" + String.valueOf(MinutosTotales));
                }
                if (HorasTotales < 10) {
                    horasTrabajadas.setText("0" + String.valueOf(HorasTotales) + ":" + String.valueOf(MinutosTotales));
                }
                if (HorasTotales < 10 && MinutosTotales < 10) {
                    System.out.println("entrando");
                    horasTrabajadas.setText("0" + String.valueOf(HorasTotales) + ":0" + String.valueOf(MinutosTotales));
                }
                if (HorasTotales >= 10 && MinutosTotales >= 10) {
                    horasTrabajadas.setText(String.valueOf(HorasTotales) + ":" + String.valueOf(MinutosTotales));
                }
                RolSeleccion = true;
            }
        }
    }

    //En el caso de que los min,utos exedan los 60 minutos, se debe de pasar a horas
    public void MinutosaHoras() {
        if (MinutosTotales >= 60) {
            MinutosTotales -= 60;
            HorasTotales += 1;
        }
        if (MinutosTotales >= 60) {
            MinutosaHoras();//recursividad
        }
    }

    public void limpiarHorario() {
        flowPane.getChildren().stream().forEach(node -> {
            ((AnchorPane) node).setId("button2");
            ((Label) ((AnchorPane) node).getChildren().get(3)).setText("");
            ((Label) ((AnchorPane) node).getChildren().get(4)).setText("");
        });
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
            m.show(Alert.AlertType.WARNING, "Alerta", "Alerta datos incompletos");
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
                m.show(Alert.AlertType.ERROR, "Envio de Correo", "Ha ocurrido un error inesperado al enviar su correo");
            }
        }
    }

    @FXML
    private void Exportar(ActionEvent event) throws WriteException {

        if (listaEmpleados.getSelectionModel() != null) {
            if (listaEmpleados.getSelectionModel().getSelectedItem() != null) {
                if (tableRol.getSelectionModel().getSelectedItem() != null) {
                    progressBar.setVisible(true);
                    lblPorcentaje.setVisible(true);
                    progressBar.setProgress(0);
                    progreso = 0;
                    Excel excel = new Excel();
                    excel.GenerarReporte();
                    time.setCycleCount(Timeline.INDEFINITE);
                    correrBarInformation();
                    time.play();
                } else {
                    m.showModal(Alert.AlertType.WARNING, "Informacion", this.getStage(), "Rol No seleccionado o no tiene roles asignados");
                }
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

    public void limpiarDias() {
        InicioLunes = "     -";
        FinalLunes = "     -";
        InicioMartes = "     -";
        FinalMartes = "     -";
        InicioMiercoles = "     -";
        FinalMiercoles = "     -";
        InicioJueves = "     -";
        FinalJueves = "     -";
        InicioViernes = "     -";
        FinalViernes = "     -";
        InicioSabado = "     -";
        FinalSabado = "     -";
        InicioDomingo = "     -";
        FinalDomingo = "     -";
    }

    @FXML
    private void ExportarTodos(ActionEvent event) throws WriteException, IOException {
        Excel excel = new Excel();
        excel.GenerarReporteTodos();
    }

    @FXML
    private void FiltrarEmpleado(ActionEvent event) {
        try {
            if (!txtFiltroEmp.getText().isEmpty()) {
                try {
                    Integer Folio = Integer.valueOf(txtFiltroEmp.getText());
                    resp = puesService.getPuestos();
                    empleados = (ArrayList) resp.getResultado("Puestos");
                    // Uso de stream para buscar al empleado que se requiere
                    empleados.stream().filter(x -> Objects.equals(x.getEmpleado().getId(), Folio)).forEach(x -> {
                        empleados.clear();
                        empleados.add(x);
                        items = FXCollections.observableArrayList(empleados);
                        listaEmpleados.setItems(items);
                        m.showModal(Alert.AlertType.INFORMATION, "Informacion", this.stage, "Encontrado exitosamente");
                    });
                    boolean bandera = empleados.stream().anyMatch(x -> Objects.equals(x.getEmpleado().getId(), Folio));
                    if (!bandera) {
                        m.showModal(Alert.AlertType.ERROR, "Error", this.stage, "Empleado no encontrado");
                    }
                } catch (NumberFormatException e) {
                    m.showModal(Alert.AlertType.WARNING, "Alerta", this.stage, "Digita únicamente números");

                }
            } else {
                resp = puesService.getPuestos();
                empleados = (ArrayList) resp.getResultado("Puestos");
                items = FXCollections.observableArrayList(empleados);
                listaEmpleados.setItems(items);
            }
        } catch (ConcurrentModificationException | NullPointerException e) {
        }
    }

    @FXML
    private void FiltrarPuesto(ActionEvent event) {
        try {
            if (!txtFiltroPuesto.getText().isEmpty()) {
                Integer ID = Integer.valueOf(txtFiltroPuesto.getText());
                resp = puesService.getPuesto(ID);
                m.show(Alert.AlertType.INFORMATION, "Informacion sobre busqueda", resp.getMensaje());
                if (resp.getResultado("PUE_CODIGO") != null) {
                    PuestoDto puesto = ((PuestoDto) resp.getResultado("PUE_CODIGO"));

                    empleados.clear();
                    empleados.add(puesto);

                    items = FXCollections.observableArrayList(empleados);
                    listaEmpleados.setItems(items);
                }
            } else {
                resp = puesService.getPuestos();
                empleados = ((ArrayList) resp.getResultado("Puestos"));
                items = FXCollections.observableArrayList(empleados);
                listaEmpleados.setItems(items);
            }
        } catch (NullPointerException e) {

        } catch (NumberFormatException e) {
            m.showModal(Alert.AlertType.WARNING, "Alerta", this.stage, "Digita únicamente números");
        }

    }
}

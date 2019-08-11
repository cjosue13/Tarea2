/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import horarios.model.DiaDto;
import horarios.model.HorarioDto;
import horarios.model.RolDto;
import horarios.service.RolService;
import horarios.util.AppContext;
import horarios.util.Mensaje;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.HijrahChronology;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import javafx.util.StringConverter;
/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class AsignacionHorarioController extends Controller {

    @FXML
    private FlowPane flowPane;
    @FXML
    private AnchorPane anchorLunes;
    @FXML
    private AnchorPane anchorMartes;
    @FXML
    private AnchorPane anchorMiercoles;
    @FXML
    private AnchorPane anchorJueves;
    @FXML
    private AnchorPane anchorViernes;
    @FXML
    private AnchorPane anchorSabado;
    @FXML
    private AnchorPane anchorDomingo;
    @FXML
    private JFXTimePicker txtHoraInicial;
    @FXML
    private JFXTimePicker txtHoraFinal;
    @FXML
    private Label Titulo;
    @FXML
    private JFXButton btnAgregarDia;
    @FXML
    private JFXDatePicker dateFechaIni;
    @FXML
    private JFXTextField txtCantidadHoras;
    @FXML
    private Button btnCancelar;
    @FXML
    private JFXButton Ayuda;
    @FXML
    private StackPane stackpane;
    private DiaDto dia;
    private Mensaje ms;
    private HorarioDto horario;
    private AnchorPane anchorAux;
    private RolService rolService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public void initialize() {
        inicio();
        Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        dateFechaIni.setDayCellFactory(dayCellFactory);
    }
    
    public void inicio() {
        typeKeys();
        ms = new Mensaje();

        if (AppContext.getInstance().get("Rol") != null) {//Si se ha seleccionado un rol para editar 
            //Busco el horario a traves del rol seleccionado
            rolService = new RolService();
            horario = (HorarioDto) rolService.getHorario((RolDto) AppContext.getInstance().get("Rol")).getResultado("Horario");
            horario.getDias().stream().forEach(diaSem -> {
                //System.out.println(diaSem.getNombre());
                flowPane.getChildren().stream().forEach(anchor -> {
                    //Verifico que los textos del AnchorPane sean iguales a la lista de dias para activarlos
                    if (((Label) ((AnchorPane) anchor).getChildren().get(0)).getText().equals(diaSem.getNombre())) {
                        anchor.setId("buttonSelec");
                    }
                });
            });
            dateFechaIni.setValue(horario.getFechaInicio());
            //Revisar mas adelante 
            AppContext.getInstance().set("horario", horario);

        } else {
            horario = new HorarioDto();
        }

        flowPane.getChildren().stream().forEach((node) -> {
            //Agrega evento de mouse a cada anchorpane
            ((AnchorPane) node).setOnMouseClicked((event) -> {
                anchorAux = (AnchorPane) node;
                //Si se ha deseleccionado
                if (node.getId().equals("buttonSelec")) {
                    node.setId("button2");
                    ocultarAtributosDia();
                } else {//Si se ha seleccionado
                    node.setId("buttonSelec");
                    mostrarAtributosDia();
                    //Deshabilita los dias distintos al que se ha selecionado
                    desabilitarBotones(((AnchorPane) node));
                }
            });
        });
    }

    public void desabilitarBotones(AnchorPane pane) {

        flowPane.getChildren().stream().forEach((node) -> {
            if (!pane.equals(node)) {//deshabilita todos los anchor que no esten seleccionados
                ((AnchorPane) node).setDisable(true);
            }
        });
    }

    void limpiarValores() {
        txtHoraFinal.setValue(null);
        txtHoraInicial.setValue(null);
        txtCantidadHoras.clear();
        ocultarAtributosDia();
        anchorAux = null;
    }

    void deseleccionar(String diaSemana) {
        horario.getDias().stream().forEach((diaSem) -> {
            if (diaSem.getNombre().equals(diaSemana)) {
                horario.getDias().remove(diaSem);
            }
        });
    }

    void mostrarAtributosDia() {
        txtCantidadHoras.setVisible(true);
        txtHoraFinal.setVisible(true);
        txtHoraInicial.setVisible(true);
        btnAgregarDia.setVisible(true);
        btnCancelar.setVisible(true);
    }

    void ocultarAtributosDia() {
        txtCantidadHoras.setVisible(false);
        txtHoraFinal.setVisible(false);
        txtHoraInicial.setVisible(false);
        btnAgregarDia.setVisible(false);
        btnCancelar.setVisible(false);
    }

    @FXML
    private void atras(ActionEvent event) {
        getStage().hide();
    }

    @FXML
    private void agregarDia(ActionEvent event) {
        if (!(txtHoraFinal.getValue() == null) && !(txtHoraInicial.getValue() == null) && !txtCantidadHoras.getText().isEmpty()) {
            LocalTime horaFin = txtHoraFinal.getValue();
            LocalTime horaIni = txtHoraInicial.getValue();
            //Calcula la diferencia entre horas 
            Integer difHoras = horaFin.getHour() - horaIni.getHour();
            if (horaFin.isAfter(horaIni) && difHoras >= 1) {
                //Cantidad de horas libres
                Integer horasLib = Integer.valueOf(txtCantidadHoras.getText());
                if (horasLib < difHoras) {
                    flowPane.getChildren().stream().forEach((node) -> {
                        //Busco el unico anchorPane que no esta deshabilitado para tomar el texto del dia al que pertenece
                        if (!((AnchorPane) node).isDisable()) {
                            String diaSemana = ((Label) ((AnchorPane) node).getChildren().get(0)).getText();
                            LocalDateTime horaFecFin = txtHoraFinal.getValue().atDate(LocalDate.now());
                            LocalDateTime horaFecIni = txtHoraInicial.getValue().atDate(LocalDate.now());

                            dia = new DiaDto(diaSemana, horaFecIni, horaFecFin, null, 1, horario, horasLib);
                            horario.getDias().add(dia);
                            limpiarValores();
                        }
                        ((AnchorPane) node).setDisable(false);//activa los anchor una vez que haya agregado las horas
                    });
                } else {
                    ms.showModal(Alert.AlertType.WARNING, "Informacion sobre agregar dia", this.getStage(), "La cantidad de horas libres debe ser menor");
                }
            } else {
                ms.showModal(Alert.AlertType.WARNING, "Informacion sobre agregar dia", this.getStage(), "Existen datos erroneos en el registro, "
                        + "La hora final no puede estar antes que la hora inicial, y "
                        + "debe existir al menos una hora de diferencia asignada al rol.");
            }
        } else {
            ms.showModal(Alert.AlertType.WARNING, "Informacion sobre agregar dia", this.getStage(), "Existen datos erroneos en el registro, "
                    + "verifica que todos los datos se hayan llenado correctamente.");
        }
    }

    @FXML
    private void agregarHorario(ActionEvent event) {
        if (!horario.getDias().isEmpty() && dateFechaIni.getValue() != null) {
            horario.setFechaInicio(dateFechaIni.getValue());
            horario.setVersion(1);
            horario.calcularHorasLibres();
            horario.setOrdenRotacion(0);
            AppContext.getInstance().set("horario", horario);
            //Cierra la ventana
            getStage().hide();
        } else {
            ms.showModal(Alert.AlertType.ERROR, "Informacion", this.getStage(), "Los datos no han sido llenado correctamente."
                    + " Verifica que hayas seleccionado un dia de la semana o que se haya elegido una fecha de inicio");
        }
    }

    @FXML
    private void cancelarDia(ActionEvent event) {
        if (ms.showConfirmation("Informacion", getStage(), "¿Estas seguro que deseas cancelar?")) {
            flowPane.getChildren().stream().forEach((node) -> {
                ((AnchorPane) node).setDisable(false);//activa los anchor una vez que haya agregado las horas
            });
            //Vuelve al estado inicial el anchor
            anchorAux.setId("button2");
            limpiarValores();
        }
    }

    @FXML
    private void ayudar(ActionEvent event) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Instrucciones de Uso"));
        content.setBody(new Text("Para poder guardar un rol correctamente es necesario\n"
                + "asignar un horario para el rol."
                + "\nPasos:"
                + " \n1)Selecionar una fecha de inicio para el rol a guardar "
                + "\n2)Selecionar el día que se requiera para el rol "
                + "\n3)Selecionar las horas de trabajo para cada día "
                + "\n4)Presionar el boton de guardar"));
        JFXDialog dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Ok");
        button.setOnAction((ActionEvent event1) -> {
            dialog.close();
        });
        content.setActions(button);
        dialog.show();
    }

    public void typeKeys() {
        txtCantidadHoras.setOnKeyReleased(aceptaNumeros);
        dateFechaIni.setOnKeyReleased(noEscribir);
        txtHoraFinal.setOnKeyReleased(noEscribir);

    }
    
    private EventHandler<KeyEvent> aceptaCaracteres = (KeyEvent event) -> {
        if (Character.isDigit(event.getCharacter().charAt(0))) {
            event.consume();
        }
    };

    private EventHandler<KeyEvent> aceptaNumeros = (KeyEvent event) -> {
        if (!Character.isDigit(event.getCharacter().charAt(0))) {
            event.consume();
        }
    };
    
    private EventHandler<KeyEvent> noEscribir = (KeyEvent event) -> {
        event.consume();
    };
    
    private Callback<DatePicker, DateCell> getDayCellFactory() {
        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                // Disable Monday, Tueday, Wednesday.
                if (item.getDayOfWeek() != DayOfWeek.MONDAY || item.compareTo(LocalDate.now()) < 0) {
                    setDisable(true);
                    setStyle("-fx-background-color: lightgray;");
                }
            }
        };
        return dayCellFactory;
    }
}

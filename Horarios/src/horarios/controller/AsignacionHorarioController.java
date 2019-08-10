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
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import horarios.model.DiaDto;
import horarios.model.EmpleadoDto;
import horarios.model.HorarioDto;
import horarios.service.DiaService;
import horarios.service.EmpleadoService;
import horarios.util.AppContext;
import horarios.util.FlowController;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import static javafx.scene.input.KeyCode.L;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Window;

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
    private DiaDto dia;
    private Mensaje ms;
    private HorarioDto horario;
    private AnchorPane anchorAux;
    @FXML
    private JFXButton Ayuda;
    @FXML
    private StackPane stackpane;

    @Override
    public void initialize() {
        horario = new HorarioDto();
        ms = new Mensaje();
        flowPane.getChildren().stream().forEach((node) -> {
            //Agrega evento de mouse a cada anchorpane
            ((AnchorPane) node).setOnMouseClicked((event) -> {
                anchorAux = (AnchorPane) node;
                //Si se deseleccionado
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

            flowPane.getChildren().stream().forEach((node) -> {
                //Busco el unico anchorPane que no esta deshabilitado para tomar el texto del dia al que pertenece
                if (!((AnchorPane) node).isDisable()) {
                    String diaSemana = ((Label) ((AnchorPane) node).getChildren().get(0)).getText();
                    LocalDateTime horaFin = txtHoraFinal.getValue().atDate(LocalDate.now());
                    LocalDateTime horaIni = txtHoraInicial.getValue().atDate(LocalDate.now());
                    Integer horasLib = Integer.valueOf(txtCantidadHoras.getText());
                    dia = new DiaDto(diaSemana, horaIni, horaFin, null, 1, horario, horasLib);
                    horario.getDias().add(dia);
                    limpiarValores();
                }
                ((AnchorPane) node).setDisable(false);//activa los anchor una vez que haya agregado las horas
            });
        } else {
            ms.show(Alert.AlertType.ERROR, "Informacion sobre agregar", "Existen datos erroneos en el registro, " + "verifica que todos los datos se hayan llenado correctamente.");
        }
    }

    @FXML
    private void agregarHorario(ActionEvent event) {
        if (!horario.getDias().isEmpty() && dateFechaIni.getValue() != null) {
            horario.setFechaInicio(dateFechaIni.getValue());
            horario.setVersion(1);
            horario.calcularHorasLibres();
            AppContext.getInstance().set("horario", horario);
            //Cierra la ventana
            getStage().hide();
        } else {
            ms.show(Alert.AlertType.ERROR, "Informacion", "Los datos no han sido llenado correctamente."
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
        JFXDialog dialog = new JFXDialog(stackpane,content,JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("okay");
        button.setOnAction((ActionEvent event1) -> {dialog.close();});
        content.setActions(button);
        dialog.show();
        
    }
}

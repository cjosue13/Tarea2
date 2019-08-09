/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import horarios.model.DiaDto;
import horarios.model.EmpleadoDto;
import horarios.model.HorarioDto;
import horarios.service.DiaService;
import horarios.service.EmpleadoService;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import static javafx.scene.input.KeyCode.L;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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
    private JFXButton agregar;
    ArrayList<DiaDto> diaList;
    boolean t = true;
    private DiaDto dia;
    private DiaService diaService;
    private Respuesta resp;
    private ArrayList<DiaDto> dias;
    private ObservableList items;
    private Mensaje ms;
    private HorarioDto horario;
    @Override
    public void initialize() {
        diaList = new ArrayList();
        
        flowPane.getChildren().stream().forEach((node) -> {
            
            ((AnchorPane) node).setOnMouseClicked((event) -> {
                desabilitarBotones(((AnchorPane) node));
                if (node.getId().equals("buttonSelec")) {
                    //Si se desleccionado
                    node.setId("button2");
                } else {
                    //Si se ha seleccionado
                    node.setId("buttonSelec");
                    //node.setDisable(true);
                }

            });
        });

    }
    
    public void desabilitarBotones(AnchorPane pane){
        
        flowPane.getChildren().stream().forEach((node) -> {
            if(!pane.equals(node)){//deshabilita todos los anchor que no esten seleccionados
                ((AnchorPane)node).setDisable(true);
            }
        });
    }

    @FXML
    private void agregar(ActionEvent event) {
        if(!(txtHoraFinal.getValue().toString() == null) && !(txtHoraInicial.getValue().toString() == null)){
            flowPane.getChildren().stream().forEach((node) -> {
                    ((AnchorPane)node).setDisable(false);//activa los anchor una vez que haya agregado las horas
            });
            
            //dia = new DiaDto(, txtHoraInicial.getValue(), txtHoraFinal.getValue(), null, 1, 1, horario);
            try{
                resp = diaService.guardarDia(dia);
                ms.show(Alert.AlertType.INFORMATION, "Informacion de guardado", resp.getMensaje());
                limpiarValores();
                dias = (ArrayList) diaService.getDias().getResultado("Dias");
                items = FXCollections.observableArrayList(dias);
            }catch(Exception e) {
                ms.show(Alert.AlertType.ERROR, "Informacion de guardado", "Hubo un error al momento de guardar el empleado.");
            }

        }else{
            ms.show(Alert.AlertType.ERROR, "Informacion acerca del guardado", "Existen datos erroneos en el registro, " + "verifica que todos los datos esten llenos.");
        }
            
    }

    void limpiarValores() {
        txtHoraFinal.setValue(null);
        txtHoraInicial.setValue(null);
    }

}

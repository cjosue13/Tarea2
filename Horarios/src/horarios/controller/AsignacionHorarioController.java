/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextField;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private JFXTextField txtHoraInicial;
    @FXML
    private JFXTextField txtHoraFinal;
    @FXML
    private Label Titulo;
    @FXML
    private JFXButton agregar;
    //ArrayList<>
    boolean t = true;
    @Override
    public void initialize() {

        /*JFXRippler rippler1 = new JFXRippler(new Label("Lunes"));
       
        rippler1.setMaskType(JFXRippler.RipplerMask.RECT);
        rippler1.setRipplerFill(Paint.valueOf("#ff0000"));
        
        anchorDomingo.getChildren().add(rippler1);*/

        //anchor.getChildren().add(rippler1);

        
        flowPane.getChildren().stream().forEach((node) -> {
            
            ((AnchorPane) node).setOnMouseClicked((event) -> {
                desabilitarBotones(((AnchorPane) node));
                //habilitarBotones(((AnchorPane) node), t);
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
            if(!pane.equals(node)){
                ((AnchorPane)node).setDisable(true);
            }
        });
    }

    @FXML
    private void agregar(ActionEvent event) {
        if(!txtHoraFinal.getText().isEmpty() && txtHoraInicial.getText().isEmpty()){
            flowPane.getChildren().stream().forEach((node) -> {
                    ((AnchorPane)node).setDisable(false);
            });
        }
    }
}

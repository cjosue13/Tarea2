/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import horarios.util.AppContext;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import horarios.util.FlowController;

/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class DrawerContentController extends Controller {

    @FXML
    private ImageView image;
    static public File filePath2;
    @FXML
    private JFXButton btnEmpleados;
    @FXML
    private JFXButton btnPuestos;
    @FXML
    private JFXButton btnRoles;
    @FXML
    private JFXButton btnAsignacionRoles;
    @FXML
    private JFXButton btnHorarios;
    @FXML
    private JFXButton btnExit;
    @FXML
    private ImageView img1;
    @FXML
    private ImageView img2;
    @FXML
    private ImageView img3;
    @FXML
    private ImageView img4;
    @FXML
    private ImageView img5;
    @FXML
    private ImageView img6;

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }

   
    @Override
    public void initialize() { 
        Image img;
        try {
            img = new Image("/horarios/resources/r.png");
            image.setImage(img);
        } catch (Exception e) {
        }

        Image img7;
        try {
            img7 = new Image("/horarios/resources/user2.png");
            img1.setImage(img7);
        } catch (Exception e) {

        }
        Image img8;
        try {
            img8 = new Image("/horarios/resources/resume.png");
            img2.setImage(img8);
        } catch (Exception e) {
        }
        Image img9;
        try {
            img9 = new Image("/horarios/resources/portfolio.png");
            img3.setImage(img9);
        } catch (Exception e) {
        }
        Image img10;
        try {
            img10 = new Image("/horarios/resources/hiring.png");
            img4.setImage(img10);
        } catch (Exception e) {
        }
        Image img11;
        try {
            img11 = new Image("/horarios/resources/calendar.png");
            img5.setImage(img11);
        } catch (Exception e) {
        }
        Image img12;
        try {
            img12 = new Image("/horarios/resources/logout.png");
            img6.setImage(img12);
        } catch (Exception e) {
        }
    }  
    @FXML
    private void btnEmpleados(ActionEvent event) {
             AppContext.getInstance().set("Vista", "Empleados");
             FlowController.getInstance().goView("Cargando");
             
    }

    @FXML
    private void btnPuestos(ActionEvent event) {
      AppContext.getInstance().set("Vista", "Puestos");
      FlowController.getInstance().goView("Cargando");
    }

    @FXML
    private void btnRoles(ActionEvent event) {
        AppContext.getInstance().set("Vista", "Roles");
        FlowController.getInstance().goView("Cargando");
    }

    @FXML
    private void btnAsignacionRoles(ActionEvent event) {
        AppContext.getInstance().set("Vista", "AsignacionRoles");
        FlowController.getInstance().goView("Cargando");
    }

    @FXML
    private void btnHorarios(ActionEvent event) {
       AppContext.getInstance().set("Vista", "Horarios");
       FlowController.getInstance().goView("Cargando");
    }

}

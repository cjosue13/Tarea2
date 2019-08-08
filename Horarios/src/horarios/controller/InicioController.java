/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class InicioController extends Controller {
    
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private Label Titulo;
    @FXML
    private ImageView imgfondo;

    @Override
    public void initialize() {
        
        Image img13;
        try {
            img13 = new Image("/horarios/resources/fondocalendar.png");
            imgfondo.setImage(img13);
        } catch (Exception e) {
        }
        
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/horarios/view/drawerContent.fxml"));
            drawer.setSidePane(box);
            
            HamburgerBackArrowBasicTransition burgerTask2 = new HamburgerBackArrowBasicTransition(hamburger);
            burgerTask2.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
                burgerTask2.setRate(burgerTask2.getRate()*-1);
                burgerTask2.play();
                
                if(drawer.isShown())
                    drawer.close();
                else{
                    drawer.open();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
}

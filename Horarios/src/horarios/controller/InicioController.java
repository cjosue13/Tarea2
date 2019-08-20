/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import horarios.util.AppContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
    private ImageView imgfondo;
    @FXML
    private BorderPane borderPane;
    private BorderPane B;
    @FXML
    private ImageView omg;
    @FXML
    private AnchorPane anchorp;
    private BorderPane Panel = new BorderPane();
    @Override
    public void initialize() {       
        AppContext.getInstance().set("Border", borderPane);       
        Image omg1;
        try {
            omg1 = new Image("/horarios/resources/ttttt.png");
            omg.setImage(omg1);
        } catch (Exception e) {
        }
        
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/horarios/view/drawerContent.fxml"));
            drawer.setSidePane(box);          
            HamburgerBackArrowBasicTransition burgerTask2 = new HamburgerBackArrowBasicTransition(hamburger);
            burgerTask2.setRate(-1);
            drawer.open();
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
    public void SetBorderPane(BorderPane pane){
        this.Panel = pane;
        Listener();
    }
    
    
    public void Listener(){
        Panel.widthProperty().addListener(x->{
           this.drawer.setPrefWidth(Panel.getWidth()-200);
        });
        
        Panel.heightProperty().addListener(v->{
            
        });
    }
}

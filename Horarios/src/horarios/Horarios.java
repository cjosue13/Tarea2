/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios;

import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Application;
import javafx.stage.Stage;
import horarios.util.FlowController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
/**
 *
 * @author Jose Pablo Bermudez
 */
public class Horarios extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        FlowController.getInstance().InitializeFlow(stage, null);
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

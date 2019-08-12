/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios;


import horarios.util.Excel;
import javafx.application.Application;
import javafx.stage.Stage;
import horarios.util.FlowController;
import javafx.scene.image.Image;
/**
 *
 * @author Jose Pablo Bermudez
 */
public class Horarios extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        stage.setFullScreen(true);
        stage.setTitle("Mantenimiento de Horarios");
        stage.getIcons().add(new Image("/horarios/resources/work.png"));

        FlowController.getInstance().InitializeFlow(stage, null);
        FlowController.getInstance().goMain();
        
        Excel excel = new Excel();
        excel.GenerarReporte();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
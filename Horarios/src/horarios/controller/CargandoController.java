/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXProgressBar;
import horarios.util.AppContext;
import horarios.util.FlowController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author JORDI RODRIGUEZ
 */
public class CargandoController extends Controller {

    @FXML
    private Label Titulo;
    @FXML
    private JFXProgressBar progressBar;
    private double progreso;
    private final Timeline timeProgress = new Timeline(new KeyFrame(Duration.ZERO, event -> correrBar()), new KeyFrame(Duration.seconds(0.017)));
    @FXML
    private Label lblPorcentaje;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize() {
        Cargando();
    }

    public void Cargando() {
        timeProgress.setCycleCount(Timeline.INDEFINITE);
        correrBar();
        timeProgress.play();
    }

    public void correrBar() {
        progreso += 0.02;
        progressBar.setProgress(progreso);
        int porcentaje = (int) (progreso * 100);
        lblPorcentaje.setText(String.valueOf(porcentaje) + "%");
        if (progreso > 0.9) {
            timeProgress.stop();
            String Vista = (String) AppContext.getInstance().get("Vista");
            FlowController.getInstance().goView(Vista);
        }
    }

}

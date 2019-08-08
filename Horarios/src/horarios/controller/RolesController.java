/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import horarios.model.RolDto;
import horarios.service.RolService;
import horarios.util.FlowController;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class RolesController extends Controller {

    @FXML
    private TableView<RolDto> table;
    @FXML
    private TableColumn<RolDto, String> COL_NOMBRE_ROL;
    @FXML
    private Label Titulo;
    @FXML
    private JFXButton btnEditar1;
    @FXML
    private JFXButton btnEliminar;
    @FXML
    private JFXButton btnAgregar1;
    @FXML
    private JFXTextField txtNombre;
    @FXML
    private RadioButton RotativoRadioButtonY;
    @FXML
    private RadioButton RotativoRadioButtonN;
    private RolService rolservice;
    private Respuesta resp;
    private ArrayList<RolDto> roles;
    private ObservableList items;
    private Mensaje ms;
    private RolDto rol;
    @FXML
    private ToggleGroup rotativo;

    @Override
    public void initialize() {
        rolservice = new RolService();
        ms = new Mensaje();
        resp = rolservice.getRoles();
        roles = ((ArrayList<RolDto>) resp.getResultado("Roles"));
        COL_NOMBRE_ROL.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombreRol()));

        items = FXCollections.observableArrayList(roles);
        table.setItems(items);

    }

    @FXML
    private void editar(ActionEvent event) {

    }

    @FXML
    private void eliminar(ActionEvent event) {

        if (table.getSelectionModel() != null) {
            if (table.getSelectionModel().getSelectedItem() != null) {
                rolservice.EliminarRol(table.getSelectionModel().getSelectedItem().getId());

                ms.show(Alert.AlertType.INFORMATION, "Información", "Datos Eliminados correctamente");

                resp = rolservice.getRoles();
                items.clear();
                roles = (ArrayList<RolDto>) resp.getResultado("Roles");
                items = FXCollections.observableArrayList(roles);
                table.setItems(items);
            } else {
             
                ms.show(Alert.AlertType.WARNING, "Información", "Debes seleccionar el elemento a eliminar");
            }
        }
    }

    @FXML
    private void agregar(ActionEvent event) {
        String rotar = null;
        if (RotativoRadioButtonN.isSelected()) {
            rotar = "N";
        } else {
            if (RotativoRadioButtonY.isSelected()) {
                rotar = "Y";
            }
        }
        if (registroCorrecto()) {
            String nombre = txtNombre.getText();

            rol = new RolDto(nombre, rotar, 1, null);
            try {
                resp = rolservice.guardarRol(rol);
                ms.show(Alert.AlertType.INFORMATION, "Informacion de guardado", resp.getMensaje());
                limpiarValores();
                roles = (ArrayList) rolservice.getRoles().getResultado("Roles");
                table.getItems().clear();
                items = FXCollections.observableArrayList(roles);
                table.setItems(items);

            } catch (Exception e) {
                //Preguntar a Carranza
                ms.show(Alert.AlertType.ERROR, "Informacion de guardado", "Hubo un error al momento de guardar el hospital.");
            }
        } else {
            ms.show(Alert.AlertType.ERROR, "Informacion acerca del guardado", "Existen datos erroneos en el registro, "
                    + "verifica que todos los datos esten llenos.");
        }
    }

    @FXML
    private void abrirHorario(ActionEvent event) {

        FlowController.getInstance().goView("AsignacionHorario");

    }

    boolean registroCorrecto() {
        if (!txtNombre.getText().isEmpty() && (!RotativoRadioButtonN.getText().isEmpty() || !RotativoRadioButtonY.getText().isEmpty()) /*&& !txtCodigo.getText().isEmpty()*/) {
            return true;
        } else {
            return false;
        }
    }

    void limpiarValores() {
        txtNombre.clear();
        //txtCodigo.clear();
    }
}

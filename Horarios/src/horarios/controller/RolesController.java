/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import horarios.Horarios;
import horarios.model.DiaDto;
import horarios.model.HorarioDto;
import horarios.model.RolDto;
import horarios.service.DiaService;
import horarios.service.HorarioService;
import horarios.service.RolService;
import horarios.util.AppContext;
import horarios.util.FlowController;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private HorarioDto horarioDto;
    @FXML
    private ToggleGroup rotativo;
    private HorarioService horarioService;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Button rbNo;
    @FXML
    private JFXTextField txtRolesFIl;
    @FXML
    private TableColumn<RolDto, Number> COL_ID_ROL;

    @Override
    public void initialize() {
        inicio();
    }

    public void inicio() {
        typeKeys();
        rolservice = new RolService();
        horarioService = new HorarioService();
        ms = new Mensaje();
        resp = rolservice.getRoles();

        roles = ((ArrayList) resp.getResultado("Roles"));
        COL_NOMBRE_ROL.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombreRol()));
        COL_ID_ROL.setCellValueFactory(value -> new SimpleIntegerProperty(value.getValue().getId()));

        items = FXCollections.observableArrayList(roles);
        table.setItems(items);
    }

    @FXML
    private void editar(ActionEvent event) {
        if (table.getSelectionModel() != null) {
            if (table.getSelectionModel().getSelectedItem() != null) {
                if (!txtNombre.getText().isEmpty() && (RotativoRadioButtonN.isSelected()
                        || RotativoRadioButtonY.isSelected())) {
                    String rotar = null;
                    if (RotativoRadioButtonN.isSelected()) {
                        rotar = "N";
                    } else if (RotativoRadioButtonY.isSelected()) {
                        rotar = "Y";
                    }

                    if (AppContext.getInstance().get("horario") == null) {
                        horarioDto = (HorarioDto) rolservice.getHorario(rol).getResultado("Horario");
                    } else {
                        horarioDto = (HorarioDto) AppContext.getInstance().get("horario");
                    }

                    String nombre = txtNombre.getText();
                    Integer id = rol.getId();
                    Integer version = rol.getVersion() + 1;
                    horarioDto.setVersion(horarioDto.getVersion() + 1);
                 
                    rol = new RolDto(nombre, rotar, version, id, null);
                    try {
                        resp = rolservice.guardarRol(rol);

                        //Validar luego si se ha cambiado de rol 
                        if (!rol.getId().equals(horarioDto.getRol().getId())) {
                            horarioDto.setRol((RolDto) resp.getResultado("Rol"));
                        }

                        resp = horarioService.guardarHorario(horarioDto);

                        DiaService diaService = new DiaService();
                        
                        Respuesta respuesta = horarioService.getDias(horarioDto.getId());
                        ArrayList<DiaDto> dias = (ArrayList) respuesta.getResultado("getDias");
                        
                        for (DiaDto dia : dias) {
                            Boolean encontrado=false;
                            for (DiaDto diaSem : horarioDto.getDias()) {
                                if(dia.getNombre().equals(diaSem.getNombre())){
                                    encontrado = true;
                                    break;
                                }
                            }
                            if(!encontrado){
                                diaService.EliminarDia(dia.getDiaid());
                            }
                        }
                        

                        horarioDto.getDias().stream().forEach(dia -> {
                            dia.setVersion(dia.getVersion() + 1);
                            diaService.guardarDia(dia);
                        });

                        ms.showModal(Alert.AlertType.INFORMATION, "Informacion de guardado", this.getStage(), resp.getMensaje());
                        roles = (ArrayList) rolservice.getRoles().getResultado("Roles");
                        table.getItems().clear();
                        items = FXCollections.observableArrayList(roles);
                        table.setItems(items);

                        limpiarValores();
                    } catch (Exception e) {
                        //Preguntar a Carranza
                        ms.showModal(Alert.AlertType.ERROR, "Informacion de guardado", this.getStage(), "Hubo un error al momento de editar el rol.");
                    }
                } else {
                    ms.showModal(Alert.AlertType.ERROR, "Informacion acerca del guardado", this.getStage(), "Existen datos erroneos en el registro, "
                            + "verifica que todos los datos esten llenos o que ya hayas creado un horario.");
                }
            } else {
                ms.showModal(Alert.AlertType.WARNING, "Información", this.getStage(), "Debes seleccionar el elemento para editar");
            }
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (table.getSelectionModel() != null) {
            if (table.getSelectionModel().getSelectedItem() != null) {
                rolservice.EliminarRol(table.getSelectionModel().getSelectedItem().getId());
                ms.showModal(Alert.AlertType.INFORMATION, "Información", this.getStage(), "Datos Eliminados correctamente");
                resp = rolservice.getRoles();
                items.clear();
                roles = (ArrayList<RolDto>) resp.getResultado("Roles");
                items = FXCollections.observableArrayList(roles);
                table.setItems(items);
                limpiarValores();

            } else {
                ms.showModal(Alert.AlertType.WARNING, "Información", this.getStage(), "Debes seleccionar el elemento a eliminar");
            }
        }
    }

    @FXML
    private void agregar(ActionEvent event) {
        //Mientras cumpla los datos del registro correcto y no haya seleccionado un dato para editar o elminar
        if (registroCorrecto()) {
            if (table.getSelectionModel().getSelectedItem() == null) {
                String rotar = null;
                if (RotativoRadioButtonN.isSelected()) {
                    rotar = "N";
                } else {
                    if (RotativoRadioButtonY.isSelected()) {
                        rotar = "Y";
                    }
                }
                String nombre = txtNombre.getText();

                rol = new RolDto(nombre, rotar, 1, null, null);
                try {
                    resp = rolservice.guardarRol(rol);
                    //Guardo el horario en base de datos
                    HorarioDto horario = (HorarioDto) AppContext.getInstance().get("horario");/*para poder usar los datos desde otra ventana*/
                    horario.setRol((RolDto) resp.getResultado("Rol"));
                    //horario.setOrdenRotacion(0);
                    HorarioService horService = new HorarioService();
                    Respuesta respHorario = horService.guardarHorario(horario);
 
                    DiaService diaService = new DiaService();
                    horario.getDias().stream().forEach(dia -> {
                        dia.setHorario((HorarioDto) respHorario.getResultado("Horario"));
                        diaService.guardarDia(dia);
                    });

                    ms.showModal(Alert.AlertType.INFORMATION, "Informacion de guardado", this.getStage(), resp.getMensaje());
                    roles = (ArrayList) rolservice.getRoles().getResultado("Roles");
                    table.getItems().clear();
                    items = FXCollections.observableArrayList(roles);
                    table.setItems(items);
                    limpiarValores();
                } catch (Exception e) {
                    //Preguntar a Carranza
                    ms.showModal(Alert.AlertType.ERROR, "Informacion de guardado", this.getStage(), "Hubo un error al momento de guardar el rol.");
                }

            } else {
                ms.showModal(Alert.AlertType.WARNING, "Informacion acerca del guardado", this.getStage(), "Has seleccionado un rol en la tabla ,solo puedes"
                        + " agregar un registro nuevo. Si agregaras un rol nuevo debes limpiar el registro.");
            }
        } else {
            ms.showModal(Alert.AlertType.ERROR, "Informacion acerca del guardado", this.getStage(), "Existen datos erroneos en el registro, "
                    + "verifica que todos los datos esten llenos o que ya hayas creado un horario.");
        }
    }

    @FXML
    private void abrirHorario(ActionEvent event) throws IOException {
        FlowController.getInstance().goViewInWindowModal("AsignacionHorario", this.getStage(), false);
    }

    boolean registroCorrecto() {
        return !txtNombre.getText().isEmpty() && (RotativoRadioButtonN.isSelected()
                || RotativoRadioButtonY.isSelected()) && AppContext.getInstance().get("horario") != null;//el app context valida que se haya selecionado una lista de horarios
    }

    void limpiarValores() {
        txtNombre.clear();
        FlowController.getInstance().delete("AsignacionHorario");
        AppContext.getInstance().delete("horario");
        AppContext.getInstance().delete("Rol");
        table.getSelectionModel().clearSelection();
        RotativoRadioButtonN.setSelected(true);
    }

    @FXML
    private void DatosRoles(MouseEvent event) {
        if (table.getSelectionModel() != null) {
            if (table.getSelectionModel().getSelectedItem() != null) {
                rol = table.getSelectionModel().getSelectedItem();
                //Guardo el Rol para utilizarlo en la vista de horarios
                AppContext.getInstance().set("Rol", rol);
                //Borra informacion en la vista de horarios 
                FlowController.getInstance().delete("AsignacionHorario");
                txtNombre.setText(rol.getNombreRol());
                if (rol.getHorarioRotativo().equals("Y")) {
                    RotativoRadioButtonY.setSelected(true);
                } else {
                    RotativoRadioButtonN.setSelected(true);
                }
            }
        }
    }

    @FXML
    private void limpiarResgistro(ActionEvent event) {
        limpiarValores();
    }
    private void typeKeys() {
        txtNombre.setOnKeyTyped(Horarios.aceptaCaracteres);

    }

    @FXML
    private void filtrar(ActionEvent event) {
        try {
            if (!txtRolesFIl.getText().isEmpty()) {
                Integer Folio = Integer.valueOf(txtRolesFIl.getText());
                resp = rolservice.getRol(Folio);
                ms.show(Alert.AlertType.INFORMATION, "Informacion sobre busqueda", resp.getMensaje());
                if (resp.getResultado("RolID") != null) {
                    RolDto emp = ((RolDto) resp.getResultado("RolID"));

                    roles.clear();
                    roles.add(emp);

                    items = FXCollections.observableArrayList(roles);
                    table.setItems(items);
                }
            }
            else {
                    resp = rolservice.getRoles();
                    roles = ((ArrayList) resp.getResultado("Roles"));
                    items = FXCollections.observableArrayList(roles);
                    table.setItems(items);
                }
        } catch (NumberFormatException e) {
            ms.showModal(Alert.AlertType.WARNING, "Alerta", this.stage, "Digita únicamente números");
        }
    }
}

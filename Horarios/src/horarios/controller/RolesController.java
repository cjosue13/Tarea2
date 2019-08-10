/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import horarios.model.HorarioDto;
import horarios.model.Rol;
import horarios.model.RolDto;
import horarios.service.DiaService;
import horarios.service.HorarioService;
import horarios.service.RolService;
import horarios.util.AppContext;
import horarios.util.FlowController;
import horarios.util.Mensaje;
import horarios.util.Respuesta;
import java.time.LocalDate;
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
import javafx.scene.input.MouseEvent;

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
    private Respuesta resp1;
    private ArrayList<RolDto> roles;
    private ArrayList<HorarioDto> horarios;
    private ObservableList items;
    private Mensaje ms;
    private RolDto rol;
    private HorarioDto horarioDto;
    @FXML
    private ToggleGroup rotativo;
    private HorarioService horarioService;
    static boolean selecionado = false;
    @Override
    public void initialize() {
        //horarioDto = new HorarioDto();
        rolservice = new RolService();
        horarioService = new HorarioService();
        ms = new Mensaje();
        resp = rolservice.getRoles();
        resp1 = horarioService.getHorarios();
        horarios = ((ArrayList<HorarioDto>) resp1.getResultado("Horarios"));
        roles = ((ArrayList<RolDto>) resp.getResultado("Roles"));
        COL_NOMBRE_ROL.setCellValueFactory(value -> new SimpleStringProperty(value.getValue().getNombreRol()));

        items = FXCollections.observableArrayList(roles);
        table.setItems(items);

    }

    @FXML
    private void editar(ActionEvent event) {
        String rotar = null;
        if (RotativoRadioButtonN.isSelected()) {
            rotar = "N";
        } else if (RotativoRadioButtonY.isSelected()) {
                rotar = "Y";
        }
        Integer IDROL = 0;
        for(int i = 0; i < roles.size(); i++){
            if(roles.get(i).getId().equals(rol.getId())){
                IDROL = roles.get(i).getId();
            }
        }
        System.out.println(IDROL);
        LocalDate FechaInicio = null;
        Integer HorasLibres = null;
        Integer id1 = 0;
        for(int i = 0; i < horarios.size(); i++){
            if(horarios.get(i).getRol().getId().equals(IDROL)){
                FechaInicio = horarios.get(i).getFechaInicio();
                HorasLibres = horarios.get(i).getHorasLibras();
                id1 = horarios.get(i).getId();
            }
        }
        horarioDto = new HorarioDto(FechaInicio,HorasLibres,1,id1,horarios.get(0).getRol());
        String nombre = txtNombre.getText();
        Integer id = rol.getId();

        rol = new RolDto(nombre, rotar, 1, id, horarioDto);
        try {
            resp = rolservice.guardarRol(rol);
            //Guardo el horario en base de datos
            horarioDto.setRol((RolDto)resp.getResultado("Rol"));

            HorarioService horService = new HorarioService();
            AppContext.getInstance().delete("horario");
            Respuesta respHorario = horService.guardarHorario(horarioDto);

            DiaService diaService = new DiaService();
            horarioDto.getDias().stream().forEach(dia->{ 
                dia.setHorario((HorarioDto)respHorario.getResultado("Horario"));
                diaService.guardarDia(dia);
            });
            ms.show(Alert.AlertType.INFORMATION, "Informacion de guardado", resp.getMensaje());
            limpiarValores();
            roles = (ArrayList) rolservice.getRoles().getResultado("Roles");
            table.getItems().clear();
            items = FXCollections.observableArrayList(roles);
            table.setItems(items);
        }catch(Exception e) {
            //Preguntar a Carranza
            ms.show(Alert.AlertType.ERROR, "Informacion de guardado", "Hubo un error al momento de guardar el hospital.");
        }
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

            rol = new RolDto(nombre, rotar, 1, null,null);
            try {
                resp = rolservice.guardarRol(rol);
                //Guardo el horario en base de datos
                HorarioDto horario = (HorarioDto) AppContext.getInstance().get("horario");/*para poder usar los datos desde otra ventana*/
                horario.setRol((RolDto)resp.getResultado("Rol"));
                
                HorarioService horService = new HorarioService();
                AppContext.getInstance().delete("horario");
                Respuesta respHorario = horService.guardarHorario(horario);
                
                DiaService diaService = new DiaService();
                horario.getDias().stream().forEach(dia->{ 
                    dia.setHorario((HorarioDto)respHorario.getResultado("Horario"));
                    diaService.guardarDia(dia);
                });
                
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
                    + "verifica que todos los datos esten llenos o que ya hayas creado un horario.");
        }
    }

    @FXML
    private void abrirHorario(ActionEvent event) {

        FlowController.getInstance().goViewInWindowModal("AsignacionHorario",this.getStage(),false);

    }

    boolean registroCorrecto() {
        return !txtNombre.getText().isEmpty() && (!RotativoRadioButtonN.getText().isEmpty() 
        || !RotativoRadioButtonY.getText().isEmpty()) && AppContext.getInstance().get("horario")!=null;//el app context valida que se haya selecionado una lista de horarios
    }

    void limpiarValores() {
        txtNombre.clear();
        FlowController.getInstance().delete("AsignacionHorario");
    }
    @FXML
    private void DatosRoles(MouseEvent event) {
        if (table.getSelectionModel() != null) {
            if (table.getSelectionModel().getSelectedItem() != null) {
                rol = table.getSelectionModel().getSelectedItem();
                txtNombre.setText(rol.getNombreRol());
                if(rol.getHorarioRotativo().equals("Y")){
                    RotativoRadioButtonY.setSelected(true);
                    RotativoRadioButtonN.setSelected(false);
                }else{
                    RotativoRadioButtonN.setSelected(true);
                    RotativoRadioButtonY.setSelected(false);
                }
                selecionado = true;
            }else{
               selecionado = false;
            }
        }
    }
}

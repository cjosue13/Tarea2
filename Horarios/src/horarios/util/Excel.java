/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.util;

import static horarios.controller.HorariosController.CantRol;
import static horarios.controller.HorariosController.FinalDomingo;
import static horarios.controller.HorariosController.FinalJueves;
import static horarios.controller.HorariosController.FinalLunes;
import static horarios.controller.HorariosController.FinalMartes;
import static horarios.controller.HorariosController.FinalMiercoles;
import static horarios.controller.HorariosController.FinalSabado;
import static horarios.controller.HorariosController.FinalViernes;
import static horarios.controller.HorariosController.InicioDomingo;
import static horarios.controller.HorariosController.InicioJueves;
import static horarios.controller.HorariosController.InicioLunes;
import static horarios.controller.HorariosController.InicioMartes;
import static horarios.controller.HorariosController.InicioMiercoles;
import static horarios.controller.HorariosController.InicioSabado;
import static horarios.controller.HorariosController.InicioViernes;
import static horarios.controller.HorariosController.nombreX;
import horarios.model.DiaDto;
import horarios.model.EmpleadoDto;
import horarios.model.HorarioDto;
import horarios.model.PuestoDto;
import horarios.model.RolDto;
import horarios.service.EmpleadoService;
import horarios.service.HorarioService;
import horarios.service.PuestoService;
import horarios.service.RolService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.format.Colour;

/**
 *
 * @author JORDI RODRIGUEZ
 */
public class Excel {

    private HorarioDto horario;
    private ArrayList<HorarioDto> horarios;
    private HorarioService horarioService = new HorarioService();
    private Mensaje ms = new Mensaje();
    private Respuesta resp = horarioService.getHorarios();
    private File directorio = new File("C:\\Reporte");
    private ArrayList<EmpleadoDto> empleados;
    private ArrayList<PuestoDto> puestos;
    private PuestoService puesService;
    Mensaje message = new Mensaje();
    String nombreXX = nombreX.replace(" ", "");
    private int o = 0;
    private int t = 2, r = 3, g = 4, w = 5, f = 6;
    private int cant = 0;
    private ArrayList<RolDto> roles;
    private Respuesta respRol;
    private RolDto rol;
    private RolService rolservice;
    private int tamano = 0;

    public void GenerarReporte(PuestoDto puesto, boolean abrirArchivo, boolean enviarArchivo) throws WriteException {
        try {
            try {
                nombreX = (puesto.getEmpleado() != null) ? puesto.getEmpleado().getNombre().replace(" ", "") + puesto.getEmpleado().getApellido().replace(" ", "") : "SinAsignar";//para que se eliminen los espacios del nombre
                WorkbookSettings conf = new WorkbookSettings();
                conf.setEncoding("ISO-8859-1");
                // Aqui se crea el archivo
                WritableWorkbook workbook = Workbook.createWorkbook(new File("C:\\Reporte\\" + nombreX + ".xls"), conf);
                // Aqui podemos crear las hojas del archivo y darles formato y todo lo demás
                WritableSheet sheet = workbook.createSheet("Reporte de Horarios", 0); // Nombre de la hoja y número de hoja
                WritableFont h = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);// Fuente de texto normal
                WritableFont h1 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);// Fuente de titulo
                WritableCellFormat hformat = new WritableCellFormat(h); // se agrega el formato a la celda
                WritableCellFormat hformat1 = new WritableCellFormat(h1);
                hformat.setBackground(Colour.GRAY_25);
                hformat1.setBackground(Colour.AQUA);
                puesto.getRoles().stream().forEach(rol -> {
                    CellView cv = new CellView();
                    cv.setAutosize(true);
                    try {
                        sheet.setColumnView(0, cv);
                        sheet.setColumnView(1, cv);
                        sheet.setColumnView(2, cv);
                        sheet.setColumnView(3, cv);
                        sheet.setColumnView(4, cv);
                        sheet.setColumnView(5, cv);
                        sheet.setColumnView(6, cv);
                        sheet.setColumnView(7, cv);
                        sheet.addCell(new jxl.write.Label(1, o, "     ", hformat1));
                        sheet.addCell(new jxl.write.Label(4, o, "       ", hformat1));
                        sheet.addCell(new jxl.write.Label(6, o, "       ", hformat1));
                        sheet.addCell(new jxl.write.Label(7, o, "       ", hformat1));
                        sheet.addCell(new jxl.write.Label(2, o, "de Empleado: ", hformat1));
                        sheet.addCell(new jxl.write.Label(0, o, "Horario", hformat1));// Esto es para escribir.. en este caso está escribiendo en la celda [0][0]
                        sheet.addCell(new jxl.write.Label(1, t, "Lunes", hformat));
                        sheet.addCell(new jxl.write.Label(2, t, "Martes", hformat));
                        sheet.addCell(new jxl.write.Label(3, t, "Miercoles", hformat));
                        sheet.addCell(new jxl.write.Label(4, t, "Jueves", hformat));
                        sheet.addCell(new jxl.write.Label(5, t, "Viernes", hformat));
                        sheet.addCell(new jxl.write.Label(6, t, "Sabado", hformat));
                        sheet.addCell(new jxl.write.Label(7, t, "Domingo", hformat));
                        sheet.addCell(new jxl.write.Label(0, t, "Dia", hformat1));
                        sheet.addCell(new jxl.write.Label(0, r, "Inicio:", hformat1));
                        sheet.addCell(new jxl.write.Label(0, g, "Salida:", hformat1));
                        sheet.addCell(new jxl.write.Label(2, o, (puesto.getEmpleado() != null) ? puesto.getEmpleado().getNombre() : "Sin asignar", hformat1));
                        sheet.addCell(new jxl.write.Label(3, o, puesto.getNombrePuesto(), hformat1));
                        sheet.addCell(new jxl.write.Label(5, o, rol.getNombreRol(), hformat1));
                    } catch (WriteException ex) {
                        Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    rol.getHorario().getDias().stream().forEach(dia -> {
                        try {

                            sheet.addCell(new jxl.write.Label(1, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(1, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(2, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(2, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(3, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(3, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(4, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(4, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(5, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(5, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(6, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(6, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(7, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(7, g, "Libre", hformat));

                            if (dia.getNombre().equals("Lunes")) {
                                sheet.addCell(new jxl.write.Label(1, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(1, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Martes")) {
                                sheet.addCell(new jxl.write.Label(2, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(2, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Miercoles")) {
                                sheet.addCell(new jxl.write.Label(3, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(3, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Jueves")) {
                                sheet.addCell(new jxl.write.Label(4, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(4, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Viernes")) {
                                sheet.addCell(new jxl.write.Label(5, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(5, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Sabado")) {
                                sheet.addCell(new jxl.write.Label(6, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(6, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Domingo")) {
                                sheet.addCell(new jxl.write.Label(7, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(7, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }

                        } catch (WriteException ex) {
                            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    //para mover el horario de cada empleado 
                    o += 6;
                    t += 6;
                    r += 6;
                    g += 6;
                    w += 6;
                    cant++;
                });
                workbook.write(); // escribimos en el archivo
                workbook.close(); //

                if (abrirArchivo) {
                    Runtime.getRuntime().exec("cmd /c start " + "C:\\Reporte\\" + nombreX + ".xls");
                }
                if (enviarArchivo) {
                    if (puesto.getEmpleado() != null) {
                        SendMail(puesto.getEmpleado().getCorreo());
                    }
                }
            } catch (FileNotFoundException e) {
                ms.show(Alert.AlertType.WARNING, "Alerta", "El archivo está abierto por favor cierralo");
            }
        } catch (Exception e) {

        }

    }

    public void GenerarReporte() throws WriteException {
        String nombreXX = nombreX.replace(" ", "");//para que se eliminen los espacios del nombre
        //para que se eliminen los espacios del nombre
        directorio.mkdir();//se crea la carpeta
        try {
            try {
                // esto es para obtener la dirección del proyecto
                WorkbookSettings conf = new WorkbookSettings();
                conf.setEncoding("ISO-8859-1");

                // Aqui se crea el archivo
                WritableWorkbook workbook = Workbook.createWorkbook(new File("C:\\Reporte\\" + nombreXX + ".xls"), conf);

                // Aqui podemos crear las hojas del archivo y darles formato y todo lo demás
                WritableSheet sheet = workbook.createSheet("Reporte de Horarios", 0); // Nombre de la hoja y número de hoja

                WritableFont h = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);// Fuente de texto normal
                WritableFont h1 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);// Fuente de titulo

                WritableCellFormat hformat = new WritableCellFormat(h); // se agrega el formato a la celda
                WritableCellFormat hformat1 = new WritableCellFormat(h1);
                CellView cv = new CellView();
                cv.setAutosize(true);
                hformat.setBackground(Colour.GRAY_25);
                hformat1.setBackground(Colour.AQUA);
                sheet.setColumnView(0, cv);
                sheet.setColumnView(1, cv);
                sheet.setColumnView(2, cv);
                sheet.setColumnView(3, cv);
                sheet.setColumnView(4, cv);
                sheet.setColumnView(5, cv);
                sheet.setColumnView(6, cv);
                sheet.setColumnView(7, cv);
                sheet.addCell(new jxl.write.Label(1, o, "     ", hformat1));
                sheet.addCell(new jxl.write.Label(4, o, "       ", hformat1));
                sheet.addCell(new jxl.write.Label(6, o, "       ", hformat1));
                sheet.addCell(new jxl.write.Label(7, o, "       ", hformat1));
                sheet.addCell(new jxl.write.Label(4, 0, "de: ", hformat1));
                sheet.addCell(new jxl.write.Label(3, 0, "Horario", hformat1));// Esto es para escribir.. en este caso está escribiendo en la celda [0][0]
                sheet.addCell(new jxl.write.Label(1, 2, "Lunes", hformat));
                sheet.addCell(new jxl.write.Label(2, 2, "Martes", hformat));
                sheet.addCell(new jxl.write.Label(3, 2, "Miercoles", hformat));
                sheet.addCell(new jxl.write.Label(4, 2, "Jueves", hformat));
                sheet.addCell(new jxl.write.Label(5, 2, "Viernes", hformat));
                sheet.addCell(new jxl.write.Label(6, 2, "Sabado", hformat));
                sheet.addCell(new jxl.write.Label(7, 2, "Domingo", hformat));
                sheet.addCell(new jxl.write.Label(0, 2, "Dia", hformat1));
                sheet.addCell(new jxl.write.Label(0, 3, "Inicio:", hformat1));
                sheet.addCell(new jxl.write.Label(0, 4, "Salida:", hformat1));
                sheet.addCell(new jxl.write.Label(0, 5, "Cant Roles:", hformat1));

                sheet.addCell(new jxl.write.Label(5, 0, nombreX, hformat1));
                sheet.addCell(new jxl.write.Label(1, 3, InicioLunes, hformat));
                sheet.addCell(new jxl.write.Label(1, 4, FinalLunes, hformat));
                sheet.addCell(new jxl.write.Label(2, 3, InicioMartes, hformat));
                sheet.addCell(new jxl.write.Label(2, 4, FinalMartes, hformat));
                sheet.addCell(new jxl.write.Label(3, 3, InicioMiercoles, hformat));
                sheet.addCell(new jxl.write.Label(3, 4, FinalMiercoles, hformat));
                sheet.addCell(new jxl.write.Label(4, 3, InicioJueves, hformat));
                sheet.addCell(new jxl.write.Label(4, 4, FinalJueves, hformat));
                sheet.addCell(new jxl.write.Label(5, 3, InicioViernes, hformat));
                sheet.addCell(new jxl.write.Label(5, 4, FinalViernes, hformat));
                sheet.addCell(new jxl.write.Label(6, 3, InicioSabado, hformat));
                sheet.addCell(new jxl.write.Label(6, 4, FinalSabado, hformat));
                sheet.addCell(new jxl.write.Label(7, 3, InicioDomingo, hformat));
                sheet.addCell(new jxl.write.Label(7, 4, FinalDomingo, hformat));
                sheet.addCell(new jxl.write.Label(1, 5, String.valueOf(CantRol), hformat1));
                workbook.write(); // escribimos en el archivo
                workbook.close(); // lo cerramos

                // Con esto se abre automáticamente el archivo
                Runtime.getRuntime().exec("cmd /c start " + "C:\\Reporte\\" + nombreXX + ".xls");
            } catch (FileNotFoundException e) {
                ms.show(Alert.AlertType.WARNING, "Alerta", "El archivo está abierto por favor cierralo");
            }
        } catch (IOException ex) {
        }
    }

    public void GenerarReporteTodos() throws WriteException, IOException {

        puesService = new PuestoService();
        resp = puesService.getPuestos();
        puestos = ((ArrayList) resp.getResultado("Puestos"));
        try {
            WorkbookSettings conf = new WorkbookSettings();
            conf.setEncoding("ISO-8859-1");
            // Aqui se crea el archivo
            WritableWorkbook workbook = Workbook.createWorkbook(new File("C:\\Reporte\\" + "Empleados" + ".xls"), conf);
            // Aqui podemos crear las hojas del archivo y darles formato y todo lo demás
            WritableSheet sheet = workbook.createSheet("Reporte de Horarios", 0); // Nombre de la hoja y número de hoja
            WritableFont h = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);// Fuente de texto normal
            WritableFont h1 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);// Fuente de titulo
            WritableCellFormat hformat = new WritableCellFormat(h); // se agrega el formato a la celda
            WritableCellFormat hformat1 = new WritableCellFormat(h1);
            CellView cv = new CellView();
            cv.setAutosize(true);
            hformat.setBackground(Colour.GRAY_25);
            hformat1.setBackground(Colour.AQUA);

            puestos.stream().forEach(puesto -> {
                puesto.getRoles().stream().forEach(rol -> {
                    try {
                        sheet.setColumnView(0, cv);
                        sheet.setColumnView(1, cv);
                        sheet.setColumnView(2, cv);
                        sheet.setColumnView(3, cv);
                        sheet.setColumnView(4, cv);
                        sheet.setColumnView(5, cv);
                        sheet.setColumnView(6, cv);
                        sheet.setColumnView(7, cv);

                        sheet.addCell(new jxl.write.Label(1, o, "     ", hformat1));
                        sheet.addCell(new jxl.write.Label(4, o, "       ", hformat1));
                        sheet.addCell(new jxl.write.Label(6, o, "       ", hformat1));
                        sheet.addCell(new jxl.write.Label(7, o, "       ", hformat1));
                        sheet.addCell(new jxl.write.Label(2, o, "de: ", hformat1));
                        sheet.addCell(new jxl.write.Label(0, o, "Horario", hformat1));// Esto es para escribir.. en este caso está escribiendo en la celda [0][0]
                        sheet.addCell(new jxl.write.Label(1, t, "Lunes", hformat));
                        sheet.addCell(new jxl.write.Label(2, t, "Martes", hformat));
                        sheet.addCell(new jxl.write.Label(3, t, "Miercoles", hformat));
                        sheet.addCell(new jxl.write.Label(4, t, "Jueves", hformat));
                        sheet.addCell(new jxl.write.Label(5, t, "Viernes", hformat));
                        sheet.addCell(new jxl.write.Label(6, t, "Sabado", hformat));
                        sheet.addCell(new jxl.write.Label(7, t, "Domingo", hformat));
                        sheet.addCell(new jxl.write.Label(0, t, "Dia", hformat1));
                        sheet.addCell(new jxl.write.Label(0, r, "Inicio:", hformat1));
                        sheet.addCell(new jxl.write.Label(0, g, "Salida:", hformat1));
                        sheet.addCell(new jxl.write.Label(2, o, (puesto.getEmpleado() != null) ? puesto.getEmpleado().getNombre() : "Sin asignar", hformat1));
                        sheet.addCell(new jxl.write.Label(3, o, puesto.getNombrePuesto(), hformat1));
                        sheet.addCell(new jxl.write.Label(5, o, rol.getNombreRol(), hformat1));

                    } catch (WriteException ex) {
                        Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    rol.getHorario().getDias().stream().forEach(dia -> {
                        try {
                            sheet.addCell(new jxl.write.Label(1, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(1, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(2, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(2, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(3, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(3, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(4, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(4, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(5, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(5, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(6, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(6, g, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(7, r, "Libre", hformat));
                            sheet.addCell(new jxl.write.Label(7, g, "Libre", hformat));
                            if (dia.getNombre().equals("Lunes")) {
                                sheet.addCell(new jxl.write.Label(1, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(1, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Martes")) {
                                sheet.addCell(new jxl.write.Label(2, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(2, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Miercoles")) {
                                sheet.addCell(new jxl.write.Label(3, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(3, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Jueves")) {
                                sheet.addCell(new jxl.write.Label(4, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(4, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Viernes")) {
                                sheet.addCell(new jxl.write.Label(5, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(5, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Sabado")) {
                                sheet.addCell(new jxl.write.Label(6, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(6, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }
                            if (dia.getNombre().equals("Domingo")) {
                                sheet.addCell(new jxl.write.Label(7, r, dia.getHora_Inicio().toLocalTime().toString(), hformat));
                                sheet.addCell(new jxl.write.Label(7, g, dia.getHora_Salida().toLocalTime().toString(), hformat));
                            }

                        } catch (WriteException ex) {
                            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    //para mover el horario de cada empleado 
                    o += 6;
                    t += 6;
                    r += 6;
                    g += 6;
                    w += 6;
                    cant++;
                });

            });
            try {
                workbook.write(); // escribimos en el archivo
                workbook.close();//cierra el archivo
                Runtime.getRuntime().exec("cmd /c start " + "C:\\Reporte\\" + "Empleados" + ".xls");//ejecuta el archivo
            } catch (IOException ex) {
                Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException e) {
            ms.show(Alert.AlertType.WARNING, "Alerta", "El archivo está abierto por favor cierralo");
        }
    }

    public void SendMail(String Destinatario) throws MessagingException, IOException {
        // Propiedades necesarias
        String nombreXX = nombreX.replace(" ", "");//para que se eliminen los espacios del nombre
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.setProperty("mail.smtp.port", "587");
        prop.setProperty("mail.smtp.user", "horarios.mantenenimiento.una@gmail.com");

        Session session = Session.getDefaultInstance(prop, null); // se inicia sesión con las propiedades
        BodyPart adjunto = new MimeBodyPart(); // Aqui se declara lo que será nuestro archivo adjunto
        adjunto.setDataHandler(new DataHandler(new FileDataSource("C:\\Reporte\\" + nombreXX + ".xls")));// con esto se le da el archivo que enviaremos
        adjunto.setFileName(nombreXX + ".xls"); // Nombre del archivo
        // Aqui es como guardar al archivo para despues añadirlo al mensaje que enviaremos
        MimeMultipart m = new MimeMultipart();
        m.addBodyPart(adjunto);
        MimeMessage mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress("horarios.mantenenimiento.una@gmail.com"));// Aqui se define el usuario que enviará el correo
        mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(Destinatario));// Destinatario
        mensaje.setSubject("Horario");// Aqui podemos escribir el asunto que necesitemos en el correo
        mensaje.setContent(m); // aqui seteamos nuestro archivo
        // Aqui se conecta con nuestro usuario y contraseña se procede a enviar y se cierra la conexión
        Transport t = session.getTransport("smtp");
        t.connect("horarios.mantenenimiento.una@gmail.com", "pzucxlddrffzikky");
        t.sendMessage(mensaje, mensaje.getAllRecipients());
        t.close();
    }
}

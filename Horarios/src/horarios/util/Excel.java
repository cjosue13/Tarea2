/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.util;

import horarios.controller.HorariosController;
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
import horarios.model.HorarioDto;
import horarios.service.HorarioService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.EnumSet.range;
import java.util.Properties;
import static java.util.stream.IntStream.range;
import static java.util.stream.LongStream.range;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
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
import jxl.Range;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import  jxl.format.Colour;
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
    private File directorio=new File("C:\\Reporte"); 
           
    
    Mensaje message = new Mensaje();
    public void GenerarReporte() throws WriteException {
        horarios = ((ArrayList<HorarioDto>) resp.getResultado("Horarios"));
        HorariosController horariosController = new HorariosController();
        try {
            
             directorio.mkdir(); 
            // Configuración necesaria
            WorkbookSettings conf = new WorkbookSettings();
            conf.setEncoding("ISO-8859-1");

            // Aqui se crea el archivo
            WritableWorkbook workbook = Workbook.createWorkbook(new File(directorio.getAbsolutePath()+nombreX+".xls"), conf);
            
            // Aqui podemos crear las hojas del archivo y darles formato y todo lo demás
            WritableSheet sheet = workbook.createSheet("Reporte de Horarios", 0); // Nombre de la hoja y número de hoja
            
            WritableFont h = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);// Fuente de texto normal
            WritableFont h1 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);// Fuente de titulo
            
            WritableCellFormat hformat = new WritableCellFormat(h); // se agrega el formato a la celda
            WritableCellFormat hformat1 = new WritableCellFormat(h1);
            
            hformat.setBackground(Colour.GRAY_25);
            hformat1.setBackground(Colour.AQUA);
            sheet.addCell(new jxl.write.Label(4, 0, "de Empleado: ", hformat1));
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
            //sheet.addCell(new jxl.write.Label(0, 3, "Horas Libres:", hformat));
            
            sheet.addCell(new jxl.write.Label(0, 4, "Salida:", hformat1));
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
            workbook.write(); // escribimos en el archivo
            workbook.close(); // lo cerramos 
            // Con esto se abre automáticamente el archivo
            Runtime.getRuntime().exec("cmd /c start " + directorio.getAbsolutePath()+nombreX+".xls");

        } catch (IOException ex) {
        }
    }

    public void SendMail(String Destinatario) throws MessagingException, IOException {
        // Propiedades necesarias
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.setProperty("mail.smtp.port", "587");
        prop.setProperty("mail.smtp.user", "horarios.mantenenimiento.una@gmail.com");
       
        Session session = Session.getDefaultInstance(prop, null); // se inicia sesión con las propiedades
        BodyPart adjunto = new MimeBodyPart(); // Aqui se declara lo que será nuestro archivo adjunto
        adjunto.setDataHandler(new DataHandler(new FileDataSource(directorio.getAbsolutePath()+nombreX+".xls")));// con esto se le da el archivo que enviaremos
        adjunto.setFileName(nombreX+".xls"); // Nombre del archivo
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
        message.show(Alert.AlertType.INFORMATION, "Envio de Correo", "Su correo ha sido enviado exitosamente a: "+Destinatario);
    }
}

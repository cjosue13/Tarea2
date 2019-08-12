/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
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
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author JORDI RODRIGUEZ
 */
public class Excel {
    Mensaje message = new Mensaje();
    public void GenerarReporte() throws WriteException {
        try {
            // esto es para obtener la dirección del proyecto
            File miDir = new File(".");
            String r = miDir.getCanonicalPath();

            // Configuración necesaria
            WorkbookSettings conf = new WorkbookSettings();
            conf.setEncoding("ISO-8859-1");

            // Aqui se crea el archivo
            WritableWorkbook workbook = Workbook.createWorkbook(new File(r + "\\Reporte" + "\\archivo.xls"), conf);

            // Aqui podemos crear las hojas del archivo y darles formato y todo lo demás
            WritableSheet sheet = workbook.createSheet("Hoja de Prueba", 0); // Nombre de la oja y número de joja
            WritableFont h = new WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD);// Fuente
            WritableCellFormat hformat = new WritableCellFormat(h); // se agrega el formato a la celda
            sheet.addCell(new jxl.write.Label(0, 0, "PRUEBA", hformat));// Esto es para escribir.. en este caso está escribiendo en la celda [0][0]
            workbook.write(); // escribimos en el archivo
            workbook.close(); // lo cerramos 

            // Con esto se abre automáticamente el archivo
            Runtime.getRuntime().exec("cmd /c start " + r + "\\Reporte" + "\\archivo.xls");

        } catch (IOException ex) {
        }
    }

    public void SendMail(String Destinatario) throws MessagingException, IOException {
        try {
            // Propiedades necesarias 
            Properties prop = new Properties();
            prop.setProperty("mail.smtp.auth", "true");
            prop.setProperty("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.setProperty("mail.smtp.port", "587");
            prop.setProperty("mail.smtp.user", "horarios.mantenenimiento.una@gmail.com");

            // Dirección de nuestro proyecto
            File miDir = new File(".");
            String r = miDir.getCanonicalPath();

            Session session = Session.getDefaultInstance(prop, null); // se inicia sesión con las propiedades
            BodyPart adjunto = new MimeBodyPart(); // Aqui se declara lo que será nuestro archivo adjunto
            adjunto.setDataHandler(new DataHandler(new FileDataSource(r + "\\Reporte\\archivo.xls")));// con esto se le da el archivo que enviaremos
            adjunto.setFileName("archivo.xls"); // Nombre del archivo

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
        } catch (FileNotFoundException e) {    
        message.show(Alert.AlertType.ERROR, "Error en el envío", "El archivo a enviar no ha sido encontrado");
        }
    }
    public void crearExcel(){
     //   Workbook work = new XSSFWorkbook();
    }
    
}

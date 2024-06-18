package pl.wipb.beershop.services;

import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import pl.wipb.beershop.models.Order;

@Stateless
public class MailService {
  @Asynchronous
  public void sendOrderSubmitMail(String destinationEmail, Order order) {
    String sourceEmail = "x5705038@gmail.com";
    String password = "cebl zqmr mgkm done";
    String smtpServer = "smtp.gmail.com";
    int smtpPort = 587;
    
    Properties props = new Properties();
    props.put("mail.smtp.auth", true);
    props.put("mail.smtp.starttls.enable", true);
    props.put("mail.smtp.host", smtpServer);
    props.put("mail.smtp.port", smtpPort);
    
    Session session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(sourceEmail, password);
      }
    });
    session.setDebug(true);
    
    try {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(sourceEmail));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinationEmail));
      message.setSubject("eBrowarek - Złożono zamówienie");
      message.setText("Potwierdzamy złożenie zamówienia id: " + order.getId() + "\nProdukty: " + order.getOrderProducts());
      Transport.send(message);
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}

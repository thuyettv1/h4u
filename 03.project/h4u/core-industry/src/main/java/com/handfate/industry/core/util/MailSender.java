package com.handfate.industry.core.util;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Gui mail
 *
 * @author HienDM
 * @since 1.0
 * @version 1.0
 */
public class MailSender {

    /**
     * Cac doi tuong dung de gui mail.
     */
    private MimeMessage message;
    private Transport transport;
    private Session session;
    /**
     * Cau hinh.
     */
    private String username;
    private String password;
    private String host;
    private String port;
    private String alias;

    /**
     * Ham khoi tao. Ket noi luon den mail server. Co the bi 2 loi: - Dang nhap
     * khong dung username, password - Khong ket noi duoc den mail server (khong
     * dung dia chi, mang bi loi,...)
     */
    public MailSender() throws Exception {
        alias = "H4U";
        host = "smtp.gmail.com";
        port = "465";
        username = "h4u.family@gmail.com";
        password = "H4u@123456";
        System.out.println("host: " + host);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        transport = session.getTransport("smtp");
        transport.connect(host, username, password);
    }

    public void sendMail(String email, String subject, String textContent, List<List> files) {
        try {
            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(textContent, "utf-8");
            multipart.addBodyPart(textPart);

            // Dinh kem file
            if (files != null) {
                for (List<String> entry : files) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.setDataHandler(new DataHandler(new FileDataSource(entry.get(1))));
                    attachmentPart.setFileName(entry.get(0));
                    multipart.addBodyPart(attachmentPart);
                }
            }

            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, alias, "UTF-8"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject, "utf-8");
            message.setSentDate(new Date());
            message.setContent(multipart);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendMail(String email, String subject, String textContent) {
        try {
            Multipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(textContent, "utf-8");
            multipart.addBodyPart(textPart);

            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, alias, "UTF-8"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject, "utf-8");
            message.setSentDate(new Date());
            message.setContent(multipart);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Dong ket noi, giai phong tai nguyen.
     */
    public void closeTransport() {
        try {
            if (transport != null) {
                transport.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

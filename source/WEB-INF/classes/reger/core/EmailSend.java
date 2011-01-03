package reger.core;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import java.util.Properties;


/**
 * Send email class.
 */
public class EmailSend {

    public static void sendMail(String from, String to, String subject, String message, boolean isHtmlEmail){
        if (from.equals("")){
            from = reger.Vars.EMAILDEFAULTFROM;
        }
        System.out.println("EMAIL BEGIN SEND VIA THREAD: to"+to+" from:"+from+" subject:"+subject);
        Debug.debug(5, "", "Email Sent<br>To:" +to+"<br>From:" + from + "<br>Subject:" + subject + "<br>Body:" + message + "<br>isHtmlEmail:" + isHtmlEmail);
        try {
            //Kick off a thread to send the email
            reger.core.EmailSendThread eThr = new reger.core.EmailSendThread();
            eThr.setPriority(Thread.MIN_PRIORITY);
            eThr.to = to;
            eThr.from = from;
            eThr.subject = subject;
            eThr.message = message;
            eThr.isHtmlEmail = isHtmlEmail;
            eThr.start();
        }catch (Exception e) {
            Debug.errorsave(e, "", "Error starting email thread.  Should have been sending to: " + reger.systemproperties.AllSystemProperties.getProp("EMAILSERVER"));
        }
        System.out.println("EMAIL END SEND VIA THREAD: to"+to+" from:"+from+" subject:"+subject);
    }

    public static void sendMail(String from, String to, String subject, String message){
        sendMail(from, to, subject, message, false);
    }

    public static void sendMailNoThread(String from, String to, String subject, String message, boolean isHtmlEmail) throws EmailSendException{
        try{

            System.out.println("EMAIL BEGIN SEND: to"+to+" from:"+from+" subject:"+subject);

            Properties props =  System.getProperties();
            props.put("mail.smtp.host",  reger.systemproperties.AllSystemProperties.getProp("EMAILSERVER"));
            props.put("mail.transport.protocol", "smtp");
            Session session = Session.getDefaultInstance(props, null);



            Message msg = new MimeMessage(session);

            InternetAddress addr = new InternetAddress(to);
            InternetAddress[] addrArray = new InternetAddress[] {addr};
            msg.addRecipients(Message.RecipientType.TO, new InternetAddress[] {addr});
            InternetAddress from_addr = new InternetAddress(from);
            msg.setFrom(from_addr);
            msg.setSubject(subject);

            if (isHtmlEmail){
                msg.setContent(message, "text/html");
            } else {
                msg.setContent(message, "text/plain");
            }

            msg.saveChanges();

            //Transport.send(msg);
            Transport transport = session.getTransport("smtp");
            transport.connect(reger.systemproperties.AllSystemProperties.getProp("EMAILSERVER"), null, null);
            transport.sendMessage(msg, addrArray);



        } catch (javax.mail.SendFailedException nsend){
            System.out.println("EMAIL FAIL: nsend:"+nsend.getMessage());
            Debug.debug(5, "EmailSendThread.java", nsend);
            EmailSendException ex = new EmailSendException("Email send failed: " + nsend.getMessage());
            throw ex;
        } catch (javax.mail.internet.AddressException ex){
            System.out.println("EMAIL FAIL: ex:"+ex.getMessage());
            Debug.debug(5, "EmailSendThread.java", ex);
            EmailSendException esex = new EmailSendException("Email address invalid: " + ex.getMessage());
            throw esex;
        } catch (Exception e) {
            System.out.println("EMAIL FAIL: e:"+e.getMessage());
            Debug.errorsave(e, "EmailSendThread.java");
            EmailSendException esex = new EmailSendException("Email send failed: " + e.getMessage());
            throw esex;
        }

        System.out.println("EMAIL END SEND: to"+to+" from:"+from+" subject:"+subject);
    }




}

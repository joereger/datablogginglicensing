package reger.core;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import java.util.Properties;

/**
 * Sends automates email subscription emails
 */
public class EmailSendThread extends Thread {

    public String to="";
    public String from="";
    public String subject="";
    public String message="";
    public boolean isHtmlEmail=false;

	/**
     * Constructor used to set the properties needed to run
     */
    public EmailSendThread() {
        //this.something = something;
    }

    /**
     * Constructor used to set the properties needed to run
     */
    public void init() {
        this.setPriority(Thread.MIN_PRIORITY);
        run();
    }

	public void run() {
        try{
            EmailSend.sendMailNoThread(from, to, subject, message, isHtmlEmail);
        } catch (EmailSendException e){
            reger.core.Debug.debug(5, "EmailSendThread.java", e);
        }


//		try{
//
//		    System.out.println("EMAIL TRY: to:"+to+" from:"+from+" subject="+subject);
//
//            Properties props =  System.getProperties();
//            props.put("mail.smtp.host",  reger.systemproperties.AllSystemProperties.getProp("EMAILSERVER"));
//            props.put("mail.transport.protocol", "smtp");
//            Session session = Session.getDefaultInstance(props, null);
//
//            Message msg = new MimeMessage(session);
//
//            InternetAddress addr = new InternetAddress(to);
//            InternetAddress[] addrArray = new InternetAddress[] {addr};
//            msg.addRecipients(Message.RecipientType.TO, new InternetAddress[] {addr});
//            InternetAddress from_addr = new InternetAddress(from);
//            msg.setFrom(from_addr);
//            msg.setSubject(subject);
//
//            if (isHtmlEmail){
//                msg.setContent(message, "text/html");
//            } else {
//                msg.setContent(message, "text/plain");
//            }
//
//            msg.saveChanges();
//
//            //Transport.send(msg);
//            Transport transport = session.getTransport("smtp");
//            transport.connect(reger.systemproperties.AllSystemProperties.getProp("EMAILSERVER"), null, null);
//            transport.sendMessage(msg, addrArray);
//
//
//
//        } catch (javax.mail.SendFailedException nsend){
//            System.out.println("EMAIL FAIL: nsend:"+nsend.getMessage());
//            Debug.debug(5, "EmailSendThread.java", nsend);
//        } catch (javax.mail.internet.AddressException ex){
//            System.out.println("EMAIL FAIL: ex:"+ex.getMessage());
//            Debug.debug(5, "EmailSendThread.java", ex);
//        } catch (Exception e) {
//            System.out.println("EMAIL FAIL: e:"+e.getMessage());
//			Debug.errorsave(e, "EmailSendThread.java");
//        }

    }




}

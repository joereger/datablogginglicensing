package reger.core.scheduler;

import reger.core.Debug;

import javax.mail.Message;
import java.io.*;
import java.net.*;

/**
 * The goal of this class is to talk over the socket, recieve an email and return it as a big long raw string.
 */

class SmtpListenerConnHandler implements Runnable  {

    public static String serverName = reger.systemproperties.AllSystemProperties.getProp("EMAILLISTENERIP");

    Socket sclient;
    SmtpListener parent;
    Thread thread;
    BufferedReader in;
    OutputStreamWriter out;

    public SmtpListenerConnHandler(Socket _sclient, SmtpListener _parent)  {
        parent= _parent;
        sclient = _sclient;
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }


    public void run()  {
        Debug.debug(5, "", "SMTP: connection from " + sclient.getInetAddress().getHostAddress());

        try  {
            InputStream inpStream = sclient.getInputStream();
            out = new OutputStreamWriter(sclient.getOutputStream());
            in = new BufferedReader(new InputStreamReader(inpStream));
            writeln("220 " + serverName + " Service ready");
            String clientcommand;
            while (true)  {
                clientcommand = readln();
                if (clientcommand == null){
                    return;
                }
                if (clientcommand.startsWith("EHLO")){
                    break;
                } else if (clientcommand.startsWith("HELO")){
                    break;
                } else if (clientcommand.equals("QUIT")){
                    return;
                }
            }

            writeln("250 " + serverName);
            while (true)  {
                clientcommand = readln();
                if (clientcommand!=null){
                    if (clientcommand.startsWith("MAIL")){
                        readMailMessage(clientcommand);
                    } else if (clientcommand.equals("QUIT")){
                        break;
                    } else {
                        ack();
                    }
                } else {
                    break;
                }
            }

            writeln("221 " + serverName + " closing connection");
            Debug.debug(5, "", "SMTP: finished session, closing connection.");
            sclient.close();
            
        }   catch(IOException ioe){
            Debug.debug(5, "", ioe);
        }   catch(Exception e)   {
            Debug.errorsave(e, "");
        }

    }

    void readMailMessage(String clientcommand)   {

        String[] allRcptTo = new String[0];

        Debug.debug(5, "", "SMTP: Reading mail message.");

//            String sSenderEmailAddr = findEmailAddr(clientcommand);
//            ack();
//            String sRecptEmailAddr = findEmailAddr(readln());
//            ack();

            ack();
            String s = "";

            while (true)  {
                //reger.core.Util.debug(5, "SMTP: Reading: s=" + reger.core.Util.cleanForHtml(s));
                s = readln();
                if (s!=null && !s.equals("DATA")){

                    if (s.toLowerCase().startsWith("rcpt to:")){
                        String tmp = findEmailAddr(s);
                        if (!tmp.equals("")){
                            allRcptTo = reger.core.Util.addToStringArray(allRcptTo, tmp);
                        }
                    }

                    ack();
                } else {
                    break;
                }
            }

            Debug.debug(5, "", "SMTP: All explicit RCPT TO: in next debug statement.");
            //reger.core.Util.logStringArrayToDb("SMTP: allRcptTo", allRcptTo);

//            if (s==null || !s.equals("DATA"))  {
//                nak();
//                return;
//            }
            writeln("354 Enter mail, end with \".\" on a line by itself");

            //Now we collect the mail content into a stringbuffer
            StringBuffer rawMailMessage = new StringBuffer();

            while (true)  {  // get the message header
                s = readln();
                if (s==null || s.equals("QUIT")){
                    return;
                } else if (s.equals(".")) { // shouldn't actually happen
                    return;
                } else if (s.length()== 0) {
                    //Add to the message... the line ending the header
                    rawMailMessage.append(s + "\n");
                    break; //Get out of this loop and go to the next one
                }
                if (s.startsWith("..")){
                    s= s.substring(1);
                }
                //Add to the message
                rawMailMessage.append(s + "\n");
            }
            while (true)  { // get the message body
                s = readln();
                if (s==null || s.equals("QUIT")){
                    return;
                } else if (s.equals(".")){
                    break;
                }
                //Add to the message
                rawMailMessage.append(s + "\n");
            }
            writeln("250 Message accepted");

            Debug.debug(5, "", reger.core.Util.cleanForHtml(rawMailMessage.toString()));


            //Turn it into a mime message
            javax.mail.internet.MimeMessage mimeMessage = reger.api.EmailApi.turnStringIntoEmail(rawMailMessage.toString());


            try{

                //Clear recipients
                mimeMessage.setRecipients(Message.RecipientType.TO, "");
                mimeMessage.setRecipients(Message.RecipientType.CC, "");
                mimeMessage.setRecipients(Message.RecipientType.BCC, "");

                //Now, parse through allRcptTo, add as BCC and send to emailApi
                for (int i = 0; i < allRcptTo.length; i++) {
                    mimeMessage.addRecipients(Message.RecipientType.BCC, allRcptTo[i]);
                }
               

                //Pass the message on to the emailApi
                if (parent!= null){
                    parent.gotMailMessage(mimeMessage);
                }
            } catch (Exception e) {
                Debug.debug(5, "", e);
            }





    }

    public void addToRawMessage(StringBuffer rawMessage, String in){
        rawMessage.append(in + "\n");
    }

    protected final void writeln(String s)  {
        try{
            Debug.debug(5, "", "SMTP: server sending (" + reger.core.Util.cleanForHtml(s) + ")");
            out.write(s+ "\r\n");
            out.flush();
        }   catch(IOException ioe){
            Debug.debug(5, "", ioe);
        }   catch(Exception e)   {
            Debug.errorsave(e, "");
        }
    }

    protected final String readln() {
        try{
            String s = in.readLine();
            Debug.debug(5, "", "SMTP: client sent    [" + reger.core.Util.cleanForHtml(s) + "]");
            return s;
        }   catch(IOException ioe){
            Debug.debug(5, "", ioe);
        }   catch(Exception e)   {
            Debug.errorsave(e, "");
        }
        return null;
    }

    protected final void ack()  {
        writeln("250 OK");
    }

    protected final void nak()  {
        writeln("451 aborted: local error");
    }

    String findEmailAddr(String s)   {
        if (s!=null){
            int ix= s.indexOf('<');
            if (ix< 0)  return s;
            String emailaddr= s.substring(ix+1);
            ix= emailaddr.indexOf('>');
            if (ix< 0)  return emailaddr;
            return emailaddr.substring(0, ix);
        } else {
            return "";
        }
    }

}

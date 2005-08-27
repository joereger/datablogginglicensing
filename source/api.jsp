<%@ page import="java.io.IOException,
                 reger.core.db.Db,
                 java.awt.*" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="javax.servlet.ServletException" %>
<%@ page import="javax.servlet.http.HttpServlet" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>
<%@ page import="org.apache.xmlrpc.*" %>

<%
//Create our output stream to the browser
OutputStream outStream = response.getOutputStream();

//reger.core.Util.logtodb("Made it to api.log.");

XmlRpc.setDebug(true);

XmlRpcServer xmlrpc = new XmlRpcServer();
xmlrpc.addHandler ("regerlicensingapi", new reger.licensingapi.RegerLicensingApiServer());


//reger.core.Util.logtodb("Created the XmlRpcServer object in api.log.");
//reger.core.Util.logtodb(reger.core.ErrorDissect.ServletUtilsdissect(request));

byte[] result = xmlrpc.execute(request.getInputStream());
response.setContentType ("text/xml");
response.setContentLength (result.length);

//reger.core.Util.logtodb("Have generated result.");
//reger.core.Util.logtodb(result);

outStream.write(result);
outStream.flush();

//Close the output stream
outStream.close();


%>
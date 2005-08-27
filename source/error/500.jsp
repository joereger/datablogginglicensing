<%@ page isErrorPage="true" %>
<%@ page import="reger.core.Util,
                 reger.core.db.Db"%>

<%


try {
    //Try to determine the accountid of this error
    try {
        //Setup a var to hold the accountid
        int accountid=-1;



        //Now try to save with the accountid
        reger.core.Util.errorsave(exception, accountid, request.getRequestURL().toString(), request);
    } catch (Throwable e){
        //Do nothing.  We failed pardner.  Nothing but a basic save now, pardner
        //Save the error to the database
        reger.core.Util.errorsave(exception, request.getRequestURL().toString(), request);
    }


    //Do the redirect
    response.sendRedirect("index.log");
    return;

} catch (Throwable e){
    e.printStackTrace();
}
%>

<html>
<head>
<title>500 Error</title>
</head>
<body bgcolor=#ffcc00>

<strong><font face=arial size=+3>We're sorry.  There has been an error.  The system administrator has been notified.:</font></strong>
<br>

<%
try {
    //out.println(reger.ErrorDissect.dissect(exception, request));
} catch (Exception e) {
    //out.println(reger.ErrorDissect.dissect(exception, request));
}
%>

</body>
</html>


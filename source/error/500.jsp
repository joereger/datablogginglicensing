<%@ page isErrorPage="true" %>
<%@ page import="reger.core.Util,
                 reger.core.db.Db"%>

<%


try {


    //Now try to save
    reger.core.Debug.errorsave(exception, "", 0, "", request);


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

<strong><font face=arial size=+3>We're sorry.  There has been an error.  The system administrator has been notified.</font></strong>
<br>

<%
try {
    out.print(reger.core.ErrorDissect.dissect(exception, request, ""));
} catch (Exception e) {
    //out.println(reger.ErrorDissect.dissect(exception, request));
}
%>

</body>
</html>


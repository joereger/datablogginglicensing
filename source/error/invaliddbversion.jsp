

<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("docheck")){
    if (!reger.core.dbupgrade.RequiredDatabaseVersion.havecorrectversion){
        reger.core.dbupgrade.UpgradeCheckAtStartup upg = new reger.core.dbupgrade.UpgradeCheckAtStartup();
        upg.doCheck();
    }
}
%>

<html>
<head>
<title>Database Version Conflict</title>
</head>
<body bgcolor=#ffcc00>
<center>
<br><br>
<img src='/images/error-tri-large.gif' width=385 height=350>
<br>
<blockquote><blockquote>
<strong><blink><font face=arial size=+3 color=#ffffff style="font-size: 76px;">Please wait.  The database schema is being updated.</font></blink></strong>
<br><br>
<blockquote><blockquote>
<strong><font face=arial size=-1 color=#ffffff style="font-size: 15px;">We're sorry.  The page you are requesting can not be served right now.  Please try again in the next few moments.</font></strong>
<br><br>
<strong><font face=arial size=-1 color=#ffffff style="font-size: 15px;">System Administrators: The required version is: <%=reger.core.dbupgrade.RequiredDatabaseVersion.version%> The installed version is: <%=reger.core.dbupgrade.RequiredDatabaseVersion.currentversion%>.  The max possible version is: <%=reger.core.dbupgrade.RequiredDatabaseVersion.maxversion%></font></strong>
<%
if (!reger.core.dbupgrade.RequiredDatabaseVersion.error.equals("")){
%>
<br><br>
<strong><font face=arial size=-1 color=#ffffff style="font-size: 15px;">There has been an error and the upgrade process has halted.  You will need to correct the problem and restart.  Or click <a href='invaliddbversion.jsp?action=docheck'>here</a> to retry the database setup.  The reported error is:</font></strong>
<br>
<%=reger.core.dbupgrade.RequiredDatabaseVersion.error%>
<br>
<%
} else {
%>
<br><br>
<strong><font face=arial size=-1 color=#ffffff style="font-size: 15px;">There is no reported error at this time.  The system is likely upgrading the database.</font></strong>
<br>
<%
}
%>
</blockquote></blockquote>
</blockquote></blockquote>
</center>
</body>
</html>


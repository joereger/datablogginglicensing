<%




//Call Header to kick everything off
reger.pageFramework.GlobalHeader.get(request, response, out, pathToAppRoot);

//If the response has been committed, return, there's nothing else that can be done
if (response.isCommitted()){
    reger.core.Debug.debug(3, "globalheader.jsp", "If/then on isCommitted in globalheader.jsp - response.isCommitted()=" + response.isCommitted());
    return;
}
%>
package reger.pageFramework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Footer
 */
public class GlobalFooter {

    public static void get(HttpServletRequest request, HttpServletResponse response, javax.servlet.jsp.JspWriter out, StringBuffer mb){
        try{
            StringBuffer output = new StringBuffer();

            //Login check
            if (!String.valueOf(request.getSession().getAttribute("isloggedin")).equals("1")){
                response.sendRedirect("login.log");
                return;
            }

            //Header Start

            output.append("<font face=arial style=\"font-size: 72px;\" color=#00ff00><b>Licensing</b></font><br>");
            output.append("<font face=arial style=\"font-size: 10px;\" color=#00ff00>");
            output.append("<a href='index.log'>Home</a> | ");
            output.append("<a href='find.log'>Find</a> | ");
            output.append("<a href='createlicense.log'>Create New</a> | ");
            output.append("<a href='events.log'>Event Log</a> | ");
            output.append("<a href='licensereader.log'>Reader</a> | ");
            output.append("<a href='transactions.log'>Transactions</a> | ");
            output.append("<a href='logout.log'>Log Out</a>");
            output.append("</font><br><br><br>");

            //Header End

            //Add the center of the page
            output.append(mb);

            //Footer Start

            //Footer End



            //Output the page
            out.print(output.toString());


        } catch (Exception ex){
            reger.core.Util.errorsave(ex, "Error in GlobalFooter.java");
        }
    }

}

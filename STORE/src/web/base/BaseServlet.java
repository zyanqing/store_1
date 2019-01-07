package web.base;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

       String method = req.getParameter("method");

       if (method == null || method.equals("") || method.trim().equals("")){
           method = "execute";
       }

        Class aClass = this.getClass();

        try {

            Method md = aClass.getMethod(method, HttpServletRequest.class, HttpServletResponse.class);
           String jspPath = (String)md.invoke(this,req,resp);

           if (jspPath != null){
               req.getRequestDispatcher(jspPath).forward(req,resp);
           }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception{
        return null;
    }

}

package com.glamourglow.glamourglow.dispatcher;


import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.rmi.ServerException;
import java.util.logging.Level;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.glamourglow.glamourglow.services.logservice.LogService;

@WebServlet(name = "Dispatcher", urlPatterns = {"/Dispatcher"})
public class Dispatcher extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            String controllerAction=request.getParameter("controllerAction");

            if (controllerAction==null) controllerAction="HomeManagement.viewhome";


            String[] splittedAction=controllerAction.split("\\.");
            Class<?> controllerClass=Class.forName("com.glamourglow.glamourglow.controller."+splittedAction[0]);
            Method controllerMethod=controllerClass.getMethod(splittedAction[1],HttpServletRequest.class,HttpServletResponse.class);
            LogService.getApplicationLogger().log(Level.INFO,splittedAction[0]+" "+splittedAction[1]);
            controllerMethod.invoke(null,request,response);

            String viewUrl=(String)request.getAttribute("viewUrl");

            RequestDispatcher view=request.getRequestDispatcher("jsp/"+viewUrl+".jsp");
            view.forward(request,response);


        } catch (Exception e) {
            e.printStackTrace(out);
            throw new ServerException("Dispatcher Servlet Error",e);

        } finally {
            out.close();
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}

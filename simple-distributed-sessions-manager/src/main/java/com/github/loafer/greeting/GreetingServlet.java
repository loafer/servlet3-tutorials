package com.github.loafer.greeting;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhaojh on 14-10-22.
 */
public class GreetingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        if(null != name){
            request.getSession().setAttribute("name", name);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/views/greeting.jsp");
        dispatcher.forward(request, response);
    }
}

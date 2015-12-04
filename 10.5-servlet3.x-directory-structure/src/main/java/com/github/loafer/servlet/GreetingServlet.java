package com.github.loafer.servlet;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;

/**
 * @author zhaojh
 */
public class GreetingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        URL root = Thread.currentThread().getContextClassLoader().getResource("/");
        System.out.println("GreetingServlet ====> " + root.toString());

        HttpSession session = request.getSession();
        session.setAttribute("hello", "balabala~~~");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/greeting.jsp");
        dispatcher.forward(request, response);
    }
}

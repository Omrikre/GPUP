package servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Engine.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        try (PrintWriter out = resp.getWriter()) {
            String userName = req.getParameter("username");
            String role = req.getParameter("role");
            String threads = req.getParameter("threadSize");
            shouldAddUser(userName);
            userManager.addUser(userName, Integer.parseInt(threads));
            out.println("Guest [" + userName + ", " + role + ", " + threads + "] was added successfully to the guest list");
            System.out.println("Guest [" + userName + ", " + role + ", " + threads + "] was added successfully to the guest list");
        }
    }

    private void shouldAddUser(String userName) throws ServletException {
        if (userName == null || userName.isEmpty())
            throw new ServletException("username doesn't exist!");
        synchronized (this) {
            if (ServletUtils.getUserManager(getServletContext()).isUserExists(userName))
                throw new ServletException("user already logged in!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}

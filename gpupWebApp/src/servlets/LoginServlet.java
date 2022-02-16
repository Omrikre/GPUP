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
        //     UserManager userManager = ServletUtils.getUserManager(getServletContext());
        try (PrintWriter out = resp.getWriter()) {
            String userName = req.getParameter("username");
            String role = req.getParameter("role");
            String threads = req.getParameter("threadSize");
            out.println("Guest [" + userName + ", " + role + ", " + threads + "] was added successfully to the guest list");
            System.out.println("1");
            out.println("1");
            if (!shouldAddUser(userName, out)) {
                out.println("2");
                System.out.println("2");
                ServletUtils.getUserManager(getServletContext()).addUser(userName, Integer.parseInt(threads));
                out.println("3");
                System.out.println("3");
                System.out.println("Guest [" + userName + ", " + role + ", " + threads + "] was added successfully to the guest list");
            } else out.println("FINISHED");
        }
    }

    private boolean shouldAddUser(String userName, PrintWriter out) throws ServletException {
        if (userName == null || userName.isEmpty()) {
            out.println("username doesn't exist!");
            return false;
        }
        synchronized (this) {
            if (ServletUtils.getUserManager(getServletContext()).isUserExists(userName)) {
                out.println("user already logged in!");
                return false;
            }
        }
        return true;
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

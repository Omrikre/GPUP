package servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            if (shouldAddUser(userName, role, threads, out, resp)) {
                resp.setStatus(200);
                ServletUtils.getUserManager(getServletContext()).addUser(userName, Integer.parseInt(threads), role);
                out.write("User [" + userName + ", " + role + ", " + threads + "] was logged in successfully");
            }
        }
    }

    private boolean shouldAddUser(String userName, String role, String threadSize, PrintWriter out, HttpServletResponse resp) {
        if (userName == null || userName.isEmpty()) {
            out.write("username doesn't exist!");
            resp.setStatus(404);
            return false;
        }
        if (role == null || role.isEmpty()) {
            out.write("role doesn't exist!");
            resp.setStatus(404);
            return false;
        }
        if (threadSize == null || threadSize.isEmpty()) {
            out.write("threadSize doesn't exist!");
            resp.setStatus(404);
            return false;
        }
        synchronized (this) {
            if (ServletUtils.getUserManager(getServletContext()).isUserExists(userName)) {
                out.write("user already logged in!");
                resp.setStatus(406);
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

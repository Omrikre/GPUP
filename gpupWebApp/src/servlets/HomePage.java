package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/gpupWebApp_Web_exploded")
public class HomePage extends HttpServlet {
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out=resp.getWriter();
        out.println(" __          __  _                            _           _____   _____  _    _  _____         ____  _ \n" +
                " \\ \\        / / | |                          | |         / ____| |  __ \\| |  | ||  __ \\       |___ \\| |\n" +
                "  \\ \\  /\\  / /__| | ___ ___  _ __ ___   ___  | |_ ___   | |  __  | |__) | |  | || |__) | __   ____) | |\n" +
                "   \\ \\/  \\/ / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\  | | |_ | |  ___/| |  | ||  ___/  \\ \\ / /__ <| |\n" +
                "    \\  /\\  /  __/ | (_| (_) | | | | | |  __/ | || (_) | | |__| |_| |_   | |__| || |_      \\ V /___) |_|\n" +
                "     \\/  \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/   \\_____(_)_(_)   \\____(_)_(_)      \\_/|____/(_)\n" +
                "                                                                                                       \n" +
                "                                                                                                       ");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }
}

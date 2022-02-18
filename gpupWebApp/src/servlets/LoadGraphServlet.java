package servlets;

import Engine.DTO.GraphDTO;
import Engine.Engine;
import Engine.GraphManager;
import Engine.Graph;
import Exceptions.FileException;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/select/graph")
public class LoadGraphServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, FileException, JAXBException {
        resp.setContentType("object/graph");
        try (PrintWriter out = resp.getWriter()) {
            String filepath = req.getParameter("filepath");
            String userName = req.getParameter("username");
            filepath=filepath.replace('(','\\');
            if (shouldAddGraph(filepath, userName, out, resp)) {
                Engine engine = new Engine();
                engine.loadFile(filepath, userName);
                Graph graph = engine.getG();
                resp.setStatus(200);
                ServletUtils.getGraphManager(getServletContext()).addGraph(graph);
                out.write("Graph [" + graph.getGraphName() + "]" + " was added successfully");
            }
        }

    }

    private boolean shouldAddGraph(String filename, String username, PrintWriter out, HttpServletResponse resp) {
        if (filename == null || filename.isEmpty()) {
            out.write("file name doesn't exist!");
            resp.setStatus(404);
            return false;
        }
        if (username == null || username.isEmpty()) {
            out.write("user name doesn't exist!");
            resp.setStatus(404);
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            processRequest(req, resp);
        } catch (FileException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            processRequest(req, resp);
        } catch (FileException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}

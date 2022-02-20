package servlets;

import Engine.DTO.GraphDTO;
import Engine.Engine;
import Engine.GraphManager;
import Engine.Graph;
import Exceptions.FileException;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet("/select/graph")
public class LoadGraphServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, FileException, JAXBException {
        resp.setContentType("text/plain");
        String usernameFromSession = SessionUtils.getUsername(req);
        Collection<Part> parts = req.getParts();
        PrintWriter out=resp.getWriter();
        if (parts != null)
            for (Part part : parts) {
                try {
                    Engine engine = new Engine();
                    engine.loadFileFromServlet(part.getInputStream(), usernameFromSession);
                    Graph graph = engine.getG();
                    resp.setStatus(200);
                    ServletUtils.getGraphManager(getServletContext()).addGraph(graph);
                    out.write("Graph [" + graph.getGraphName() + "]" + " was added successfully");
                }
                catch (FileException | JAXBException e) {
                    resp.setStatus(500);
                    out.write("The XML file is not valid! error: " + e.getMessage());
                }
            }


        // resp.setContentType("object/graph");
//        try (PrintWriter out = resp.getWriter()) {
//            String filepath = req.getParameter("filepath");
//            String userName = req.getParameter("username");
//            filepath=filepath.replace('(','\\');
//            if (shouldAddGraph(filepath, userName, out, resp)) {
//                Engine engine = new Engine();
//                engine.loadFile(filepath, userName);
//                Graph graph = engine.getG();
//                resp.setStatus(200);
//                ServletUtils.getGraphManager(getServletContext()).addGraph(graph);
//                out.write("Graph [" + graph.getGraphName() + "]" + " was added successfully");
//            }
//        }

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
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            processRequest(req, resp);
        }
    }
}



/*
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet("/file/xml/load")
public class LoadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        /// request before login
        if (usernameFromSession == null || usernameFromSession.isEmpty() || userManager.getUserRole(usernameFromSession) == null){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("please login before upload file");
        }
        /// worker request load graph but only admin can load graph)
        else if(!userManager.getUserRole(usernameFromSession).equals("Admin")){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("only admin can load a file");
        }
        else {
            GraphManger graphManager = ServletUtils.getGraphManager(getServletContext());
            PrintWriter out = response.getWriter();
            Collection<Part> parts = request.getParts();
            if (parts != null)
            for (Part part : parts) {
                if (checkFileExtension(part.getSubmittedFileName())) {
                    try {
                        Xmlimpl xmlFile = new Xmlimpl(part.getInputStream());
                        graphManager.addGraph(new Graph(xmlFile, usernameFromSession));

                        response.setStatus(HttpServletResponse.SC_OK);
                        out.write("Add new graph successfully: \n" +
                                "creator: " + usernameFromSession + "\n" +
                                "graph name: " + xmlFile.getGraphName() + "\n");
                        out.flush();

                    } catch (GraphIsExists e) {
                        out.write(e.toString());
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    } catch (IllegalArgumentException e) {
                        out.write(e.toString());
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    } catch (Exception e) {
                        out.write(e.toString());
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                } else {
                    /// not xml file
                    out.write("please choose xml file");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
            else{
                out.write("please insert file");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    public static boolean checkFileExtension(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null!");
        }
        String extension = "";
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }

        return extension.equals("xml");
    }

 */

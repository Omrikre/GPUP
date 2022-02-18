package servlets;

import Engine.DTO.GraphDTO;
import Engine.GraphManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/graphlist")
public class GraphServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("graphname");
        String isCycle = req.getParameter("cycle");
        String targetA = req.getParameter("target-a");
        String targetB = req.getParameter("target-b");
        String bond = req.getParameter("bond");
        if (name == null) { //no graph specified, returns the entire list
            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                if (!(isCycle != null || targetA != null || targetB != null || bond != null)) {
                    Gson gson = new Gson();
                    GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
                    Map<String, GraphDTO> graphMap = graphManager.getGraphsAsDTOs();
                    String json = gson.toJson(graphMap);
                    out.println(json);
                    out.flush();
                } else {
                    out.write("graph name doesn't exist!");
                    resp.setStatus(404);
                }
            }
        } else { //graph has a name
            if (isCycle == null) {
                try (PrintWriter out = resp.getWriter()) {
                    if (targetA != null && targetB != null && bond != null) { //path
                        resp.setContentType("application/json");
                        Gson gson = new Gson();
                        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
                        String json = gson.toJson(graphManager.getPathBetweenTwoTargets(name, targetA, targetB, bond));
                        out.println(json);
                        out.flush();
                    } else {
                        if (targetA == null&&targetB!=null) {
                            out.write("target A doesn't exist!");
                            resp.setStatus(404);
                        } else if (targetB == null&&targetA!=null) {
                            out.write("target B doesn't exist!");
                            resp.setStatus(404);
                        } else if(bond==null&&(targetA!=null||targetB!=null||(targetA==null&&targetB==null))){
                            out.write("bond doesn't exist!");
                            resp.setStatus(404);
                        }
                        else if(targetA==null&&targetB==null&&bond==null){ //specific graph
                            try (PrintWriter out = resp.getWriter()) {
                                Gson gson = new Gson();
                                GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
                                resp.setContentType("application/json");
                                GraphDTO graphDTO = graphManager.getGraphDTOByName(name);
                                String json = gson.toJson(graphDTO);
                                out.println(json);
                                out.flush();
                            }
                        }
                    }
                }
            } else { //cycle
                try (PrintWriter out = resp.getWriter()) {
                    if (((targetA != null && targetB == null) || (targetA == null && targetB != null)) && (bond == null)) {
                        resp.setContentType("application/json");
                        Gson gson = new Gson();
                        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
                        String json;
                        if (targetA == null)
                            json = gson.toJson(graphManager.getCycle(name, targetB));
                        else json = gson.toJson(graphManager.getCycle(name, targetA));
                        out.println(json);
                        out.flush();
                    } else {
                        if (targetA == null && targetB == null) {
                            out.write("target doesn't exist!");
                            resp.setStatus(404);
                        }
                        if (bond != null) {
                            out.write("bond shouldn't exist!");
                            resp.setStatus(406);
                        }
                    }
                }
            }
        }

    }
}

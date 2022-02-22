package servlets;

import Engine.DTO.GraphDTO;
import Engine.DTO.GraphDTOWithoutCB;
import Engine.DTO.TargetDTO;
import Engine.DTO.TargetDTOWithoutCB;
import Engine.Enums.Bond;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/graphlist")
public class GraphServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("graphname");
        String isCycle = req.getParameter("cycle");
        String targetA = req.getParameter("target-a");
        String targetB = req.getParameter("target-b");
        String targets = req.getParameter("targets");
        String bond = req.getParameter("bond");
        if (name == null) { //no graph specified, returns the entire list
            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                if (!(isCycle != null || targetA != null || targetB != null || bond != null || targets != null)) {
                    Gson gson = new Gson();
                    GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
                    List<GraphDTOWithoutCB> graphList = graphManager.getGraphsAsDTOs();
                    String json = gson.toJson(graphList);
                    resp.setStatus(200);
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
                        Bond b = null;
                        if (bond.equals("req"))
                            b = Bond.REQUIRED_FOR;
                        else if (bond.equals("dep"))
                            b = Bond.DEPENDS_ON;
                        String json = gson.toJson(graphManager.getPathBetweenTwoTargets(name, targetA, targetB, b));
                        resp.setStatus(200);
                        out.println(json);
                        out.flush();
                    } else {
                        if (targetA != null && targetB != null && bond == null) {
                            out.write("bond doesn't exist!");
                            resp.setStatus(404);
                        } else if (targetA == null && targetB != null && bond != null) { //what if
                            Gson gson = new Gson();
                            GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
                            resp.setContentType("application/json");
                            Bond b;
                            if (bond.equals("req"))
                                b = Bond.REQUIRED_FOR;
                            else b = Bond.DEPENDS_ON;
                            Set<String> whatif = graphManager.getWhatIf(name, targetB, b);
                            String json = gson.toJson(whatif);
                            resp.setStatus(200);
                            out.println(json);
                            out.flush();
                        } else if (targetA != null && targetB == null && bond != null) {
                            Gson gson = new Gson();
                            GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
                            resp.setContentType("application/json");
                            Bond b;
                            if (bond.equals("req"))
                                b = Bond.REQUIRED_FOR;
                            else b = Bond.DEPENDS_ON;
                            Set<String> whatif = graphManager.getWhatIf(name, targetA, b);
                            String json = gson.toJson(whatif);
                            resp.setStatus(200);
                            out.println(json);
                            out.flush();
                        } else if (targetA == null && targetB != null && bond == null) { //single target
                            Gson gson = new Gson();
                            GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
                            resp.setContentType("application/json");
                            TargetDTOWithoutCB targetDTO = graphManager.getTargetDTO(name, targetB);
                            String json = gson.toJson(targetDTO);
                            resp.setStatus(200);
                            out.println(json);
                            out.flush();
                        } else if (targetA != null && targetB == null && bond == null) {
                            Gson gson = new Gson();
                            GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
                            resp.setContentType("application/json");
                            TargetDTOWithoutCB targetDTO = graphManager.getTargetDTO(name, targetA);
                            String json = gson.toJson(targetDTO);
                            resp.setStatus(200);
                            out.println(json);
                            out.flush();
                        } else if (targetA == null && targetB == null && bond == null) { //specific graph
                            Gson gson = new Gson();
                            GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
                            resp.setContentType("application/json");
                            if (targets != null) {
                                String json = gson.toJson(graphManager.getTargetDTOList(name));
                                out.println(json);
                            } else {
                                GraphDTOWithoutCB graphDTO = graphManager.getGraphDTOByName(name);
                                String json = gson.toJson(graphDTO);
                                out.println(json);
                            }
                            out.flush();
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
                        resp.setStatus(200);
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}

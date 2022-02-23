package servlets;

import Engine.DTO.*;
import Engine.Engine;
import Engine.Enums.State;
import Engine.Graph;
import Engine.Enums.Location;
import Engine.Enums.MissionState;
import Engine.GraphManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/mission/add")
public class LoadTaskServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("object/task");
        try (PrintWriter out = resp.getWriter()) {
            String aot = req.getParameter("amount-of-targets");
            Integer amountOfTargets;
            if (aot != null)
                amountOfTargets = Integer.parseInt(aot);
            else amountOfTargets = 0;
            String src = req.getParameter("src"); //source folder
            String compilationFolder = req.getParameter("compilation-folder");
            String r = req.getParameter("runtime");
            Integer runtime;
            if (r != null)
                runtime = Integer.parseInt(r);
            else runtime = 0;
            Boolean randomRunTime;
            String temp = req.getParameter("random-runtime");
            if (temp != null) {
                if (temp.equals(String.valueOf(true)))
                    randomRunTime = true;
                else randomRunTime = false;
            } else randomRunTime = false;
            String ss = req.getParameter("success");
            Integer success;
            if (ss != null)
                success = Integer.parseInt(ss);
            else success = 0;
            String sw = req.getParameter("success-warnings");
            Integer successWithWarnings;
            if (sw != null)
                successWithWarnings = Integer.parseInt(sw);
            else successWithWarnings = 0;
            String missionName = req.getParameter("name");
            //String creatorName = req.getParameter("creator");
            String creatorName = SessionUtils.getUsername(req);
            String graphName = req.getParameter("graph-name");
            Boolean fromScratch = Boolean.valueOf(req.getParameter("from-scratch"));
            Boolean incremental = Boolean.valueOf(req.getParameter("incremental"));
            String newName = req.getParameter("new-name");
            GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());

            Integer price = null;
            if (graphName != null) {
                GraphDTOWithoutCB graph = graphManager.getGraphDTOByName(graphName);

                if (compilationFolder == null)
                    price = amountOfTargets * graph.getSimPricePerTarget();
                else
                    price = amountOfTargets * graph.getCompPricePerTarget();
            }
            if (fromScratch) {
                Map<String, TargetDTOWithoutCB> targetDTOMap = new HashMap<>();
                List<String> targets = ServletUtils.getTaskManager(getServletContext()).getMissionByName(missionName).getTargets();
                for (String s : targets) {
                    TargetDTOWithoutCB t = ServletUtils.getGraphManager(getServletContext()).getTargetDTO(ServletUtils.getTaskManager(getServletContext()).getMissionByName(missionName).getGraphName(), s);
                    t.setTargetState(State.FROZEN);
                    targetDTOMap.put(s, t);
                }
                ServletUtils.getTaskManager(getServletContext()).addTaskFromScratch(missionName, newName, creatorName, targetDTOMap);
            } else if (incremental) {
                ServletUtils.getTaskManager(getServletContext()).addTaskIncremental(missionName, newName, creatorName);
            } else {
                Gson gson = new Gson();
                String[] targets = gson.fromJson(req.getParameter("targets-array"), String[].class);
                List<String> targestList = Arrays.asList(targets);
                Graph miniGraph = graphManager.getGraphOfRunnableTargetsFromArrayAndGraph(graphName, targestList);
                Map<Location, Integer> locations = miniGraph.howManyTargetsInEachLocation();
                MissionDTOWithoutCB task = new MissionDTOWithoutCB(amountOfTargets, targestList, src, compilationFolder, runtime, randomRunTime, success, successWithWarnings, missionName, MissionState.READY.toString(), 0, 0,
                        price, creatorName, graphName, 0, 0, locations.get(Location.INDEPENDENT), locations.get(Location.LEAF), locations.get(Location.MIDDLE), locations.get(Location.ROOT));
                Map<String, TargetDTOWithoutCB> targetDTOMap = new HashMap<>();
                for (Graph.Target s : miniGraph.getTargets().values()) {
                    TargetDTOWithoutCB t = new TargetDTOWithoutCB(miniGraph.getSimulationPrice(), miniGraph.getCompilationPrice(),s);
                    t.setTargetState(State.FROZEN);
                    System.out.println("LOADED CRED: " + t.getSimCreds());
                    targetDTOMap.put(s.getName(), t);
                }
                ServletUtils.getTaskManager(getServletContext()).addTask(task, targetDTOMap);
            }
            resp.setStatus(200);
            out.write("Task " + ServletUtils.getTaskManager(getServletContext()).getMissionByName(missionName).getMissionName() + " was added successfully.");
            System.out.println("Task " + ServletUtils.getTaskManager(getServletContext()).getMissionByName(missionName).getMissionName() + " was added successfully.");

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}

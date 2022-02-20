package servlets;

import Engine.DTO.GraphDTO;
import Engine.DTO.GraphDTOWithoutCB;
import Engine.DTO.MissionDTO;
import Engine.DTO.MissionDTOWithoutCB;
import Engine.Engine;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet("/mission/add")
public class LoadTaskServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("object/task");
        try (PrintWriter out = resp.getWriter()) {
            Integer amountOfTargets = Integer.parseInt(req.getParameter("amount-of-targets"));
            String src=req.getParameter("src"); //source folder
            String compilationFolder = req.getParameter("compilation-folder");
            Integer runtime = Integer.parseInt(req.getParameter("runtime"));
            Boolean randomRunTime;
            String temp = req.getParameter("random-runtime");
            if (temp.equals(String.valueOf(true)))
                randomRunTime = true;
            else randomRunTime = false;
            Integer success = Integer.parseInt(req.getParameter("success"));
            Integer successWithWarnings = Integer.parseInt(req.getParameter("success-warnings"));
            String missionName = req.getParameter("name");
            String creatorName = req.getParameter("creator");
            String graphName = req.getParameter("graph-name");
            Integer waitingTargets = Integer.parseInt(req.getParameter("waiting-targets"));
            GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
            GraphDTOWithoutCB graph = graphManager.getGraphDTOByName(graphName);
            Integer price;
            if (compilationFolder == null)
                price = waitingTargets * graph.getSimPricePerTarget();
            else
                price = waitingTargets * graph.getCompPricePerTarget();
            Gson gson=new Gson();
            String[] targets=gson.fromJson(req.getParameter("targets-array"), String[].class);
            List<String> targestList= Arrays.asList(targets);
            Map<Location,Integer> locations=graphManager.getGraphOfRunnableTargetsFromArrayAndGraph(graphName,targestList).howManyTargetsInEachLocation();
            MissionDTOWithoutCB task = new MissionDTOWithoutCB(amountOfTargets, src, compilationFolder, runtime, randomRunTime, success, successWithWarnings, missionName, MissionState.READY.toString(), 0, 0,
                    price, creatorName, graphName, 0, 0, locations.get(Location.INDEPENDENT),locations.get(Location.LEAF),locations.get(Location.MIDDLE),locations.get(Location.ROOT));
            ServletUtils.getTaskManager(getServletContext()).addTask(task);
            resp.setStatus(200);
            out.write("Task " + task.getMissionName() + " was added successfully.");
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

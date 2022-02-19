package servlets;

import Engine.DTO.GraphDTO;
import Engine.DTO.MissionDTO;
import Engine.Enums.MissionState;
import Engine.GraphManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/mission/add")
public class LoadTaskServlet extends HttpServlet {
    @Override
    protected void procecssRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("object/task");
        try (PrintWriter out = resp.getWriter()) {
            Integer amountOfTargets = Integer.parseInt(req.getParameter("amount-of-targets"));
            String compilationFolder = req.getParameter("compilation-folder");
            Integer runtime = Integer.parseInt(req.getParameter("runtime"));
            Boolean randomRunTime;
            String temp = req.getParameter("random-runtime");
            if (temp.equals("true"))
                randomRunTime = true;
            else randomRunTime = false;
            Integer success = Integer.parseInt(req.getParameter("success"));
            Integer successWithWarnings = Integer.parseInt(req.getParameter("success-warnings"));
            String missionName = req.getParameter("name");
            String creatorName = req.getParameter("creator");
            String graphName = req.getParameter("graph-name");
            Integer waitingTargets = Integer.parseInt(req.getParameter("waiting-targets"));
            GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
            GraphDTO graph = graphManager.getGraphDTOByName(graphName);
            Integer price;
            if (compilationFolder == null)
                price = waitingTargets * graph.getPricePerTarget().get("Simulation";
            else price = waitingTargets * graph.getPricePerTarget().get("Compilation");
            MissionDTO task = new MissionDTO(amountOfTargets, compilationFolder, runtime, randomRunTime, success, successWithWarnings, missionName, MissionState.READY, 0, 0,
                    price, creatorName, graphName, graph, 0, waitingTargets, null);
            ServletUtils.getTaskManager(getServletContext()).addTask(task);
            resp.setStatus(200);
            out.write("Task " + task.getMissionName() + " was added successfully.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        procecssRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        procecssRequest(req, resp);
    }
}

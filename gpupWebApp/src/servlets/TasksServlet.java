package servlets;

import Engine.DTO.MissionDTO;
import Engine.DTO.MissionDTOWithoutCB;
import Engine.DTO.TargetDTOWithoutCB;
import Engine.Enums.MissionState;
import Engine.users.UserManager;
import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
import java.util.List;
import java.util.Map;

@WebServlet("/missionlist")
public class TasksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");
        String name = req.getParameter("name");
        String targets = req.getParameter("targets");
        String add = req.getParameter("add-worker");
        String sign = req.getParameter("sign-worker");
        String get = req.getParameter("get");
        String remove = req.getParameter("remove");
        String upload = req.getParameter("upload");
        Boolean isUpload = Boolean.valueOf(upload);
        String target = req.getParameter("json");
        Boolean isRemove = Boolean.valueOf(remove);
        Boolean isGet = Boolean.valueOf(get);
        Boolean shouldSign = Boolean.valueOf(sign);
        String wName = SessionUtils.getUsername(req);
        Boolean shouldAdd = Boolean.valueOf(add);
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            if (isUpload) {
                Gson gson = new Gson();
                TargetDTOWithoutCB t = gson.fromJson(target, TargetDTOWithoutCB.class);
                ServletUtils.getTaskManager(getServletContext()).updateTarget(name, t);
                resp.setStatus(200);
            } else {
                if (isRemove) {
                    ServletUtils.getTaskManager(getServletContext()).removeWorkerFromAllTasks(wName);
                } else {
                    if (isGet) {
                        TargetDTOWithoutCB t = ServletUtils.getTaskManager(getServletContext()).getAvailableTargetForWorker(wName);
                        Gson gson = new Gson();
                        String json = gson.toJson(t);
                        out.println(json);
                        resp.setStatus(200);
                    }
                    if (add != null) { //signup
                        if (shouldSign) {
                            ServletUtils.getTaskManager(getServletContext()).signWorkerUpForTask(wName, name);
                            resp.setStatus(200);
                        } else {
                            String stat = ServletUtils.getTaskManager(getServletContext()).getMissionByName(name).getStatus();
                            if (stat.equals(MissionState.READY.toString()) || !shouldAdd) {
                                ServletUtils.getTaskManager(getServletContext()).changeWorker(wName, name, shouldAdd);
                                resp.setStatus(200);
                            } else {
                                if (shouldAdd && stat.equals(MissionState.FINISHED.toString())) {
                                    out.println("Mission is finished!");
                                    resp.setStatus(500);
                                } else if (shouldAdd && stat.equals(MissionState.PAUSED.toString())) {
                                    out.println("Mission is paused!");
                                    resp.setStatus(500);
                                } else if (shouldAdd && stat.equals(MissionState.STOPPED.toString())) {
                                    out.println("Mission is stopped!");
                                    resp.setStatus(500);
                                }
                            }
                        }
                    } else { //not signup
                        if (status == null) {
                            Gson gson = new Gson();
                            if (name == null) { //all missions
                                List<MissionDTOWithoutCB> lst = ServletUtils.getTaskManager(getServletContext()).getTaskDTOList();
                                String json = gson.toJson(lst);
                                System.out.println("MISSIONS: " + json);
                                out.println(json);
                                resp.setStatus(200);
                            } else {
                                if (targets == null) { //get only mission
                                    MissionDTOWithoutCB m = ServletUtils.getTaskManager(getServletContext()).getMissionByName(name);
                                    String json = gson.toJson(m);
                                    System.out.println("MISSION: " + json);
                                    out.println(json);
                                    resp.setStatus(200);
                                } else { //get mission targets
                                    List<TargetDTOWithoutCB> lst = ServletUtils.getTaskManager(getServletContext()).getTargets(name);
                                    String json = gson.toJson(lst);
                                    System.out.println("targets: " + json);
                                    out.println(json);
                                    resp.setStatus(200);
                                }
                            }
                            out.flush();
                        } else {
                            // no content type
                            if (name == null) {
                                out.println("No name given!");
                                resp.setStatus(404);
                            } else { //set status
                                ServletUtils.getTaskManager(getServletContext()).setMissionStatus(name, status);
                                resp.setStatus(200);
                            }
                        }
                    }
                }
            }
        }
    }
}

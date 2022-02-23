package servlets;

import Engine.DTO.MissionDTO;
import Engine.DTO.MissionDTOWithoutCB;
import Engine.DTO.TargetDTOWithoutCB;
import Engine.DTO.TargetForWorkerDTO;
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
import java.util.Set;

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
        String workerMission = req.getParameter("worker-mission");
        String workerName = req.getParameter("worker-name");
        String remove = req.getParameter("remove");
        String upload = req.getParameter("upload");
        String getSpecialTarget = req.getParameter("special");
        String json = req.getParameter("json");
        Boolean isUpload;
        if (upload != null)
            isUpload = Boolean.valueOf(upload);
        else isUpload = false;
        String target = req.getParameter("json");
        Boolean isRemove;
        if (remove != null)
            isRemove = Boolean.valueOf(remove);
        else isRemove = false;
        Boolean isGet;
        if (get != null)
            isGet = Boolean.valueOf(get);
        else isGet = false;
        Boolean shouldSign;
        if (sign != null)
            shouldSign = Boolean.valueOf(sign);
        else shouldSign = false;
        String wName = SessionUtils.getUsername(req);
        if (wName == null)
            wName = workerName;
        Boolean shouldAdd;
        if (add != null)
            shouldAdd = Boolean.valueOf(add);
        else shouldAdd = false;
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            if (getSpecialTarget != null) {
                Gson gson = new Gson();
                Set<String> s = gson.fromJson(json, Set.class);
                TargetForWorkerDTO t = ServletUtils.getTaskManager(getServletContext()).getTargetFromSetOfMissions(s);
                String j = gson.toJson(t);
                out.println(j);
                resp.setStatus(200);
            } else {
                if (workerMission != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(ServletUtils.getTaskManager(getServletContext()).getMissionForWorker(workerName, name));
                    out.println(json);
                    resp.setStatus(200);
                } else {
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
                            } else {
                                if (add != null) { //signup
                                    if (shouldSign) {
                                        ServletUtils.getTaskManager(getServletContext()).signWorkerUpForTask(wName, name);
                                        if (ServletUtils.getTaskManager(getServletContext()).getMissionByName(name).getCompilationFolder() == null)
                                            out.println("sim");
                                        else out.println("comp");
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
                                            //System.out.println("MISSIONS: " + json);
                                            out.println(json);
                                            resp.setStatus(200);
                                        } else {
                                            if (targets == null) { //get only mission
                                                MissionDTOWithoutCB m = ServletUtils.getTaskManager(getServletContext()).getMissionByName(name);
                                                String json = gson.toJson(m);
                                                out.write(json);
                                                resp.setStatus(200);
                                            } else { //get mission targets
                                                List<TargetDTOWithoutCB> lst = ServletUtils.getTaskManager(getServletContext()).getTargets(name);
                                                String json = gson.toJson(lst);
                                                System.out.println("targets: " + json);
                                                out.println(json);
                                                resp.setStatus(200);
                                            }
                                        }
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
        }
    }
}

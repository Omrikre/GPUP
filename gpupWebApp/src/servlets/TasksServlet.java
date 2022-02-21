package servlets;

import Engine.DTO.MissionDTO;
import Engine.DTO.MissionDTOWithoutCB;
import Engine.Enums.MissionState;
import Engine.users.UserManager;
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

@WebServlet("/missionlist")
public class TasksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");
        String name = req.getParameter("name");
        if (status == null) {
            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                Gson gson = new Gson();
                List<MissionDTOWithoutCB> lst = ServletUtils.getTaskManager(getServletContext()).getTaskDTOList();
                String json = gson.toJson(lst);
                System.out.println("MISSIONS: " + json);
                resp.setStatus(200);
                out.println(json);
                out.flush();
            }
        } else {
            try (PrintWriter out = resp.getWriter()) {
                // no content type
                if (name == null) {
                    resp.setStatus(404);
                    out.println("No name given!");
                } else {
                    ServletUtils.getTaskManager(getServletContext()).getMissionByName(name).setStatus(status);
                    resp.setStatus(200);
                }
            }
        }
    }
}

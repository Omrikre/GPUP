package servlets;

import Engine.DTO.MissionDTO;
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
import java.util.Map;

@WebServlet("/missionlist")
public class TasksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            ServletUtils.getTaskManager(getServletContext()).addTask(new MissionDTO(0,"ss",
                    1,true,2,3, "ds", MissionState.FINISHED,3,4,5,
                    "noam","no",null,1,2,null));
            String json = gson.toJson(ServletUtils.getTaskManager(getServletContext()).getTaskDTOList());
            out.println(json);
            out.flush();
        }
    }
}

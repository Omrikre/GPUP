package servlets;

import Engine.DTO.GraphDTO;
import Engine.Graph;
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
        resp.setContentType("application/json");
        try(PrintWriter out= resp.getWriter()){
            Gson gson=new Gson();
            GraphManager graphManager= ServletUtils.getGraphManager(getServletContext());
            Map<String, GraphDTO> graphMap=graphManager.getGraphsAsDTOs();
            String json=gson.toJson(graphMap);
            out.println(json);
            out.flush();
        }
    }
}

package Engine;

import Engine.DTO.GraphDTO;
import Engine.DTO.GraphDTOWithoutCB;
import Engine.DTO.TargetDTO;
import Engine.DTO.TargetDTOWithoutCB;
import Engine.Enums.Bond;
import Exceptions.FileException;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphManager {
    private Map<String, Graph> graphMap;

    public GraphManager() {
        graphMap = new HashMap<>();
    }

    public synchronized void addGraph(Graph g) {
        graphMap.put(g.getGraphName(), g);
    }

    public synchronized List<GraphDTOWithoutCB> getGraphsAsDTOs() {
        List<GraphDTOWithoutCB> res = new ArrayList<>();
        for (Graph g : graphMap.values()) {
            res.add(g.getGraphDTO());
        }
        return res;
    }

    public synchronized TargetDTOWithoutCB getTargetDTO(String graphname, String targetName) {
        return new TargetDTOWithoutCB(graphMap.get(graphname).getSimulationPrice(), graphMap.get(graphname).getCompilationPrice(), graphMap.get(graphname).getTargetByName(targetName));
    }

    public synchronized List<TargetDTOWithoutCB> getTargetDTOList(String graphname) {
        List<TargetDTOWithoutCB> res = new ArrayList<>();
        for (Graph.Target t : graphMap.get(graphname).getTargets().values()) {
            res.add(new TargetDTOWithoutCB(t));
        }
        return res;
    }

    public synchronized GraphDTOWithoutCB getGraphDTOByName(String name) {
        return graphMap.get(name).getGraphDTO();
    }

    //functions from targil 2:
    public synchronized java.util.Set<java.util.List<String>> getPathBetweenTwoTargets(String gName, String a, String b, Bond bond) {
        return graphMap.get(gName).getPathBetweenTargets(a, b, bond);
    }

    public synchronized java.util.Set<java.util.List<String>> getCycle(String gName, String t) {
        return graphMap.get(gName).isTargetInCircleByName(t);
    }

    public synchronized java.util.Set<String> getWhatIf(String gName, String t, Bond b) {
        return graphMap.get(gName).getSetOfAllAffectedTargetsByBond(t, b);
    }

    public synchronized Graph getGraphOfRunnableTargetsFromArrayAndGraph(String graphname, List<String> targets) {
        Engine e = new Engine();
        try {
            return e.getGraphOfRunnableTargetsFromArrayAndGraph(graphMap.get(graphname), targets);
        } catch (FileException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

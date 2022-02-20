package Engine;

import Engine.DTO.GraphDTO;
import Engine.DTO.GraphDTOWithoutCB;
import Engine.DTO.TargetDTO;
import Engine.Enums.Bond;

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

    public synchronized TargetDTO getTargetDTO(String graphname, String targetName) {
        return new TargetDTO(graphMap.get(graphname).getTargetByName(targetName));
    }

    public synchronized List<TargetDTO> getTargetDTOList(String graphname) {
        List<TargetDTO> res = new ArrayList<>();
        for (Graph.Target t : graphMap.get(graphname).getTargets().values()) {
            res.add(new TargetDTO(t));
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
}

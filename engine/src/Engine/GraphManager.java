package Engine;

import Engine.DTO.GraphDTO;
import Engine.DTO.TargetDTO;
import Engine.Enums.Bond;

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

    public synchronized List<GraphDTO> getGraphsAsDTOs() {
        List<GraphDTO> res = new ArrayList<>();
        for (Graph g : graphMap.values()) {
            res.add(g.getGraphDTO());
        }
        return res;
    }

    public synchronized TargetDTO getTargetDTO(String graphname, String targetName) {
        return new TargetDTO(graphMap.get(graphname).getTargetByName(targetName));
    }

    public synchronized GraphDTO getGraphDTOByName(String name) {
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

package Engine;

import Engine.DTO.GraphDTO;
import Engine.Enums.Bond;

import java.util.HashMap;
import java.util.Map;

public class GraphManager {
    private Map<String, Graph> graphMap;

    public GraphManager() {
        graphMap = new HashMap<>();
    }

    public synchronized void addGraph(Graph g) {
        graphMap.put(g.getGraphName(), g);
    }

    public synchronized Map<String, GraphDTO> getGraphsAsDTOs() {
        Map<String, GraphDTO> res = new HashMap<>();
        for (Graph g : graphMap.values()) {
            res.put(g.getGraphName(), g.getGraphDTO());
        }
        return res;
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

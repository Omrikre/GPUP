package Engine;

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

    public synchronized Map<String, Graph> getGraphs() {
        return graphMap;
    }
}

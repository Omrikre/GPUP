package Engine;

import Engine.Enums.Location;
import Engine.Enums.State;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Engine {
    private Graph g;

    public Engine() {
        g = new Graph();
    }


    /**
     * This method gets a target name, and returns its data as a TargetDTO object if it exists.
     * if not, returns null.
     *
     * @param name The target's name
     * @return A DTO containing the target's info
     */
    public TargetDTO getTargetDataTransferObjectByName(String name) {
        if (g.isTargetInGraphByName(name))
            return new TargetDTO(g.getTargetByName(name));
        else
            return null;
    }

    /**
     * This method gets a target's name, and returns whether it's in the graph
     *
     * @param name The target's name
     * @return True if the target is in the graph, False if not
     */
    public boolean isTargetInGraphByName(String name) {
        return g.isTargetInGraphByName(name);
    }

    /**
     * This method gets a source node and a destination node from the user, and returns a set of all paths
     * between the source and the destination, without cycles. (simple paths)
     *
     * @param src  The source node
     * @param dest The destination node
     * @param type The method of running of the graph: Depends On or Required For
     * @return A set that contains Lists of Strings (Paths), with each String being the name of a target in said path
     */
    public Set<List<String>> getPathBetweenTargets(String src, String dest, int type) {
        return g.getPathBetweenTargets(src, dest, type);
    }

    /**
     * This method returns the amount of the total targets in the graph.
     *
     * @return int
     */
    public int getAmountOfTargets() {
        return g.getAmountOfTargets();
    }

    /**
     * This method returns a map containing how many targets are in each location.
     *
     * @return A map with key: Location and value: Integer (the amount of targets in said location)
     */
    public Map<Location, Integer> howManyTargetsInEachLocation() {
        return g.howManyTargetsInEachLocation();
    }

    /**
     * This method returns a set of targets names if they are in a waiting state,
     * from independent to roots. If the entire graph was finished (no more waiting),
     * returns null.
     *
     * @return A set of targets names that are in a waiting state, or null if no target is waiting
     */
    public Set<String> getSetOfWaitingTargetsNamesBottomsUp() {
        return g.getSetOfWaitingTargetsNamesBottomsUp();
    }

    /**
     * This method gets a target's name and a state (calculated by the UI from the task)
     * it sets the state for the target, and if it affects other targets sets their state too
     *
     * @param targetName  The target's name
     * @param targetState The given state after the task
     */
    public void setFinishedState(String targetName, State targetState) {
        //if state!=FINISHED, throw exception
        g.setFinishedState(targetName, targetState);
    }

}
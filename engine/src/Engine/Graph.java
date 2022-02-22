package Engine;

import Engine.DTO.GraphDTO;
import Engine.DTO.GraphDTOWithoutCB;
import Engine.DTO.TargetDTO;
import Engine.Enums.Bond;
import Engine.Enums.Location;
import Engine.Enums.State;
import Exceptions.FileException;

import java.io.*;
import java.sql.Time;
import java.util.*;

public class Graph implements Serializable {
    private Map<String, Target> targets; //database that can find a target by its name
    private Map<String, Set<String>> serialSets; //a collection of all the serial sets in the graph, each set containing target names and has a name
    private String graphName;
    private String username;
    private int simulationPrice, compilationPrice;

    public Graph() {
        targets = new HashMap<>();
        serialSets = new HashMap<>();
    }

    public class Target implements Serializable {
        private String name;
        private Location location;
        private Set<String> dependsOn;
        private Set<String> requiredFor;
        private String info;
        private State state;
        private long startingTime, endingTime;
        private long time;
        private int serialSetsBelongs; //how many serial sets the target is in

        public Target() {

        }

        public Target(String name, String info) throws FileException {
            this.name = name;
            dependsOn = new HashSet<>();
            requiredFor = new HashSet<>();
            this.info = info;
            this.state = State.FROZEN;
            serialSetsBelongs = 0;
            if (targets.containsKey(name))
                throw new FileException(2, name);
            targets.put(name, this);
        }

        public String getName() {
            return name;
        }

        public Location getLocation() {
            return location;
        }

        public Set<String> getDependsOn() {
            return dependsOn;
        }

        public Set<String> getRequiredFor() {
            return requiredFor;
        }

        public String getInfo() {
            return info;
        }

        public State getState() {
            return state;
        }

        public long getTime() {
            return time;
        }

        public void setTime() {
            this.time = endingTime - startingTime;
        }

        public void setTime(int num) {
            this.time = num;
        }

        public void setStartingTime(long sTime) {
            startingTime = sTime;
        }

        public void setEndingTime(long eTime) {
            endingTime = eTime;
        }

        //used only in the function that automatically sets a location to all the targets in the graph
        private void setLocation(Location l) {
            this.location = l;
        }

        public void setState(State s) {
            this.state = s;
        }

        public void setFinishedTargetState(State targetState) {
            Target t = this;
            t.setState(targetState);
            if (t.getState().equals(State.FINISHED_FAILURE)) {
                setAllRequiredTargetsSkipped(t);
            }
        }

        private void addDependedTarget(Target t) throws FileException {
            if (t.dependsOn.contains(this.getName()))
                throw new FileException(4, this.getName(), Bond.DEPENDS_ON, t.getName());
            dependsOn.add(t.getName());
            if (!t.requiredFor.contains(this.getName()))
                t.addRequiredTarget(this);
        }

        private void addRequiredTarget(Target t) throws FileException {
            if (t.requiredFor.contains(this.getName()))
                throw new FileException(4, this.getName(), Bond.REQUIRED_FOR, t.getName());
            requiredFor.add(t.getName());
            if (!t.dependsOn.contains(this.getName()))
                t.addDependedTarget(this);
        }

        public void addBondedTarget(Bond b, String t) throws FileException {
            if (!targets.containsKey(t))
                throw new FileException(3, t, b, this.getName());
            if (b.equals(Bond.DEPENDS_ON)) {
                addDependedTarget(getTargetByName(t));
            } else if (b.equals(Bond.REQUIRED_FOR)) {
                addRequiredTarget(getTargetByName(t));
            }
        }

        /**
         * this function raises the counter of how many serial sets the target belongs to
         */
        public void addTargetToSerialSet() {
            this.serialSetsBelongs++;
        }

        public int getSerialSetsBelongs() {
            return serialSetsBelongs;
        }

        public int getNumberOfBonds(Bond bond) {
            return getSetOfAllAffectedTargetsByBond(this.name, bond).size();
        }

        public void setTargetStateByParameters(int success, int successWithWarnings) {
            Random rand = new Random();
            float magicNumber = rand.nextFloat();
            if ((float) (success) / 100 >= magicNumber) {
                magicNumber = rand.nextFloat();
                if ((float) (successWithWarnings) >= magicNumber) {
                    setFinishedState(this.name, State.FINISHED_WARNINGS);
                } else {
                    setFinishedState(this.name, State.FINISHED_SUCCESS);
                }
            } else {
                setFinishedState(this.name, State.FINISHED_FAILURE);
            }
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Target target = (Target) o;
            return name.equals(target.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    } //Target

    public void setGraphName(String name) {
        graphName = name;
    }

    public String getGraphName() {
        return graphName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getSimulationPrice() {
        return simulationPrice;
    }

    public int getCompilationPrice() {
        return compilationPrice;
    }

    public void setSimulationPrice(int simulationPrice) {
        this.simulationPrice = simulationPrice;
    }

    public void setCompilationPrice(int compilationPrice) {
        this.compilationPrice = compilationPrice;
    }

    public GraphDTOWithoutCB getGraphDTO() {
        Map<Location, Integer> locations = howManyTargetsInEachLocation();
        return new GraphDTOWithoutCB(graphName, username, simulationPrice, compilationPrice, locations.get(Location.INDEPENDENT), locations.get(Location.LEAF),
                locations.get(Location.MIDDLE), locations.get(Location.ROOT), checkIfTheGraphContainsCycle());
    }

    public Map<String, Target> getTargets() {
        return targets;
    }

    public Map<String, Set<String>> getSerialSets() {
        return serialSets;
    }

    /**
     * This method gets a name and return true if a target corresponding to said name is in the graph
     * or false if it isn't.
     *
     * @param name The target's name
     * @return true if the target is in the graph, false if it isn't.
     */
    public boolean isTargetInGraphByName(String name) {
        return targets.containsKey(name);
    }

    /**
     * This method gets a name and returns the corresponding target.
     * If the target doesn't exist, returns null. expected to check!
     *
     * @param name The target's name.
     * @return The target as a Target object.
     */
    public Target getTargetByName(String name) {
        if (isTargetInGraphByName(name))
            return targets.get(name);
        else return null;
    }

    /**
     * This method sets the location of all the nodes in the graph, by their dependencies and requirements.
     */
    public void setLocationForAllTargets() {
        for (Target t : targets.values()) { //going over all the values in the graph
            if (t.getDependsOn().isEmpty() && t.getRequiredFor().isEmpty()) //has no dependencies or requirements
                t.setLocation(Location.INDEPENDENT);
            else if (t.getDependsOn().isEmpty()) //has no dependencies
                t.setLocation(Location.LEAF);
            else if (t.getRequiredFor().isEmpty()) //has no requirements
                t.setLocation(Location.ROOT);
            else //has dependencies and requirements
                t.setLocation(Location.MIDDLE);
        }
    }


    /**
     * This method gets a source node and a destination node from the user, and returns a set of all paths
     * between the source and the destination, without cycles. (simple paths)
     *
     * @param src  The source node
     * @param dest The destination node
     * @param bond The method of running of the graph: Depends On or Required For
     * @return A set that contains Lists of Strings (Paths), with each String being the name of a target in said path
     */
    public Set<List<String>> getPathBetweenTargets(String src, String dest, Bond bond) {
        Map<String, Boolean> visited = new HashMap<>();
        Set<List<String>> allPaths = new HashSet<>(); //where we'll save all the paths
        List<String> singlePath = new ArrayList<>();
        Target source = targets.get(src);
        Target destination = targets.get(dest);
        //calls a private method for recursion:
        getPathBetweenTargetsRec(visited, allPaths, singlePath, source, destination, bond);
        return allPaths;
    }

    public boolean checkIfTheGraphContainsCycle() {
        for (String s : targets.keySet()) {
            Set<List<String>> temp = isTargetInCircleByName(s);
            if (!temp.isEmpty())
                return true;
        }
        return false;
    }

    //to be used only in the big function, finding each simple path and adding to all the paths
    private void getPathBetweenTargetsRec(Map<String, Boolean> visited,
                                          Set<List<String>> allPaths, List<String> singlePath,
                                          Target src, Target dest, Bond bond) {
        if (visited.get(src.getName()) != null &&
                visited.get(src.getName()).equals(true)) { //check if we visited the node yet
            return; //go back to not create a cycle
        }
        visited.put(src.getName(), true); //we now visited the node
        singlePath.add(src.getName()); //adding the current node to the back of the list
        if (src.getName().equals(dest.getName())) { //if we reached our destination
            List<String> temp = new ArrayList<>(singlePath);
            allPaths.add(temp); //adding it to the set containing all the paths
            visited.put(src.getName(), false); //for the case we have another path
            singlePath.remove(src.getName()); //removing it from the current path as we go back
            return;
        }
        if (bond.equals(Bond.DEPENDS_ON))
            for (String t : src.dependsOn) {
                getPathBetweenTargetsRec(visited, allPaths, singlePath, targets.get(t), dest, bond);
            }
        else if (bond.equals(Bond.REQUIRED_FOR))
            for (String t : src.requiredFor) {
                getPathBetweenTargetsRec(visited, allPaths, singlePath, targets.get(t), dest, bond);
            }
        singlePath.remove(src.getName()); //if nowhere else to go, we remove everything
        visited.put(src.getName(), false); //and reset the visited block for different paths
    }

    /**
     * This method returns the amount of the total targets in the graph.
     *
     * @return int
     */
    public int getAmountOfTargets() {
        return targets.size();
    }

    /**
     * This method returns a map containing how many targets are in each state.
     *
     * @return A map with key: State and value: Integer (how many targets in each state)
     */
    public Map<State, Integer> howManyTargetsInEachState() {
        Map<State, Integer> res = new HashMap<>();
        int frozen = 0, skipped = 0, failure = 0, warnings = 0, success = 0;
        for (Target t : targets.values()) {
            switch (t.getState()) {
                case FROZEN:
                    frozen++;
                    break;
                case SKIPPED:
                    skipped++;
                    break;
                case FINISHED_FAILURE:
                    failure++;
                    break;
                case FINISHED_WARNINGS:
                    warnings++;
                    break;
                case FINISHED_SUCCESS:
                    success++;
                    break;
            }
        }
        res.put(State.FROZEN, frozen);
        res.put(State.SKIPPED, skipped);
        res.put(State.FINISHED_FAILURE, failure);
        res.put(State.FINISHED_SUCCESS, success);
        res.put(State.FINISHED_WARNINGS, warnings);
        return res;
    }

    public Map<State, Set<String>> getTargetsInEachState() {
        Map<State, Set<String>> res = new HashMap<>();
        res.put(State.FROZEN, new HashSet<>());
        res.put(State.SKIPPED, new HashSet<>());
        res.put(State.FINISHED_SUCCESS, new HashSet<>());
        res.put(State.FINISHED_FAILURE, new HashSet<>());
        res.put(State.FINISHED_WARNINGS, new HashSet<>());
        for (Target t : targets.values()) {
            switch (t.getState()) {
                case FROZEN:
                    res.get(State.FROZEN).add(t.getName());
                    break;
                case SKIPPED:
                    res.get(State.SKIPPED).add(t.getName());
                    break;
                case FINISHED_FAILURE:
                    res.get(State.FINISHED_FAILURE).add(t.getName());
                    break;
                case FINISHED_WARNINGS:
                    res.get(State.FINISHED_WARNINGS).add(t.getName());
                    break;
                case FINISHED_SUCCESS:
                    res.get(State.FINISHED_SUCCESS).add(t.getName());
                    break;
            }
        }
        return res;
    }

    /**
     * This method returns a map containing how many targets are in each location.
     *
     * @return A map with key: Location and value: Integer (the amount of targets in said location)
     */
    public Map<Location, Integer> howManyTargetsInEachLocation() {
        Map<Location, Integer> res = new HashMap<>();
        int leaf = 0, root = 0, independent = 0, middle = 0; //the counters to put in the map
        for (Target t : targets.values()) {
            switch (t.getLocation()) {
                case LEAF:
                    leaf++;
                    break;
                case ROOT:
                    root++;
                    break;
                case MIDDLE:
                    middle++;
                    break;
                case INDEPENDENT:
                    independent++;
                    break;
            }
        }
        res.put(Location.LEAF, leaf);
        res.put(Location.ROOT, root);
        res.put(Location.INDEPENDENT, independent);
        res.put(Location.MIDDLE, middle);
        return res;
    }

    /**
     * This method gets a location, and returns a set of all the frozen
     * targets in said location
     *
     * @param location The target's location
     * @return a Set of all the frozen targets with said location.
     */
    private Set<Graph.Target> getSetOfFrozenTargetsByLocation(Location location) {
        Set<Graph.Target> res = new HashSet<>();
        for (Target t : targets.values()) {
            if (t.location.equals(location))
                if (t.getState().equals(State.FROZEN))
                    res.add(t);
        }
        return res;
    }

    /**
     * This method sets all failed (and skipped) targets to frozen. For starting the task again!
     */
    public void setAllFailedAndSkippedTargetsFrozen() {
        for (Target t : targets.values()) {
            if (t.getState().equals(State.FINISHED_FAILURE) || t.getState().equals((State.SKIPPED)))
                t.setState(State.FROZEN);
        }
    }

    /**
     * This method checks if all targets finished their task successfully
     *
     * @return True or False
     */
    public boolean isFinishedSuccessfully() {
        for (Target t : targets.values()) {
            if (!((t.state.equals(State.FINISHED_SUCCESS)) || (t.state.equals(State.FINISHED_WARNINGS))))
                return false;
        }
        return true;
    }

    /**
     * This method sets all targets to frozen. For starting the task from scratch!
     */
    public void setAllTargetsFrozen() {
        for (Target t : targets.values()) {
            if (!t.getState().equals(State.FROZEN)) {
                t.setState(State.FROZEN);
                t.setTime(0);
            }
        }
    }

    /**
     * This method returns a set of waiting targets names,
     * from independent to roots. If the entire graph was finished (no more waiting),
     * returns null.
     *
     * @return A set of targets names that are waiting, or null if no target is waiting
     */
    public Set<String> getSetOfWaitingTargetsNamesBottomsUp() {
        Set<String> res = new HashSet<>();
        for (Target t : getSetOfFrozenTargetsByLocation(Location.INDEPENDENT)) {
            t.setState(State.WAITING);
            res.add(t.getName());
        }
        if (!res.isEmpty()) //there were waiting targets in the independent branch
            return res;
        for (Target t : getSetOfFrozenTargetsByLocation(Location.LEAF)) {
            t.setState(State.WAITING);
            res.add(t.getName());
        }
        if (!res.isEmpty()) //there were waiting targets in the leaves branch
            return res;
        for (Target t : getSetOfFrozenTargetsByLocation(Location.MIDDLE)) {
            t.setState(State.WAITING);
            res.add(t.getName());
        }
        if (!res.isEmpty()) //there were waiting targets in the middle branch
            return res;
        for (Target t : getSetOfFrozenTargetsByLocation(Location.ROOT)) {
            t.setState(State.WAITING);
            res.add(t.getName());
        }
        if (!res.isEmpty()) //there were waiting targets in the root branch
            return res;
        return null; //there were no waiting targets in the graph
    }

    /**
     * This method gets a target's name and a state (calculated by the UI from the task)
     * it sets the state for the target, and if it affects other targets sets their state too
     *
     * @param targetName  The target's name
     * @param targetState The given state after the task
     */
    public void setFinishedState(String targetName, State targetState) {
        Target t = targets.get(targetName);
        t.setState(targetState);
        if (t.getState().equals(State.FINISHED_FAILURE)) {
            setAllRequiredTargetsSkipped(t);
        }
    }

    /**
     * This method gets a target and sets all it's requiredFor targets to SKIPPED
     * to be used if target task has failed
     *
     * @param t The failed target
     */
    private void setAllRequiredTargetsSkipped(Target t) {
        //if t.state!=failed, throw exception
        for (String temp : t.getRequiredFor()) {
            setAllRequiredTargetsSkipped(targets.get(temp));
            if (!targets.get(temp).getState().equals(State.SKIPPED))
                targets.get(temp).setState(State.SKIPPED);
        }
    }

    public Set<String> getRequiredFor(String targetName) {
        Set<String> res = new HashSet<>();
        res = getTargetByName(targetName).requiredFor;
        return res;
    }

    /**
     * This method gets a finished target's name, and returns a set with the names of all the directs which directly depend on it and are currently waiting to run the task on.
     *
     * @param targetName The finished target's name
     * @return A set of strings (target's names) of the direct dependencies which can now perform the task.
     */
    public Set<String> getRunnableTargetsNamesFromFinishedTarget(String targetName) {
        //if targetName not finished throw exception
        Set<String> res = new HashSet<>();
        boolean runnable = true;
        if (!targets.get(targetName).requiredFor.isEmpty()) {
            for (String t : targets.get(targetName).requiredFor) {
                for (String k : targets.get(t).dependsOn) {
                    if (!(targets.get(k).state.equals(State.FINISHED_FAILURE) || targets.get(k).state.equals(State.FINISHED_SUCCESS) || targets.get(k).state.equals(State.FINISHED_WARNINGS))) {
                        runnable = false;
                        break;
                    }
                }
                if (runnable)
                    res.add(t);
                else runnable = true;
            }
        }
        return res;
    }

    //inner method to be used
    private void getSkippedTargetsNamesFromFailedTargetRec(Target t, Set<String> res) {
        for (String temp : t.requiredFor) {
            getSkippedTargetsNamesFromFailedTargetRec(targets.get(temp), res);
            res.add(temp);
        }
    }

    /**
     * This method gets a failed target's name, and returns a set of the names all the targets that are Skipped because it failed.
     *
     * @param targetName The failed target's name
     * @return A set of strings (the names of targets) that are Skipped because the target has failed
     */
    public Set<String> getSkippedTargetsNamesFromFailedTarget(String targetName) {
        //if not failed throw exception
        Set<String> res = new HashSet<>();
        getSkippedTargetsNamesFromFailedTargetRec(targets.get(targetName), res);
        return res;
    }

    /**
     * This method gets a target's name and returns a list of all the circles it is in
     *
     * @param name The target's name
     * @return A set of lists of target names, each list represents a circle that the target is in
     */
    public Set<List<String>> isTargetInCircleByName(String name) {
        Target t = targets.get(name);
        Set<List<String>> res = new HashSet<>();
        if (t.location.equals(Location.MIDDLE))
            for (String temp : t.dependsOn) {
                res = getPathBetweenTargets(temp, name, Bond.DEPENDS_ON);
                if (!res.isEmpty())
                    return res;
            }
        return res;
    }

    //part 2:

    /**
     * this method gets a target's name and a bond type, and returns a set of all targets names connect to it by said bond
     *
     * @param name The target's name
     * @param bond The required bond
     * @return a set of targets names
     */
    public Set<String> getSetOfAllAffectedTargetsByBond(String name, Bond bond) {
        Set<String> res = new HashSet<>();
        Target t = getTargetByName(name);
        getSetOfAllAffectedTargetsByBondRec(res, t, bond);
        return res;
    }

    private void getSetOfAllAffectedTargetsByBondRec(Set<String> res, Target t, Bond bond) {
        if (bond.equals(Bond.REQUIRED_FOR)) {
            for (String str : t.requiredFor) {
                if (!res.contains(str)) {
                    res.add(str);
                    getSetOfAllAffectedTargetsByBondRec(res, getTargetByName(str), bond);
                }
            }
        } else { //depends on
            for (String str : t.dependsOn) {
                if (!res.contains(str)) {
                    res.add(str);
                    getSetOfAllAffectedTargetsByBondRec(res, getTargetByName(str), bond);
                }
            }
        }
    }

    public Set<String> getFailedTargetsFromSkipped(String name) {
        Set<String> res = new HashSet<>();
        getFailedTargetsFromSkippedRec(res, name);
        return res;
    }

    private void getFailedTargetsFromSkippedRec(Set<String> res, String name) {
        for (String s : targets.get(name).dependsOn) {
            if (targets.get(s).state.equals(State.FINISHED_FAILURE))
                res.add(s);
            else
                getFailedTargetsFromSkippedRec(res, s);
        }
    }

    public Set<String> getAllTargetsByLocation(Location location) {
        Set<String> res = new HashSet<>();
        for (Target t : targets.values()) {
            if (t.location.equals(location))
                res.add(t.getName());
        }
        return res;
    }

}



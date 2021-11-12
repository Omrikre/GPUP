package Engine;

import Engine.Enums.Location;
import Engine.Enums.State;

import java.util.*;

public class Graph {
    private Map<String, Target> targets; //database that can find a target by its name

    public Graph() {
        targets = new HashMap<>();
    }

    public class Target {
        private final String name;
        private Location location;
        private Set<Target> dependsOn;
        private Set<Target> requiredFor;
        private String info;
        private State state;

        public Target(String name, String info) {
            this.name = name;
            dependsOn = new HashSet<>();
            requiredFor = new HashSet<>();
            this.info = info;
            this.state = State.WAITING;
            targets.put(name, this);
        }

        public Target(String name) {
            this.name = name;
            dependsOn = new HashSet<>();
            requiredFor = new HashSet<>();
            this.state = State.WAITING;
            targets.put(name, this);
        }

        public String getName() {
            return name;
        }

        public Location getLocation() {
            return location;
        }

        public Set<Target> getDependsOn() {
            return dependsOn;
        }

        public Set<Target> getRequiredFor() {
            return requiredFor;
        }

        public String getInfo() {
            return info;
        }

        public State getState() {
            return state;
        }

        //used only in the function that automatically sets a location to all the targets in the graph
        private void setLocation(Location l) {
            location = l;
        }

        public void setState(State s) {
            state = s;
        }

        public void addDependedTarget(Target t) {
            dependsOn.add(t);
            if (!t.requiredFor.contains(this))
                t.addRequiredTarget(this);
            //can check for errors in the file here,
            // if x is dependent on y
            // then y can't be dependent on x!
        }

        public void addRequiredTarget(Target t) {
            requiredFor.add(t);
            if (!t.dependsOn.contains(this))
                t.addDependedTarget(this);
            //can check for errors in the file here,
            // if x is required for y
            // then y can't be required for x!
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
     * @param type The method of running of the graph: Depends On or Required For
     * @return A set that contains Lists of Strings (Paths), with each String being the name of a target in said path
     */
    public Set<List<String>> getPathBetweenTargets(String src, String dest, int type) {
        Map<String, Boolean> visited = new HashMap<>();
        Set<List<String>> allPaths = new HashSet<>(); //where we'll save all the paths
        List<String> singlePath = new ArrayList<>();
        Target source = targets.get(src);
        Target destination = targets.get(dest);
        //calls a private method for recursion:
        getPathBetweenTargetsRec(visited, allPaths, singlePath, source, destination, type);
        return allPaths;
    }

    //to be used only in the big function, finding each simple path and adding to all the paths
    private void getPathBetweenTargetsRec(Map<String, Boolean> visited,
                                          Set<List<String>> allPaths, List<String> singlePath,
                                          Target src, Target dest, int type) {
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
        if (type == 1)
            for (Target t : src.dependsOn) {
                getPathBetweenTargetsRec(visited, allPaths, singlePath, t, dest, type);
            }
        else
            for (Target t : src.requiredFor) {
                getPathBetweenTargetsRec(visited, allPaths, singlePath, t, dest, type);
            }
        singlePath.remove(src.getName()); //if nowhere else to go, we remove everything
        visited.put(src.getName(), false); //and reset the visited block for different paths
    }

    //method that checks if there's a circle for bonus:
    /*
                TBD
     */

    /**
     * This method returns the amount of the total targets in the graph.
     *
     * @return int
     */
    public int getAmountOfTargets() {
        return targets.size();
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
     * This method gets a location, and returns a set of all the targets with said location
     * @param location The target's location
     * @return a Set of all the targets with said location. If there are no targets in it, returns null
     */
    public Set<Graph.Target> getSetOfTargetsByLocation(Location location) {
        Set<Graph.Target> res = new HashSet<>();
        for (Target t : targets.values()) {
            if (t.location.equals(location))
                res.add(t);
        }
        if (res.isEmpty()) //no targets were added
            return null;
        else return res;
    }


}

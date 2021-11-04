import com.sun.deploy.security.SelectableSecurityManager;

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

        public Target(String name, String info) {
            this.name = name;
            dependsOn = new HashSet<Target>();
            requiredFor = new HashSet<Target>();
            this.info = info;
            targets.put(name, this);
        }

        public Target(String name) {
            this.name = name;
            dependsOn = new HashSet<Target>();
            requiredFor = new HashSet<Target>();
            targets.put(name, this);
        }

        public String getName() {
            return name;
        }

        public Location getLocation() {
            return location;
        }

        //for debugging! need to do an automatic function that determines it
        public void setLocation(Location l) {
            location = l;
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

        public String getInfo() {
            String depends;
            if (dependsOn == null) {
                depends = "Depends on no one";
            } else depends = dependsOn.toString();
            String required;
            if (requiredFor == null) {
                required = "Required for no one";
            } else required = requiredFor.toString();
            String infoString = info;
            if (info == null) {
                infoString = "Has no info";
            }
            return "Target details:\n" +
                    "Name: " + name + "\n" +
                    "Location: " + location + "\n" +
                    "DependsOn: " + depends + "\n" +
                    "RequiredFor: " + required + "\n" +
                    "Info: " + infoString + "\n";
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
    }

    public Set<List<String>> getPathBetweenTargets(String src, String dest, int type) {
        Map<String, Boolean> visited = new HashMap<>();
        Set<List<String>> allPaths = new HashSet<>(); //where we'll save all the paths
        List<String> singlePath = new ArrayList<>();
        Target source = targets.get(src);
        Target destination = targets.get(dest);
        getPathBetweenTargetsRec(visited, allPaths, singlePath, source, destination, type);
        return allPaths;
    }

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


    /**
     * This method gets a target name, and returns its info if it exists.
     *
     * @param name The target's name.
     * @return String representing the info on said target.
     */
    public String getTargetInfoByName(String name) {
        if (targets.containsKey(name)) {
            return targets.get(name).getInfo();
        } else {
            return "Target doesn't exist!";
        }
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

}

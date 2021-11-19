package Engine;

import Engine.DTO.TargetDTO;
import Engine.Enums.Bond;
import Engine.Enums.Location;
import Engine.Enums.State;
import Engine.Tasks.SimulationTask;
import Engine.Tasks.Task;
import Engine.XML.GPUPDescriptor;
import Engine.XML.GPUPTarget;
import Engine.XML.GPUPTargetDependencies;
import Exceptions.FileException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.*;


public class Engine {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "Engine.XML";

    private Graph g;
    private Task task;
    private String filePath;

    public Engine() {
        g = new Graph();
    }

    public long getTotalRuntime() {
        return task.getTotalRuntime();
    }

    /**
     * This method gets a target name, and returns its data as a TargetDTO object if it exists.
     * if not, returns null.
     *
     * @param name The target's name
     * @return A DTO containing the target's info
     */
    public static TargetDTO getTargetDataTransferObjectByName(String name) {
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
     * @param bond The method of running of the graph: Depends On or Required For
     * @return A set that contains Lists of Strings (Paths), with each String being the name of a target in said path
     */
    public Set<List<String>> getPathBetweenTargets(String src, String dest, Bond bond) {
        return g.getPathBetweenTargets(src, dest, bond);
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
     * This method returns a map containing how many targets are in each state.
     *
     * @return A map with key: State and value: Integer (how many targets in each state)
     */
    public Map<State, Integer> howManyTargetsInEachState() {
        return g.howManyTargetsInEachState();
    }

    /**
     * This method returns a set of targets names if they are in a given state,
     * from independent to roots. If the entire graph was finished (no more of said state),
     * returns null.
     *
     * @param state The required target's state
     * @return A set of targets names that are in a given state, or null if no target is waiting
     */
    public Set<String> getSetOfTargetsNamesBottomsUpByState(State state) {
        return g.getSetOfTargetsNamesBottomsUpByState(state);
    }

    /**
     * This method gets a target's name and a state (calculated by the UI from the task)
     * it sets the state for the target, and if it affects other targets sets their state too
     *
     * @param targetName  The target's name
     * @param targetState The given state after the task
     */
    public void setFinishedState(String targetName, State targetState, String time) {
        //if state!=FINISHED, throw exception
        g.setFinishedState(targetName, targetState, time);
    }

    private GPUPDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GPUPDescriptor) u.unmarshal(in);
    }

    /**
     * This method gets a filepath, and loads a file from it into the graph.
     *
     * @param filePath The file's path
     * @throws JAXBException         In case the jaxb had a problem
     * @throws FileNotFoundException In case the file doesn't exist
     * @throws FileException         In case the file is not valid
     */
    public void loadFile(String filePath) throws JAXBException, FileNotFoundException, FileException {
        if (!filePath.endsWith("xml"))
            throw new FileException(1, filePath);
        this.filePath = filePath;
        InputStream inputStream = new FileInputStream(filePath);
        GPUPDescriptor gp = deserializeFrom(inputStream);

        Graph res = new Graph();

        List<GPUPTarget> targetsList = gp.getGPUPTargets().getGPUPTarget();
        for (GPUPTarget t : targetsList) { //adding all the targets to a graph, if there are two targets with the same name the exception will be thrown from the target constructor
            res.new Target(t.getName(), t.getGPUPUserData());
        }
        for (GPUPTarget t : targetsList) { //adding all the dependencies
            List<GPUPTargetDependencies.GPUGDependency> dependencies;
            if (t.getGPUPTargetDependencies() != null) {
                dependencies = t.getGPUPTargetDependencies().getGPUGDependency();
                Graph.Target target = res.getTargetByName(t.getName());
                for (GPUPTargetDependencies.GPUGDependency gpugDependency : dependencies) {
                    String dependencyType = gpugDependency.getType();
                    target.addBondedTarget(Bond.valueOf(dependencyType.replaceAll("([A-Z])", "_$1").toUpperCase())
                            , gpugDependency.getValue());
                }
            }
        }
        res.setLocationForAllTargets();
        g = res;
    }

    /**
     * This method gets a failed target's name, and returns a set of the names all the targets that are Skipped because it failed.
     *
     * @param targetName The failed target's name
     * @return A set of strings (the names of targets) that are Skipped because the target has failed
     */
    public Set<String> getSkippedTargetsNamesFromFailedTarget(String targetName) {
        return g.getSkippedTargetsNamesFromFailedTarget(targetName);
    }

    /**
     * This method gets a finished target's name, and returns a set with the names of all the directs which directly depend on it and are currently waiting to run the task on.
     *
     * @param targetName The finished target's name
     * @return A set of strings (target's names) of the direct dependencies which can now perform the task.
     */
    public Set<String> getRunnableTargetsNamesFromFinishedTarget(String targetName) {
        return g.getRunnableTargetsNamesFromFinishedTarget(targetName);
    }

    /**
     * This method gets a target's name and returns a list of all the circles it is in
     * if the set is empty, the target is not in any circle
     *
     * @param name The target's name
     * @return A set of lists of target names, each list represents a circle that the target is in
     */
    public Set<List<String>> isTargetInCircleByName(String name) {
        return g.isTargetInCircleByName(name);
    }

    private void saveTargetToFileByName(String name) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        Path p=Paths.get(filePath);

        filePath = filePath.substring(0, filePath.lastIndexOf('\\')) + task.getName() + dtf.format(task.getExecutionDate()) + name + ".log";
        File targetInfoFile = new File(filePath);

    }


}
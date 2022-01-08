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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Engine {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "Engine.XML";

    private Graph g;
    private Task task;
    private String directoryPath, targetFilePath, slash, fileName;
    private Path XMLFilePath;
    private boolean newRun;
    private final String systemStateFileEnding = ".bin";
    private int maxThreads;

    public Engine() {
        g = new Graph();
        newRun = true;
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
            return null; //exception?
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
     * @return A set of targets names that are in a given state, or null if no target is waiting
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
    public void loadFile(String filePath) throws JAXBException, IOException, FileException {
        if (!filePath.endsWith(".xml"))
            throw new FileException(1, filePath);
        fileName = Paths.get(filePath).getFileName().toString();
        InputStream inputStream = new FileInputStream(filePath);
        GPUPDescriptor gp = deserializeFrom(inputStream);
        XMLFilePath = Paths.get(gp.getGPUPConfiguration().getGPUPWorkingDirectory());
        if (!Files.isDirectory(XMLFilePath))
            Files.createDirectory(XMLFilePath);
        if (XMLFilePath.toString().contains("\\"))
            slash = "\\";
        else slash = "/";
        Graph res = new Graph();

        List<GPUPTarget> targetsList = gp.getGPUPTargets().getGPUPTarget();
        Set<String> serialSetsNames = new HashSet<>();
        Set<GPUPDescriptor.GPUPSerialSets.GPUPSerialSet> serialSets = new HashSet<>();
        if (gp.getGPUPSerialSets()!=null)
            for (GPUPDescriptor.GPUPSerialSets.GPUPSerialSet ss : gp.getGPUPSerialSets().getGPUPSerialSet()) { //checking if the serial sets are fine and adding them to a set
                if (serialSetsNames.contains(ss.getName()))
                    //exception
                    throw new FileException(3, ss.getName());
                else {
                    serialSetsNames.add(ss.getName());
                    serialSets.add(ss);
                }
            }
        for (GPUPTarget t : targetsList) { //adding all the targets to a graph, if there are two targets with the same name the exception will be thrown from the target constructor
            res.new Target(t.getName(), t.getGPUPUserData());
        }
        for (GPUPDescriptor.GPUPSerialSets.GPUPSerialSet s : serialSets) { //checking if the targets in the serial sets are well-defined and adding their serial set to the graph
            String[] split = s.getTargets().split(",");
            for (String str : split) {
                if (!res.isTargetInGraphByName(str))
                    throw new FileException(4, str);
                else {
                    res.getTargetByName(str).addTargetToSerialSet();
                    if (res.getSerialSets().isEmpty() || !res.getSerialSets().containsKey(s.getName())) { //serial set doesnt exist
                        Set<String> names = new HashSet<>();
                        names.add(str);
                        res.getSerialSets().put(s.getName(), names);
                    } else { //serial set exists
                        res.getSerialSets().get(s.getName()).add(str);
                    }
                }
            }
        }


        maxThreads = gp.getGPUPConfiguration().

                getGPUPMaxParallelism();
        for (
                GPUPTarget t : targetsList) { //adding all the dependencies
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
     * This method returns a list of all targets as DTOs
     *
     * @return A list of target DTOs
     */
    public List<TargetDTO> getListOfAllTargetsDTOsInGraph() {
        List<TargetDTO> res = new ArrayList<>();
        for (Graph.Target t : g.getTargets().values()) {
            res.add(getTargetDataTransferObjectByName(t.getName()));
        }
        res.sort(Comparator.comparing(TargetDTO::getTargetName));
        return res;
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
     * This method checks if all targets finished their task successfully
     *
     * @return True or False
     */
    public boolean isGraphFinishedSuccessfully() {
        return g.isFinishedSuccessfully();
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

    /**
     * This method gets a path and creates a folder for the simulation for it.
     *
     * @param XMLfilePath The XML path
     */
    private void createTaskFolder(Path XMLfilePath) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss");
        directoryPath = XMLfilePath + slash + task.getName() + " - " + dtf.format(task.getExecutionDate());
        File dir = new File(directoryPath);
        dir.mkdir(); //if false throw exception?
    }

    /**
     * This method gets an info as a string, and saves it to a file of the current target.
     *
     * @param info String, the info.
     * @throws IOException
     */
    private void saveTargetInfoToFile(String info) throws IOException {
        try (Writer out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(targetFilePath, true)))) {
            out.write(info + "\n");
        }
    }

    public String simulationStartInfo(String name) throws IOException {
        if (newRun) {
            task = new SimulationTask();
            createTaskFolder(XMLFilePath);
            newRun = false;
        }
        TargetDTO targetDTO = getTargetDataTransferObjectByName(name);
        //if null exception
        createTargetFileByName(name); //creating the target's file because it's the first time
        String info = ((SimulationTask) task).simulationStartInfo(targetDTO);
        saveTargetInfoToFile(info);
        return info;
    }

    /**
     * This method gets a target's name and creates a log file for it
     *
     * @param name String, the target's name.
     */
    private void createTargetFileByName(String name) {
        targetFilePath = (directoryPath + slash + name + ".log");

    }

    public int getSleepTime(int runTime) throws IOException {
        int info = ((SimulationTask) task).getSleepTime(runTime);
        saveTargetInfoToFile("The target slept for: " + makeMStoString(info));
        return info;
    }


    public String simulationRunAndResult(String targetName, long runTime, float success, float successWithWarnings) throws
            IOException {
        State state = ((SimulationTask) task).changeTargetState(success, successWithWarnings);
        //save info to target as well
        setFinishedState(targetName, state);
        g.getTargetByName(targetName).setTime(runTime);
        String targetChanges = getTargetChanges(targetName, state);
        String info = ((SimulationTask) task).simulationRunAndResult(targetChanges, state, runTime);
        saveTargetInfoToFile(info);
        return info;
    }

    private String getTargetChanges(String targetName, State state) {
        boolean mainTargetSucceed = (state == State.FINISHED_SUCCESS || state == State.FINISHED_WARNINGS);
        Set<String> targetChanges;
        if (mainTargetSucceed)
            targetChanges = getRunnableTargetsNamesFromFinishedTarget(targetName);
        else
            targetChanges = getSkippedTargetsNamesFromFailedTarget(targetName);
        return ((SimulationTask) task).getTargetChanges(mainTargetSucceed, targetChanges, targetName);

    }

    public static String makeMStoString(long time) {
        long millis = time % 1000;
        long second = (time / 1000) % 60;
        long minute = (time / (1000 * 60)) % 60;
        long hour = (time / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
    }

    public String getTotalRuntime() {
        newRun = true; //TEMPORARY, FIX!!
        return makeMStoString(task.getTotalRuntime());
    }

    /**
     * This method sets all failed (and skipped) targets to frozen. For starting the task again!
     */
    public void setAllFailedAndSkippedTargetsFrozen() {
        g.setAllFailedAndSkippedTargetsFrozen();
    }

    /**
     * This method sets all targets to frozen. For starting the task from scratch!
     */
    public void setAllTargetsFrozen() {
        g.setAllTargetsFrozen();
    }


    /**
     * This method gets a file name, and saves the current state of the graph to it.
     * Returns true or false if the state could be saved or not (the graph was empty)
     *
     * @param fileName String, the file's name.
     * @throws IOException
     */
    public void saveCurrentStateToFile(String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName + systemStateFileEnding))) {
            out.writeUTF(XMLFilePath.toString()); //filepath for simulations if no graph was loaded
            out.writeInt(g.getTargets().size()); //size of the graph
            out.writeObject(g);
        }
    }

    /**
     * This method gets a file name, and loads the previously saved state of the graph from it.
     *
     * @param fileName String, the file's name.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadCurrentStateFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName + systemStateFileEnding))) {
            XMLFilePath = Paths.get(in.readUTF());
            if (XMLFilePath.toString().contains("\\"))
                slash = "\\";
            else slash = "/";
            int size = in.readInt();
            for (int i = 0; i < size; i++) { //reading the size
                g.new Target(); //creating targets in g for the file to read into
            }
            g = (Graph) in.readObject();
        }
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
        return g.getSetOfAllAffectedTargetsByBond(name, bond);
    }

    /**
     * This method checks if the graph contains a cycle
     *
     * @return True if it contains a cycle, false if not
     */
    public boolean checkIfTheGraphContainsCycle() {
        for (String s : g.getTargets().keySet()) {
            Set<List<String>> temp = isTargetInCircleByName(s);
            if (!temp.isEmpty())
                return true;
        }
        return false;
    }

    /**
     * This method returns the max numbers of usable threads for the graph
     *
     * @return The max number of usable threads
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * This method returns the file name
     *
     * @return The file's name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * This method returns all the serial sets the graph has
     *
     * @return All the serial sets the graph has. Each map is a serial set with a name and a list of targets
     */
    public Map<String, Set<String>> getSerialSets() {
        return g.getSerialSets();
    }

    /**
     * This method gets a target name and returns all the serial sets the target belongs to.
     *
     * @param t The target's name
     * @return All serial sets it belongs to
     */
    public Map<String, Set<String>> getSerialSetsByTargetName(String t) {
        Map<String, Set<String>> res = new HashMap<>();
        for (String s : g.getSerialSets().keySet()) {
            if (g.getSerialSets().get(s).contains(t))
                res.put(s, g.getSerialSets().get(s));
        }
        return res;

    }

    void runSimulation(ArrayList<String> targets, int runTime, boolean randomRunTime, int success, int successWithWarnings, int threadsNum) {

    }

    //TODO progress bar - add counter to target when it progresses
}
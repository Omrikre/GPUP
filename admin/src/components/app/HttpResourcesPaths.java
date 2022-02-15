package components.app;

public class HttpResourcesPaths {

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/GPUP";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    // login
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    // logout
    public final static String LOGOUT_PAGE = FULL_SERVER_PATH + "/logout";
    // list of user
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    // graph
    public final static String GRAPH_LIST = FULL_SERVER_PATH + "/graphlist";
    public final static String GRAPH = FULL_SERVER_PATH + "/select/graph";
    // load xml
    public final static String LOAD_XML_FILE = FULL_SERVER_PATH + "/file/xml/load";
    // mission
    public final static String ADD_MISSION = FULL_SERVER_PATH + "/mission/add";
    public final static String MISSION_LIST = FULL_SERVER_PATH + "/missionlist";
    public final static String UPDATE_TARGET_IN_MISSION = FULL_SERVER_PATH + "/mission/update/target";
    public final static String CHANGE_STATUS_OF_MISSION = FULL_SERVER_PATH + "/mission/setstatus";
    /// worker
    public final static String CHANGE_WORKER_STATUS_IN_MISSION = FULL_SERVER_PATH + "/mission/worker/status";
    public final static String SEND_TARGET_TO_MISSION = FULL_SERVER_PATH + "/mission/worker/add/target";
    public final static String TARGET_COMPLETE_LIST = FULL_SERVER_PATH + "/worker/target/complete";

    // Chat
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";


}

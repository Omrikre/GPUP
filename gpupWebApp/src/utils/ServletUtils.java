package utils;

import Engine.GraphManager;
import Engine.Tasks.Task;
import Engine.Tasks.TaskManager;
import Engine.chat.ChatManager;
import jakarta.servlet.ServletContext;
import Engine.users.UserManager;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private static final String GRAPH_MANAGER_ATTRIBUTE_NAME = "graphManager";
    private static final String TASK_MANAGER_ATTRIBUTE_NAME = "taskManager";

    private static final Object userManagerLock = new Object();
    private static final Object chatManagerLock = new Object();
    private static final Object graphManagerLock = new Object();
    private static final Object taskManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {
        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static ChatManager getChatManager(ServletContext servletContext) {
        synchronized (chatManagerLock) {
            if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
            }
        }
        return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
    }

    public static GraphManager getGraphManager(ServletContext servletContext) {
        synchronized (graphManagerLock) {
            if (servletContext.getAttribute(GRAPH_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(GRAPH_MANAGER_ATTRIBUTE_NAME, new GraphManager());
            }
        }
        return (GraphManager) servletContext.getAttribute(GRAPH_MANAGER_ATTRIBUTE_NAME);
    }

    public static TaskManager getTaskManager(ServletContext servletContext) {
        synchronized (taskManagerLock) {
            if (servletContext.getAttribute(TASK_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(TASK_MANAGER_ATTRIBUTE_NAME, new TaskManager());
            }
        }
        return (TaskManager) servletContext.getAttribute(TASK_MANAGER_ATTRIBUTE_NAME);
    }
}

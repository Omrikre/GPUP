package Engine.users;

import java.util.*;

public class UserManager {
    public static class User {
        private String name;
        private int threads;

        public User(String name, int threads) {
            this.name = name;
            this.threads = threads;
        }
    }

    private final Map<String, User> usersMap;

    public UserManager() {
        usersMap = new HashMap<>();
    }

    public synchronized void addUser(String username, int threads) {
        usersMap.put(username, new User(username, threads));
    }

    public synchronized void removeUser(String username) {
        usersMap.remove(username);
    }

    public synchronized Map<String, User> getUsers() {
        return usersMap;
    }

    public boolean isUserExists(String username) {
        return usersMap.containsKey(username);
    }
}

package Engine.users;

import java.util.*;

public class UserManager {
    public static class User {
        private String name;
        private int threads;
        private String role;

        public User(String name, int threads, String role) {
            this.name = name;
            this.threads = threads;
            this.role=role;
        }

        public int getThreads() {
            return threads;
        }

        public String getName() {
            return name;
        }

        public String getRole(){
            return role;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", threads=" + threads +
                    ", role='" + role + '\'' +
                    '}';
        }
    }

    private final Map<String, User> usersMap;

    public UserManager() {
        usersMap = new HashMap<>();
    }

    public synchronized void addUser(String username, int threads, String role) {
        usersMap.put(username, new User(username, threads, role));
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

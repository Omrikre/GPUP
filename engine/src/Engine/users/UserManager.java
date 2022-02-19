package Engine.users;

import java.util.*;

public class UserManager {
    private final List<User> usersList;

    public UserManager() {
        usersList = new ArrayList<>();
    }

    public synchronized void addUser(String username, int threads, String role) {
        usersList.add(new User(username, threads, role));
    }

    private User findUserByName(String name) {
        for (User u : usersList) {
            if (u.getName().equals(name))
                return u;
        }
        return null;
    }

    public synchronized void removeUser(String username) {
        usersList.remove(findUserByName(username));
    }

    public synchronized List<User> getUsers() {
        return usersList;
    }

    public boolean isUserExists(String username) {
        return usersList.contains(findUserByName(username));
    }
}

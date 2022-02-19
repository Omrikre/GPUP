package Engine.users;

public class User {
    private String name;
    private int threads;
    private String role;

    public User(String name, int threads, String role) {
        this.name = name;
        this.threads = threads;
        this.role=role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public void setRole(String role) {
        this.role = role;
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
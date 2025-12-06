package team.databasenmysql.model;

public class User {
    private final String SSN;
    private final String name;
    private final String password;
    public User(String SSN,String name,String password) {
        this.name = name;
        this.password = password;
        this.SSN = SSN;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
    public String getSSN() {
        return SSN;
    }
}

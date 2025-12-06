package team.databasenmysql.model;

public class User {
    private final String SSN;
    public User(String SSN) {
        this.SSN = SSN;
    }
    public String getSSN() {
        return SSN;
    }
}

module team.databasenmysql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens team.databasenmysql to javafx.fxml;
    exports team.databasenmysql;
}
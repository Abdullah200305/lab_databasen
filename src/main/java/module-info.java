module team.databasenmysql {
    requires javafx.controls;
    requires javafx.fxml;


    opens team.databasenmysql to javafx.fxml;
    exports team.databasenmysql;
}
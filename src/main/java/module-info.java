module team.databasenmysql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;



    opens team.databasenmysql to javafx.fxml;
    opens team.databasenmysql.model to javafx.base;

    exports team.databasenmysql;
    exports team.databasenmysql.model;

}
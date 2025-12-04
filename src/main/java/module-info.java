module team.databasenmysql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;



    opens team.databasenmysql to javafx.fxml;
    opens team.databasenmysql.model to javafx.base;

    exports team.databasenmysql;
    exports team.databasenmysql.model;

}
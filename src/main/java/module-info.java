module team.databasenmysql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires mysql.connector.j;
    requires java.desktop;
    requires org.mongodb.bson;

    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;

    opens team.databasenmysql to javafx.fxml;
    opens team.databasenmysql.model to javafx.base;

    exports team.databasenmysql;
    exports team.databasenmysql.model;

}
module se.kth.anderslm.booksdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens se.kth.anderslm.booksdb to javafx.base;
    opens se.kth.anderslm.booksdb.model to javafx.base;
    exports se.kth.anderslm.booksdb;
}
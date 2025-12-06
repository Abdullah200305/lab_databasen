package team.databasenmysql;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import team.databasenmysql.model.*;
import team.databasenmysql.model.exceptions.InsertException;
import team.databasenmysql.view.BooksPane;
import team.databasenmysql.view.LoginPane;


import java.io.IOException;
import java.sql.SQLException;

import static javafx.scene.control.Alert.AlertType.ERROR;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        IBooksDb booksDb = new IBooksDbMockImpl(); // model
        booksDb.connect("bibliotek");// also creates a controller
        // Don't forget to connect to the db, somewhere...

        BooksPane booksPane = new BooksPane(booksDb);
        Scene booksScene = new Scene(booksPane, 800, 600);

        LoginPane loginPane = new LoginPane(booksDb);
        Scene loginScene = new Scene(loginPane, 400, 300);

        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();

        loginPane.setOnAuthenticated(() -> {
            primaryStage.setScene(booksScene);
            primaryStage.setTitle("Books Database Client");
        });

        loginPane.setOnGuest(() -> {
            primaryStage.setScene(booksScene);
            primaryStage.setTitle("Books Database Client");
        });

        // add an exit handler to the stage (X)
        primaryStage.setOnCloseRequest(event -> {
            try {
                booksDb.disconnect();
            } catch (Exception e) {
            }
        });


    }



    public static void main(String[] args) {
        launch();
    }
}
package team.databasenmysql;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mysql.cj.xdevapi.Client;
import com.mysql.cj.xdevapi.Session;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.bson.Document;
import team.databasenmysql.model.*;
import team.databasenmysql.view.BooksPane;

import java.io.IOException;
import java.sql.SQLException;

import static com.mongodb.client.model.Filters.*;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {

        ///  twst for mondb




        IBooksDb booksDb = new IBooksDbMockImpl();
       // model
        BooksPane booksPane = new BooksPane(booksDb);
      /*  booksDb.connect("bibliotek");// also creates a controller*/
        // Don't forget to connect to the db, somewhere...
        Scene scene = new Scene(booksPane, 800, 600);
        primaryStage.setTitle("Books Database Client");
        // add an exit handler to the stage (X)
        primaryStage.setOnCloseRequest(event -> {
            try {
                booksDb.disconnect();
            } catch (Exception e) {
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }





    public static void main(String[] args) {
        launch();
    }
}
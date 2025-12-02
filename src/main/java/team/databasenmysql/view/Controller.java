package team.databasenmysql.view;



import javafx.scene.control.Alert;
import team.databasenmysql.model.Book;
import team.databasenmysql.model.IBooksDb;
import team.databasenmysql.model.SearchMode;
import team.databasenmysql.model.exceptions.ConnectionException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.control.Alert.AlertType.*;

/**
 * The controller is responsible for handling user requests and update the view
 * (and in some cases the model).
 *
 * @author anderslm@kth.se
 */
public class Controller {

    private final BooksPane booksView; // view
    private final IBooksDb booksDb; // model

    public Controller(IBooksDb booksDb, BooksPane booksView) {
        this.booksDb = booksDb;
        this.booksView = booksView;
    }

    protected void onSearchSelected(String searchFor, SearchMode mode) {
        try {
            if (searchFor != null && searchFor.length() > 1) {
                List<Book> result = null;
                switch (mode) {
                    case Title:
                        result = booksDb.findBooksByTitle(searchFor);
                        break;
                    case ISBN:
                        result = booksDb.findBooksByIsbn(searchFor);
                        break;
                    case Author:
                        // ...
                        break;
                    default:
                        result= new ArrayList<>();
                }
                if (result == null || result.isEmpty()) {
                    booksView.showAlertAndWait(
                            "No results found.", INFORMATION);
                } else {
                    booksView.displayBooks(result);
                }
            } else {
                booksView.showAlertAndWait(
                        "Enter a search string!", WARNING);
            }
        } catch (Exception e) {
            booksView.showAlertAndWait("Database error.",ERROR);
        }
    }




    ///  by Abody
    protected void onclickConnection(String Db_name){
       try {
           if(booksDb.connect(Db_name)){
             /*  booksView.displayBooks();*/
           }

       }
       catch (ConnectionException e) {
            booksView.showAlertAndWait("Somthing wrong in connection!",ERROR);
       }
    }
    protected void onclickDisconnection(){
        try {
            booksDb.disconnect();
        }
        catch (ConnectionException e) {
            booksView.showAlertAndWait("Somthing wrong in disconnection!",ERROR);
        }
    }
    // TODO:
    // Add methods for all types of user interaction
}

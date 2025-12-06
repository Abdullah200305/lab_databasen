package team.databasenmysql.view;



import javafx.scene.control.*;
import team.databasenmysql.model.Authors;
import team.databasenmysql.model.Book;
import team.databasenmysql.model.IBooksDb;
import team.databasenmysql.model.SearchMode;
import team.databasenmysql.model.exceptions.ConnectionException;
import team.databasenmysql.model.exceptions.InsertException;
import team.databasenmysql.model.exceptions.SelectException;

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
                        result = booksDb.findBooksByAuthor(searchFor);
                        break;
                    case Genera:
                        result = booksDb.findBooksByGenre(searchFor);
                        break;
                    case Grade:
                        result = booksDb.findBooksByGrade(searchFor);
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
           booksDb.connect(Db_name);
      /*     if(booksDb.connect(Db_name)){
               ///  By Chefen
           // Lisa av books behövs för att mata in i displayBooks.
          *//*     List<Book> booksTitle = booksDb.findBooksByTitle("Dune");
               booksView.displayBooks(booksTitle);*//*

           }*/

       }
       catch (ConnectionException e) {
            booksView.showAlertAndWait("Somthing wrong in connection!",ERROR);
       }/* catch (SelectException e) {
           throw new RuntimeException(e);
       }*/
    }
    protected void onclickDisconnection(){
        try {
            booksDb.disconnect();
        }
        catch (ConnectionException e) {
            booksView.showAlertAndWait("Somthing wrong in disconnection!",ERROR);
        }
    }

    protected void onclickTitleSearch(){

    }

    protected void onclickISBNSearch(){

    }

    protected void onclickAuthorSearch(){

    }

    protected void onclickAddItem() throws SQLException {
        try {
            Book book = booksView.showAddBookDialog();
          /*  book.addAuthor(book.getAuthor());
            book.addGenre(book.getGenre());*/
            System.out.println(book);
            booksDb.InsertBook(book);
        }
        catch (InsertException e) {
            booksView.showAlertAndWait("Somthing wrong in Insert a book!",ERROR);
        }

    }
    protected void onclickRemoveItem(){
        String ISBN = booksView.showDeleteBookDialog();
        Book book = booksDb.DeleteBook(ISBN);

        if(book!= null){
            String text = String.format("Are you sure for delete this book ISBN:%s\n" +
                    "TITLE: %s\n",book.getIsbn(),book.getTitle());
            booksView.showAlertAndWait(text,ERROR);
        }
        else {
            booksView.showAlertAndWait("Somthing wrong in Insert a book!",ERROR);
            booksView.showAlertAndWait("Somthing wrong in Delete a book!",ERROR);
        }

    }

    protected void onclickUpdateItem(){
        try {
            UpdateChoice choiceValue = booksView.showUpdateChoiceDialog();


            List<Book> result = booksDb.findBooksByIsbn(choiceValue.getIsbn());


           List<String> oldValues = new ArrayList<>();
            switch (choiceValue.getMode()) {
                case Title:
                    oldValues.add(result.getFirst().getTitle());
                    break;
                case Author:
                    for (Authors authors : result.getFirst().getAuthors()){
                        oldValues.add(authors.getAuthorName());
                    }
                    break;
                case Genera:
                    oldValues.addAll(result.getFirst().getGenres());
                    break;
                case Grade:
                    oldValues.add(result.getFirst().getGrade().toString());
                    break;
                default:
                    result = null;
            }

            if (result == null || result.isEmpty()) {
                booksView.showAlertAndWait(
                        "No results found.", INFORMATION);
            } else {
               booksView.showUpdateBookDialog(choiceValue, oldValues);


                booksDb.UppdateBook(choiceValue, choiceValue.getNew_item(),choiceValue.getOld_item());
            }


        } catch (Exception e) {
            booksView.showAlertAndWait("Database error.",ERROR);
        }

    }
    protected void onclickShowInformation(Book book){
        System.out.println(book);

    }


    // TODO:
    // Add methods for all types of user interaction
}

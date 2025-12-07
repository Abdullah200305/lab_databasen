package team.databasenmysql.view;



import javafx.scene.control.*;
import team.databasenmysql.model.*;
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
    private final IUsersDb usersDb = null; // model
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
    protected boolean onclickConnection(String Db_name){
        try {
            booksDb.connect(Db_name);
            User user = booksView.showLoginUser();
            boolean isGuest = user.getName().equalsIgnoreCase("guest");
            boolean isValidUser = booksDb.CheckUser(user.getName(), user.getPassword()) != null;

            if (isValidUser) {
                // valid user
                booksView.displayNameUser(user.getName());
                return false;
            } else if (isGuest) {
                // guest
                booksView.displayNameUser(user.getName());
                return true;
            }
            // Unknown non-guest user → reject
            booksView.displayNameUser("****");
            booksDb.disconnect();
            return true;
        }
        catch (ConnectionException e) {
            booksView.showAlertAndWait("Somthing wrong in connection!",ERROR);
            return true;
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

    protected void onclickTitleSearch() throws SelectException {
        String title = booksView.showSearchTitle();
        if (title == null || title.isBlank()) {
            return;
        }
        booksView.displayBooks(booksDb.findBooksByTitle(title));
    }

    protected void onclickISBNSearch() throws SelectException {
        String Isbn = booksView.showSearchIsbn();
        if (Isbn == null || Isbn.isBlank()) {
            return;
        }
        booksView.displayBooks(booksDb.findBooksByIsbn(Isbn));
    }

    protected void onclickAuthorSearch() throws SelectException {
        String author = booksView.showSearchAuthor();
        if (author == null || author.isBlank()) {
            return;
        }
        booksView.displayBooks(booksDb.findBooksByAuthor(author));
    }

    protected void onclickGenreSearch() throws SelectException {
        String genre = booksView.showSearchGenre();
        if (genre == null || genre.isBlank()) {
            return;
        }
        booksView.displayBooks(booksDb.findBooksByGenre(genre));
    }

    protected void onclickGradeSearch() throws SelectException {
        String grade = booksView.showSearchGrade();
        if (grade == null || grade.isBlank()) {
            return;
        }
        booksView.displayBooks(booksDb.findBooksByGrade(grade));
    }






    protected void onclickAddItem() throws SQLException {
        try {
            Book book = booksView.showAddBookDialog(booksDb.bringAuthors());
            if (book == null) {
                return;
            }
            booksDb.InsertBook(book);
        }
        catch (InsertException e) {
            booksView.showAlertAndWait("Somthing wrong in Insert a book!",ERROR);
        }

    }
    protected void onclickRemoveItem(){
        String ISBN = booksView.showDeleteBookDialog();
        if (ISBN == null || ISBN.isBlank()) {
            return;
        }
        Book book = booksDb.DeleteBook(ISBN);

        if(book!= null){
            String text = String.format("Are you sure for delete this book ISBN:%s\n" +
                    "TITLE: %s\n",book.getIsbn(),book.getTitle());
            booksView.showAlertAndWait(text,ERROR);
        }
        else {
            booksView.showAlertAndWait("Somthing wrong in Insert a book!",ERROR);
        }

    }

    protected void onclickUpdateItem(){
        try {
            UpdateChoice choiceValue = booksView.showUpdateChoiceDialog();
            if (choiceValue == null) {
                return;
            }

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
               String update = booksView.showUpdateBookDialog(choiceValue, oldValues);
                if (update == null || update.isBlank()) {
                    return;
                }

               booksDb.UppdateBook(choiceValue, choiceValue.getNew_item(),choiceValue.getOld_item());
            }


        } catch (Exception e) {
            booksView.showAlertAndWait("Database error.",ERROR);
        }

    }
    protected void onclickShowInformation(Book book){
        booksView.showBookInformation(book);
    }





    protected void onclickReview(){
        try {
            UpdateChoice choiceValue = booksView.ReviewDialog();
            if (choiceValue == null) {
                return;
            }
            List<Book> result = booksDb.findBooksByIsbn(choiceValue.getIsbn());
            Review review = booksView.showReviewDialog();
            if (review == null) {
                return;
            }
            if(result.getFirst().getGrade()==null){
                result.getFirst().addReviews(review);
                booksDb.insertReview(review, choiceValue.getIsbn());
                System.out.println("user have not ");
            }
            else {
                booksView.showAlertAndWait("Grade is Already exsist!!!",WARNING);
                System.out.println("user have it ");
            }







//            List<String> oldValues = new ArrayList<>();
//            oldValues.add(result.getFirst().getGrade().toString());
//            booksDb.UppdateBook(choiceValue, choiceValue.getNew_item(),choiceValue.getOld_item());
        }
        catch (SelectException | InsertException e) {
            throw new RuntimeException(e);
        }
    }

}

package team.databasenmysql.view;



import javafx.application.Platform;
import javafx.scene.control.*;
import team.databasenmysql.model.*;
import team.databasenmysql.model.exceptions.ConnectionException;
import team.databasenmysql.model.exceptions.InsertException;
import team.databasenmysql.model.exceptions.SelectException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static javafx.scene.control.Alert.AlertType.*;

public class Controller {

    private final BooksPane booksView; // view layer (JavaFX)
    private final IBooksDb booksDb; // model layer (database or mock implementation)

    /**
     * Creates a controller for the BooksPane UI and the database model.
     *
     * @param booksDb   the database layer implementation
     * @param booksView the UI view
     */
    public Controller(IBooksDb booksDb, BooksPane booksView) {
        this.booksDb = booksDb;
        this.booksView = booksView;
    }

    /**
     * Handles generic search requests when the user selects a search mode.
     * Validates the input, performs the proper database query, and displays results.
     *
     * @param searchFor the search string provided by the user
     * @param mode      the search mode (Title, ISBN, Author, etc.)
     */
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


    /**
     * Handles the user login process when connecting to a database.
     * Shows a login dialog, validates the user, and updates the view.
     *
     * @return true if login should continue as guest, false if logged in successfully
     */
    public void onclickConnection(String dbName) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    booksDb.connect(dbName);
                    final User[] userHolder = new User[1];
                    final CountDownLatch latch = new CountDownLatch(1);

                    Platform.runLater(() -> {
                        try {
                            userHolder[0] = booksView.showLoginUser();
                        } finally {
                            latch.countDown();
                        }
                    });

                    latch.await();
                    User user = userHolder[0];
                    if (user == null) {
                        Platform.runLater(() ->
                                booksView.showAlertAndWait("Login was cancelled.", ERROR)
                        );
                        return;
                    }


                    boolean isGuest = user.getName().equalsIgnoreCase("guest");
                    boolean isValidUser = booksDb.CheckUser(user.getName(), user.getPassword()) != null;

                    if (isValidUser) {
                        Platform.runLater(() -> {
                            booksView.displayNameUser(user.getName());
                            booksView.setGuest(false);
                            booksView.updateManageMenuAccess();
                        });
                    }
                    else if (isGuest) {
                        Platform.runLater(() -> {
                            booksView.displayNameUser("guest");
                            booksView.setGuest(true);
                            booksView.updateManageMenuAccess();
                        });
                    }
                    else {
                        Platform.runLater(() -> {
                            booksView.displayNameUser("****");
                            booksView.setGuest(true);
                            booksView.updateManageMenuAccess();
                        });

                        booksDb.disconnect();
                    }
                }
                catch (Exception e) {
                    Platform.runLater(() ->
                            booksView.showAlertAndWait("Something wrong in connection!\n" + e.getMessage(), ERROR)
                    );
                }
            }
        };
        new Thread(task).start();
    }


    /**
     * Disconnects from the database safely and shows an alert if something fails.
     */

    protected void onclickDisconnection(){
        try {
            booksView.getChildren().clear();
            booksView.init(this);
            booksDb.disconnect();
        }
        catch (ConnectionException e) {
            booksView.showAlertAndWait("Somthing wrong in disconnection!",ERROR);
        }
    }

    /**
     * Searches for books by title via a dialog input.
     */
    protected void onclickTitleSearch() throws SelectException {
        String title = booksView.showSearchTitle();
        if (title == null || title.isBlank()) {
            return;
        }
        booksView.displayBooks(booksDb.findBooksByTitle(title));
    }

    /**
     * Searches for books by ISBN via a dialog input.
     */
    protected void onclickISBNSearch() throws SelectException {
        String Isbn = booksView.showSearchIsbn();
        if (Isbn == null || Isbn.isBlank()) {
            return;
        }
        booksView.displayBooks(booksDb.findBooksByIsbn(Isbn));
    }

    /**
     * Searches for books by author name via a dialog input.
     */
    protected void onclickAuthorSearch() throws SelectException {
        String author = booksView.showSearchAuthor();
        if (author == null || author.isBlank()) {
            return;
        }
        booksView.displayBooks(booksDb.findBooksByAuthor(author));
    }

    /**
     * Searches for books by genre via a dialog input.
     */
    protected void onclickGenreSearch() throws SelectException {
        String genre = booksView.showSearchGenre();
        if (genre == null || genre.isBlank()) {
            return;
        }
        booksView.displayBooks(booksDb.findBooksByGenre(genre));
    }
    /**
     * Searches for books by grade via a dialog input.
     */

    protected void onclickGradeSearch() throws SelectException {
        String grade = booksView.showSearchGrade();
        if (grade == null || grade.isBlank()) {
            return;
        }
        booksView.displayBooks(booksDb.findBooksByGrade(grade));
    }





    /**
     * Handles adding a new book.
     * Shows a dialog, validates the result, and inserts the book into the database.
     */
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

    /**
     * Handles removal of a book by showing dialog, confirming deletion,
     * and sending the delete request.
     */
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

    /**
     * Handles updating book data.
     * First asks what ISBN to modify and what field to update.
     * Then retrieves old values and lets the user enter new ones.
     * Finally, sends update request to the database.
     */
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
                    oldValues.add(result.getFirst().getReviews().getFirst().toString());
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

    /**
     * Displays detailed information about a selected book.
     *
     * @param book the book to show
     */
    protected void onclickShowInformation(Book book){

        booksView.showBookInformation(book);
    }

    /**
     * Handles creation of a review for a book.
     * Ensures no duplicate review exists, then inserts the new review.
     */

    protected void onclickReview(){
        try {
            UpdateChoice choiceValue = booksView.ReviewDialog();
            if (choiceValue == null) {
                return;
            }
            List<Book> result = booksDb.findBooksByIsbn(choiceValue.getIsbn());
            if(result.getFirst().getReviews().isEmpty()){
                Review review = booksView.showReviewDialog();
                if (review == null) {
                    return;
                }
                result.getFirst().addReviews(review);
                booksDb.insertReview(review, choiceValue.getIsbn());
                System.out.println("user have not ");
            }
            else {
                booksView.showAlertAndWait("Grade is Already exsist!!!",WARNING);
                System.out.println("user have it ");
            }
        }
        catch (SelectException | InsertException e) {
            throw new RuntimeException(e);
        }
    }

}

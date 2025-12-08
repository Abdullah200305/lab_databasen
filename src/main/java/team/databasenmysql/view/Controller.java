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
   /* protected boolean onclickConnection(String Db_name){

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

            booksView.displayNameUser("****");
            booksDb.disconnect();
            return true;
        }
        catch (ConnectionException e) {
            booksView.showAlertAndWait("Somthing wrong in connection!",ERROR);
            return true;
       }
    }*/



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

            booksView.displayNameUser("****");
            booksDb.disconnect();
            return true;
        }
        catch (ConnectionException e) {
            booksView.showAlertAndWait("Somthing wrong in connection!",ERROR);
            return true;
        }
    }


    /**
     * Disconnects from the database safely and shows an alert if something fails.
     */
    protected void onclickDisconnection(){
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    booksDb.disconnect();
                    Platform.runLater(() -> {
                        booksView.displayNameUser("****");
                    });

                } catch (ConnectionException e) {
                    Platform.runLater(() -> {
                        booksView.showAlertAndWait("Somthing wrong in disconnection!", ERROR);
                    });

                }

            }
        };
        new Thread(thread).start();
    }

    /**
     * Searches for books by title via a dialog input.
     */



    protected void onclickTitleSearch() throws SelectException {
        Runnable thread = new Runnable() {
            String title = booksView.showSearchTitle();
            @Override
            public void run() {
                try {
                    booksDb.findBooksByTitle(title);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        booksView.showAlertAndWait("Something wrong in TITLE!!",ERROR);
                    });
                }
            }
        };
        new Thread(thread).start();
    }

    /**
     * Searches for books by ISBN via a dialog input.
     */
    protected void onclickISBNSearch() throws SelectException {
        Runnable thread = new Runnable() {
            String Isbn = booksView.showSearchIsbn();
            @Override
            public void run() {
                try {
                    booksDb.findBooksByIsbn(Isbn);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        booksView.showAlertAndWait("Something wrong in ISBN!!",ERROR);
                    });
                }
            }
        };
        new Thread(thread).start();
    }

    /**
     * Searches for books by author name via a dialog input.
     */
    protected void onclickAuthorSearch() throws SelectException {
        Runnable thread = new Runnable() {
            String author = booksView.showSearchAuthor();
            @Override
            public void run() {
                try {
                    booksDb.findBooksByAuthor(author);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        booksView.showAlertAndWait("Something wrong in Author!!",ERROR);
                    });
                }
            }
        };
        new Thread(thread).start();
    }

    /**
     * Searches for books by genre via a dialog input.
     */
    protected void onclickGenreSearch() throws SelectException {
        Runnable thread = new Runnable() {
            String genre = booksView.showSearchGenre();
            @Override
            public void run() {
                try {
                    booksDb.findBooksByGenre(genre);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        booksView.showAlertAndWait("Something wrong in Genre!!",ERROR);
                    });
                }
            }
        };
        new Thread(thread).start();
    }
    /**
     * Searches for books by grade via a dialog input.
     */

    protected void onclickGradeSearch() throws SelectException {
        Runnable thread = new Runnable() {
            String grade = booksView.showSearchGrade();
            @Override
            public void run() {
                try {
                    booksDb.findBooksByGrade(grade);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        booksView.showAlertAndWait("Something wrong in Grade!!",ERROR);
                    });
                }
            }
        };
        new Thread(thread).start();
    }





    /**
     * Handles adding a new book.
     * Shows a dialog, validates the result, and inserts the book into the database.
     */
    protected void onclickAddItem() throws SQLException {
        Book book = booksView.showAddBookDialog(booksDb.bringAuthors());
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    booksDb.InsertBook(book);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        booksView.showAlertAndWait("Somthing wrong in Insert a book!",ERROR);
                    });
                }
            }
        };
        new Thread(thread).start();
    }

    /**
     * Handles removal of a book by showing dialog, confirming deletion,
     * and sending the delete request.
     */
    protected void onclickRemoveItem(){
        String ISBN = booksView.showDeleteBookDialog();
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    if (ISBN == null || ISBN.isBlank()) {
                        return;
                    }
                    Book book = booksDb.DeleteBook(ISBN);
                    Platform.runLater(()->{
                        String text = String.format("Are you sure for delete this book ISBN:%s\n" +
                                "TITLE: %s\n",book.getIsbn(),book.getTitle());
                                 booksView.showAlertAndWait(text,ERROR);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        booksView.showAlertAndWait("Somthing wrong in Insert a book!",ERROR);
                    });
                }
            }
        };
        new Thread(thread).start();
    }

    /**
     * Handles updating book data.
     * First asks what ISBN to modify and what field to update.
     * Then retrieves old values and lets the user enter new ones.
     * Finally, sends update request to the database.
     */
    protected void onclickUpdateItem() {
        // Visa dialog på JavaFX-tråden
        UpdateChoice choiceValue = booksView.showUpdateChoiceDialog();
        if (choiceValue == null) return; // avbruten

        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    List<Book> result = booksDb.findBooksByIsbn(choiceValue.getIsbn());
                    if (result == null || result.isEmpty()) {
                        Platform.runLater(() -> booksView.showAlertAndWait(
                                "No books found for the given ISBN!", Alert.AlertType.INFORMATION));
                        return;
                    }

                    Book book = result.get(0);
                    List<String> oldValues = new ArrayList<>();
                    switch (choiceValue.getMode()) {
                        case Title:
                            oldValues.add(book.getTitle());
                            break;
                        case Author:
                            for (Authors author : book.getAuthors()) {
                                oldValues.add(author.getAuthorName());
                            }
                            break;
                        case Genera:
                            oldValues.addAll(book.getGenres());
                            break;
                        case Grade:
                            if (!book.getReviews().isEmpty())
                                oldValues.add(book.getReviews().get(0).toString());
                            break;
                        default:
                            break;
                    }
                    Platform.runLater(() -> {
                        String newValue = booksView.showUpdateBookDialog(choiceValue, oldValues);
                        System.out.println(newValue);
                        if (newValue == null || newValue.isBlank()) return;

                        new Thread(() -> {
                            try {
                                booksDb.UppdateBook(choiceValue, newValue, oldValues.get(0));
                                Platform.runLater(() ->
                                        booksView.showAlertAndWait("Book updated successfully!", Alert.AlertType.INFORMATION));
                            } catch (Exception e) {
                                Platform.runLater(() ->
                                        booksView.showAlertAndWait("Something went wrong in Update!!", Alert.AlertType.ERROR));
                            }
                        }).start();
                    });

                } catch (Exception e) {
                    Platform.runLater(() ->
                            booksView.showAlertAndWait("Something went wrong in Update!!", Alert.AlertType.ERROR));
                    e.printStackTrace();
                }
            }
        };

        new Thread(thread).start();
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
        UpdateChoice choiceValue = booksView.ReviewDialog();
        if (choiceValue == null) {return;}

        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        List<Book> result = booksDb.findBooksByIsbn(choiceValue.getIsbn());
                        Platform.runLater(() -> {
                            if(result.getFirst().getReviews().isEmpty()){
                                Review review = booksView.showReviewDialog();
                                if (review == null) {
                                    return;
                                }
                                result.getFirst().addReviews(review);
                                try {
                                    booksDb.insertReview(review, choiceValue.getIsbn());
                                } catch (InsertException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println("user have not ");
                            }
                            else {
                                booksView.showAlertAndWait("Grade is Already exsist!!!",WARNING);
                                System.out.println("user have it ");
                            }
                        });
                    }
                    catch (SelectException e) {
                        throw new RuntimeException(e);
                    }

                } catch (Exception e) {
                    Platform.runLater(() -> {booksView.showAlertAndWait("Wrrrong",ERROR); });
                }
            }
        };
        new Thread(thread).start();
    }

}

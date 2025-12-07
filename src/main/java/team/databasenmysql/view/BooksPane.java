package team.databasenmysql.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.databasenmysql.model.*;
import javafx.scene.control.ButtonBar.ButtonData;
import team.databasenmysql.model.exceptions.SelectException;

import java.security.PublicKey;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static javafx.scene.control.Alert.AlertType.WARNING;


/**
 * The main pane for the view, extending VBox and including the menus. An
 * internal BorderPane holds the TableView for books and a search utility.
 *
 * @author anderslm@kth.se
 */
public class BooksPane extends VBox {

    private TableView<Book> booksTable;
    private ObservableList<Book> booksInTable; // the data backing the table view

    private ComboBox<SearchMode> searchModeBox;
    private TextField searchField;
    private Button searchButton;
    private Label userNameLabel;

    private MenuBar menuBar;

    public BooksPane(IBooksDb booksDb) {
        final Controller controller = new Controller(booksDb, this);
        this.init(controller);
    }

 /*   public BooksPane(team.databasenmysql.model.IBooksDb booksDb) {
    }*/

    /**
     * Display a new set of books, e.g. from a database select, in the
     * booksTable table view.
     *
     * @param books the books to display
     */
    public void displayBooks(List<Book> books) {
        booksInTable.clear();
        booksInTable.addAll(books);
    }
    public void displayNameUser(String name){
        userNameLabel.setText(name);
    }

    /**
     * Notify user on input error or exceptions.
     *
     * @param msg  the message
     * @param type types: INFORMATION, WARNING et c.
     */
    protected void showAlertAndWait(String msg, Alert.AlertType type) {
        // types: INFORMATION, WARNING et c.
        Alert alert = new Alert(type, msg);
        alert.showAndWait();
    }

    public User showLoginUser(){
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Login or be a guest");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        ButtonType LoginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        ButtonType GuestButtonType = new ButtonType("Guest");
        dialog.getDialogPane().getButtonTypes().addAll(LoginButtonType, GuestButtonType);

        VBox box =  new VBox(20);
        box.getChildren().addAll(
                new Label("Title"), usernameField,
                new Label("password"),passwordField
        );

        dialog.getDialogPane().setContent(box);
        dialog.setResultConverter(button -> {
            if (button == LoginButtonType) {
                return new User(usernameField.getText(), passwordField.getText());
            } else{
                return new User("guest", "1234");
            }
        });
        return dialog.showAndWait().orElse(null);
    }

    public void showBookInformation(Book book){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Book Information");
        alert.setHeaderText("Information om boken: " + book.getTitle());

        // Bygg upp texten som ska visas i alert
        StringBuilder info = new StringBuilder();
        info.append("Title: ").append(book.getTitle()).append("\n");
        info.append("ISBN: ").append(book.getIsbn()).append("\n");
        info.append("Published: ").append(book.getPublished()).append("\n");
        // Bytes senare till en loop för att hämta flera grade
        info.append("Grade: ").append(book.getGrade()).append("\n");

        info.append("Authors:\n");
        for (Authors authors: book.getAuthors()){
            info.append("- ").append(authors.toString()).append("\n");
        }

        info.append("Genre:\n");
        for (String genre : book.getGenres()) {
            info.append("- ").append(genre).append("\n");
        }

        alert.setContentText(info.toString());

        alert.showAndWait();
    }

    public String showSearchTitle(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Search title");
        dialog.setHeaderText("Search for value included in title: ");

        ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        TextField textField = new TextField();
        textField.setPromptText("Title");

        VBox box =  new VBox(10);
        box.getChildren().addAll(new Label("Title"), textField);

        dialog.getDialogPane().setContent(box);
        dialog.setResultConverter(button -> {
            if (button == searchButtonType) {
                return textField.getText();
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }


    public String showSearchIsbn(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Search ISBN");
        dialog.setHeaderText("Search with isbn: ");

        ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        TextField textField = new TextField();
        textField.setPromptText("ISBN");

        VBox box =  new VBox(10);
        box.getChildren().addAll(new Label("ISBN"), textField);

        dialog.getDialogPane().setContent(box);
        dialog.setResultConverter(button -> {
            if (button == searchButtonType) {
                return textField.getText();
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    public String showSearchAuthor(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Search Author");
        dialog.setHeaderText("Search book with author: ");

        ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        TextField textField = new TextField();
        textField.setPromptText("Author");

        VBox box =  new VBox(10);
        box.getChildren().addAll(new Label("Author"), textField);

        dialog.getDialogPane().setContent(box);
        dialog.setResultConverter(button -> {
            if (button == searchButtonType) {
                return textField.getText();
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    public String showSearchGenre(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Search Genre");
        dialog.setHeaderText("Search book with genre: ");

        ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        TextField textField = new TextField();
        textField.setPromptText("Genre");

        VBox box =  new VBox(10);
        box.getChildren().addAll(new Label("Genre"), textField);

        dialog.getDialogPane().setContent(box);
        dialog.setResultConverter(button -> {
            if (button == searchButtonType) {
                return textField.getText();
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    public String showSearchGrade(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Search Grade");
        dialog.setHeaderText("Search book with grade: ");

        ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        TextField textField = new TextField();
        textField.setPromptText("Grade");

        VBox box =  new VBox(10);
        box.getChildren().addAll(new Label("Grade"), textField);

        dialog.getDialogPane().setContent(box);
        dialog.setResultConverter(button -> {
            if (button == searchButtonType) {
                return textField.getText();
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }


    public String showUpdateBookDialog(UpdateChoice choiceType, List<String> oldValues){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Update " + choiceType.getMode().toString());
        dialog.setHeaderText("Update Value: ");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);
        TextField textField = new TextField();
        VBox box = new VBox(10);
        box.getChildren().add( new Label("ISBN:"));
        ComboBox<String> ShowList = new ComboBox<>();

        if(oldValues.size()>1){

            for (String item : oldValues){
                ShowList.getItems().add(item);
            }
            ShowList.setOnAction(event ->{
                if(!ShowList.getValue().isEmpty()){
                    textField.setText(ShowList.getValue());
                }});
            box.getChildren().add(ShowList);
           /* textField.setPromptText("Choose from list...");*/
        }
        else
        {
            textField.setText(oldValues.getFirst());
        }

        box.getChildren().add(textField);
        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(button -> {
            if (button == updateButtonType) {
                choiceType.setNew_item(textField.getText());
                choiceType.setOld_item(ShowList.getValue());
                return textField.getText();
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }


    public Review showReviewDialog(){
        Dialog<Review> dialog = new Dialog<>();
        dialog.setTitle("Review");
        dialog.setHeaderText("Review book");

        ButtonType ReviewButtonType = new ButtonType("Review", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ReviewButtonType, ButtonType.CANCEL);

        TextField SummaryField = new TextField();
        SummaryField.setPromptText("ISBN");

        ComboBox<Grade> GradeBox = new ComboBox<>();
        GradeBox.getItems().addAll(Grade.AA, Grade.BB, Grade.CC, Grade.DD, Grade.FF);

        DatePicker publishedPicker = new DatePicker();

        VBox box = new VBox(10,
                new Label("Grade:"), GradeBox,
                new Label("Summary"), SummaryField,
                new Label("Date:"), publishedPicker
        );

        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(dialogButton ->{
            if (dialogButton == ReviewButtonType){
                java.sql.Date sqlDate = null;
                if (publishedPicker.getValue() != null){
                    sqlDate = java.sql.Date.valueOf(publishedPicker.getValue());
                }
                return new Review(GradeBox.getValue(), SummaryField.getText(), sqlDate);
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    public UpdateChoice ReviewDialog(){
        Dialog<UpdateChoice> dialog = new Dialog<>();
        dialog.setTitle("Review" );
        dialog.setHeaderText("Add Review Value: ");

        ButtonType okButtonType = new ButtonType("Review", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        TextField ISBNField = new TextField();
        ISBNField.setPromptText("ISBN");


        VBox box = new VBox(10, new Label("ISBN:"), ISBNField);

        dialog.getDialogPane().setContent(box);
        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                return new UpdateChoice(ISBNField.getText(),SearchMode.Grade);
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }






    public UpdateChoice showUpdateChoiceDialog(){
        Dialog<UpdateChoice> dialog = new Dialog<>();
        dialog.setTitle("Update book");
        dialog.setHeaderText("Choose update options:");

        ButtonType okButtonType = new ButtonType("Choose", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        TextField ISBNField = new TextField();
        ISBNField.setPromptText("ISBN");

        ComboBox<SearchMode> choiceBox = new ComboBox<>();
        choiceBox.getItems().addAll(SearchMode.Title, SearchMode.Author, SearchMode.Grade, SearchMode.Genera);


        VBox box = new VBox(10,
                new Label("ISBN:"), ISBNField,
                new Label("Update field:"), choiceBox
        );

        dialog.getDialogPane().setContent(box);
        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                return new UpdateChoice(ISBNField.getText(), choiceBox.getValue());
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }
    public Book showAddBookDialog(List<Authors> authors){
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add new book");
        dialog.setHeaderText("Fill in book information:");


        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField ISBNField = new TextField();
        ISBNField.setPromptText("ISBN");

        ComboBox<Authors> authorBox = new ComboBox<>();
        authorBox.getItems().addAll(authors);

        authorBox.setPromptText("Author");

        DatePicker publishedPicker = new DatePicker();


        ComboBox<String> generaBox = new ComboBox<>();
        generaBox.getItems().addAll("Drama", "Comedy", "Action", "Science fiction", "Horror", "Fantasy", "Romance", "Mystery");
        generaBox.setPromptText("Genera");

        VBox box = new VBox(10);
        box.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("ISBN"), ISBNField,
                new Label("Author"), authorBox,
                new Label("Published Date:"), publishedPicker,
                new Label("Genera"), generaBox
        );

        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(dialogButton ->{
            if (dialogButton == addButtonType){
                java.sql.Date sqlDate = null;
                if (publishedPicker.getValue() != null){
                    sqlDate = java.sql.Date.valueOf(publishedPicker.getValue());
                }
                Book book =  new Book(ISBNField.getText(), titleField.getText(), sqlDate);
                book.addAuthor(authorBox.getValue());
                book.addGenre(generaBox.getValue());
                return book;
            }
            return null;
        });

        // IMPORTANT: return the result from the dialog
        return dialog.showAndWait().orElse(null);
    }

    public String showDeleteBookDialog(){


        TextField IsbnField = new TextField();
        IsbnField.setPromptText("ISBN");
        ButtonType deleteButtonType = new ButtonType("Delete",ButtonData.OK_DONE);

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete a book");
        dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.CANCEL);


        VBox box = new VBox(20);
        Label info = new Label("Here you can delete a book by using the ISBN of the book.");
        box.getChildren().addAll(info,IsbnField);
        dialog.getDialogPane().setContent(box);
        dialog.setResultConverter(dialogButton ->{
            if(dialogButton == deleteButtonType){
               if(IsbnField.getText()!=null){return IsbnField.getText();};
            };
            return null;});
        return dialog.showAndWait().orElse(null);
    }







    void init(Controller controller) {
        booksInTable = FXCollections.observableArrayList();
        // init views and event handlers
        initBooksTable(controller);
        initSearchView(controller);
        initMenus(controller);
        iniTUser_name();

        FlowPane bottomPane = new FlowPane();
        bottomPane.setHgap(10);
        bottomPane.setPadding(new Insets(10, 10, 10, 10));
        bottomPane.getChildren().addAll(searchModeBox, searchField, searchButton,new Label("Name: "),userNameLabel);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(booksTable);
        mainPane.setBottom(bottomPane);
        mainPane.setPadding(new Insets(10, 10, 10, 10));

        this.getChildren().addAll(menuBar, mainPane);
        VBox.setVgrow(mainPane, Priority.ALWAYS);
    }


    private void initBooksTable(Controller controller) {
        booksTable = new TableView<>();
        booksTable.setEditable(false); // don't allow user updates (yet)
        booksTable.setPlaceholder(new Label("No rows to display"));

        // define columns
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        TableColumn<Book, Date> publishedCol = new TableColumn<>("Published");
        booksTable.getColumns().addAll(titleCol, isbnCol, publishedCol);
        // give title column some extra space
        titleCol.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.5));

        // define how to fill data for each cell, 
        // get values from Book properties
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishedCol.setCellValueFactory(new PropertyValueFactory<>("published"));

        // associate the table view with the data
        booksTable.setItems(booksInTable);

        booksTable.setOnMouseClicked(event ->{
            controller.onclickShowInformation(booksTable.getSelectionModel().getSelectedItem());}
        );
    }

    private void iniTUser_name(){
        userNameLabel = new Label("****");
    }


    private void initSearchView(Controller controller) {
        searchField = new TextField();
        searchField.setPromptText("Search for...");
        searchModeBox = new ComboBox<>();
        searchModeBox.getItems().addAll(SearchMode.values());
        searchModeBox.setValue(SearchMode.Title);
        searchButton = new Button("Search");

        // event handling (dispatch to controller)
        searchButton.setOnAction(event -> {
            String searchFor = searchField.getText();
            SearchMode mode = searchModeBox.getValue();
            controller.onSearchSelected(searchFor, mode);
        });
    }

    private void initMenus(Controller controller) {

        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem connectItem = new MenuItem("Connect to Db");
        MenuItem disconnectItem = new MenuItem("Disconnect");
        fileMenu.getItems().addAll(exitItem, connectItem, disconnectItem);

        Menu searchMenu = new Menu("Search");
        MenuItem titleItem = new MenuItem("Title");
        MenuItem isbnItem = new MenuItem("ISBN");
        MenuItem authorItem = new MenuItem("Author");
        MenuItem GenreItem = new MenuItem("Genre");
        MenuItem GradeItem = new MenuItem("Grade");
        searchMenu.getItems().addAll(titleItem, isbnItem, authorItem, GenreItem, GradeItem);

        Menu manageMenu = new Menu("Manage");
        MenuItem addItem = new MenuItem("Add");
        MenuItem removeItem = new MenuItem("Remove");
        MenuItem updateItem = new MenuItem("Update");
        MenuItem ReviewItem = new MenuItem("Review");
        manageMenu.getItems().addAll(addItem, removeItem, updateItem,ReviewItem);
        manageMenu.setDisable(true);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, searchMenu, manageMenu);

        // TODO: add event handlers ...
        ///  by abody
        connectItem.setOnAction(event -> {
            manageMenu.setDisable(controller.onclickConnection("bibliotek"));
        });
        disconnectItem.setOnAction(event -> {
            controller.onclickDisconnection();
        });

        titleItem.setOnAction(event -> {
            try {
                controller.onclickTitleSearch();
            } catch (SelectException e) {
                throw new RuntimeException(e);
            }
        });

        isbnItem.setOnAction(event -> {
            try {
                controller.onclickISBNSearch();
            } catch (SelectException e) {
                throw new RuntimeException(e);
            }
        });

        authorItem.setOnAction(event -> {
            try {
                controller.onclickAuthorSearch();
            } catch (SelectException e) {
                throw new RuntimeException(e);
            }
        });

        GenreItem.setOnAction(event -> {
            try {
                controller.onclickGenreSearch();
            } catch (SelectException e) {
                throw new RuntimeException(e);
            }
        });

        GradeItem.setOnAction(event -> {
            try {
                controller.onclickGradeSearch();
            } catch (SelectException e) {
                throw new RuntimeException(e);
            }
        });


        addItem.setOnAction(event -> {
            if (!userNameLabel.getText().contains("guest")) {
                try {
                    controller.onclickAddItem();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                showAlertAndWait("You dont have premsions!!",WARNING);
            }

        });

        removeItem.setOnAction(event -> {
            if (!userNameLabel.getText().contains("guest")) {
                controller.onclickRemoveItem();
            }
            else {
                showAlertAndWait("You dont have premsions!!",WARNING);
            }

        });

        updateItem.setOnAction(event -> {
            if (!userNameLabel.getText().contains("guest")) {
                    controller.onclickUpdateItem();}
            else {
                showAlertAndWait("You dont have premsions!!",WARNING);
            }

        });
        ReviewItem.setOnAction(event->{
            if (!userNameLabel.getText().contains("guest")) {
                controller.onclickReview();}
            else {
                showAlertAndWait("You dont have premsions!!",WARNING);
            }
        });
    }
}

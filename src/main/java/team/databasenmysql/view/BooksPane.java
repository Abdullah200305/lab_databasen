package team.databasenmysql.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import team.databasenmysql.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;



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

    public String showUpdateBookDialog(UpdateChoice choiceType, String oldValue){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Update " + choiceType.getMode().toString());
        dialog.setHeaderText("Update Value: ");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextField textField = new TextField();
        textField.setText(oldValue);

        VBox box = new VBox(10,
                new Label("ISBN:"), textField
        );

        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(button -> {
            if (button == updateButtonType) {
                return textField.getText();
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);    }

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

    public Book showAddBookDialog(){
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
        authorBox.getItems().addAll(
                new Authors(1,"Frank Herbert", new Date(1990, 1, 1)),
                new Authors(2,"William Shakespeare", new Date(1890, 12, 15)),
                new Authors(3,"Jane Austen", new Date(1750, 4, 12)),
                new Authors(4,"Franz Kafka", new Date(1378, 3, 22)),
                new Authors(5,"Agatha Christie", new Date(2005, 8, 9)),
                new Authors(6,"Mark Twain", new Date(1778, 7, 25)));
        authorBox.setPromptText("Author");

        DatePicker publishedPicker = new DatePicker();

        ComboBox<Grade> gradeBox = new ComboBox<>();
        gradeBox.getItems().addAll(Grade.A, Grade.B, Grade.C, Grade.D, Grade.E, Grade.F);
        gradeBox.setPromptText("Grade");

        ComboBox<String> generaBox = new ComboBox<>();
        generaBox.getItems().addAll("Drama", "Comedy", "Action", "Science fiction", "Horror", "Fantasy", "Romance", "Mystery");
        generaBox.setPromptText("Genera");

        VBox box = new VBox(10);
        box.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("ISBN"), ISBNField,
                new Label("Author"), authorBox,
                new Label("Published Date:"), publishedPicker,
                new Label("Grade"), gradeBox,
                new Label("Genera"), generaBox
        );

        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(dialogButton ->{
            if (dialogButton == addButtonType){
                java.sql.Date sqlDate = null;
                if (publishedPicker.getValue() != null){
                    sqlDate = java.sql.Date.valueOf(publishedPicker.getValue());
                }
                return new Book(ISBNField.getText(), titleField.getText(), authorBox.getValue(), sqlDate, gradeBox.getValue(), generaBox.getValue());
            }
            return null;
        });

        // IMPORTANT: return the result from the dialog
        return dialog.showAndWait().orElse(null);
    }

    public String showDeleteBookDialog(){
        String ISBN=null;
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete a book");
        VBox box = new VBox();
        Label text = new Label("Type ISBN of book: ");
        TextField IsbnField = new TextField("ISBN");
        box.getChildren().addAll(text,IsbnField);
        dialog.getDialogPane().setContent(text);
        dialog.showAndWait();
        return ISBN;
    }





    void init(Controller controller) {

        booksInTable = FXCollections.observableArrayList();

        // init views and event handlers
        initBooksTable();
        initSearchView(controller);
        initMenus(controller);

        FlowPane bottomPane = new FlowPane();
        bottomPane.setHgap(10);
        bottomPane.setPadding(new Insets(10, 10, 10, 10));
        bottomPane.getChildren().addAll(searchModeBox, searchField, searchButton);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(booksTable);
        mainPane.setBottom(bottomPane);
        mainPane.setPadding(new Insets(10, 10, 10, 10));

        this.getChildren().addAll(menuBar, mainPane);
        VBox.setVgrow(mainPane, Priority.ALWAYS);
    }

    private void initBooksTable() {
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
        searchMenu.getItems().addAll(titleItem, isbnItem, authorItem);

        Menu manageMenu = new Menu("Manage");
        MenuItem addItem = new MenuItem("Add");
        MenuItem removeItem = new MenuItem("Remove");
        MenuItem updateItem = new MenuItem("Update");
        manageMenu.getItems().addAll(addItem, removeItem, updateItem);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, searchMenu, manageMenu);

        // TODO: add event handlers ...
        ///  by abody
        connectItem.setOnAction(event -> {
            controller.onclickConnection("bibliotek");
        });
        disconnectItem.setOnAction(event -> {
            controller.onclickDisconnection();
        });

        titleItem.setOnAction(event -> {
            controller.onclickTitleSearch();
        });

        isbnItem.setOnAction(event -> {
            controller.onclickISBNSearch();
        });

        authorItem.setOnAction(event -> {
            controller.onclickAuthorSearch();
        });

        addItem.setOnAction(event -> {
            try {
                controller.onclickAddItem();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        removeItem.setOnAction(event -> {
            controller.onclickRemoveItem();
        });

        updateItem.setOnAction(event -> {
            controller.onclickUpdateItem();
        });
    }
}

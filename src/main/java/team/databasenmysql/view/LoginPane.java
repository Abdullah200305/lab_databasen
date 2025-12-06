package team.databasenmysql.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import team.databasenmysql.model.IBooksDb;

public class LoginPane extends VBox {
    private TextField username;
    private PasswordField password;
    private Button loginButton;
    private Button guestButton;

    private Runnable onAuthenticated;
    private Runnable onGuest;

    public LoginPane(IBooksDb booksDb){
        setSpacing(15);
        setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Välkommen! Logga in eller fortsätt som gäst:");

        username = new TextField();
        username.setPromptText("Username");

        password = new PasswordField();
        password.setPromptText("Password");

        loginButton = new Button("Login");
        guestButton = new Button("Guest");

        loginButton.setOnAction(e ->{
            if ("admin".equals(username.getText()) && "1234".equals(password.getText())){
                if (onAuthenticated != null) onAuthenticated.run();
            } else {
                javafx.scene.control.Alert alert =
                        new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Wrong username or password!");
                alert.showAndWait();
            }
        });

        guestButton.setOnAction(e->{
            if (onGuest != null) onGuest.run();
        });

        getChildren().addAll(welcomeLabel, username, password, loginButton, guestButton);
    }

    public void setOnAuthenticated(Runnable r) {
        this.onAuthenticated = r;
    }

    public void setOnGuest(Runnable r) {
        this.onGuest = r;
    }

}

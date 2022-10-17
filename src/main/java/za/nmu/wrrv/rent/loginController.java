package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class loginController
{
    @FXML
    protected TextField user;
    @FXML
    protected PasswordField pass;
    @FXML
    protected Button cancel;
    @FXML
    protected Button login;

    protected static User thisUser;

    @FXML
    protected void keyClicked(KeyEvent keyEvent) throws SQLException
    {
        switch(keyEvent.getCode())
        {
            case ESCAPE -> closeStage();
            case ENTER -> onLogin();
        }
    }
    @FXML
    protected void buttonClicked(MouseEvent mouseEvent) throws SQLException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();

            switch(buttonId)
            {
                case "cancel" -> closeStage();
                case "login" -> onLogin();
            }
        }
    }

    private void onLogin() throws SQLException
    {
        Alert alert;

        String username = user.getText();
        String password = pass.getText();
        thisUser = User.getUser(username);

        if(thisUser == null)
        {
            alert = new Alert(Alert.AlertType.ERROR);
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
            alert.setTitle("Failed");
            alert.setHeaderText("Login failed");

            if(username.isEmpty())
                alert.setContentText("Username is empty");
            else
                alert.setContentText("Username is incorrect");
        }
        else
        {
            if(password.equals(thisUser.getPassword()))
            {
                alert = new Alert(Alert.AlertType.INFORMATION);
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
                alert.setTitle("Success");
                alert.setHeaderText("Login successful");
                baseController.isLoggedOn = true;
            }
            else
            {
                alert = new Alert(Alert.AlertType.ERROR);
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
                alert.setTitle("Failed");
                alert.setHeaderText("Login failed");

                if(password.isEmpty())
                    alert.setContentText("Password is empty");
                else
                    alert.setContentText("Password is incorrect");
            }
        }
        user.clear();
        pass.clear();
        alert.showAndWait();
        if(baseController.isLoggedOn)
            closeStage();
    }
    private void closeStage()
    {
        Stage thisStage = (Stage) user.getScene().getWindow();
        thisStage.close();
    }
}

package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.sql.SQLException;

public class loginController extends baseController
{
    @FXML
    private TextField user;
    @FXML
    private PasswordField pass;

    protected static User thisUser;

    @FXML
    protected void onCancel(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            BorderPane fakeMain = (BorderPane) user.getScene().getWindow().getScene().getRoot();
            fakeMain.setCenter(null);
        }
    }

    @FXML
    protected void onLogin(MouseEvent mouseEvent) throws SQLException
    {
        String username = user.getText();
        String password = pass.getText();
        thisUser = User.getUser(username);

        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Alert alert;

            if(thisUser == null)
            {
                alert = new Alert(Alert.AlertType.ERROR);
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
                    alert.setTitle("Success");
                    alert.setHeaderText("Login successful");
                    isLoggedOn = true;
                }
                else
                {
                    alert = new Alert(Alert.AlertType.ERROR);
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
            if(isLoggedOn)
            {
                BorderPane fakeMain = (BorderPane) user.getScene().getWindow().getScene().getRoot();
                fakeMain.setCenter(newCenter("clerkMenu"));
            }
        }
    }
}

package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.SQLException;

public class loginController
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
            closeStage();
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
                    baseController.isLoggedOn = true;
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
            if(baseController.isLoggedOn)
            {
                if(thisUser.isAdmin())
                    baseController.userLoggedOn = "adminMenu";
                else
                    baseController.userLoggedOn = "clerkMenu";

                baseController.nextScene(baseController.userLoggedOn);

                closeStage();
            }
        }
    }
    private void closeStage()
    {
        Stage thisStage = (Stage) user.getScene().getWindow();
        thisStage.close();
    }
}

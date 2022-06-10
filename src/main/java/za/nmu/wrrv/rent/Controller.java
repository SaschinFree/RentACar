package za.nmu.wrrv.rent;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    private SceneLoader thisScene = new SceneLoader();

    @FXML
    private BorderPane main;
    @FXML
    private TextField user;
    @FXML
    private PasswordField pass;
    @FXML
    private Button logout;

    private static boolean isLoggedOn = false;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

    }

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
    protected void onLogin(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Alert alert;
            if(user.getText().equals("user") && pass.getText().equals("pass"))
            {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Login successful");

                isLoggedOn = true;
                BorderPane fakeMain = (BorderPane) user.getScene().getWindow().getScene().getRoot();
                fakeMain.setCenter(newCenter("clerkMenu"));
            }
            else
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setHeaderText("Login failed");

                if(user.getText().isEmpty() | pass.getText().isEmpty())
                {
                    if(user.getText().isEmpty())
                        alert.setContentText("Username is empty");
                    else
                    {
                        if(pass.getText().isEmpty())
                            alert.setContentText("Password is empty");
                        else
                            alert.setContentText("Username and Password is empty");
                    }
                }
                else
                    alert.setContentText("Username and/or password is incorrect");
                user.clear();
                pass.clear();
            }
            alert.showAndWait();
        }
    }

    @FXML
    protected void logoutClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Alert alert;
            if(isLoggedOn)
            {
                main.setCenter(null);
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setHeaderText("Logout successful");
                alert.showAndWait();
                isLoggedOn = false;
            }
            else
            {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("User is not logged in");
                alert.showAndWait();
            }
        }
    }
    @FXML
    protected void loginClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            if(!isLoggedOn)
            {
                main.setCenter(newCenter("login"));
            }
        }
    }

    private Parent newCenter(String centerName)
    {
        Scene scene = thisScene.getPage(centerName);

        return scene.getRoot();
    }
}
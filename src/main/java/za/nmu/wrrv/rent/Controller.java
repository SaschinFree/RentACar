package za.nmu.wrrv.rent;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class Controller
{
    private Stage thisStage;
    private FXMLLoader thisLoader;

    @FXML
    private BorderPane main;
    @FXML
    private TextField user;
    @FXML
    private PasswordField pass;

    private boolean isLoggedOn = false;


    @FXML
    protected void onCancel(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            user.getScene().getWindow().hide();
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

                user.getScene().getWindow().hide();
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
                Pane pane = (Pane) main.getCenter();
                pane.getChildren().removeAll();
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
    protected void loginClicked(MouseEvent mouseEvent) throws Exception
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            if(!isLoggedOn)
            {
                login();
                thisStage.show();
                isLoggedOn = true;
            }
        }
    }


    private Stage login() throws IOException
    {
        thisStage = new Stage();

        thisLoader = new FXMLLoader(RentACar.class.getResource("login.fxml"));

        Scene loginScene = new Scene(thisLoader.load());

        thisStage.setScene(loginScene);
        thisStage.setTitle("Login");
        thisStage.initModality(Modality.APPLICATION_MODAL);
        thisStage.initStyle(StageStyle.UTILITY);
        thisStage.initOwner(main.getScene().getWindow());

        return thisStage;
    }
}
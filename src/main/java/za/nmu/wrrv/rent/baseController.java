package za.nmu.wrrv.rent;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class baseController
{
    protected static final SceneLoader thisScene = new SceneLoader();
    @FXML
    protected BorderPane main;
    @FXML
    protected Button logout;

    protected static boolean isLoggedOn = false;

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
    protected void loginClicked(MouseEvent mouseEvent) throws IOException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            if(!isLoggedOn)
            {
                FXMLLoader loginLoader = new FXMLLoader(RentACar.class.getResource("login.fxml"));
                Scene loginScene = new Scene(loginLoader.load());
                Stage loginStage = new Stage();

                loginStage.setScene(loginScene);
                loginStage.setTitle("Login");
                loginStage.setResizable(false);
                loginStage.initModality(Modality.WINDOW_MODAL);
                loginStage.initOwner(RentACar.mainStage);

                loginStage.show();
            }
            else
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("User is already logged in");
                alert.showAndWait();
            }
        }
    }
}
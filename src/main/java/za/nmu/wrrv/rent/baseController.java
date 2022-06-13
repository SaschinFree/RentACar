package za.nmu.wrrv.rent;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class baseController implements Initializable
{
    private final SceneLoader thisScene = new SceneLoader();

    @FXML
    protected BorderPane main;
    @FXML
    protected Button logout;

    protected static boolean isLoggedOn = false;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

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

    protected Parent newCenter(String centerName)
    {
        Scene scene = thisScene.getPage(centerName);

        return scene.getRoot();
    }
}
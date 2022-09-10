package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class clerkController implements Initializable
{
    @FXML
    protected Label user;
    @FXML
    protected Button manageClients;
    @FXML
    protected Button manageBookings;
    @FXML
    protected Button dispatchVehicle;
    @FXML
    protected Button returnVehicle;
    @FXML
    protected Button manageVehicles;
    @FXML
    protected Button manageSettings;
    @FXML
    protected Button generateReport;
    @FXML
    protected Button payClient;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        if(loginController.thisUser.isAdmin())
            user.setText("Admin");
        else
            user.setText("Clerk");
    }
    @FXML
    protected void buttonClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();

            nextScene(buttonId);
        }
    }

    private void nextScene(String sceneName)
    {
        BorderPane fakeMain = (BorderPane) user.getScene().getRoot();
        fakeMain.setCenter(baseController.thisScene.getPage(sceneName).getRoot());
    }
}

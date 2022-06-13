package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class clerkController extends baseController
{
    @FXML
    private Button manageClients;

    @FXML
    protected void manageClientsClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            nextScene("manageClients");
        }
    }

    @FXML
    protected void manageBookingsClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            nextScene("manageBookings");
        }
    }

    @FXML
    protected void sendVehicleClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            nextScene("sendVehicle");
        }
    }

    @FXML
    protected void returnVehicleClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            nextScene("returnVehicle");
        }
    }

    @FXML
    protected void manageVehiclesClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            nextScene("manageVehicles");
        }
    }

    @FXML
    protected void manageSettingsClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            nextScene("manageSettings");
        }
    }

    @FXML
    protected void generateReportClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            nextScene("generateReport");
        }
    }

    @FXML
    protected void payClientClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            nextScene("payClient");
        }
    }

    private void nextScene(String sceneName)
    {
        BorderPane fakeMain = (BorderPane) manageClients.getScene().getWindow().getScene().getRoot();
        fakeMain.setCenter(newCenter(sceneName));
    }
}

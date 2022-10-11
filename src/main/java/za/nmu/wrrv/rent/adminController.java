package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class adminController
{
    @FXML
    protected Button manageClients;
    @FXML
    protected Button manageVehicles;
    @FXML
    protected Button manageBookings;
    @FXML
    protected Button managePayments;
    @FXML
    protected Button manageSettings;
    @FXML
    protected Button adminReport;

    @FXML
    protected void buttonClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();

            baseController.nextScene(buttonId);
        }
    }
}

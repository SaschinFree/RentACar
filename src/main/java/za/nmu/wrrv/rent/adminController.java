package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class adminController implements Initializable
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

    protected static boolean alertShown = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        setupTooltips();

        setupAlert();
    }

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
    @FXML
    protected void buttonHover(MouseEvent mouseEvent)
    {
        Button thisButton = (Button) mouseEvent.getSource();

        if(thisButton.isHover())
            thisButton.setStyle("-fx-background-color: #8D918D");
        else
            thisButton.setStyle("-fx-background-color: #3B2F2F");
    }

    private void setupTooltips()
    {
        manageClients.setTooltip(new Tooltip("View Clients"));
        manageVehicles.setTooltip(new Tooltip("View Vehicles"));
        manageBookings.setTooltip(new Tooltip("View Bookings"));
        managePayments.setTooltip(new Tooltip("Manage Payments"));
        manageSettings.setTooltip(new Tooltip("Manage Settings"));
        adminReport.setTooltip(new Tooltip("Generate Report"));
    }

    private void setupAlert()
    {
        if(!alertShown)
        {
            List<Client> moneyOwed = baseController.clients.stream().filter(client -> client.isActive() && client.getMoneyOwed() > 0).toList();

            if(moneyOwed.size() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
                alert.setHeaderText("Clients To Be Paid");
                String message = "";

                for(Client client : moneyOwed)
                {
                    message = message.concat("R" + client.getMoneyOwed() + " to " +
                            client.getFirstName() + " " + client.getSurname() + "\n");
                }

                alert.setContentText(message);
                alert.show();
            }
            alertShown = true;
        }
    }
}

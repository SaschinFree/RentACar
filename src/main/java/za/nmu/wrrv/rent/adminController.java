package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

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
        if(!alertShown)
        {
            List<Client> moneyOwed = baseController.clients.stream().filter(client -> client.isActive() && client.getMoneyOwed() > 0).toList();

            if(moneyOwed.size() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Clients To Be Paid");
                String message = "";

                for(Client client : moneyOwed)
                {
                    message = message.concat("R" + client.getMoneyOwed() + " to " +
                            client.getFirstName() + " " + client.getSurname() + "\n");
                }

                alert.setContentText(message);
                alert.showAndWait();
            }
            alertShown = true;
        }
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
}

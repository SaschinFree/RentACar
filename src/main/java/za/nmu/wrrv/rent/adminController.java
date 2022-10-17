package za.nmu.wrrv.rent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class adminController implements Initializable, EventHandler<Event>
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
        setupMnemonics();

        setupAlert();
    }
    @Override
    public void handle(Event event)
    {
        Button thisButton = (Button) event.getSource();
        String buttonId = thisButton.getId();

        baseController.nextScene(buttonId);
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

    private void setupMnemonics()
    {
        manageClients.setMnemonicParsing(true);
        manageVehicles.setMnemonicParsing(true);
        manageBookings.setMnemonicParsing(true);
        managePayments.setMnemonicParsing(true);
        manageSettings.setMnemonicParsing(true);
        adminReport.setMnemonicParsing(true);

        manageClients.setOnAction(this::handle);
        manageVehicles.setOnAction(this::handle);
        manageBookings.setOnAction(this::handle);
        managePayments.setOnAction(this::handle);
        manageSettings.setOnAction(this::handle);
        adminReport.setOnAction(this::handle);

        manageClients.setTooltip(new Tooltip("Alt+C"));
        manageVehicles.setTooltip(new Tooltip("Alt+V"));
        manageBookings.setTooltip(new Tooltip("Alt+B"));
        managePayments.setTooltip(new Tooltip("Alt+P"));
        manageSettings.setTooltip(new Tooltip("Alt+S"));
        adminReport.setTooltip(new Tooltip("Alt+G"));
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
                alert.showAndWait();
            }
            alertShown = true;
        }
    }
}

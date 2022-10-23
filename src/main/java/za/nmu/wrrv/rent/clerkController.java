package za.nmu.wrrv.rent;

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
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class clerkController implements Initializable
{
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
    protected Button clerkReport;

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
        manageClients.setTooltip(new Tooltip("Manage Clients"));
        manageVehicles.setTooltip(new Tooltip("Manage Vehicles"));
        manageBookings.setTooltip(new Tooltip("Manage Bookings"));
        dispatchVehicle.setTooltip(new Tooltip("Dispatch Vehicle"));
        returnVehicle.setTooltip(new Tooltip("Return Vehicle"));
        clerkReport.setTooltip(new Tooltip("Generate Report"));
    }

    private void setupAlert()
    {
        if(!alertShown)
        {
            List<Booking> dispatchToday = baseController.bookings.stream().filter(booking -> booking.isActive() && booking.getStartDate().equals(Date.valueOf(LocalDate.now())) && booking.isIsBeingRented().equals("No")).toList();
            List<Booking> returnToday = baseController.bookings.stream().filter(booking -> booking.isActive() && booking.getEndDate().equals(Date.valueOf(LocalDate.now())) && booking.isIsBeingRented().equals("Yes")).toList();

            if(dispatchToday.size() > 0 || returnToday.size() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
                alert.setHeaderText("Vehicles To Be Dispatched/Returned Today");
                String message = "Dispatch: \n";

                for(Booking booking : dispatchToday)
                {
                    Client thisClient = baseController.clients.stream().filter(client -> client.isActive() && client.getClientNumber() == booking.getClientNumber()).toList().get(0);
                    Vehicle thisVehicle = baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getVehicleRegistration().equals(booking.getVehicleRegistration())).toList().get(0);

                    message = message.concat("\t" + thisVehicle.getVehicleRegistration() + ": \t" +
                            thisVehicle.getMake() + " " +thisVehicle.getModel() + " to " +
                            thisClient.getFirstName() + " " + thisClient.getSurname() + "\n");
                }

                message = message.concat("Return: \n");

                for(Booking booking : returnToday)
                {
                    Client thisClient = baseController.clients.stream().filter(client -> client.isActive() && client.getClientNumber() == booking.getClientNumber()).toList().get(0);
                    Vehicle thisVehicle = baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getVehicleRegistration().equals(booking.getVehicleRegistration())).toList().get(0);

                    message = message.concat("\t" + thisVehicle.getVehicleRegistration() + ": \t" +
                            thisVehicle.getMake() + " " +thisVehicle.getModel() + " by " +
                            thisClient.getFirstName() + " " + thisClient.getSurname() + "\n");
                }

                alert.setContentText(message);
                alert.show();
            }
            alertShown = true;
        }
    }
}

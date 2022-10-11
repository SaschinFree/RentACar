package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

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
        if(!alertShown)
        {
            List<Booking> dispatchToday = baseController.bookings.stream().filter(booking -> booking.isActive() && booking.getStartDate().equals(Date.valueOf(LocalDate.now())) && booking.isIsBeingRented().equals("No")).toList();
            List<Booking> returnToday = baseController.bookings.stream().filter(booking -> booking.isActive() && booking.getEndDate().equals(Date.valueOf(LocalDate.now())) && booking.isIsBeingRented().equals("Yes")).toList();

            if(dispatchToday.size() > 0 || returnToday.size() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
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

package za.nmu.wrrv.rent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class clerkController implements Initializable,EventHandler<Event>
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
        dispatchVehicle.setMnemonicParsing(true);
        returnVehicle.setMnemonicParsing(true);
        clerkReport.setMnemonicParsing(true);

        manageClients.setOnAction(this::handle);
        manageVehicles.setOnAction(this::handle);
        manageBookings.setOnAction(this::handle);
        dispatchVehicle.setOnAction(this::handle);
        returnVehicle.setOnAction(this::handle);
        clerkReport.setOnAction(this::handle);

        manageClients.setTooltip(new Tooltip("Alt+C"));
        manageVehicles.setTooltip(new Tooltip("Alt+V"));
        manageBookings.setTooltip(new Tooltip("Alt+B"));
        dispatchVehicle.setTooltip(new Tooltip("Alt+D"));
        returnVehicle.setTooltip(new Tooltip("Alt+R"));
        clerkReport.setTooltip(new Tooltip("Alt+G"));
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
}

package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class clerkController
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
    protected Button generateReport;

    static
    {
        try
        {
            baseController.clients = Client.getClients();
            baseController.vehicles = Vehicle.getVehicles();
            baseController.bookings = Booking.getBookings();
            baseController.settings = Settings.getSettings();
            baseController.payments = Payment.getPayments();
        } catch (SQLException e)
        {
            e.printStackTrace();
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

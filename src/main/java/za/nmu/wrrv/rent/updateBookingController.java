package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class updateBookingController implements Initializable
{
    @FXML
    protected Label bookingNumber;
    @FXML
    protected Label clientNumber;
    @FXML
    protected Label vehicleRegistration;
    @FXML
    protected Label startDate;
    @FXML
    protected Label endDate;
    @FXML
    protected Label cost;
    @FXML
    protected CheckBox isPaid;
    @FXML
    protected CheckBox isCancel;
    @FXML
    protected Button cancel;
    @FXML
    protected Button updateBooking;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        bookingNumber.textProperty().bind(manageBookingsController.thisBooking.bookingNumberProperty().asString());
        clientNumber.textProperty().bind(manageBookingsController.thisBooking.clientNumberProperty().asString());
        vehicleRegistration.textProperty().bind(manageBookingsController.thisBooking.vehicleRegistrationProperty());

        startDate.setText(manageBookingsController.thisBooking.getStartDate().toString());
        endDate.setText(manageBookingsController.thisBooking.getEndDate().toString());

        cost.textProperty().bind(manageBookingsController.thisBooking.costProperty().asString());

        isPaid.setSelected(manageBookingsController.thisBooking.isHasPaid().equals("Yes"));
    }

    @FXML
    protected void buttonClicked(MouseEvent mouseEvent) throws SQLException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();

            switch(buttonId)
            {
                case "cancel" -> closeStage();
                case "updateBooking" -> onUpdate();
            }
        }
    }

    private void onUpdate() throws SQLException
    {
        if(isCancel.selectedProperty().getValue())
        {
            String sql = "UPDATE Booking SET active = No WHERE bookingNumber = \'" + manageBookingsController.thisBooking.getBookingNumber() + "\'";
            RentACar.statement.executeUpdate(sql);

            manageBookingsController.thisBooking.setActive(false);
            baseController.bookings.removeAll(manageBookingsController.thisBooking);
        }
        else
        {
            String hasPaid = "No";
            if(isPaid.selectedProperty().getValue())
                hasPaid = "Yes";

            String sql = "UPDATE Booking SET hasPaid = " + hasPaid + " WHERE bookingNumber = \'" + manageBookingsController.thisBooking.getBookingNumber() + "\'";
            RentACar.statement.executeUpdate(sql);

            manageBookingsController.thisBooking.setHasPaid(hasPaid);
        }

        closeStage();
    }
    private void closeStage()
    {
        Stage thisStage = (Stage) cancel.getScene().getWindow();
        thisStage.close();
    }
}

package za.nmu.wrrv.rent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class returnVehicleController implements Initializable
{
    @FXML
    protected TableColumn<Booking, String> registrationNumber;
    @FXML
    protected TableColumn<Booking, Date> bookingStart;
    @FXML
    protected TableColumn<Booking, Date> bookingEnd;
    @FXML
    protected TextField searchQuery;
    @FXML
    protected TableView<Booking> filteredTable;
    @FXML
    protected Button search;
    @FXML
    protected Button back;
    @FXML
    protected Button returnVehicle;

    protected static ObservableList<Booking> filteredBookings = FXCollections.observableArrayList();

    static
    {
        for(Booking thisBooking : baseController.bookings)
        {
            if(thisBooking.isActive() & thisBooking.getEndDate().before(Date.valueOf(LocalDate.now())) & thisBooking.isIsBeingRented())
                filteredBookings.add(thisBooking);
        }
    }

    protected static Booking thisBooking;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        returnVehicle.setVisible(false);

        filteredTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        registrationNumber.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        bookingStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        bookingEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        filteredTable.setItems(filteredBookings);
    }

    @FXML
    protected void bookingSelected(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            thisBooking = filteredTable.getSelectionModel().getSelectedItem();
            if(thisBooking != null)
                returnVehicle.setVisible(true);
        }
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
                case "search" -> onSearch();
                case "dispatchVehicle" -> onReturn();
                case "back" -> baseController.nextScene(baseController.userLoggedOn);
            }
        }
    }
    private void onSearch()
    {
        //searchQuery
    }
    private void onReturn() throws SQLException
    {
        String dispatch = "UPDATE Booking SET isBeingRented = No WHERE vehicleRegistration = \'" + thisBooking.getVehicleRegistration() + "\'";
        RentACar.statement.executeUpdate(dispatch);

        thisBooking.setIsBeingRented(false);
        thisBooking.setActive(false);
    }
}

package za.nmu.wrrv.rent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class dispatchVehicleController implements Initializable
{
    @FXML
    protected ChoiceBox<String> searchFilter;
    @FXML
    protected TextField searchQuery;
    @FXML
    protected Button search;
    @FXML
    protected TableColumn<Booking, String> registrationNumber;
    @FXML
    protected TableColumn<Booking, Date> bookingStart;
    @FXML
    protected TableColumn<Booking, Date> bookingEnd;
    @FXML
    protected TableView<Booking> filteredTable;
    @FXML
    protected Button back;
    @FXML
    protected Button dispatchVehicle;

    protected static ObservableList<Booking> filteredBookings = FXCollections.observableArrayList();

    protected static Booking thisBooking;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            filteredBookings = Booking.searchQuery("startDate", String.valueOf(LocalDate.now()), "AND isBeingRented = No AND hasPaid = Yes");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        searchFilter.getItems().addAll(
                "vehicleRegistration",
                "startDate",
                "endDate");

        searchFilter.setValue("vehicleRegistration");

        searchFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, stringSingleSelectionModel, t1) ->
        {
            searchQuery.clear();

            switch(searchFilter.getSelectionModel().getSelectedItem())
            {
                case "vehicleRegistration" -> searchQuery.setPromptText("ABC123 EC or CUSTOM MP");
                case "startDate", "endDate" -> searchQuery.setPromptText("YYYY/MM/DD");
            }
        });

        dispatchVehicle.setVisible(false);

        filteredTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        registrationNumber.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
        bookingStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        bookingEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        filteredTable.setItems(filteredBookings);
        search.setTooltip(baseController.searchTip);
    }

    @FXML
    protected void bookingSelected(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            thisBooking = filteredTable.getSelectionModel().getSelectedItem();
            if(thisBooking != null)
                dispatchVehicle.setVisible(true);
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
                case "dispatchVehicle" -> onDispatch();
                case "back" -> baseController.nextScene(baseController.userLoggedOn);
            }
        }
    }
    private void onSearch() throws SQLException
    {
        if(searchQuery.getText().isEmpty())
            filteredTable.setItems(filteredBookings);
        else
        {
            ObservableList<Booking> filteredList = Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText(), "AND isBeingRented = No");
            filteredTable.setItems(filteredList);
        }
    }
    private void onDispatch() throws SQLException
    {
        String dispatch = "UPDATE Booking SET isBeingRented = Yes WHERE vehicleRegistration = \'" + thisBooking.getVehicleRegistration() + "\' AND startDate = \'" + thisBooking.getStartDate() + "\' AND endDate = \'" + thisBooking.getEndDate() + "\'";
        RentACar.statement.executeUpdate(dispatch);

        for(Booking booking : baseController.bookings)
        {
            if(booking.getBookingNumber() == thisBooking.getBookingNumber())
            {
                booking.setIsBeingRented("Yes");
                break;
            }
        }
        filteredBookings.removeAll(thisBooking);

        ObservableList<Vehicle> clientVehicle = Vehicle.searchQuery("vehicleRegistration", String.valueOf(thisBooking.getVehicleRegistration()), "");
        Vehicle thisVehicle = clientVehicle.get(0);

        String updateClient = "UPDATE Client SET moneyOwed = \'" + thisBooking.getOwnerCommission() + "\' WHERE clientNumber = \'" + thisVehicle.getClientNumber() + "\'";
        RentACar.statement.executeUpdate(updateClient);

        for(Client thisClient : baseController.clients)
        {
            if(thisClient.getClientNumber() == thisVehicle.getClientNumber())
            {
                thisClient.setMoneyOwed(thisClient.getMoneyOwed() + thisBooking.getOwnerCommission());
                break;
            }
        }
    }
}

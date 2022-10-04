package za.nmu.wrrv.rent;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import java.time.Duration;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class returnVehicleController implements Initializable
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
    protected Button returnVehicle;

    protected static ObservableList<Booking> filteredBookings = FXCollections.observableArrayList();

    protected static Booking thisBooking;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            filteredBookings = Booking.searchQuery("isBeingRented", "Yes", "");
        } catch (SQLException e)
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
                case "vehicleRegistration" -> searchQuery.setPromptText("ABC123 EC or CUSTOM MP etc");
                case "startDate", "endDate" -> searchQuery.setPromptText("YYYY/MM/DD");
            }
        });

        returnVehicle.setVisible(false);

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
                case "returnVehicle" -> onReturn();
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
            ObservableList<Booking> filteredList = Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText(), "AND isBeingRented = Yes");
            filteredTable.setItems(filteredList);
        }
    }
    private void onReturn() throws SQLException
    {
        String returnVehicle = "UPDATE Booking SET active = No AND isBeingRented = No WHERE vehicleRegistration = \'" + thisBooking.getVehicleRegistration() + "\'";
        RentACar.statement.executeUpdate(returnVehicle);

        if(Date.valueOf(LocalDate.now()).after(thisBooking.getEndDate()))
        {
            Duration difference = Duration.between(thisBooking.getEndDate().toLocalDate().atStartOfDay(), LocalDate.now().atStartOfDay());
            double days = Double.parseDouble(String.valueOf(difference.toDays()));

            double flatRate = 0;
            Settings thisSetting = Settings.getSetting("Daily Rental Cost");

            if(thisSetting != null)
                flatRate = thisSetting.getSettingValue();

            ObservableList<Vehicle> thisVehicle = Vehicle.searchQuery("vehicleRegistration", thisBooking.getVehicleRegistration(), "");
            Vehicle vehicle = thisVehicle.get(0);

            double costMulti = vehicle.getCostMultiplier();

            double extraMoneyOwed = flatRate*days*costMulti;

            String updateClient = "UPDATE Client SET moneyOwed = moneyOwed + " + extraMoneyOwed + " WHERE clientNumber = \'" + vehicle.getClientNumber() + "\' AND active = Yes";
            RentACar.statement.executeUpdate(updateClient);

            ObservableList<Client> thisClient = Client.searchQuery("clientNumber", String.valueOf(vehicle.getClientNumber()), "");
            Client updatedClient = thisClient.get(0);

            Client placeHolder = manageClientsController.thisClient;

            manageClientsController.thisClient = updatedClient;

            manageClientsController.thisClient.setMoneyOwed(manageClientsController.thisClient.getMoneyOwed() + extraMoneyOwed);

            manageClientsController.thisClient = placeHolder;
        }

        thisBooking.setIsBeingRented("No");
        thisBooking.setActive(false);

        filteredBookings.removeAll(thisBooking);

        for(Booking booking : baseController.bookings)
        {
            if(booking.getBookingNumber() == thisBooking.getBookingNumber())
            {
                booking.setActive(false);
                baseController.bookings.removeAll(booking);
                break;
            }
        }
    }
}

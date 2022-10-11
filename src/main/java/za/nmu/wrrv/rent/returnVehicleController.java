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
                "None",
                "vehicleRegistration",
                "startDate",
                "endDate");

        searchFilter.setValue("None");

        searchFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, stringSingleSelectionModel, t1) ->
        {
            searchQuery.clear();

            switch(searchFilter.getSelectionModel().getSelectedItem())
            {
                case "None" -> searchQuery.setPromptText("Search");
                case "vehicleRegistration" -> searchQuery.setPromptText("ABC123 EC or CUSTOM MP etc");
                case "startDate", "endDate" -> searchQuery.setPromptText("YYYY-MM-DD");
            }
        });

        returnVehicle.setVisible(false);

        filteredTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        registrationNumber.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
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
                case "returnVehicle" -> onReturn();
                case "back" -> baseController.nextScene(baseController.userLoggedOn);
            }
        }
    }
    private void onSearch() throws SQLException
    {
        if(searchFilter.getSelectionModel().getSelectedItem().equals("None"))
            filteredTable.setItems(filteredBookings);
        else
        {
            ObservableList<Booking> filteredList = Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText(), "AND isBeingRented = Yes");
            filteredTable.setItems(filteredList);
        }
    }
    private void onReturn() throws SQLException
    {
        String returnV = "UPDATE Booking SET active = No AND isBeingRented = No WHERE vehicleRegistration = \'" + thisBooking.getVehicleRegistration() + "\'";
        RentACar.statement.executeUpdate(returnV);

        double extraMoneyOwed = 0;
        boolean overdue = false;
        double days = 0;

        if(Date.valueOf(LocalDate.now()).after(thisBooking.getEndDate()))
        {
            overdue = true;

            Duration difference = Duration.between(thisBooking.getEndDate().toLocalDate().atStartOfDay(), LocalDate.now().atStartOfDay());
            days = Double.parseDouble(String.valueOf(difference.toDays()));

            double flatRate = 0;
            Settings thisSetting = Settings.getSetting("Daily Rental Cost");

            if(thisSetting != null)
                flatRate = thisSetting.getSettingValue();

            ObservableList<Vehicle> thisVehicle = Vehicle.searchQuery("vehicleRegistration", thisBooking.getVehicleRegistration(), "");
            Vehicle vehicle = thisVehicle.get(0);

            double costMulti = vehicle.getCostMultiplier();

            extraMoneyOwed = flatRate*days*costMulti;
            double clientMoneyOwed = extraMoneyOwed / 2;

            String updateClient = "UPDATE Client SET moneyOwed = moneyOwed + " + clientMoneyOwed + " WHERE clientNumber = \'" + vehicle.getClientNumber() + "\' AND active = Yes";
            RentACar.statement.executeUpdate(updateClient);

            ObservableList<Client> thisClient = Client.searchQuery("clientNumber", String.valueOf(vehicle.getClientNumber()), "");
            Client updatedClient = thisClient.get(0);

            for(Client client : baseController.clients)
            {
                if(client.getClientID().equals(updatedClient.getClientID()))
                {
                    client.setMoneyOwed(client.getMoneyOwed() + clientMoneyOwed);
                    break;
                }
            }

            String updateBooking = "UPDATE Booking SET cost = cost + " + extraMoneyOwed + " WHERE bookingNumber = \'" + thisBooking.getBookingNumber() + "\' AND active = Yes";
            RentACar.statement.executeUpdate(updateBooking);
        }

        thisBooking.setIsBeingRented("No");
        thisBooking.setActive(false);

        filteredBookings.removeAll(thisBooking);

        for(Booking booking : baseController.bookings)
        {
            if(booking.getBookingNumber() == thisBooking.getBookingNumber())
            {
                booking.setCost(booking.getCost() + extraMoneyOwed);
                booking.setActive(false);
                baseController.bookings.removeAll(booking);
                break;
            }
        }

        Alert returnVehicle = new Alert(Alert.AlertType.INFORMATION);
        if(overdue)
        {
            returnVehicle.setHeaderText("Vehicle Returned From Rental (Overdue)");
            returnVehicle.setContentText("Vehicle overdue by: " + (int) days + " days\nFine amount: R" + extraMoneyOwed);
            returnVehicle.showAndWait();
        }
        else
        {
            returnVehicle.setHeaderText("Vehicle Returned From Rental");
            returnVehicle.showAndWait();
        }
    }
}

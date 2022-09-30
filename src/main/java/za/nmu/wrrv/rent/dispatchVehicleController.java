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

    static
    {
        for(Booking thisBooking : baseController.bookings)
        {
            if(thisBooking.isActive() && (thisBooking.getStartDate().equals(Date.valueOf(LocalDate.now())) || thisBooking.getStartDate().after(Date.valueOf(LocalDate.now()))) && thisBooking.isIsBeingRented().equals("No"))
                filteredBookings.add(thisBooking);
        }
    }

    protected static Booking thisBooking;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
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

        dispatchVehicle.setVisible(false);

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
        ObservableList<Booking> filteredList = Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText());

        filteredBookings = FXCollections.observableArrayList();

        for(Booking thisBooking : filteredList)
        {
            if(thisBooking.isActive() & thisBooking.getStartDate().after(Date.valueOf(LocalDate.now())) && thisBooking.isIsBeingRented().equals("No"))
                filteredBookings.add(thisBooking);
        }

        filteredTable.setItems(filteredBookings);
    }
    private void onDispatch() throws SQLException
    {
        String dispatch = "UPDATE Booking SET isBeingRented = Yes WHERE vehicleRegistration = \'" + thisBooking.getVehicleRegistration() + "\' AND startDate = \'" + thisBooking.getStartDate() + "\' AND endDate = \'" + thisBooking.getEndDate() + "\'";
        RentACar.statement.executeUpdate(dispatch);

        thisBooking.setIsBeingRented("Yes");
        filteredBookings.removeAll(thisBooking);

        String updateClient = "UPDATE Client SET moneyOwed = \'" + thisBooking.getOwnerCommission() + "\' WHERE clientNumber = \'" + thisBooking.getClientNumber() + "\'";
        RentACar.statement.executeUpdate(updateClient);

        for(Client thisClient : baseController.clients)
        {
            if(thisClient.getClientNumber() == thisBooking.getClientNumber())
            {
                thisClient.setMoneyOwed(thisBooking.getOwnerCommission());
                break;
            }
        }
    }
}

package za.nmu.wrrv.rent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
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

public class dispatchVehicleController implements Initializable, EventHandler<Event>
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
        setupMnemonics();

        try
        {
            filteredBookings = Booking.searchQuery("startDate", String.valueOf(LocalDate.now()), "AND isBeingRented = No AND hasPaid = Yes");
        }
        catch (SQLException e)
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
                case "None" ->
                        {
                            searchQuery.setPromptText("Search");
                            filteredTable.setItems(filteredBookings);
                        }
                case "vehicleRegistration" -> searchQuery.setPromptText("ABC123 EC or CUSTOM MP");
                case "startDate", "endDate" -> searchQuery.setPromptText("YYYY-MM-DD");
            }
        });

        dispatchVehicle.setVisible(false);

        filteredTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        registrationNumber.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
        bookingStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        bookingEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        filteredTable.setItems(filteredBookings);
    }
    @Override
    public void handle(Event event)
    {
        Button thisButton = (Button) event.getSource();
        String buttonId = thisButton.getId();
        switch(buttonId)
        {
            case "search" ->
                    {
                        try
                        {
                            onSearch();
                        } catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }
            case "dispatchVehicle" ->
                    {
                        if(thisBooking != null)
                        {
                            try
                            {
                                onDispatch();
                            }
                            catch (SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
            case "back" -> baseController.nextScene(baseController.userLoggedOn);
        }
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

    private void setupMnemonics()
    {
        search.setMnemonicParsing(true);
        back.setMnemonicParsing(true);
        dispatchVehicle.setMnemonicParsing(true);

        search.setOnAction(this::handle);
        back.setOnAction(this::handle);
        dispatchVehicle.setOnAction(this::handle);

        search.setTooltip(new Tooltip("Alt+S"));
        back.setTooltip(new Tooltip("Alt+B"));
        dispatchVehicle.setTooltip(new Tooltip("Alt+D"));
    }

    private void onSearch() throws SQLException
    {
        if(searchFilter.getSelectionModel().getSelectedItem().equals("None"))
            filteredTable.setItems(filteredBookings);
        else
        {
            if(searchQuery.getText().contains("/") || searchQuery.getText().contains("-"))
            {
                String thisDate = searchQuery.getText();
                thisDate = thisDate.replace("/", "-");

                if(baseController.errorValidationCheck(baseController.letterArray, thisDate) || baseController.symbolCheck(thisDate, '-'))
                {
                    String[] split = thisDate.split("-");
                    if(split[0].length() != 4 || split[1].length() != 2 || Integer.parseInt(split[1]) < 1 || Integer.parseInt(split[1]) > 12 || split[2].length() != 2)
                        filteredTable.setItems(null);
                    else
                    {
                        ObservableList<Booking> filteredList = Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText(), "AND isBeingRented = No");
                        filteredTable.setItems(filteredList);
                    }
                }
                else
                    filteredTable.setItems(null);
            }
            else
            {
                ObservableList<Booking> filteredList = Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText(), "AND isBeingRented = No");
                filteredTable.setItems(filteredList);
            }
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

        Alert dispatchVehicle = new Alert(Alert.AlertType.INFORMATION);
        dispatchVehicle.setHeaderText("Vehicle Dispatched For Rental");
        dispatchVehicle.showAndWait();
    }
}

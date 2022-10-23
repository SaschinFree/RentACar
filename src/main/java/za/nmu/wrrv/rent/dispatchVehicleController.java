package za.nmu.wrrv.rent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.ucanaccess.converters.Functions;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
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

        filteredBookings = FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.getStartDate().equals(Date.valueOf(LocalDate.now())) && booking.isIsBeingRented().equals("No") && booking.isHasPaid().equals("Yes")).toList());

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

        searchQuery.focusedProperty().addListener((observableValue, aBoolean, t1) ->
        {
            enableMnemonics(!searchQuery.isFocused());
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
            case "search" -> onSearch();
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
    @FXML
    protected void buttonHover(MouseEvent mouseEvent)
    {
        baseController.changeStyle((Button) mouseEvent.getSource());
    }

    private void setupMnemonics()
    {
        enableMnemonics(true);

        search.setOnAction(this::handle);
        back.setOnAction(this::handle);
        dispatchVehicle.setOnAction(this::handle);

        searchQuery.setTooltip(baseController.searchTip);
        search.setTooltip(new Tooltip("Alt+S"));
        back.setTooltip(new Tooltip("Alt+B"));
        dispatchVehicle.setTooltip(new Tooltip("Alt+D"));
    }
    private void enableMnemonics(boolean value)
    {
        search.setMnemonicParsing(value);
        back.setMnemonicParsing(value);
        dispatchVehicle.setMnemonicParsing(value);
    }

    private void onSearch()
    {
        if(searchFilter.getSelectionModel().getSelectedItem().equals("None"))
            filteredTable.setItems(filteredBookings);
        else
        {
            if(searchFilter.getSelectionModel().getSelectedItem().equals("startDate") || searchFilter.getSelectionModel().getSelectedItem().equals("endDate"))
            {
                ObservableList<Booking> filteredList = dateValid(searchQuery.getText());
                if(filteredList != null)
                    filteredList = FXCollections.observableList(filteredList.stream().filter(booking -> booking.getStartDate().equals(Date.valueOf(LocalDate.now())) && booking.isIsBeingRented().equals("No") && booking.isHasPaid().equals("Yes")).toList());
                filteredTable.setItems(filteredList);
            }
            else
            {
                ObservableList<Booking> filteredList = Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText());
                if(filteredList != null)
                    filteredList = FXCollections.observableList(filteredList.stream().filter(booking -> booking.getStartDate().equals(Date.valueOf(LocalDate.now())) && booking.isIsBeingRented().equals("No") && booking.isHasPaid().equals("Yes")).toList());
                filteredTable.setItems(filteredList);
            }
        }
    }
    private ObservableList<Booking> dateValid(String date)
    {
        date = date.replace("/", "-");

        if(baseController.dateCheck(date))
        {
            String[] split = date.split("-");
            if(Functions.isNumeric(split[0]) && Functions.isNumeric(split[1]) && Functions.isNumeric(split[2]))
            {
                switch (Integer.parseInt(split[1]))
                {
                    case 1,3,5,7,8,10,12 ->
                            {
                                if(Integer.parseInt(split[2]) > 0 && Integer.parseInt(split[2]) <= 31)
                                    return Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), date);
                            }
                    case 4,6,9,11 ->
                            {
                                if(Integer.parseInt(split[2]) > 0 && Integer.parseInt(split[2]) <= 30)
                                    return Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), date);
                            }
                    case 2 ->
                            {
                                if(Year.of(Integer.parseInt(split[0])).isLeap())
                                {
                                    if(Integer.parseInt(split[2]) > 0 && Integer.parseInt(split[2]) <= 29)
                                        return Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), date);
                                }
                                else
                                {
                                    if(Integer.parseInt(split[2]) > 0 && Integer.parseInt(split[2]) <= 28)
                                        return Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), date);
                                }
                            }
                }
            }
        }
        return null;
    }
    private void onDispatch() throws SQLException
    {
        String dispatch = "UPDATE Booking SET isBeingRented = Yes WHERE vehicleRegistration = \'" + thisBooking.getVehicleRegistration() + "\' AND startDate = \'" + thisBooking.getStartDate() + "\' AND endDate = \'" + thisBooking.getEndDate() + "\'";
        RentACar.statement.executeUpdate(dispatch);
        thisBooking.setIsBeingRented("Yes");

        int index = 0;
        for(Booking booking : baseController.bookings)
        {
            if(booking.getBookingNumber() == thisBooking.getBookingNumber())
            {
                baseController.bookings.set(index, thisBooking);
                break;
            }
            index++;
        }
        filteredBookings.removeAll(thisBooking);

        ObservableList<Vehicle> clientVehicle = FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getVehicleRegistration().equals(thisBooking.getVehicleRegistration())).toList());
        Vehicle thisVehicle = clientVehicle.get(0);

        String updateClient = "UPDATE Client SET moneyOwed = \'" + thisBooking.getOwnerCommission() + "\' WHERE clientNumber = \'" + thisVehicle.getClientNumber() + "\'";
        RentACar.statement.executeUpdate(updateClient);

        index = 0;
        for(Client thisClient : baseController.clients)
        {
            if(thisClient.getClientNumber() == thisVehicle.getClientNumber())
            {
                thisClient.setMoneyOwed(thisClient.getMoneyOwed() + thisBooking.getOwnerCommission());
                baseController.clients.set(index, thisClient);
                break;
            }
            index++;
        }

        Alert dispatchVehicle = new Alert(Alert.AlertType.INFORMATION);
        ((Stage) dispatchVehicle.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
        dispatchVehicle.setHeaderText("Vehicle Dispatched For Rental");
        dispatchVehicle.showAndWait();
    }
}

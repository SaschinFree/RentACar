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
import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.util.ResourceBundle;

public class returnVehicleController implements Initializable, EventHandler<Event>
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
        setupMnemonics();

        filteredBookings = FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.isIsBeingRented().equals("Yes")).toList());

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
    @Override
    public void handle(Event event)
    {
        Button thisButton = (Button) event.getSource();
        String buttonId = thisButton.getId();
        switch(buttonId)
        {
            case "search" -> onSearch();
            case "returnVehicle" ->
                    {
                        if(thisBooking != null)
                        {
                            try
                            {
                                onReturn();
                            } catch (SQLException e)
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

    private void setupMnemonics()
    {
        search.setMnemonicParsing(true);
        back.setMnemonicParsing(true);
        returnVehicle.setMnemonicParsing(true);

        search.setOnAction(this::handle);
        back.setOnAction(this::handle);
        returnVehicle.setOnAction(this::handle);

        search.setTooltip(new Tooltip("Alt+S"));
        back.setTooltip(new Tooltip("Alt+B"));
        returnVehicle.setTooltip(new Tooltip("Alt+R"));
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
                    filteredList = FXCollections.observableList(filteredList.stream().filter(booking -> booking.isIsBeingRented().equals("Yes")).toList());
                filteredTable.setItems(filteredList);
            }
            else
            {
                ObservableList<Booking> filteredList = Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText());
                if(filteredList != null)
                    filteredList = FXCollections.observableList(filteredList.stream().filter(booking -> booking.isIsBeingRented().equals("Yes")).toList());
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

            ObservableList<Vehicle> thisVehicle = FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getVehicleRegistration().equals(thisBooking.getVehicleRegistration())).toList());
            Vehicle vehicle = thisVehicle.get(0);

            double costMulti = vehicle.getCostMultiplier();

            extraMoneyOwed = flatRate * days * costMulti;
            double clientMoneyOwed = extraMoneyOwed / 2;

            String updateClient = "UPDATE Client SET moneyOwed = moneyOwed + " + clientMoneyOwed + " WHERE clientNumber = \'" + vehicle.getClientNumber() + "\' AND active = Yes";
            RentACar.statement.executeUpdate(updateClient);

            ObservableList<Client> thisClient = FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getClientNumber() == vehicle.getClientNumber()).toList());
            Client updatedClient = thisClient.get(0);

            int index = 0;
            for(Client client : baseController.clients)
            {
                if(client.getClientID().equals(updatedClient.getClientID()))
                {
                    client.setMoneyOwed(client.getMoneyOwed() + clientMoneyOwed);
                    baseController.clients.set(index, client);
                    break;
                }
                index++;
            }

            String updateBooking = "UPDATE Booking SET cost = cost + " + extraMoneyOwed + " WHERE bookingNumber = \'" + thisBooking.getBookingNumber() + "\' AND active = Yes";
            RentACar.statement.executeUpdate(updateBooking);
        }

        thisBooking.setCost(thisBooking.getCost() + extraMoneyOwed);
        thisBooking.setIsBeingRented("No");
        thisBooking.setActive(false);

        filteredBookings.removeAll(thisBooking);

        int index = 0;
        for(Booking booking : baseController.bookings)
        {
            if(booking.getBookingNumber() == thisBooking.getBookingNumber())
            {
                baseController.bookings.set(index, thisBooking);
                baseController.bookings.removeAll(booking);
                break;
            }
            index++;
        }

        Alert returnVehicle = new Alert(Alert.AlertType.INFORMATION);
        if(overdue)
        {
            returnVehicle.setHeaderText("Vehicle Returned From Rental (Overdue)");
            returnVehicle.setContentText("Vehicle overdue by: " + (int) days + " days\nFine amount: R" + extraMoneyOwed);
        }
        else
            returnVehicle.setHeaderText("Vehicle Returned From Rental");

        ((Stage) returnVehicle.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
        returnVehicle.showAndWait();
    }
}

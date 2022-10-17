package za.nmu.wrrv.rent;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import net.ucanaccess.converters.Functions;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Year;
import java.util.ResourceBundle;

public class manageBookingsController implements Initializable, EventHandler<Event>
{
    @FXML
    protected ChoiceBox<String> searchFilter;
    @FXML
    protected TableColumn<Booking, Integer> bookingNumber;
    @FXML
    protected TableColumn<Booking, Integer> clientNumber;
    @FXML
    protected TableColumn<Booking, String> vehicleRegistration;
    @FXML
    protected TableColumn<Booking, Date> startDate;
    @FXML
    protected TableColumn<Booking, Date> endDate;
    @FXML
    protected TableColumn<Booking, Double> cost;
    @FXML
    protected TableColumn<Booking, Boolean> isBeingRented;
    @FXML
    protected TableColumn<Booking, Boolean> hasPaid;
    @FXML
    protected TextField searchQuery;
    @FXML
    protected Button search;
    @FXML
    protected Button addBooking;
    @FXML
    protected Button updateBooking;
    @FXML
    protected Button back;
    @FXML
    protected TableView<Booking> bookingTable;

    protected static Booking thisBooking;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        setupMnemonics();

        searchFilter.getItems().addAll(
                "None",
                "bookingNumber",
                "clientNumber",
                "vehicleRegistration",
                "startDate",
                "endDate",
                "cost",
                "isBeingRented",
                "hasPaid");
        searchFilter.setValue("None");

        searchFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, stringSingleSelectionModel, t1) ->
        {
            searchQuery.clear();

            switch(searchFilter.getSelectionModel().getSelectedItem())
            {
                case "None" ->
                        {
                            searchQuery.setPromptText("Search");
                            bookingTable.setItems(baseController.bookings);
                        }
                case "bookingNumber", "clientNumber" -> searchQuery.setPromptText("1");
                case "vehicleRegistration" -> searchQuery.setPromptText("ABC123 EC or CUSTOM MP etc");
                case "startDate", "endDate" -> searchQuery.setPromptText("YYYY-MM-DD");
                case "cost" -> searchQuery.setPromptText("0.0");
                case "isBeingRented", "hasPaid" -> searchQuery.setPromptText("Yes or No");
            }
        });

        if(loginController.thisUser.isAdmin())
            addBooking.setVisible(false);

        updateBooking.setVisible(false);

        bookingTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        bookingNumber.setCellValueFactory(new PropertyValueFactory<>("bookingNumber"));
        clientNumber.setCellValueFactory(new PropertyValueFactory<>("clientNumber"));
        vehicleRegistration.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        isBeingRented.setCellValueFactory(new PropertyValueFactory<>("isBeingRented"));
        hasPaid.setCellValueFactory(new PropertyValueFactory<>("hasPaid"));

        bookingTable.setItems(baseController.bookings);
    }
    @Override
    public void handle(Event event)
    {
        Button thisButton = (Button) event.getSource();
        String buttonId = thisButton.getId();
        switch(buttonId)
        {
            case "search" -> onSearch();
            case "addBooking" ->
                    {
                        try
                        {
                            baseController.newScreen("addBooking", "Add A Booking");
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
            case "updateBooking" ->
                    {
                        if(thisBooking != null)
                        {
                            try
                            {
                                baseController.newScreen("updateBooking", "Update A Booking");
                            }
                            catch (IOException e)
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
            if(!loginController.thisUser.isAdmin())
            {
                thisBooking = bookingTable.getSelectionModel().getSelectedItem();
                if(thisBooking != null)
                    updateBooking.setVisible(true);
            }
        }
    }
    @FXML
    protected void buttonClicked(MouseEvent mouseEvent) throws IOException, SQLException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();
            switch(buttonId)
            {
                case "search" -> onSearch();
                case "addBooking" -> baseController.newScreen("addBooking", "Add A Booking");
                case "updateBooking" -> baseController.newScreen("updateBooking", "Update A Booking");
                case "back" -> baseController.nextScene(baseController.userLoggedOn);
            }
        }
    }

    private void setupMnemonics()
    {
        search.setMnemonicParsing(true);
        addBooking.setMnemonicParsing(true);
        updateBooking.setMnemonicParsing(true);
        back.setMnemonicParsing(true);

        search.setOnAction(this::handle);
        addBooking.setOnAction(this::handle);
        updateBooking.setOnAction(this::handle);
        back.setOnAction(this::handle);

        search.setTooltip(new Tooltip("Alt+S"));
        addBooking.setTooltip(new Tooltip("Alt+A"));
        updateBooking.setTooltip(new Tooltip("Alt+U"));
        back.setTooltip(new Tooltip("Alt+B"));
    }

    private void onSearch()
    {
        if(searchFilter.getSelectionModel().getSelectedItem().equals("None"))
            bookingTable.setItems(baseController.bookings);
        else
        {
            if(searchFilter.getSelectionModel().getSelectedItem().equals("startDate") || searchFilter.getSelectionModel().getSelectedItem().equals("endDate"))
                bookingTable.setItems(dateValid(searchQuery.getText()));
            else
            {
                ObservableList<Booking> filteredList = Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText());
                bookingTable.setItems(filteredList);
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
}

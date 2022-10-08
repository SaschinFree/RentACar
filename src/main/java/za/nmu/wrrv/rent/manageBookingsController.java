package za.nmu.wrrv.rent;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class manageBookingsController implements Initializable
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
    protected TableColumn<Booking, Integer> cost;
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
    protected TableView<Booking> bookingTable;

    protected static Booking thisBooking;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        searchFilter.getItems().addAll(
                "bookingNumber",
                "clientNumber",
                "vehicleRegistration",
                "startDate",
                "endDate",
                "cost",
                "isBeingRented",
                "hasPaid");
        searchFilter.setValue("bookingNumber");

        searchFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, stringSingleSelectionModel, t1) ->
        {
            searchQuery.clear();

            switch(searchFilter.getSelectionModel().getSelectedItem())
            {
                case "bookingNumber", "clientNumber" -> searchQuery.setPromptText("1");
                case "vehicleRegistration" -> searchQuery.setPromptText("ABC123 EC or CUSTOM MP etc");
                case "startDate", "endDate" -> searchQuery.setPromptText("YYYY/MM/DD");
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
        search.setTooltip(baseController.searchTip);
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

    private void onSearch() throws SQLException
    {
        if(searchQuery.getText().isEmpty())
            bookingTable.setItems(baseController.bookings);
        else
        {
            ObservableList<Booking> filteredList = Booking.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText(), "");
            bookingTable.setItems(filteredList);
        }
    }
}

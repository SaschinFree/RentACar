package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    protected TableColumn<Booking, Integer> companyCommission;
    @FXML
    protected TableColumn<Booking, Integer> ownerCommission;
    @FXML
    protected TableColumn<Booking, Boolean> isBeingRented;
    @FXML
    protected TableColumn<Booking, Boolean> hasPaid;
    @FXML
    protected TextField searchQuery;
    @FXML
    protected Button search;
    @FXML
    protected Button createBooking;
    @FXML
    protected Button cancelBooking;
    @FXML
    protected TableView<Booking> bookingTable;

    protected static Booking thisBooking;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        if(loginController.thisUser.isAdmin())
            createBooking.setVisible(false);

        cancelBooking.setVisible(false);

        bookingTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        bookingNumber.setCellValueFactory(new PropertyValueFactory<>("bookingNumber"));
        clientNumber.setCellValueFactory(new PropertyValueFactory<>("clientNumber"));
        vehicleRegistration.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        companyCommission.setCellValueFactory(new PropertyValueFactory<>("companyCommission"));
        ownerCommission.setCellValueFactory(new PropertyValueFactory<>("ownerCommission"));
        isBeingRented.setCellValueFactory(new PropertyValueFactory<>("isBeingRented"));
        hasPaid.setCellValueFactory(new PropertyValueFactory<>("hasPaid"));

        bookingTable.setItems(baseController.bookings);
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
                    cancelBooking.setVisible(true);
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
                case "createBooking" -> baseController.newScreen("createBooking", "Create A Booking");
                case "cancelBooking" -> onCancel();
                case "back" -> baseController.nextScene(baseController.userLoggedOn);
            }
        }
    }

    private void onSearch()
    {
        //searchQuery
    }
    private void onCancel() throws SQLException
    {
        String cancelBooking = "UPDATE Booking SET active = No WHERE bookingNumber = \'" + thisBooking.getBookingNumber() + "\'";
        RentACar.statement.executeUpdate(cancelBooking);
        thisBooking.setActive(false);
        baseController.bookings.removeAll(thisBooking);
    }
}

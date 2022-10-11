package za.nmu.wrrv.rent;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class addBookingController implements Initializable
{
    @FXML
    protected Label R;
    @FXML
    protected Label clientIDLabel;
    @FXML
    protected TextField clientSearchQuery;
    @FXML
    protected Button clientSearch;
    @FXML
    protected Label clientNameLabel;
    @FXML
    protected Label clientSurnameLabel;
    @FXML
    protected Button confirmClient;
    @FXML
    protected Label bookingPeriod;
    @FXML
    protected Label start;
    @FXML
    protected DatePicker startDate;
    @FXML
    protected Label end;
    @FXML
    protected DatePicker endDate;
    @FXML
    protected Label totalDays;
    @FXML
    protected Label days;
    @FXML
    protected Button confirmPeriod;
    @FXML
    protected Label vehicleRegistration;
    @FXML
    protected TextField vehicleSearchQuery;
    @FXML
    protected Button vehicleSearch;
    @FXML
    protected Label vehicleMakeLabel;
    @FXML
    protected Label vehicleModelLabel;
    @FXML
    protected Button confirmVehicle;
    @FXML
    protected TableView clientVehicleTable;
    @FXML
    protected TableColumn clientIDVehicleReg;
    @FXML
    protected TableColumn clientNameVehicleMake;
    @FXML
    protected TableColumn clientSurnameVehicleModel;
    @FXML
    protected Label totalCost;
    @FXML
    protected Label cost;
    @FXML
    protected Label bookingPaid;
    @FXML
    protected CheckBox isPaid;
    @FXML
    protected Button cancel;
    @FXML
    protected Button addBooking;

    private static Client thisClient;

    private final Alert alert = new Alert(Alert.AlertType.ERROR);
    private String errorMessage;

    private  int thisBookingNumber;
    private int thisClientNumber;
    private String thisVehicleRegistration;
    private Date thisStartDate;
    private Date thisEndDate;
    private double thisCost;

    private final DoubleProperty rateDays = new SimpleDoubleProperty();
    private final DoubleProperty finalCost = new SimpleDoubleProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        startDate.setConverter(baseController.dateConverter);
        endDate.setConverter(baseController.dateConverter);

        clientVehicleTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        clientVehicleTable.setItems(baseController.clients);

        clientIDVehicleReg.setCellValueFactory(new PropertyValueFactory<>("clientID"));
        clientNameVehicleMake.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        clientSurnameVehicleModel.setCellValueFactory(new PropertyValueFactory<>("surname"));

        clientNameLabel.setVisible(false);
        clientSurnameLabel.setVisible(false);
        confirmClient.setVisible(false);

        bookingPeriod.setVisible(false);
        start.setVisible(false);
        startDate.setVisible(false);
        end.setVisible(false);
        endDate.setVisible(false);
        totalDays.setVisible(false);
        days.setVisible(false);
        confirmPeriod.setVisible(false);

        vehicleRegistration.setVisible(false);
        vehicleSearchQuery.setVisible(false);
        vehicleSearch.setVisible(false);
        vehicleMakeLabel.setVisible(false);
        vehicleModelLabel.setVisible(false);
        confirmVehicle.setVisible(false);

        totalCost.setVisible(false);
        R.setVisible(false);
        cost.setVisible(false);
        bookingPaid.setVisible(false);
        isPaid.setVisible(false);

        addBooking.setVisible(false);
    }
    @FXML
    protected void keyClicked(KeyEvent keyEvent) throws SQLException
    {
        switch(keyEvent.getCode())
        {
            case ESCAPE -> closeStage();
            case ENTER ->
                    {
                        if(addBooking.isVisible())
                            onAdd();
                    }
        }
    }
    @FXML
    protected void clientVehicleSelected(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            if(clientVehicleTable.getSelectionModel().getSelectedItem() instanceof Client)
            {
                thisClient = (Client) clientVehicleTable.getSelectionModel().getSelectedItem();
                if(thisClient != null)
                {
                    clientSearchQuery.setText(String.valueOf(thisClient.getClientNumber()));
                    clientNameLabel.textProperty().bind(thisClient.firstNameProperty());
                    clientSurnameLabel.textProperty().bind(thisClient.surnameProperty());

                    clientNameLabel.setVisible(true);
                    clientSurnameLabel.setVisible(true);
                    confirmClient.setVisible(true);

                    thisClientNumber = thisClient.getClientNumber();
                }
            }
            else
            {
                Vehicle thisVehicle = (Vehicle) clientVehicleTable.getSelectionModel().getSelectedItem();
                if(thisVehicle != null)
                {
                    vehicleSearchQuery.setText(thisVehicle.getVehicleRegistration());
                    vehicleMakeLabel.textProperty().bind(thisVehicle.makeProperty());
                    vehicleModelLabel.textProperty().bind(thisVehicle.modelProperty());

                    vehicleMakeLabel.setVisible(true);
                    vehicleModelLabel.setVisible(true);
                    confirmVehicle.setVisible(true);

                    thisVehicleRegistration = thisVehicle.getVehicleRegistration();

                    finalCost.set(rateDays.multiply(thisVehicle.costMultiplierProperty()).doubleValue());
                    thisCost = finalCost.doubleValue();

                    cost.textProperty().unbind();

                    cost.textProperty().bind(StringProperty.stringExpression(finalCost));
                }
            }
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
                case "cancel" -> closeStage();
                case "clientSearch" -> clientSearch();
                case "confirmClient" -> confirmClient();
                case "confirmPeriod" -> confirmPeriod();
                case "vehicleSearch" -> vehicleSearch();
                case "confirmVehicle" -> confirmVehicle();
                case "addBooking" -> onAdd();
            }
        }
    }

    private void clientSearch() throws SQLException
    {
        if(clientSearchQuery.getText().equals(""))
            clientVehicleTable.setItems(baseController.clients);
        else
        {
            ObservableList<Client> filteredClients = Client.searchQuery("clientNumber", clientSearchQuery.getText(), "");
            clientVehicleTable.setItems(filteredClients);
        }
    }
    private void confirmClient()
    {
        clientSearchQuery.setDisable(true);
        clientSearch.setVisible(false);
        confirmClient.setVisible(false);

        bookingPeriod.setVisible(true);
        start.setVisible(true);
        startDate.setVisible(true);
        end.setVisible(true);
        endDate.setVisible(true);
        confirmPeriod.setVisible(true);

        clientVehicleTable.setVisible(false);
    }
    private void confirmPeriod() throws SQLException
    {
        String startString = startDate.getEditor().getText();
        startString = startString.replace("/", "-");

        String endString = endDate.getEditor().getText();
        endString = endString.replace("/", "-");

        if(baseController.dateCheck(startDate, startString) && baseController.dateCheck(endDate, endString))
        {
            if(dateEmptyChecks(startString, endString) && dateErrorChecks(Date.valueOf(startString), Date.valueOf(endString)))
            {
                thisStartDate = Date.valueOf(startString);
                thisEndDate = Date.valueOf(endString);

                startDate.setDisable(true);
                endDate.setDisable(true);
                confirmPeriod.setVisible(false);

                vehicleRegistration.setVisible(true);
                vehicleSearchQuery.setVisible(true);
                vehicleSearch.setVisible(true);

                ObservableList<Vehicle> vehicles = replaceTableContents("");

                totalCost.setVisible(true);
                R.setVisible(true);
                cost.setVisible(true);
                bookingPaid.setVisible(true);
                isPaid.setVisible(true);

                clientIDVehicleReg.setText("Vehicle Registration");
                clientNameVehicleMake.setText("Make");
                clientSurnameVehicleModel.setText("Model");

                clientIDVehicleReg.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
                clientNameVehicleMake.setCellValueFactory(new PropertyValueFactory<>("make"));
                clientSurnameVehicleModel.setCellValueFactory(new PropertyValueFactory<>("model"));

                TableColumn<Vehicle, Integer> seats = new TableColumn<>("Seats");
                TableColumn<Vehicle, Double> costMulti = new TableColumn<>("Cost Multiplier");

                seats.setCellValueFactory(new PropertyValueFactory<>("seats"));
                costMulti.setCellValueFactory(new PropertyValueFactory<>("costMultiplier"));

                clientVehicleTable.getColumns().addAll(seats, costMulti);
                clientVehicleTable.setItems(vehicles);
                clientVehicleTable.setVisible(true);

                Duration difference = Duration.between(thisStartDate.toLocalDate().atStartOfDay(), thisEndDate.toLocalDate().atStartOfDay());
                DoubleProperty differenceDays = new SimpleDoubleProperty(difference.toDays() + 1);

                DoubleProperty flatRate = new SimpleDoubleProperty();
                Settings thisSetting = Settings.getSetting("Daily Rental Cost");

                if(thisSetting != null)
                    flatRate.set(thisSetting.getSettingValue());

                rateDays.set(flatRate.multiply(differenceDays).doubleValue());

                days.setText(String.valueOf((int) differenceDays.get()));
                cost.textProperty().bind(StringProperty.stringExpression(rateDays));

                totalDays.setVisible(true);
                days.setVisible(true);
            }
            else
            {
                alert.setHeaderText(errorMessage);
                alert.showAndWait();
            }
        }
        else
        {
            alert.setHeaderText(errorMessage);
            alert.showAndWait();
        }
    }
    private void vehicleSearch() throws SQLException
    {
        ObservableList<Vehicle> filteredVehicles = replaceTableContents(vehicleSearchQuery.getText());
        clientVehicleTable.setItems(filteredVehicles);
    }
    private void confirmVehicle()
    {
        vehicleSearchQuery.setDisable(true);
        vehicleSearch.setVisible(false);
        confirmVehicle.setVisible(false);

        clientVehicleTable.setVisible(false);
        addBooking.setVisible(true);
    }
    private void onAdd() throws SQLException
    {
        double compCom = 0;
        double ownerCom = 0;

        Settings thisSetting = Settings.getSetting("Company Commission");
        if(thisSetting != null)
            compCom = thisSetting.getSettingValue();

        thisSetting = Settings.getSetting("Owner Commission");
        if(thisSetting != null)
            ownerCom = thisSetting.getSettingValue();

        double thisCompanyCommission = thisCost * compCom;
        double thisOwnerCommission = thisCost * ownerCom;

        String thisHasPaid;
        if(isPaid.selectedProperty().getValue())
            thisHasPaid = "Yes";
        else
            thisHasPaid = "No";

        String sql = "INSERT INTO Booking (clientNumber, vehicleRegistration, startDate, endDate, cost, companyCommission, ownerCommission, isBeingRented, hasPaid, active)" +
                "VALUES (\'" + thisClientNumber + "\', \'" + thisVehicleRegistration + "\', \'" + thisStartDate + "\', \'" + thisEndDate + "\', \'" + thisCost + "\', \'" + thisCompanyCommission + "\', \'" + thisOwnerCommission + "\', No, " + thisHasPaid + ", Yes)";
        RentACar.statement.executeUpdate(sql);

        String getNumber = "SELECT bookingNumber FROM Booking WHERE clientNumber = \'" + thisClientNumber + "\' AND vehicleRegistration = \'" + thisVehicleRegistration + "\' AND startDate = \'" + thisStartDate + "\' AND endDate = \'" + thisEndDate + "\'";
        ResultSet result = RentACar.statement.executeQuery(getNumber);

        if(result.next())
            thisBookingNumber = result.getInt("bookingNumber");

        Booking thisBooking = new Booking(thisBookingNumber, thisClientNumber, thisVehicleRegistration, thisStartDate, thisEndDate, thisCost, thisCompanyCommission, thisOwnerCommission, "No", thisHasPaid);
        baseController.bookings.add(thisBooking);

        Alert bookingAdded = new Alert(Alert.AlertType.INFORMATION);
        bookingAdded.setHeaderText("Booking Added Successfully");
        bookingAdded.showAndWait();
        closeStage();
    }

    private boolean dateEmptyChecks(String startDate, String endDate)
    {
        if(startDate.isEmpty())
        {
            errorMessage = "Booking Start Date is empty";
            this.startDate.setValue(null);
            return false;
        }
        if(endDate.isEmpty())
        {
            errorMessage = "Booking End Date is empty";
            this.endDate.setValue(null);
            return false;
        }
        return true;
    }
    private boolean dateErrorChecks(Date startDate, Date endDate)
    {
        if(startDate.before(Date.valueOf(LocalDate.now())))
        {
            errorMessage = "Booking Start Date cannot be a past date";
            this.startDate.setValue(null);
            return false;
        }
        if(startDate.after(endDate))
        {
            errorMessage = "Booking Start Date cannot be after Booking End Date";
            this.startDate.setValue(null);
            this.endDate.setValue(null);
            return false;
        }
        return true;
    }
    private ObservableList<Vehicle> replaceTableContents(String extraParameter) throws SQLException
    {
        ObservableList<Vehicle> thisList = FXCollections.observableArrayList();

        String bookingCheck = "SELECT DISTINCT vehicleRegistration FROM Booking WHERE active = No OR startDate <= \'" + this.thisStartDate + "\' AND endDate >= \'" + this.thisStartDate + "\' OR startDate <= \'" + this.thisEndDate + "\' AND endDate >= \'" + this.thisEndDate + "\'";

        String sql = " SELECT * FROM Vehicle WHERE clientNumber != \'" + thisClient.getClientNumber() + "\' AND startDate <= \'" + this.thisStartDate + "\' AND endDate >= \'" + this.thisEndDate + "\' AND active = Yes AND NOT vehicleRegistration IN (" + bookingCheck + ")";
        if(!extraParameter.equals(""))
            sql = " SELECT * FROM Vehicle WHERE vehicleRegistration = \'" + extraParameter + "\' AND clientNumber != \'" + thisClient.getClientNumber() + "\' AND startDate <= \'" + this.thisStartDate + "\' AND endDate >= \'" + this.thisEndDate + "\' AND active = Yes AND NOT vehicleRegistration IN (" + bookingCheck + ")";
        ResultSet result = RentACar.statement.executeQuery(sql);

        while(result.next())
        {
            String thisVehicleRegistration = result.getString("vehicleRegistration");
            int thisClientNumber = result.getInt("clientNumber");
            Date thisRegistrationExpiryDate = result.getDate("registrationExpiryDate");
            boolean thisInsured = result.getBoolean("insured");

            String isInsured = "No";
            if(thisInsured)
                isInsured = "Yes";

            String thisMake = result.getString("make");
            String thisModel = result.getString("model");
            String thisColour = result.getString("colour");
            int thisSeats = result.getInt("seats");
            Date thisStartDate = result.getDate("startDate");
            Date thisEndDate = result.getDate("endDate");
            int thisCostMultiplier = result.getInt("costMultiplier");

            thisList.add( new Vehicle(
                    thisVehicleRegistration,
                    thisClientNumber,
                    thisRegistrationExpiryDate,
                    isInsured,
                    thisMake,
                    thisModel,
                    thisColour,
                    thisSeats,
                    thisStartDate,
                    thisEndDate,
                    thisCostMultiplier));
        }
        return thisList;
    }
    private void closeStage()
    {
        Stage thisStage = (Stage) cancel.getScene().getWindow();
        thisStage.close();
    }
}

package za.nmu.wrrv.rent;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Vehicle
{
    private static final ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();
    private StringProperty vehicleRegistration = new SimpleStringProperty();
    private IntegerProperty clientNumber = new SimpleIntegerProperty();
    private Property<Date> registrationExpiryDate = new SimpleObjectProperty<>();
    private BooleanProperty insured = new SimpleBooleanProperty();
    private StringProperty model = new SimpleStringProperty();
    private StringProperty make = new SimpleStringProperty();
    private StringProperty colour = new SimpleStringProperty();
    private IntegerProperty seats = new SimpleIntegerProperty();
    private Property<Date> startDate = new SimpleObjectProperty<>();
    private Property<Date> endDate = new SimpleObjectProperty<>();
    private IntegerProperty costMultiplier = new SimpleIntegerProperty();

    public Vehicle(String vehicleRegistration, int clientNumber, Date registrationExpiryDate, boolean insured, String model, String make, String colour, int seats, Date startDate, Date endDate, int costMultiplier)
    {
        this.vehicleRegistration.set(vehicleRegistration);
        this.clientNumber.set(clientNumber);
        this.registrationExpiryDate.setValue(registrationExpiryDate);
        this.insured.set(insured);
        this.model.set(model);
        this.make.set(make);
        this.colour.set(colour);
        this.seats.set(seats);
        this.startDate.setValue(startDate);
        this.endDate.setValue(endDate);
        this.costMultiplier.set(costMultiplier);
    }

    public static ObservableList<Vehicle> getVehicles() throws SQLException
    {
        String sql = "SELECT * FROM Vehicle";
        ResultSet result = RentACar.statement.executeQuery(sql);

        while(result.next())
        {
            String thisVehicleRegistration = result.getString("vehicleRegistration");
            int thisClientNumber = result.getInt("clientNumber");
            Date thisRegistrationExpiryDate = result.getDate("registrationExpiryDate");
            boolean thisInsured = result.getBoolean("insured");
            String thisModel = result.getString("model");
            String thisMake = result.getString("make");
            String thisColour = result.getString("colour");
            int thisSeats = result.getInt("seats");
            Date thisStartDate = result.getDate("startDate");
            Date thisEndDate = result.getDate("endDate");
            int thisCostMultiplier = result.getInt("costMultiplier");

            vehicleList.add( new Vehicle(
                    thisVehicleRegistration,
                    thisClientNumber,
                    thisRegistrationExpiryDate,
                    thisInsured,
                    thisModel,
                    thisMake,
                    thisColour,
                    thisSeats,
                    thisStartDate,
                    thisEndDate,
                    thisCostMultiplier));
        }
        return vehicleList;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration.get();
    }
    public StringProperty vehicleRegistrationProperty() {
        return vehicleRegistration;
    }
    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration.set(vehicleRegistration);
    }

    public int getClientNumber() {
        return clientNumber.get();
    }
    public IntegerProperty clientNumberProperty() {
        return clientNumber;
    }
    public void setClientNumber(int clientNumber) {
        this.clientNumber.set(clientNumber);
    }

    public Date getRegistrationExpiryDate() {
        return registrationExpiryDate.getValue();
    }
    public Property<Date> registrationExpiryDateProperty() {
        return registrationExpiryDate;
    }
    public void setRegistrationExpiryDate(Date registrationExpiryDate) {
        this.registrationExpiryDate.setValue(registrationExpiryDate);
    }

    public boolean isInsured() {
        return insured.get();
    }
    public BooleanProperty insuredProperty() {
        return insured;
    }
    public void setInsured(boolean insured) {
        this.insured.set(insured);
    }

    public String getModel() {
        return model.get();
    }
    public StringProperty modelProperty() {
        return model;
    }
    public void setModel(String model) {
        this.model.set(model);
    }

    public String getMake() {
        return make.get();
    }
    public StringProperty makeProperty() {
        return make;
    }
    public void setMake(String make) {
        this.make.set(make);
    }

    public String getColour() {
        return colour.get();
    }
    public StringProperty colourProperty() {
        return colour;
    }
    public void setColour(String colour) {
        this.colour.set(colour);
    }

    public int getSeats() {
        return seats.get();
    }
    public IntegerProperty seatsProperty() {
        return seats;
    }
    public void setSeats(int seats) {
        this.seats.set(seats);
    }

    public Date getStartDate() {
        return startDate.getValue();
    }
    public Property<Date> startDateProperty() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate.setValue(startDate);
    }

    public Date getEndDate() {
        return endDate.getValue();
    }
    public Property<Date> endDateProperty() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate.setValue(endDate);
    }

    public int getCostMultiplier() {
        return costMultiplier.get();
    }
    public IntegerProperty costMultiplierProperty() {
        return costMultiplier;
    }
    public void setCostMultiplier(int costMultiplier) {
        this.costMultiplier.set(costMultiplier);
    }
}

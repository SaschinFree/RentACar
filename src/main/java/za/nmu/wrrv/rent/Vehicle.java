package za.nmu.wrrv.rent;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.ucanaccess.converters.Functions;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Vehicle
{
    protected static final ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();
    private final StringProperty vehicleRegistration = new SimpleStringProperty();
    private final IntegerProperty clientNumber = new SimpleIntegerProperty();
    private final Property<Date> registrationExpiryDate = new SimpleObjectProperty<>();
    private final StringProperty insured = new SimpleStringProperty();
    private final StringProperty make = new SimpleStringProperty();
    private final StringProperty model = new SimpleStringProperty();
    private final StringProperty colour = new SimpleStringProperty();
    private final IntegerProperty seats = new SimpleIntegerProperty();
    private final Property<Date> startDate = new SimpleObjectProperty<>();
    private final Property<Date> endDate = new SimpleObjectProperty<>();
    private final DoubleProperty costMultiplier = new SimpleDoubleProperty();
    private final BooleanProperty active = new SimpleBooleanProperty();

    public Vehicle(String vehicleRegistration, int clientNumber, Date registrationExpiryDate, String insured, String make, String model, String colour, int seats, Date startDate, Date endDate, double costMultiplier)
    {
        this.vehicleRegistration.set(vehicleRegistration);
        this.clientNumber.set(clientNumber);
        this.registrationExpiryDate.setValue(registrationExpiryDate);
        this.insured.set(insured);
        this.make.set(make);
        this.model.set(model);
        this.colour.set(colour);
        this.seats.set(seats);
        this.startDate.setValue(startDate);
        this.endDate.setValue(endDate);
        this.costMultiplier.set(costMultiplier);
        active.set(true);
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

            boolean thisActive = result.getBoolean("active");

            if(thisActive)
            {
                Vehicle thisVehicle = new Vehicle(thisVehicleRegistration, thisClientNumber, thisRegistrationExpiryDate, isInsured, thisMake, thisModel, thisColour, thisSeats, thisStartDate, thisEndDate, thisCostMultiplier);
                vehicleList.add(thisVehicle);

                thisVehicle.active.set(true);
            }
        }
        return vehicleList;
    }
    public static ObservableList<Vehicle> searchQuery(String tableColumn, String search)
    {
        if(search.isEmpty())
            return null;

        switch (tableColumn)
        {
            case "vehicleRegistration" ->
                    {
                        return FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getVehicleRegistration().contains(search)).toList());
                    }
            case "clientNumber" ->
                    {
                        if(Functions.isNumeric(search))
                            return FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && String.valueOf(vehicle.getClientNumber()).contains(search)).toList());
                    }
            case "registrationExpiryDate" ->
                    {
                        return FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getRegistrationExpiryDate().equals(Date.valueOf(search))).toList());
                    }
            case "insured" ->
                    {
                        String uppercase = String.valueOf(search.charAt(0)).toUpperCase();
                        String finalUppercase = search.replace(String.valueOf(search.charAt(0)), uppercase);

                        if(finalUppercase.equals("Yes") || finalUppercase.equals("No"))
                            return FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.isInsured().equals(finalUppercase)).toList());
                    }
            case "make" ->
                    {
                        return FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getMake().contains(search)).toList());
                    }
            case "model" ->
                    {
                        return FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getModel().contains(search)).toList());
                    }
            case "colour" ->
                    {
                        return FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getColour().contains(search)).toList());
                    }
            case "seats" ->
                    {
                        if(Functions.isNumeric(search))
                            return FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getSeats() == Integer.parseInt(search)).toList());
                    }
            case "startDate" ->
                    {
                        return FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getStartDate().equals(Date.valueOf(search))).toList());
                    }
            case "endDate" ->
                    {
                        return FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getEndDate().equals(Date.valueOf(search))).toList());
                    }
            case "costMultiplier" ->
                    {
                        return FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getCostMultiplier() == Double.parseDouble(search)).toList());
                    }
        }

        return null;
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
    public void setClientNumber(int clientNumber) {
        this.clientNumber.set(clientNumber);
    }

    public Date getRegistrationExpiryDate() {
        return registrationExpiryDate.getValue();
    }
    public void setRegistrationExpiryDate(Date registrationExpiryDate) {
        this.registrationExpiryDate.setValue(registrationExpiryDate);
    }

    public String isInsured() {
        return insured.get();
    }
    public void setInsured(String insured) {
        this.insured.set(insured);
    }

    public String getMake() {
        return make.get();
    }
    public StringProperty makeProperty() {
        return make;
    }

    public String getModel() {
        return model.get();
    }
    public StringProperty modelProperty() {
        return model;
    }

    public String getColour() {
        return colour.get();
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

    public Date getStartDate() {
        return startDate.getValue();
    }
    public void setStartDate(Date startDate) {
        this.startDate.setValue(startDate);
    }

    public Date getEndDate() {
        return endDate.getValue();
    }
    public void setEndDate(Date endDate) {
        this.endDate.setValue(endDate);
    }

    public double getCostMultiplier() {
        return costMultiplier.get();
    }
    public DoubleProperty costMultiplierProperty() {
        return costMultiplier;
    }
    public void setCostMultiplier(double costMultiplier) {
        this.costMultiplier.set(costMultiplier);
    }

    public boolean isActive()
    {
        return active.get();
    }
    public void setActive(boolean active)
    {
        this.active.set(active);
    }
}

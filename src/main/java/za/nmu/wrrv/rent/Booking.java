package za.nmu.wrrv.rent;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Booking
{
    private static final ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    private final IntegerProperty bookingNumber = new SimpleIntegerProperty();
    private final IntegerProperty clientNumber = new SimpleIntegerProperty();
    private final StringProperty vehicleRegistration = new SimpleStringProperty();
    private final Property<Date> startDate = new SimpleObjectProperty<>();
    private final Property<Date> endDate = new SimpleObjectProperty<>();
    private final DoubleProperty cost = new SimpleDoubleProperty();
    private final DoubleProperty companyCommission = new SimpleDoubleProperty();
    private final DoubleProperty ownerCommission = new SimpleDoubleProperty();
    private final BooleanProperty isBeingRented = new SimpleBooleanProperty();
    private final BooleanProperty hasPaid = new SimpleBooleanProperty();
    private final BooleanProperty active = new SimpleBooleanProperty();

    public Booking(int bookingNumber, int clientNumber, String vehicleRegistration, Date startDate, Date endDate, double cost, double companyCommission, double ownerCommission, boolean isBeingRented, boolean hasPaid)
    {
        this.bookingNumber.set(bookingNumber);
        this.clientNumber.set(clientNumber);
        this.vehicleRegistration.set(vehicleRegistration);
        this.startDate.setValue(startDate);
        this.endDate.setValue(endDate);
        this.cost.set(cost);
        this.companyCommission.set(companyCommission);
        this.ownerCommission.set(ownerCommission);
        this.isBeingRented.set(isBeingRented);
        this.hasPaid.set(hasPaid);
        active.set(true);
    }
    public static ObservableList<Booking> getBookings() throws SQLException
    {
        String sql = "SELECT * FROM Booking";
        ResultSet result = RentACar.statement.executeQuery(sql);

        while(result.next())
        {
            int thisBookingNumber = result.getInt("bookingNumber");
            int thisClientNumber = result.getInt("clientNumber");
            String thisVehicleRegistration = result.getString("vehicleRegistration");
            Date thisStartDate = result.getDate("startDate");
            Date thisEndDate = result.getDate("endDate");
            int thisCost = result.getInt("cost");
            int thisCompanyCommission = result.getInt("companyCommission");
            int thisOwnerCommission = result.getInt("ownerCommission");
            boolean thisBeingRented = result.getBoolean("isBeingRented");
            boolean thisPaid = result.getBoolean("hasPaid");

            boolean thisActive = result.getBoolean("active");

            if(thisActive)
            {
                bookingList.add(new Booking(
                thisBookingNumber,
                thisClientNumber,
                thisVehicleRegistration,
                thisStartDate,
                thisEndDate,
                thisCost,
                thisCompanyCommission,
                thisOwnerCommission,
                thisBeingRented,
                thisPaid));
            }
        }
        return bookingList;
    }
    public static ObservableList<Booking> searchQuery(String tableColumn, String searchQuery) throws SQLException
    {
        ObservableList<Booking> thisList = FXCollections.observableArrayList();
        String sql;

        searchQuery = searchQuery.replace("/", "-");
        if(searchQuery.contains("-"))
            sql = "SELECT * FROM Client WHERE " + tableColumn + " = \'" + Date.valueOf(searchQuery) + "\'";
        else
            sql = "SELECT * FROM Client WHERE " + tableColumn + " LIKE \'" + "*" + searchQuery + "*" + "\'";

        ResultSet result = RentACar.statement.executeQuery(sql);

        while(result.next())
        {
            int thisBookingNumber = result.getInt("bookingNumber");
            int thisClientNumber = result.getInt("clientNumber");
            String thisVehicleRegistration = result.getString("vehicleRegistration");
            Date thisStartDate = result.getDate("startDate");
            Date thisEndDate = result.getDate("endDate");
            int thisCost = result.getInt("cost");
            int thisCompanyCommission = result.getInt("companyCommission");
            int thisOwnerCommission = result.getInt("ownerCommission");
            boolean thisBeingRented = result.getBoolean("isBeingRented");
            boolean thisPaid = result.getBoolean("hasPaid");

            boolean thisActive = result.getBoolean("active");

            if(thisActive)
            {
                thisList.add(new Booking(
                        thisBookingNumber,
                        thisClientNumber,
                        thisVehicleRegistration,
                        thisStartDate,
                        thisEndDate,
                        thisCost,
                        thisCompanyCommission,
                        thisOwnerCommission,
                        thisBeingRented,
                        thisPaid));
            }
        }
        return thisList;
    }

    public int getBookingNumber()
    {
        return bookingNumber.get();
    }
    public IntegerProperty bookingNumberProperty()
    {
        return bookingNumber;
    }
    public void setBookingNumber(int bookingNumber)
    {
        this.bookingNumber.set(bookingNumber);
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

    public String getVehicleRegistration() {
        return vehicleRegistration.get();
    }
    public StringProperty vehicleRegistrationProperty() {
        return vehicleRegistration;
    }
    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration.set(vehicleRegistration);
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

    public double getCost() {
        return cost.get();
    }
    public DoubleProperty costProperty() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public double getCompanyCommission() {
        return companyCommission.get();
    }
    public DoubleProperty companyCommissionProperty() {
        return companyCommission;
    }
    public void setCompanyCommission(double companyCommission) {
        this.companyCommission.set(companyCommission);
    }

    public double getOwnerCommission() {
        return ownerCommission.get();
    }
    public DoubleProperty ownerCommissionProperty() {
        return ownerCommission;
    }
    public void setOwnerCommission(double ownerCommission) {
        this.ownerCommission.set(ownerCommission);
    }

    public boolean isIsBeingRented() {
        return isBeingRented.get();
    }
    public BooleanProperty isBeingRentedProperty() {
        return isBeingRented;
    }
    public void setIsBeingRented(boolean isBeingRented) {
        this.isBeingRented.set(isBeingRented);
    }

    public boolean isHasPaid() {
        return hasPaid.get();
    }
    public BooleanProperty hasPaidProperty() {
        return hasPaid;
    }
    public void setHasPaid(boolean hasPaid) {
        this.hasPaid.set(hasPaid);
    }

    public boolean isActive() {
        return active.get();
    }
    public BooleanProperty activeProperty() {
        return active;
    }
    public void setActive(boolean active) {
        this.active.set(active);
    }
}

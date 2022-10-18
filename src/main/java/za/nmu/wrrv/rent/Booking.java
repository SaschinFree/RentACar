package za.nmu.wrrv.rent;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.ucanaccess.converters.Functions;

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
    private final StringProperty isBeingRented = new SimpleStringProperty();
    private final StringProperty hasPaid = new SimpleStringProperty();
    private final BooleanProperty active = new SimpleBooleanProperty();

    public Booking(int bookingNumber, int clientNumber, String vehicleRegistration, Date startDate, Date endDate, double cost, double companyCommission, double ownerCommission, String isBeingRented, String hasPaid)
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
            double thisCost = result.getDouble("cost");
            int thisCompanyCommission = result.getInt("companyCommission");
            int thisOwnerCommission = result.getInt("ownerCommission");
            boolean thisBeingRented = result.getBoolean("isBeingRented");

            String rented = "No";
            if(thisBeingRented)
                rented = "Yes";

            boolean thisPaid = result.getBoolean("hasPaid");

            String paid = "No";
            if(thisPaid)
                paid = "Yes";

            boolean thisActive = result.getBoolean("active");

            if(thisActive)
            {
                Booking thisBooking = new Booking(thisBookingNumber, thisClientNumber, thisVehicleRegistration, thisStartDate, thisEndDate, thisCost, thisCompanyCommission, thisOwnerCommission, rented, paid);
                bookingList.add(thisBooking);

                thisBooking.setActive(true);
            }
        }
        return bookingList;
    }
    public static ObservableList<Booking> searchQuery(String tableColumn, String search)
    {
        if(search.isEmpty())
            return null;

        switch(tableColumn)
        {
            case "bookingNumber" ->
                    {
                        if(Functions.isNumeric(search))
                            return FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && String.valueOf(booking.getBookingNumber()).contains(search)).toList());
                    }
            case "clientNumber" ->
                    {
                        if(Functions.isNumeric(search))
                            return FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && String.valueOf(booking.getClientNumber()).contains(search)).toList());
                    }
            case "vehicleRegistration" ->
                    {
                        return FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.getVehicleRegistration().contains(search)).toList());
                    }
            case "startDate" ->
                    {
                        return FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.getStartDate().equals(Date.valueOf(search))).toList());
                    }
            case "endDate" ->
                    {
                        return FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.getEndDate().equals(Date.valueOf(search))).toList());
                    }
            case "cost" ->
                    {
                        if(Functions.isNumeric(search))
                        {
                            String finalSearch = search.replace(",", ".");
                            return FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.getCost() == Double.parseDouble(finalSearch)).toList());
                        }
                    }
            case "companyCommission" ->
                    {
                        if(Functions.isNumeric(search))
                            return FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.getCompanyCommission() == Double.parseDouble(search)).toList());
                    }
            case "ownerCommission" ->
                    {
                        if(Functions.isNumeric(search))
                            return FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.getOwnerCommission() == Double.parseDouble(search)).toList());
                    }
            case "isBeingRented" ->
                    {
                        String finalUppercase = search.replace(String.valueOf(search.charAt(0)), String.valueOf(search.charAt(0)).toUpperCase());

                        if (finalUppercase.equals("Yes") || finalUppercase.equals("No"))
                            return FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.isIsBeingRented().equals(finalUppercase)).toList());
                    }
            case "hasPaid" ->
                    {
                        String finalUppercase = search.replace(String.valueOf(search.charAt(0)), String.valueOf(search.charAt(0)).toUpperCase());

                        if (finalUppercase.equals("Yes") || finalUppercase.equals("No"))
                            return FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.isHasPaid().equals(finalUppercase)).toList());
                    }
        }

        return null;
    }

    public int getBookingNumber()
    {
        return bookingNumber.get();
    }
    public IntegerProperty bookingNumberProperty()
    {
        return bookingNumber;
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
    public void setStartDate(Date startDate) {
        this.startDate.setValue(startDate);
    }

    public Date getEndDate() {
        return endDate.getValue();
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

    public double getOwnerCommission() {
        return ownerCommission.get();
    }

    public String isIsBeingRented() {
        return isBeingRented.get();
    }
    public void setIsBeingRented(String isBeingRented) {
        this.isBeingRented.set(isBeingRented);
    }

    public String isHasPaid() {
        return hasPaid.get();
    }
    public void setHasPaid(String hasPaid) {
        this.hasPaid.set(hasPaid);
    }

    public boolean isActive() {
        return active.get();
    }
    public void setActive(boolean active) {
        this.active.set(active);
    }
}

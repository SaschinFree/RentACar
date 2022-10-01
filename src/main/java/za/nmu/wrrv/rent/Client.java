package za.nmu.wrrv.rent;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client
{
    private static final ObservableList<Client> clientList = FXCollections.observableArrayList();
    private final IntegerProperty clientNumber = new SimpleIntegerProperty();
    private final StringProperty clientID = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty surname = new SimpleStringProperty();
    private final StringProperty contactNumber = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final Property<Date> licenceExpiryDate = new SimpleObjectProperty<>();
    private final IntegerProperty streetNumber = new SimpleIntegerProperty();
    private final StringProperty streetName = new SimpleStringProperty();
    private final StringProperty suburb = new SimpleStringProperty();
    private final StringProperty city = new SimpleStringProperty();
    private final IntegerProperty postalCode = new SimpleIntegerProperty();
    private final StringProperty companyName = new SimpleStringProperty();
    private final DoubleProperty moneyOwed = new SimpleDoubleProperty();
    private final BooleanProperty active = new SimpleBooleanProperty();

    public Client(int clientNumber, String clientID, String firstName, String surname, String contactNumber, String email, Date licenceExpiryDate, int streetNumber, String streetName, String suburb, String city, int postalCode, String companyName, double moneyOwed)
    {
        this.clientNumber.set(clientNumber);
        this.clientID.set(clientID);
        this.firstName.set(firstName);
        this.surname.set(surname);
        this.contactNumber.set(contactNumber);
        this.email.set(email);
        this.licenceExpiryDate.setValue(licenceExpiryDate);
        this.streetNumber.set(streetNumber);
        this.streetName.set(streetName);
        this.suburb.set(suburb);
        this.city.set(city);
        this.postalCode.set(postalCode);
        this.companyName.set(companyName);
        this.moneyOwed.set(moneyOwed);
    }

    public static ObservableList<Client> getClients() throws SQLException
    {
        String sql = "SELECT * FROM Client";
        ResultSet result = RentACar.statement.executeQuery(sql);

        while(result.next())
        {
            int thisClientNumber = result.getInt("clientNumber");
            String thisClientID = result.getString("clientID");
            String thisFirstName = result.getString("firstName");
            String thisSurname = result.getString("surname");
            String thisContactNumber = result.getString("contactNumber");
            String thisEmail = result.getString("email");
            Date thisLicenceExpiryDate = result.getDate("licenceExpiryDate");
            int thisStreetNumber = result.getInt("streetNumber");
            String thisStreetName = result.getString("streetName");
            String thisSuburb = result.getString("suburb");
            String thisCity = result.getString("city");
            int thisPostalCode = result.getInt("postalCode");
            String thisCompanyName = result.getString("companyName");
            double thisMoneyOwed = result.getDouble("moneyOwed");

            boolean thisActive = result.getBoolean("active");

            if(thisActive)
            {
                Client thisClient = new Client(thisClientNumber, thisClientID, thisFirstName, thisSurname, thisContactNumber, thisEmail, thisLicenceExpiryDate, thisStreetNumber, thisStreetName, thisSuburb, thisCity, thisPostalCode, thisCompanyName, thisMoneyOwed);
                clientList.add(thisClient);

                thisClient.setActive(true);
            }
        }
        return clientList;
    }
    public static ObservableList<Client> searchQuery(String tableColumn, String searchQuery, String extraParameter) throws SQLException
    {
        ObservableList<Client> thisList = FXCollections.observableArrayList();
        String sql;

        searchQuery = searchQuery.replace("/", "-");
        if(searchQuery.contains("-"))
            sql = "SELECT * FROM Client WHERE " + tableColumn + " = \'" + Date.valueOf(searchQuery) + "\' " + extraParameter + "";
        else
        {
            if(searchQuery.contains(".") || searchQuery.contains("Yes") || searchQuery.contains("No"))
                sql = "SELECT * FROM Client WHERE " + tableColumn + " = " + searchQuery + " " + extraParameter  + "";
            else
                sql = "SELECT * FROM Client WHERE " + tableColumn + " LIKE \'" + searchQuery + "\' " + extraParameter + "";
        }

        ResultSet result = RentACar.statement.executeQuery(sql);

        while(result.next())
        {
            int thisClientNumber = result.getInt("clientNumber");
            String thisClientID = result.getString("clientID");
            String thisFirstName = result.getString("firstName");
            String thisSurname = result.getString("surname");
            String thisContactNumber = result.getString("contactNumber");
            String thisEmail = result.getString("email");
            Date thisLicenceExpiryDate = result.getDate("licenceExpiryDate");
            int thisStreetNumber = result.getInt("streetNumber");
            String thisStreetName = result.getString("streetName");
            String thisSuburb = result.getString("suburb");
            String thisCity = result.getString("city");
            int thisPostalCode = result.getInt("postalCode");
            String thisCompanyName = result.getString("companyName");
            double thisMoneyOwed = result.getDouble("moneyOwed");

            boolean thisActive = result.getBoolean("active");

            if(thisActive)
            {
                Client thisClient = new Client(thisClientNumber, thisClientID, thisFirstName, thisSurname, thisContactNumber, thisEmail, thisLicenceExpiryDate, thisStreetNumber, thisStreetName, thisSuburb, thisCity, thisPostalCode, thisCompanyName, thisMoneyOwed);
                thisList.add(thisClient);

                thisClient.setActive(true);
            }
        }
        return thisList;
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

    public String getClientID() {
        return clientID.get();
    }
    public StringProperty clientIDProperty() {
        return clientID;
    }
    public void setClientID(String clientID) {
        this.clientID.set(clientID);
    }

    public String getFirstName() {
        return firstName.get();
    }
    public StringProperty firstNameProperty() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getSurname() {
        return surname.get();
    }
    public StringProperty surnameProperty() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getContactNumber() {
        return contactNumber.get();
    }
    public StringProperty contactNumberProperty() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber.set(contactNumber);
    }

    public String getEmail() {
        return email.get();
    }
    public StringProperty emailProperty() {
        return email;
    }
    public void setEmail(String email) {
        this.email.set(email);
    }

    public Date getLicenceExpiryDate() {
        return licenceExpiryDate.getValue();
    }
    public Property<Date> licenceExpiryDateProperty() {
        return licenceExpiryDate;
    }
    public void setLicenceExpiryDate(Date licenceExpiryDate) {
        this.licenceExpiryDate.setValue(licenceExpiryDate);
    }

    public int getStreetNumber() {
        return streetNumber.get();
    }
    public IntegerProperty streetNumberProperty() {
        return streetNumber;
    }
    public void setStreetNumber(int streetNumber) {
        this.streetNumber.set(streetNumber);
    }

    public String getStreetName() {
        return streetName.get();
    }
    public StringProperty streetNameProperty() {
        return streetName;
    }
    public void setStreetName(String streetName) {
        this.streetName.set(streetName);
    }

    public String getSuburb() {
        return suburb.get();
    }
    public StringProperty suburbProperty() {
        return suburb;
    }
    public void setSuburb(String suburb) {
        this.suburb.set(suburb);
    }

    public int getPostalCode() {
        return postalCode.get();
    }
    public IntegerProperty postalCodeProperty() {
        return postalCode;
    }
    public void setPostalCode(int postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getCity() {
        return city.get();
    }
    public StringProperty cityProperty() {
        return city;
    }
    public void setCity(String city) {
        this.city.set(city);
    }

    public String getCompanyName() {
        return companyName.get();
    }
    public StringProperty companyNameProperty() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public double getMoneyOwed() {
        return moneyOwed.get();
    }
    public DoubleProperty moneyOwedProperty() {
        return moneyOwed;
    }
    public void setMoneyOwed(double moneyOwed) {
        this.moneyOwed.set(moneyOwed);
    }

    public boolean isActive()
    {
        return active.get();
    }
    public BooleanProperty activeProperty()
    {
        return active;
    }
    public void setActive(boolean active)
    {
        this.active.set(active);
    }
}

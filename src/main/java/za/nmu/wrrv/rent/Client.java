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
    private IntegerProperty clientID = new SimpleIntegerProperty();
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty surname = new SimpleStringProperty();
    private StringProperty contactNumber = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private Property<Date> licenceExpiryDate = new SimpleObjectProperty<>();
    private IntegerProperty streetNumber = new SimpleIntegerProperty();
    private StringProperty streetName = new SimpleStringProperty();
    private StringProperty suburb = new SimpleStringProperty();
    private IntegerProperty postalCode = new SimpleIntegerProperty();
    private StringProperty city = new SimpleStringProperty();
    private StringProperty companyName = new SimpleStringProperty();
    private IntegerProperty moneyOwed = new SimpleIntegerProperty();

    public Client(int clientID, String firstName, String surname, String contactNumber, String email, Date licenceExpiryDate, int streetNumber, String streetName, String suburb, int postalCode, String city, String companyName, int moneyOwed)
    {
        this.clientID.set(clientID);
        this.firstName.set(firstName);
        this.surname.set(surname);
        this.contactNumber.set(contactNumber);
        this.email.set(email);
        this.licenceExpiryDate.setValue(licenceExpiryDate);
        this.streetNumber.set(streetNumber);
        this.streetName.set(streetName);
        this.suburb.set(suburb);
        this.postalCode.set(postalCode);
        this.city.set(city);
        this.companyName.set(companyName);
        this.moneyOwed.set(moneyOwed);
    }

    public static ObservableList<Client> getClients() throws SQLException
    {
        String sql = "SELECT * FROM Client";
        ResultSet result = RentACar.statement.executeQuery(sql);

        while(result.next())
        {
            int thisClientID = result.getInt("clientID");
            String thisFirstName = result.getString("firstName");
            String thisSurname = result.getString("surname");
            String thisContactNumber = result.getString("contactNumber");
            String thisEmail = result.getString("email");
            Date thisLicenceExpiryDate = result.getDate("licenceExpiryDate");
            int thisStreetNumber = result.getInt("streetNumber");
            String thisStreetName = result.getString("streetName");
            String thisSuburb = result.getString("suburb");
            int thisPostalCode = result.getInt("postalCode");
            String thisCity = result.getString("city");
            String thisCompanyName = result.getString("companyName");
            int thisMoneyOwed = result.getInt("moneyOwed");

            clientList.add(new Client(
                    thisClientID,
                    thisFirstName,
                    thisSurname,
                    thisContactNumber,
                    thisEmail,
                    thisLicenceExpiryDate,
                    thisStreetNumber,
                    thisStreetName,
                    thisSuburb,
                    thisPostalCode,
                    thisCity,
                    thisCompanyName,
                    thisMoneyOwed));
        }
        return clientList;
    }

    public int getClientID() {
        return clientID.get();
    }
    public IntegerProperty clientIDProperty() {
        return clientID;
    }
    public void setClientID(int clientID) {
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

    public int getMoneyOwed() {
        return moneyOwed.get();
    }
    public IntegerProperty moneyOwedProperty() {
        return moneyOwed;
    }
    public void setMoneyOwed(int moneyOwed) {
        this.moneyOwed.set(moneyOwed);
    }

    @Override
    public String toString()
    {
        return "ID: " + getClientID() + "\n" +
                "Name: " + getFirstName() + "\n" +
                "Surname: " + getSurname() + "\n" +
                "Number: " + getContactNumber() + "\n" +
                "Email: " + getEmail() + "\n" +
                "Licence: " + getLicenceExpiryDate() + "\n" +
                "Street Number: " + getStreetNumber() + "\n" +
                "Street Name: " + getStreetName() + "\n" +
                "Suburb: " + getSuburb() + "\n" +
                "Postal Code: " + getPostalCode() + "\n" +
                "City: " + getCity() + "\n" +
                "Company Name: " + getCompanyName() + "\n" +
                "Money Owed: " + getMoneyOwed() + "\n";
    }
}

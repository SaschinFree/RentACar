package za.nmu.wrrv.rent;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.ucanaccess.converters.Functions;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
    private final StringProperty streetNumber = new SimpleStringProperty();
    private final StringProperty streetName = new SimpleStringProperty();
    private final StringProperty suburb = new SimpleStringProperty();
    private final StringProperty city = new SimpleStringProperty();
    private final StringProperty postalCode = new SimpleStringProperty();
    private final StringProperty companyName = new SimpleStringProperty();
    private final DoubleProperty moneyOwed = new SimpleDoubleProperty();
    private final BooleanProperty active = new SimpleBooleanProperty();

    public Client(int clientNumber, String clientID, String firstName, String surname, String contactNumber, String email, Date licenceExpiryDate, String streetNumber, String streetName, String suburb, String city, String postalCode, String companyName, double moneyOwed)
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
        active.set(true);
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
            String thisStreetNumber = result.getString("streetNumber");
            String thisStreetName = result.getString("streetName");
            String thisSuburb = result.getString("suburb");
            String thisCity = result.getString("city");
            String thisPostalCode = result.getString("postalCode");
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
    public static ObservableList<Client> searchQuery(String tableColumn, String search)
    {
        if(search.isEmpty())
            return null;

        switch(tableColumn)
        {
            case "clientNumber" ->
                    {
                        if(Functions.isNumeric(search))
                            return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && String.valueOf(client.getClientNumber()).contains(search)).toList());
                    }
            case "clientID" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getClientID().contains(search)).toList());
                    }
            case "firstName" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getFirstName().contains(search)).toList());
                    }
            case "surname" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getSurname().contains(search)).toList());
                    }
            case "contactNumber" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getContactNumber().contains(search)).toList());
                    }
            case "email" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getEmail().contains(search)).toList());
                    }
            case "licenceExpiryDate" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getLicenceExpiryDate().equals(Date.valueOf(search))).toList());
                    }
            case "streetNumber" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getStreetNumber().contains(search)).toList());
                    }
            case "streetName" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getStreetName().contains(search)).toList());
                    }
            case "suburb" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getSuburb().contains(search)).toList());
                    }
            case "city" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getCity().contains(search)).toList());
                    }
            case "postalCode" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getPostalCode().contains(search)).toList());
                    }
            case "companyName" ->
                    {
                        return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getCompanyName().contains(search)).toList());
                    }
            case "moneyOwed" ->
                    {
                        if(Functions.isNumeric(search))
                        {
                            String finalSearch = search.replace(",", ".");
                            return FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getMoneyOwed() == Double.parseDouble(finalSearch)).toList());
                        }
                    }
        }

        return null;
    }

    public int getClientNumber() {
        return clientNumber.get();
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
    public void setEmail(String email) {
        this.email.set(email);
    }

    public Date getLicenceExpiryDate() {
        return licenceExpiryDate.getValue();
    }
    public void setLicenceExpiryDate(Date licenceExpiryDate) {
        this.licenceExpiryDate.setValue(licenceExpiryDate);
    }

    public String getStreetNumber() {
        return streetNumber.get();
    }
    public void setStreetNumber(String streetNumber) {
        this.streetNumber.set(streetNumber);
    }

    public String getStreetName() {
        return streetName.get();
    }
    public void setStreetName(String streetName) {
        this.streetName.set(streetName);
    }

    public String getSuburb() {
        return suburb.get();
    }
    public void setSuburb(String suburb) {
        this.suburb.set(suburb);
    }

    public String getPostalCode() {
        return postalCode.get();
    }
    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getCity() {
        return city.get();
    }
    public void setCity(String city) {
        this.city.set(city);
    }

    public String getCompanyName() {
        return companyName.get();
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
    public void setActive(boolean active)
    {
        this.active.set(active);
    }
}

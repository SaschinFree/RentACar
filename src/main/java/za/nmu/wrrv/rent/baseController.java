package za.nmu.wrrv.rent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class baseController implements Initializable
{
    @FXML
    protected BorderPane main;
    @FXML
    protected Button login;
    @FXML
    protected ImageView loginIcon;
    @FXML
    protected Label logged;
    @FXML
    protected Label user;

    protected static BorderPane mainReference;
    protected static final SceneLoader thisScene = new SceneLoader();
    protected static boolean isLoggedOn = false;
    protected static String userLoggedOn;
    protected static final Tooltip searchTip = new Tooltip("To continue using shortcuts, press tab after your search query");

    protected static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected static final StringConverter<LocalDate> dateConverter = new StringConverter<>()
    {
        @Override
        public String toString(LocalDate localDate)
        {
            if(localDate  != null)
                return dateFormat.format(localDate);
            return "";
        }

        @Override
        public LocalDate fromString(String s)
        {
            if(s != null && !s.isEmpty())
                return LocalDate.parse(s,dateFormat);
            return null;
        }
    };

    protected static final char[] symbolArray = new char[33];
    protected static final char[] letterArray = new char[52];
    protected static final char[] numberArray = new char[10];

    protected static ObservableList<Client> clients;
    protected static ObservableList<Vehicle> vehicles;
    protected static ObservableList<Booking> bookings;
    protected static ObservableList<Settings> settings;
    protected static ObservableList<Payment> payments;

    static
    {
        try
        {
            baseController.clients = Client.getClients();
            baseController.vehicles = Vehicle.getVehicles();
            baseController.bookings = Booking.getBookings();
            baseController.settings = Settings.getSettings();
            baseController.payments = Payment.getPayments();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        login.setTooltip(new Tooltip("Login/Logout"));

        mainReference = main;
        logged.setVisible(false);
        user.setVisible(false);

        setSymbolArray();
        setLetterArray();
        setNumberArray();

        login.setFocusTraversable(false);
    }

    @FXML
    protected void buttonClicked(MouseEvent mouseEvent) throws IOException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            String buttonId = ((Button) mouseEvent.getSource()).getId();

            switch (buttonId)
            {
                case "login" -> onLogin();
                case "logout" -> onLogout();
            }
        }
    }

    public void onLogout()
    {
        Alert alert;

        main.setCenter(null);

        isLoggedOn = false;
        userLoggedOn = null;

        clerkController.alertShown = false;
        adminController.alertShown = false;

        logged.setVisible(false);
        user.setVisible(false);

        alert = new Alert(AlertType.INFORMATION);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
        alert.setHeaderText("Logout successful");

        login.setId("login");
        loginIcon.setImage(new Image("/login.png"));

        alert.showAndWait();
    }
    public void onLogin() throws IOException
    {
        newScreen("login", "Login");

        if(isLoggedOn)
        {
            logged.setVisible(true);

            if(loginController.thisUser.isAdmin())
            {
                user.setText("Admin");
                userLoggedOn = "adminMenu";
            }
            else
            {
                user.setText("Clerk");
                userLoggedOn = "clerkMenu";
            }

            user.setVisible(true);

            login.setId("logout");
            loginIcon.setImage(new Image("/logout.png"));

            nextScene(userLoggedOn);
        }
    }

    private void setSymbolArray()
    {
        int start = 0;
        for(int i = 32; i < 127; i++)
        {
            switch (i)
            {
                case 48 -> i = 57;
                case 65 -> i = 90;
                case 97 -> i = 122;
                default ->
                        {
                    symbolArray[start] = (char) i;
                    start++;
                        }
            }
        }
    }
    private void setLetterArray()
    {
        int start = 0;
        for(int i = 65; i < 123; i++)
        {
            if (i == 91)
                i = 96;
            else
            {
                letterArray[start] = (char) i;
                start++;
            }
        }
    }
    private void setNumberArray()
    {
        int start = 0;
        for(int i = 48; i < 58; i++)
        {
            numberArray[start] = (char) i;
            start++;
        }
    }

    protected static boolean dateCheck(String thisDateString)
    {
        int dashCount = 0;
        for(Character thisChar : thisDateString.toCharArray())
        {
            if (thisChar =='-')
                dashCount++;
        }
        return errorValidationCheck(letterArray, thisDateString) && symbolCheck(thisDateString, '-') && thisDateString.length() == 10 && thisDateString.charAt(4) == '-' && thisDateString.charAt(7) == '-' && dashCount <= 2;
    }
    protected static boolean dateCheck(DatePicker thisDate, String thisDateString)
    {
        if(!dateCheck(thisDateString))
        {
            thisDate.setValue(null);

            return false;
        }
        return true;
    }
    protected static boolean errorValidationCheck(char[] thisArray, String thisValue)
    {
        for(Character symbol : thisArray)
        {
            if(thisValue.contains(symbol.toString()))
                return false;
        }
        return true;
    }
    protected static boolean symbolCheck(String thisValue)
    {
        for(Character symbol : baseController.symbolArray)
        {
            if(thisValue.contains(symbol.toString()))
                return false;
        }
        return true;
    }
    protected static boolean symbolCheck(String thisValue, char... extraParameter)
    {
        boolean check = true;
        for(Character symbol : baseController.symbolArray)
        {
            if(thisValue.contains(symbol.toString()))
            {
                for(char thisChar : extraParameter)
                {
                    if(symbol.equals(thisChar))
                        return true;
                    else
                        check = false;
                }
            }
        }
        return check;
    }

    protected static void deleteVehicles(Client thisClient) throws SQLException
    {
        ObservableList<Vehicle> clientVehicles = FXCollections.observableArrayList(baseController.vehicles.stream().filter(vehicle -> vehicle.isActive() && vehicle.getClientNumber() == thisClient.getClientNumber()).toList());

        if(clientVehicles.size() > 0)
        {
            String delete = "UPDATE Vehicle SET active = false WHERE active  = true AND clientNumber = \'" + thisClient.getClientNumber() + "\'";
            RentACar.statement.executeUpdate(delete);
            for(Vehicle thisVehicle : clientVehicles)
            {
                int index = 0;
                for(Vehicle vehicle : vehicles)
                {
                    if(vehicle.getVehicleRegistration().equals(thisVehicle.getVehicleRegistration()))
                    {
                        vehicle.setActive(false);
                        vehicles.set(index, vehicle);
                        deleteBookings(vehicle);
                        vehicles.removeAll(vehicle);
                        break;
                    }
                    index++;
                }
            }
        }
    }
    protected static void deleteBookings(Vehicle thisVehicle) throws SQLException
    {
        ObservableList<Booking> vehicleBookings = FXCollections.observableArrayList(bookings.stream().filter(booking -> booking.isActive() && booking.getVehicleRegistration().equals(thisVehicle.getVehicleRegistration()) && booking.isIsBeingRented().equals("No") && booking.getStartDate().equals(Date.valueOf(LocalDate.now())) || booking.getStartDate().after(Date.valueOf(LocalDate.now()))).toList());

        if(vehicleBookings.size() > 0)
        {
            String delete = "UPDATE Booking SET active = false WHERE active = true AND vehicleRegistration = \'" + thisVehicle.getVehicleRegistration() + "\' AND startDate >= \'" + Date.valueOf(LocalDate.now()) + "\'";
            RentACar.statement.executeUpdate(delete);
            for(Booking thisBooking : vehicleBookings)
            {
                int index = 0;
                for(Booking booking : bookings)
                {
                    if(booking.getBookingNumber() == thisBooking.getBookingNumber())
                    {
                        if(booking.isHasPaid().equals("Yes"))
                        {
                            String updateMoney = "UPDATE Client SET moneyOwed = moneyOwed + " + booking.getCost() + " WHERE clientNumber = \'" + booking.getClientNumber() + "\'";
                            RentACar.statement.executeUpdate(updateMoney);

                            ObservableList<Client> thisClient = FXCollections.observableArrayList(clients.stream().filter(client -> client.isActive() && client.getClientNumber() == booking.getClientNumber()).toList());
                            Client updatedClient = thisClient.get(0);

                            int index2 = 0;
                            for(Client client : baseController.clients)
                            {
                                if(client.getClientID().equals(updatedClient.getClientID()))
                                {
                                    client.setMoneyOwed(client.getMoneyOwed() + booking.getCost());
                                    baseController.clients.set(index2, client);
                                    break;
                                }
                                index2++;
                            }
                        }
                        booking.setActive(false);
                        bookings.set(index, booking);
                        bookings.removeAll(booking);
                        break;
                    }
                    index++;
                }
            }
        }
    }

    protected static void newScreen(String screenName, String title) throws IOException
    {
        FXMLLoader newScreenLoader = new FXMLLoader(RentACar.class.getResource(screenName + ".fxml"));
        Scene newScreenScene = new Scene(newScreenLoader.load());
        Stage newScreenStage = new Stage();

        newScreenStage.getIcons().add(new Image("icon.png"));

        newScreenStage.setScene(newScreenScene);
        newScreenStage.setTitle(title);
        newScreenStage.setResizable(false);
        newScreenStage.initModality(Modality.WINDOW_MODAL);
        newScreenStage.initOwner(RentACar.mainStage);

        newScreenStage.showAndWait();
    }
    protected static void nextScene(String sceneName)
    {
        mainReference.setCenter(thisScene.getPage(sceneName).getRoot());
    }
}
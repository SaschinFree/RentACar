package za.nmu.wrrv.rent;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class baseController implements Initializable
{
    @FXML
    protected BorderPane main;
    @FXML
    protected Button logout;
    @FXML
    protected Label logged;
    @FXML
    protected Label user;

    protected static BorderPane mainReference;
    protected static final SceneLoader thisScene = new SceneLoader();
    protected static boolean isLoggedOn = false;
    protected static String userLoggedOn;

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
        mainReference = main;
        logged.setVisible(false);
        user.setVisible(false);

        setSymbolArray();
        setLetterArray();
        setNumberArray();
    }

    @FXML
    protected void logoutClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Alert alert;
            if(isLoggedOn)
            {
                main.setCenter(null);

                isLoggedOn = false;
                userLoggedOn = null;

                logged.setVisible(false);
                user.setVisible(false);

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setHeaderText("Logout successful");
            }
            else
            {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("User is not logged in");
            }
            alert.showAndWait();
        }
    }
    @FXML
    protected void loginClicked(MouseEvent mouseEvent) throws IOException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            if(!isLoggedOn)
            {
                FXMLLoader loginLoader = new FXMLLoader(RentACar.class.getResource("login.fxml"));
                Scene loginScene = new Scene(loginLoader.load());
                Stage loginStage = new Stage();

                loginStage.setScene(loginScene);
                loginStage.setTitle("Login");
                loginStage.setResizable(false);
                loginStage.initModality(Modality.WINDOW_MODAL);
                loginStage.initOwner(RentACar.mainStage);

                loginStage.showAndWait();

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

                    nextScene(userLoggedOn);
                }
            }
            else
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("User is already logged in");
                alert.showAndWait();
            }
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

    protected static boolean dateCheck(DatePicker thisDate, String thisDateString)
    {
        if(!errorValidationCheck(letterArray, thisDateString) | !symbolCheck(thisDateString, '-'))
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
    protected static boolean symbolCheck(String thisValue, char extraParameter)
    {
        for(Character symbol : baseController.symbolArray)
        {
            if(thisValue.contains(symbol.toString()) & !symbol.equals(extraParameter))
                return false;
        }
        return true;
    }

    protected static void newScreen(String screenName, String title) throws IOException
    {
        FXMLLoader newScreenLoader = new FXMLLoader(RentACar.class.getResource(screenName + ".fxml"));
        Scene newScreenScene = new Scene(newScreenLoader.load());
        Stage newScreenStage = new Stage();

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
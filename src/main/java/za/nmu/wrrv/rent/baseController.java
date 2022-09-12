package za.nmu.wrrv.rent;


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

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class baseController implements Initializable
{    @FXML
    protected BorderPane main;
    @FXML
    protected Button logout;

    protected static BorderPane mainReference;
    protected static final SceneLoader thisScene = new SceneLoader();
    protected static boolean isLoggedOn = false;
    protected static String userLoggedOn;

    protected static final char[] symbols = new char[84];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        mainReference = main;

        int start = 0;
        for(int i = 32; i < 127; i++)
        {
            switch(i)
            {
                case 46:
                    break;
                case 48:
                    i = 57;
                    break;
                default:
                    symbols[start] = (char) i;
                    start++;
            }
        }
    }

    @FXML
    protected void logoutClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Alert alert;
            if(isLoggedOn)
            {
                disconnectToDB();
                main.setCenter(null);
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setHeaderText("Logout successful");
                alert.showAndWait();
                isLoggedOn = false;
                userLoggedOn = "";
            }
            else
            {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("User is not logged in");
                alert.showAndWait();
            }
        }
    }
    @FXML
    protected void loginClicked(MouseEvent mouseEvent) throws IOException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            if(!isLoggedOn)
            {
                if(connectToDB())
                {
                    FXMLLoader loginLoader = new FXMLLoader(RentACar.class.getResource("login.fxml"));
                    Scene loginScene = new Scene(loginLoader.load());
                    Stage loginStage = new Stage();

                    loginStage.setScene(loginScene);
                    loginStage.setTitle("Login");
                    loginStage.setResizable(false);
                    loginStage.initModality(Modality.WINDOW_MODAL);
                    loginStage.initOwner(RentACar.mainStage);

                    loginStage.show();
                }
                else
                    System.exit(0);
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
    private boolean connectToDB()
    {
        try
        {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            RentACar.connection = DriverManager.getConnection("jdbc:ucanaccess://RentACar.accdb");
            RentACar.statement = RentACar.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            return true;
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Database Connection Failed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

            return false;
        }
    }
    private void disconnectToDB()
    {
        RentACar.connection = null;
        RentACar.statement = null;
    }
    protected static void nextScene(String sceneName)
    {
        mainReference.setCenter(thisScene.getPage(sceneName).getRoot());
    }
}
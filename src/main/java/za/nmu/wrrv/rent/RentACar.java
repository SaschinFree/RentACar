package za.nmu.wrrv.rent;

import java.sql.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class RentACar extends Application
{
    protected static Connection connection = null;
    protected static Statement statement = null;
    protected static Stage mainStage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader mainLoader = new FXMLLoader(RentACar.class.getResource("main.fxml"));
        Scene mainScene = new Scene(mainLoader.load());

        if(connectToDB())
        {
            stage.setScene(mainScene);
            stage.setTitle("Main");
            stage.setResizable(false);
            mainStage = stage;

            stage.show();
        }
        else
            System.exit(0);
    }
    private boolean connectToDB()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:ucanaccess://RentACar.accdb");
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

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
}
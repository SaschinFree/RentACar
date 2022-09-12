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

        stage.setScene(mainScene);
        stage.setTitle("Main");
        stage.setResizable(false);
        mainStage = stage;

        stage.show();
    }
}
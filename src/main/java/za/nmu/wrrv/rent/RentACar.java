package za.nmu.wrrv.rent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RentACar extends Application
{
    Stage rootStage;
    Stage loginStage;
    Stage mainScreen;

    FXMLLoader loginLoader;
    FXMLLoader mainLoader;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        rootStage = stage;

        login();

        loginStage.show();
    }
    private Stage login() throws IOException
    {
        loginStage = new Stage();

        loginLoader = new FXMLLoader(RentACar.class.getResource("login.fxml"));

        Scene loginScene = new Scene(loginLoader.load());

        loginStage.setScene(loginScene);
        loginStage.setTitle("Login");

        return loginStage;
    }
}
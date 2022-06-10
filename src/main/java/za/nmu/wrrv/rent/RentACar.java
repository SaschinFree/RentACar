package za.nmu.wrrv.rent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class RentACar extends Application
{
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

        stage.show();
    }
}
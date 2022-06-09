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
    private Stage mainStage;

    private FXMLLoader mainLoader;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        mainStage(stage);

        mainStage.show();
    }
    private Stage mainStage(Stage stage) throws IOException
    {
        mainStage = stage;

        mainLoader = new FXMLLoader(RentACar.class.getResource("main.fxml"));

        Scene mainScene = new Scene(mainLoader.load());

        mainStage.setScene(mainScene);
        mainStage.setTitle("Main");

        return mainStage;
    }
}
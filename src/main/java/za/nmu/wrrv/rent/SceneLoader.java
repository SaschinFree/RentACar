package za.nmu.wrrv.rent;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;

import java.io.IOException;

public class SceneLoader
{
    private Scene view;

    public Scene getPage(String fileName)
    {
        FXMLLoader loader = new FXMLLoader(RentACar.class.getResource(fileName + ".fxml"));
        try
        {
            view = new Scene(loader.load());
        }
        catch(IOException e)
        {
            System.out.printf("%s", e.getMessage());
        }
        return view;
    }
}

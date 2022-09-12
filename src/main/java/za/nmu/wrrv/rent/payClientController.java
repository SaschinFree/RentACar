package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class payClientController
{
    @FXML
    protected TextField searchQuery;
    @FXML
    protected TableView queryTable;

    @FXML
    protected void backToMenu(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            baseController.nextScene(baseController.userLoggedOn);
    }

    @FXML
    protected void onSearchClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            baseController.nextScene("");
    }

    @FXML
    protected void onPayClicked(MouseEvent mouseEvent) throws IOException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            FXMLLoader makePaymentLoader = new FXMLLoader(RentACar.class.getResource("makePayment.fxml"));
            Scene makePaymentScene = new Scene(makePaymentLoader.load());
            Stage makePaymentStage = new Stage();

            makePaymentStage.setScene(makePaymentScene);
            makePaymentStage.setTitle("Make A Payment");
            makePaymentStage.setResizable(false);
            makePaymentStage.initModality(Modality.WINDOW_MODAL);
            makePaymentStage.initOwner(RentACar.mainStage);

            makePaymentStage.show();
        }
    }
}

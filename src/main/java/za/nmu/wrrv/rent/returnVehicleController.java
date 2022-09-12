package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class returnVehicleController
{
    @FXML
    protected TextField searchQuery;
    @FXML
    protected TableView vehicleTable;

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
    protected void onReturnClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            baseController.nextScene("");
    }
}

package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class manageSettingsController extends baseController
{
    @FXML
    protected Label currentTab;
    @FXML
    protected TableView settingTable;

    @FXML
    protected void backToMenu(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            nextScene("clerkMenu");
    }

    @FXML
    protected void onUpdateClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            nextScene("updateSetting");
    }

    @FXML
    protected void onBackClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            nextScene("clerkMenu");
    }

    private void nextScene(String sceneName)
    {
        BorderPane fakeMain = (BorderPane) currentTab.getScene().getWindow().getScene().getRoot();
        fakeMain.setCenter(newCenter(sceneName));
    }
}

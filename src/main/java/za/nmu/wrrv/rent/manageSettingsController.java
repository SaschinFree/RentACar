package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class manageSettingsController
{
    @FXML
    protected Button updateSetting;
    @FXML
    protected TableView settingTable;

    @FXML
    protected void backToMenu(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            nextScene("clerkMenu");
    }

    @FXML
    protected void buttonClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();

            nextScene(buttonId);
        }
    }

    private void nextScene(String sceneName)
    {
        BorderPane fakeMain = (BorderPane) updateSetting.getScene().getRoot();
        fakeMain.setCenter(baseController.thisScene.getPage(sceneName).getRoot());
    }
}

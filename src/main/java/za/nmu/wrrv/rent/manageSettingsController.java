package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class manageSettingsController implements Initializable
{
    @FXML
    protected TableColumn<Settings, String> settingName;
    @FXML
    protected TableColumn<Settings, Double> settingValue;
    @FXML
    protected Button back;
    @FXML
    protected Button updateSetting;
    @FXML
    protected TableView<Settings> settingTable;

    protected static Settings thisSetting;
    protected static boolean settingUpdated;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        settingUpdated = false;

        updateSetting.setVisible(false);

        settingTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        settingName.setCellValueFactory(new PropertyValueFactory<>("settingName"));
        settingValue.setCellValueFactory(new PropertyValueFactory<>("settingValue"));

        settingTable.setItems(baseController.settings);
    }

    @FXML
    protected void settingSelected(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            thisSetting = settingTable.getSelectionModel().getSelectedItem();
            updateSetting.setVisible(true);
        }
    }
    @FXML
    protected void buttonClicked(MouseEvent mouseEvent) throws IOException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();

            switch (buttonId)
            {
                case "updateSetting" ->
                        {
                            if(thisSetting != null)
                                baseController.newScreen("updateSetting", "Update A Setting");
                        }
                case "back" -> baseController.nextScene(baseController.userLoggedOn);
            }
        }
    }
}

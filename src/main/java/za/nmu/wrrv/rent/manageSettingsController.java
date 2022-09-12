package za.nmu.wrrv.rent;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class manageSettingsController implements Initializable
{
    @FXML
    protected TableColumn settingID;
    @FXML
    protected TableColumn settingValue;
    @FXML
    protected Button back;
    @FXML
    protected Button updateSetting;
    @FXML
    protected TableView settingTable;

    protected static Settings thisSetting;
    protected static boolean settingUpdated;

    private static ObservableList<Settings> settings;
    static
    {
        try
        {
            settings = Settings.getSettings();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        settingUpdated = false;

        updateSetting.setVisible(false);

        settingTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        settingID.setCellValueFactory(new PropertyValueFactory<Settings, String>("settingID"));
        settingValue.setCellValueFactory(new PropertyValueFactory<Settings, Double>("settingValue"));

        settingTable.setItems(settings);
    }

    @FXML
    protected void settingSelected(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            thisSetting = (Settings) settingTable.getSelectionModel().getSelectedItem();

            updateSetting.setVisible(thisSetting != null);
        }
    }
    @FXML
    protected void buttonClicked(MouseEvent mouseEvent) throws IOException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();

            switch(buttonId)
            {
                case "updateSetting":
                    updateSettingClicked();
                    break;
                case "back":
                    baseController.nextScene(baseController.userLoggedOn);
            }
        }
    }
    private void updateSettingClicked() throws IOException
    {
        FXMLLoader updateSettingLoader = new FXMLLoader(RentACar.class.getResource("updateSetting.fxml"));
        Scene updateSettingScene = new Scene(updateSettingLoader.load());
        Stage updateSettingStage = new Stage();

        updateSettingStage.setScene(updateSettingScene);
        updateSettingStage.setTitle("Update A Setting");
        updateSettingStage.setResizable(false);
        updateSettingStage.initModality(Modality.WINDOW_MODAL);
        updateSettingStage.initOwner(RentACar.mainStage);

        updateSettingStage.showAndWait();
    }
}

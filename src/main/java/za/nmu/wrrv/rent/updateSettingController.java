package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class updateSettingController implements Initializable
{
    @FXML
    protected Label settingName;
    @FXML
    protected TextField settingValue;
    @FXML
    protected Button cancel;
    @FXML
    protected Button updateSetting;

    private final Alert alert = new Alert(Alert.AlertType.ERROR);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        settingName.textProperty().bind(manageSettingsController.thisSetting.settingNameProperty());
        settingValue.setText(String.valueOf(manageSettingsController.thisSetting.getSettingValue()));
    }

    @FXML
    protected void buttonClicked(MouseEvent mouseEvent) throws SQLException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();

            switch (buttonId)
            {
                case "cancel" -> closeStage();
                case "updateSetting" -> onUpdate();
            }
        }
    }

    private void onUpdate() throws SQLException
    {
        String thisValue = settingValue.getText();
        if(!baseController.errorValidationCheck(baseController.letterArray, thisValue) | !baseController.symbolCheck(thisValue, '.'))
        {
            settingValue.clear();
            alert.setHeaderText(thisValue + " is not a number");
            alert.showAndWait();
        }
        else
        {
            String sql = "UPDATE Settings SET settingValue = \'" + Double.parseDouble(thisValue) + "\' WHERE settingID = \'" + settingName.getText() + "\'";
            RentACar.statement.executeUpdate(sql);
            manageSettingsController.thisSetting.setSettingValue(Double.parseDouble(thisValue));
            closeStage();
        }
    }
    private void closeStage()
    {
        Stage thisStage = (Stage) settingName.getScene().getWindow();
        thisStage.close();
    }
}

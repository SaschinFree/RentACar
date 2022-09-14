package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    protected Label settingID;
    @FXML
    protected TextField settingValue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        settingID.setText(manageSettingsController.thisSetting.getSettingID());
    }

    @FXML
    protected void onCancel(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            closeStage();
    }

    @FXML
    protected void onUpdate(MouseEvent mouseEvent) throws SQLException
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            String thisValue = settingValue.getText();
            if(!baseController.errorValidationCheck(baseController.letterArray, thisValue, '.'))
            {
                settingValue.clear();
                alert.setTitle("Error");
                alert.setHeaderText("Incorrect Data Type");
                alert.setContentText(thisValue + " is not a number");
                alert.showAndWait();
            }
            else
            {
                String sql = "UPDATE Settings SET settingValue = \'" + Double.parseDouble(thisValue) + "\' WHERE settingID = \'" + settingID.getText() + "\'";
                RentACar.statement.executeUpdate(sql);
                manageSettingsController.thisSetting.setSettingValue(Double.parseDouble(thisValue));
                closeStage();
            }
        }
    }
    private void closeStage()
    {
        Stage thisStage = (Stage) settingID.getScene().getWindow();
        thisStage.close();
    }
}

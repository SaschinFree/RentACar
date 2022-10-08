package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
    protected void keyClicked(KeyEvent keyEvent) throws SQLException
    {
        switch(keyEvent.getCode())
        {
            case ESCAPE -> closeStage();
            case ENTER -> onUpdate();
        }
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
        double value;
        if(!baseController.errorValidationCheck(baseController.letterArray, thisValue) | !baseController.symbolCheck(thisValue, '.'))
        {
            settingValue.clear();
            alert.setHeaderText(thisValue + " is not a valid number");
            alert.showAndWait();
        }
        else
        {
            value = Double.parseDouble(thisValue);
            if(settingName.getText().equals("Company Commission") || settingName.getText().equals("Owner Commission"))
            {
                if(value <= 1.0)
                {
                    String sql = "UPDATE Settings SET settingValue = \'" + value + "\' WHERE settingName = \'" + settingName.getText() + "\'";
                    RentACar.statement.executeUpdate(sql);
                    manageSettingsController.thisSetting.setSettingValue(value);

                    double remainder = 1 - Double.parseDouble(thisValue);
                    String otherSettingName = "";

                    switch (settingName.getText())
                    {
                        case "Company Commission" ->
                                {
                                    otherSettingName = "Owner Commission";
                                    sql = "UPDATE Settings SET settingValue = \'" + remainder + "\' WHERE settingName = \'" + otherSettingName + "\' ";
                                }
                        case "Owner Commission" ->
                                {
                                    otherSettingName = "Company Commission";
                                    sql = "UPDATE Settings SET settingValue = \'" + remainder + "\' WHERE settingName = \'" + otherSettingName + "\' ";
                                }
                    }

                    RentACar.statement.executeUpdate(sql);

                    for(Settings thisSetting : baseController.settings)
                    {
                        if(thisSetting.getSettingName().equals(otherSettingName))
                        {
                            thisSetting.setSettingValue(remainder);
                            break;
                        }
                    }

                    closeStage();
                }
                else
                {
                    settingValue.clear();
                    alert.setHeaderText(thisValue + " is greater than 100%");
                    alert.showAndWait();
                }
            }
            else
            {
                value = Double.parseDouble(thisValue);
                String sql = "UPDATE Settings SET settingValue = \'" + value + "\' WHERE settingName = \'" + settingName.getText() + "\'";
                RentACar.statement.executeUpdate(sql);
                manageSettingsController.thisSetting.setSettingValue(value);
                closeStage();
            }
        }
    }
    private void closeStage()
    {
        Stage thisStage = (Stage) settingName.getScene().getWindow();
        thisStage.close();
    }
}

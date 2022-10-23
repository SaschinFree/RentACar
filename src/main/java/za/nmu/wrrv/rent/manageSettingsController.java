package za.nmu.wrrv.rent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class manageSettingsController implements Initializable, EventHandler<Event>
{
    @FXML
    protected TableColumn<Settings, String> settingName;
    @FXML
    protected TableColumn<Settings, Double> settingValue;
    @FXML
    protected TableView<Settings> settingTable;
    @FXML
    protected Button back;
    @FXML
    protected Button updateSetting;

    protected static Settings thisSetting;
    protected static boolean settingUpdated;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        setupMnemonics();

        settingUpdated = false;

        updateSetting.setVisible(false);

        settingTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        settingName.setCellValueFactory(new PropertyValueFactory<>("settingName"));
        settingValue.setCellValueFactory(new PropertyValueFactory<>("settingValue"));

        settingTable.setItems(baseController.settings);
    }
    @Override
    public void handle(Event event)
    {
        Button thisButton = (Button) event.getSource();
        String buttonId = thisButton.getId();

        switch (buttonId)
        {
            case "updateSetting" ->
                    {
                        if(thisSetting != null)
                        {
                            try
                            {
                                baseController.newScreen("updateSetting", "Update A Setting");
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
            case "back" -> baseController.nextScene(baseController.userLoggedOn);
        }
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
    @FXML
    protected void buttonHover(MouseEvent mouseEvent)
    {
        baseController.changeStyle((Button) mouseEvent.getSource());
    }

    private void setupMnemonics()
    {
        back.setMnemonicParsing(true);
        updateSetting.setMnemonicParsing(true);

        back.setOnAction(this::handle);
        updateSetting.setOnAction(this::handle);

        back.setTooltip(new Tooltip("Alt+B"));
        updateSetting.setTooltip(new Tooltip("Alt+U"));
    }
}

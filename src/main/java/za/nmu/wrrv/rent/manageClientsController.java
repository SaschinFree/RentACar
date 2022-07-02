package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class manageClientsController extends baseController
{
    @FXML
    protected Label currentTab;
    @FXML
    protected TextField searchQuery;
    @FXML
    protected TableView clientTable;

    public void backToMenu(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            nextScene("clerkMenu");
    }

    @FXML
    protected void onSearchClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            nextScene("");
    }

    @FXML
    protected void onAddClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            nextScene("addClient");
    }

    @FXML
    protected void onUpdateClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            nextScene("updateClient");
    }

    private void nextScene(String sceneName)
    {
        BorderPane fakeMain = (BorderPane) currentTab.getScene().getWindow().getScene().getRoot();
        fakeMain.setCenter(newCenter(sceneName));
    }
}

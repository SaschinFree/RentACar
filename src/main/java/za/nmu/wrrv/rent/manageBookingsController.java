package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class manageBookingsController
{
    @FXML
    protected Button search;
    @FXML
    protected Button createBooking;
    @FXML
    protected Button cancelBooking;
    @FXML
    protected TextField searchQuery;
    @FXML
    protected TableView bookingTable;


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
            if(buttonId.equals("search"))
                onSearchClicked(searchQuery);
            else
                nextScene(buttonId);
        }
    }

    @FXML
    protected void onSearchClicked(TextField thisQuery)
    {

    }

    private void nextScene(String sceneName)
    {
        BorderPane fakeMain = (BorderPane) searchQuery.getScene().getRoot();
        fakeMain.setCenter(baseController.thisScene.getPage(sceneName).getRoot());
    }
}

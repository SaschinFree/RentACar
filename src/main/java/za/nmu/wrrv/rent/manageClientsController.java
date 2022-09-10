package za.nmu.wrrv.rent;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class manageClientsController implements Initializable
{
    @FXML
    protected TextField searchQuery;
    @FXML
    protected TableColumn clientNumber;
    @FXML
    protected TableColumn clientID;
    @FXML
    protected TableColumn firstName;
    @FXML
    protected TableColumn surname;
    @FXML
    protected TableColumn contactNumber;
    @FXML
    protected TableColumn email;
    @FXML
    protected TableColumn licenceExpiryDate;
    @FXML
    protected TableColumn streetNumber;
    @FXML
    protected TableColumn streetName;
    @FXML
    protected TableColumn suburb;
    @FXML
    protected TableColumn city;
    @FXML
    protected TableColumn postalCode;
    @FXML
    protected TableColumn companyName;
    @FXML
    protected TableColumn moneyOwed;
    @FXML
    protected Button addClient;
    @FXML
    protected Button updateClient;
    @FXML
    protected TableView clientTable;

    private static ObservableList<Client> clients;
    static
    {
        try
        {
            clients = Client.getClients();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        clientTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        clientNumber.setCellValueFactory(new PropertyValueFactory<>("clientNumber"));
        clientID.setCellValueFactory(new PropertyValueFactory<>("clientID"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        contactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        licenceExpiryDate.setCellValueFactory(new PropertyValueFactory<>("licenceExpiryDate"));
        streetNumber.setCellValueFactory(new PropertyValueFactory<>("streetNumber"));
        streetName.setCellValueFactory(new PropertyValueFactory<>("streetName"));
        suburb.setCellValueFactory(new PropertyValueFactory<>("suburb"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        companyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        moneyOwed.setCellValueFactory(new PropertyValueFactory<>("moneyOwed"));

        clientTable.setItems(clients);
    }

    public void backToMenu(MouseEvent mouseEvent)
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

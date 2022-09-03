package za.nmu.wrrv.rent;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.sql.SQLException;

public class manageClientsController extends baseController
{
    @FXML
    protected Button addClient;
    @FXML
    protected Button updateClient;
    @FXML
    protected Label currentTab;
    @FXML
    protected TextField searchQuery;
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

    public void backToMenu(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            nextScene("clerkMenu");
    }

    @FXML
    protected void setupExtras(MouseEvent mouseEvent)
    {
        clientTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn colID = (TableColumn) clientTable.getColumns().get(0);
        colID.setCellValueFactory(new PropertyValueFactory<>("clientID"));

        TableColumn colFName = (TableColumn) clientTable.getColumns().get(1);
        colFName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn colSName = (TableColumn) clientTable.getColumns().get(2);
        colSName.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn colCNum = (TableColumn) clientTable.getColumns().get(3);
        colCNum.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        TableColumn colEmail = (TableColumn) clientTable.getColumns().get(4);
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn colLicence = (TableColumn) clientTable.getColumns().get(5);
        colLicence.setCellValueFactory(new PropertyValueFactory<>("licenceExpiryDate"));

        TableColumn colStreetNum = (TableColumn) clientTable.getColumns().get(6);
        colStreetNum.setCellValueFactory(new PropertyValueFactory<>("streetNumber"));

        TableColumn colStreetName = (TableColumn) clientTable.getColumns().get(7);
        colStreetName.setCellValueFactory(new PropertyValueFactory<>("streetName"));

        TableColumn colSub = (TableColumn) clientTable.getColumns().get(8);
        colSub.setCellValueFactory(new PropertyValueFactory<>("suburb"));

        TableColumn colPost = (TableColumn) clientTable.getColumns().get(9);
        colPost.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        TableColumn colCity = (TableColumn) clientTable.getColumns().get(10);
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));

        TableColumn colCompany = (TableColumn) clientTable.getColumns().get(11);
        colCompany.setCellValueFactory(new PropertyValueFactory<>("companyName"));

        TableColumn colMoney = (TableColumn) clientTable.getColumns().get(12);
        colMoney.setCellValueFactory(new PropertyValueFactory<>("moneyOwed"));

        if(clientTable.getItems().size() == 0)
            clientTable.setItems(clients);
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
        BorderPane fakeMain = (BorderPane) currentTab.getScene().getWindow().getScene().getRoot();
        fakeMain.setCenter(newCenter(sceneName));
    }
}

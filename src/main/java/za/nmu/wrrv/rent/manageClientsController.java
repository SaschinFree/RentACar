package za.nmu.wrrv.rent;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class manageClientsController implements Initializable
{
    @FXML
    protected ChoiceBox<String> searchFilter;
    @FXML
    protected TextField searchQuery;
    @FXML
    protected Button search;
    @FXML
    protected TableView<Client> clientTable;
    @FXML
    protected TableColumn<Client, Integer> clientNumber;
    @FXML
    protected TableColumn<Client, Integer> clientID;
    @FXML
    protected TableColumn<Client, String> firstName;
    @FXML
    protected TableColumn<Client, String> surname;
    @FXML
    protected TableColumn<Client, String> contactNumber;
    @FXML
    protected TableColumn<Client, String> email;
    @FXML
    protected TableColumn<Client, Date> licenceExpiryDate;
    @FXML
    protected TableColumn<Client, Integer> companyName;
    @FXML
    protected TableColumn<Client, Double> moneyOwed;
    @FXML
    protected Button addClient;
    @FXML
    protected Button updateClient;
    @FXML
    protected Button back;

    protected static Client thisClient;
    protected static boolean clientUpdated;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        searchFilter.getItems().addAll(
                "clientNumber",
                "clientID",
                "firstName",
                "surname",
                "contactNumber",
                "email",
                "licenceExpiryDate",
                "companyName",
                "moneyOwed");
        searchFilter.setValue("clientNumber");

        searchFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, stringSingleSelectionModel, t1) ->
        {
            searchQuery.clear();

            switch(searchFilter.getSelectionModel().getSelectedItem())
            {
                case "clientNumber" -> searchQuery.setPromptText("1");
                case "clientID" -> searchQuery.setPromptText("1234567898765");
                case "firstName" -> searchQuery.setPromptText("John");
                case "surname" -> searchQuery.setPromptText("Doe");
                case "contactNumber" -> searchQuery.setPromptText("0123456789");
                case "email" -> searchQuery.setPromptText("johndoe@gmail.com");
                case "licenceExpiryDate" -> searchQuery.setPromptText("YYYY/MM/DD");
                case "companyName" -> searchQuery.setPromptText("Uber or Private");
                case "moneyOwed" -> searchQuery.setPromptText("0.0");
            }
        });

        if(loginController.thisUser.isAdmin())
            addClient.setVisible(false);

        clientUpdated = false;

        updateClient.setVisible(false);

        clientTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        clientNumber.setCellValueFactory(new PropertyValueFactory<>("clientNumber"));
        clientID.setCellValueFactory(new PropertyValueFactory<>("clientID"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        contactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        licenceExpiryDate.setCellValueFactory(new PropertyValueFactory<>("licenceExpiryDate"));
        companyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        moneyOwed.setCellValueFactory(new PropertyValueFactory<>("moneyOwed"));

        clientTable.setItems(baseController.clients);
        search.setTooltip(baseController.searchTip);
    }
    @FXML
    protected void clientSelected(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            if(!loginController.thisUser.isAdmin())
            {
                thisClient = clientTable.getSelectionModel().getSelectedItem();
                if(thisClient != null)
                    updateClient.setVisible(true);
            }
        }
    }
    @FXML
    protected void buttonClicked(MouseEvent mouseEvent) throws IOException, SQLException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();

            switch(buttonId)
            {
                case "search" -> onSearch();
                case "addClient" -> baseController.newScreen("addClient", "Add A Client");
                case "updateClient" ->
                        {
                            if(thisClient != null)
                                baseController.newScreen("updateClient", "Update A Client");
                        }
                case "back" -> baseController.nextScene(baseController.userLoggedOn);
            }
        }
    }
    private void onSearch() throws SQLException
    {
        if(searchQuery.getText().isEmpty())
            clientTable.setItems(baseController.clients);
        else
        {
            ObservableList<Client> filteredList = Client.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText(), "");
            clientTable.setItems(filteredList);
        }
    }
}

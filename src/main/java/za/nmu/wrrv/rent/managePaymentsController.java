package za.nmu.wrrv.rent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class managePaymentsController implements Initializable
{
    @FXML
    protected ChoiceBox<String> searchFilter;
    @FXML
    protected TextField searchQuery;
    @FXML
    protected Button search;
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
    protected TableColumn<Client, String> companyName;
    @FXML
    protected TableColumn<Client, Integer> moneyOwed;
    @FXML
    protected Button back;
    @FXML
    protected Button payClient;
    @FXML
    protected TableView<Client> queryTable;

    protected static ObservableList<Client> filteredClients = FXCollections.observableArrayList();

    protected static Client thisClient;
    protected static boolean clientPaid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        try
        {
            filteredClients = Client.searchQuery("active", "Yes", "AND moneyOwed > 0");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        searchFilter.getItems().addAll(
                "None",
                "clientID",
                "firstName",
                "surname",
                "contactNumber",
                "email",
                "companyName",
                "moneyOwed");
        searchFilter.setValue("None");

        searchFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, stringSingleSelectionModel, t1) ->
        {
            searchQuery.clear();

            switch(searchFilter.getSelectionModel().getSelectedItem())
            {
                case "None" -> searchQuery.setPromptText("Search");
                case "clientID" -> searchQuery.setPromptText("1234567898765");
                case "firstName" -> searchQuery.setPromptText("John");
                case "surname" -> searchQuery.setPromptText("Doe");
                case "contactNumber" -> searchQuery.setPromptText("0123456789");
                case "email" -> searchQuery.setPromptText("johndoe@gmail.com");
                case "companyName" -> searchQuery.setPromptText("Uber or Private");
                case "moneyOwed" -> searchQuery.setPromptText("0.0");
            }
        });

        clientPaid = false;

        payClient.setVisible(false);

        queryTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        clientID.setCellValueFactory(new PropertyValueFactory<>("clientID"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        contactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        companyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        moneyOwed.setCellValueFactory(new PropertyValueFactory<>("moneyOwed"));

        queryTable.setItems(filteredClients);
    }
    @FXML
    protected void clientSelected(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            thisClient = queryTable.getSelectionModel().getSelectedItem();
            if(thisClient != null)
                payClient.setVisible(true);
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
                case "payClient" ->
                        {
                            if(thisClient != null)
                            {
                                baseController.newScreen("payClient", "Pay A Client");

                                if(clientPaid)
                                {
                                    if(thisClient.getMoneyOwed() == 0)
                                        filteredClients.removeAll(thisClient);
                                }
                            }
                        }
                case "back" -> baseController.nextScene(baseController.userLoggedOn);
            }
        }
    }

    private void onSearch() throws SQLException
    {
        if(searchFilter.getSelectionModel().getSelectedItem().equals("None"))
            queryTable.setItems(filteredClients);
        else
        {
            ObservableList<Client> filteredList = Client.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText(), "AND moneyOwed > 0");
            queryTable.setItems(filteredList);
        }
    }
}

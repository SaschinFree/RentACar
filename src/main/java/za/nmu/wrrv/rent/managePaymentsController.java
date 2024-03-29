package za.nmu.wrrv.rent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class managePaymentsController implements Initializable, EventHandler<Event>
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
        setupMnemonics();

        filteredClients = FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.isActive() && client.getMoneyOwed() > 0.0).toList());

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
                case "None" ->
                        {
                            searchQuery.setPromptText("Search");
                            queryTable.setItems(filteredClients);
                        }
                case "clientID" -> searchQuery.setPromptText("1234567898765");
                case "firstName" -> searchQuery.setPromptText("John");
                case "surname" -> searchQuery.setPromptText("Doe");
                case "contactNumber" -> searchQuery.setPromptText("0123456789");
                case "email" -> searchQuery.setPromptText("johndoe@gmail.com");
                case "companyName" -> searchQuery.setPromptText("Uber or Private");
                case "moneyOwed" -> searchQuery.setPromptText("0.0");
            }
        });

        searchQuery.focusedProperty().addListener((observableValue, aBoolean, t1) ->
        {
            enableMnemonics(!searchQuery.isFocused());
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
    @Override
    public void handle(Event event)
    {
        Button thisButton = (Button) event.getSource();
        String buttonId = thisButton.getId();

        switch(buttonId)
        {
            case "search" -> onSearch();
            case "payClient" ->
                    {
                        if(thisClient != null)
                        {
                            try
                            {
                                baseController.newScreen("payClient", "Pay A Client");

                                if(clientPaid)
                                {
                                    if(thisClient.getMoneyOwed() == 0)
                                        filteredClients.removeAll(thisClient);
                                }
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
    @FXML
    protected void buttonHover(MouseEvent mouseEvent)
    {
        baseController.changeStyle((Button) mouseEvent.getSource());
    }

    private void setupMnemonics()
    {
        enableMnemonics(true);

        search.setOnAction(this::handle);
        back.setOnAction(this::handle);
        payClient.setOnAction(this::handle);

        searchQuery.setTooltip(baseController.searchTip);
        search.setTooltip(new Tooltip("Alt+S"));
        back.setTooltip(new Tooltip("Alt+B"));
        payClient.setTooltip(new Tooltip("Alt+P"));
    }
    private void enableMnemonics(boolean value)
    {
        search.setMnemonicParsing(value);
        back.setMnemonicParsing(value);
        payClient.setMnemonicParsing(value);
    }

    private void onSearch()
    {
        if(searchFilter.getSelectionModel().getSelectedItem().equals("None"))
            queryTable.setItems(filteredClients);
        else
        {
            ObservableList<Client> filteredList = Client.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText());
            if (filteredList != null)
                filteredList = FXCollections.observableList(filteredList.stream().filter(client -> client.getMoneyOwed() > 0.0).toList());
            queryTable.setItems(filteredList);
        }
    }
}

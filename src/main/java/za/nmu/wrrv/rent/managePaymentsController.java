package za.nmu.wrrv.rent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class managePaymentsController implements Initializable
{
    public TableColumn<Client, Integer> clientID;
    public TableColumn<Client, String> firstName;
    public TableColumn<Client, String> surname;
    public TableColumn<Client, String> contactNumber;
    public TableColumn<Client, String> email;
    public TableColumn<Client, String> companyName;
    public TableColumn<Client, Integer> moneyOwed;
    @FXML
    protected TextField searchQuery;
    @FXML
    protected Button search;
    @FXML
    protected Button back;
    @FXML
    protected Button payClient;
    @FXML
    protected TableView<Client> queryTable;

    protected static ObservableList<Client> filteredClients = FXCollections.observableArrayList();

    static
    {
        for(Client thisClient : baseController.clients)
        {
            if(thisClient.getMoneyOwed() > 0)
                filteredClients.add(thisClient);
        }
    }

    protected static Client thisClient;
    protected static boolean clientPaid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
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
    protected void buttonClicked(MouseEvent mouseEvent) throws IOException
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
    private void onSearch()
    {
        //searchQuery
    }
}

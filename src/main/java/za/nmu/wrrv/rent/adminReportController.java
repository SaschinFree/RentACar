package za.nmu.wrrv.rent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class adminReportController implements Initializable
{
    @FXML
    protected VBox adminBox;
    @FXML
    protected ChoiceBox<String> clientFilter;
    @FXML
    protected ScrollPane scrollPane;
    @FXML
    protected VBox insideScroll;
    @FXML
    protected HBox amountBox;
    @FXML
    protected Label amountLabel;
    @FXML
    protected Label amount;
    @FXML
    protected HBox dateBox;
    @FXML
    protected Label dateLabel;
    @FXML
    protected Label date;
    @FXML
    protected Line line;
    @FXML
    protected Button back;

    protected final List<String> clients = new ArrayList<>();
    protected String surnameName = "";
    protected ObservableList<Payment> clientPayments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        scrollPane.setVisible(false);

        setupComboBox();

        attachEventHandler();

    }
    @FXML
    protected void buttonClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            baseController.nextScene(baseController.userLoggedOn);
        }
    }

    private void setupComboBox()
    {
        for(Payment thisClient : baseController.payments)
        {
            List<Client> clientDetails = baseController.clients.stream().filter(client -> client.getClientNumber() == thisClient.getClientNumber()).toList();
            Client selectedClient = clientDetails.get(0);

            String surnameName = selectedClient.getSurname() + ", " + selectedClient.getFirstName();
            if(!checkClient(surnameName))
                clients.add(surnameName);
        }

        for(String client : clients)
            clientFilter.getItems().add(client);
    }
    private void attachEventHandler()
    {
        adminBox.addEventHandler(EventType.ROOT, event ->
        {
            String check = clientFilter.getSelectionModel().getSelectedItem();
            if(check != null)
            {
                if(!surnameName.equals(check))
                {
                    String[] thisClient = clientFilter.getSelectionModel().getSelectedItem().split(", ");
                    ObservableList<Client> clientSelected;

                    surnameName = thisClient[0] + ", " + thisClient[1];

                    clientSelected = FXCollections.observableArrayList(baseController.clients.stream().filter(client -> client.getSurname().equals(thisClient[0]) && client.getFirstName().equals(thisClient[1])).collect(Collectors.toList()));
                    clientPayments = FXCollections.observableArrayList(baseController.payments.stream().filter(client -> client.getClientNumber() == clientSelected.get(0).getClientNumber()).collect(Collectors.toList()));

                    scrollPane.setContent(null);

                    VBox insideScrollCopy = new VBox();
                    insideScrollCopy.setPrefSize(insideScroll.getPrefWidth(), insideScroll.getPrefHeight());
                    insideScrollCopy.backgroundProperty().set(insideScroll.getBackground());

                    insideScroll = insideScrollCopy;

                    Payment thisPayment = clientPayments.get(0);

                    amount.setText("R" + thisPayment.getAmount());
                    date.setText(String.valueOf(thisPayment.getPayDate()));

                    insideScroll.getChildren().addAll(amountBox, dateBox, line);

                    if(clientPayments.size() > 1)
                    {
                        for(int i = 1; i < clientPayments.size(); i++)
                        {
                            thisPayment = clientPayments.get(i);

                            HBox copyAmountBox = new HBox();
                            HBox copyDateBox = new HBox();

                            copyAmountBox.setPrefSize(amountBox.getPrefWidth(), amountBox.getPrefHeight());

                            copyDateBox.setPrefSize(dateBox.getPrefWidth(), dateBox.getPrefHeight());

                            Label copyAmountLabel = new Label("Amount");
                            copyAmountLabel.setPrefWidth(amountLabel.getPrefWidth());
                            copyAmountLabel.setFont(Font.font(String.valueOf(amountLabel.getFont()), amountLabel.getFont().getSize()));

                            Label copyAmount = new Label("R" + thisPayment.getAmount());
                            copyAmount.setFont(Font.font(String.valueOf(amount.getFont()), amount.getFont().getSize()));

                            Label copyDateLabel = new Label("Date");
                            copyDateLabel.setPrefWidth(dateLabel.getPrefWidth());
                            copyDateLabel.setFont(Font.font(String.valueOf(dateLabel.getFont()), dateLabel.getFont().getSize()));

                            Label copyDate = new Label(String.valueOf(thisPayment.getPayDate()));
                            copyDate.setFont(Font.font(String.valueOf(date.getFont()), date.getFont().getSize()));

                            copyAmountBox.getChildren().addAll(copyAmountLabel, copyAmount);
                            copyDateBox.getChildren().addAll(copyDateLabel, copyDate);

                            Line copyLine = new Line();
                            copyLine.setStartX(line.getStartX());
                            copyLine.setEndX(line.getEndX());
                            copyLine.setStrokeWidth(line.getStrokeWidth());
                            copyLine.setStroke(line.getStroke());

                            insideScroll.getChildren().addAll(copyAmountBox, copyDateBox, copyLine);
                        }
                    }
                    scrollPane.setContent(insideScroll);
                    scrollPane.setVisible(true);
                }
            }
        });
    }
    private boolean checkClient(String thisClient)
    {
        for(String client : clients)
        {
            if(client.equals(thisClient))
                return true;
        }
        return false;
    }
}

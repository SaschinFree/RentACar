package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class payClientController implements Initializable
{
    @FXML
    protected Label clientID;
    @FXML
    protected Label clientName;
    @FXML
    protected Label clientSurname;
    @FXML
    protected Label clientContactNumber;
    @FXML
    protected Label clientMoneyOwed;
    @FXML
    protected TextField amountPaid;
    @FXML
    protected Button cancel;

    private final Alert alert = new Alert(Alert.AlertType.ERROR);
    private String errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        clientID.setText(managePaymentsController.thisClient.getClientID());
        clientName.setText(managePaymentsController.thisClient.getFirstName());
        clientSurname.setText(managePaymentsController.thisClient.getSurname());
        clientContactNumber.setText(managePaymentsController.thisClient.getContactNumber());
        clientMoneyOwed.setText(String.valueOf(managePaymentsController.thisClient.getMoneyOwed()));
    }

    @FXML
    protected void onCancel(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            closeStage();
        }
    }
    @FXML
    protected void onPay(MouseEvent mouseEvent) throws SQLException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            String thisAmount = amountPaid.getText();
            if(emptyCheck(thisAmount) & errorCheck(thisAmount) & valueCheck(Double.parseDouble(thisAmount), Double.parseDouble(clientMoneyOwed.getText())))
            {
                double amount = Double.parseDouble(thisAmount);
                double remainder = Double.parseDouble(clientMoneyOwed.getText()) - amount;

                String updateClient = "UPDATE Client " +
                        "SET moneyOwed = \'" + remainder + "\' " +
                        "WHERE clientID = \'" + clientID.getText() + "\'";
                RentACar.statement.executeUpdate(updateClient);

                managePaymentsController.thisClient.setMoneyOwed(Double.parseDouble(clientMoneyOwed.getText()) - amount);
                managePaymentsController.clientPaid = true;

                int paymentID = baseController.payments.size() + 1;
                int clientNumber = managePaymentsController.thisClient.getClientNumber();
                Date payDate = Date.valueOf(LocalDate.now());

                String updatePayment = "INSERT INTO Payments (paymentID, clientNumber, amount, payDate)" +
                        "VALUES (\'" + paymentID + "\', \'" + clientNumber + "\', \'" + amount + "\', \'" + payDate + "\')";
                RentACar.statement.executeUpdate(updatePayment);

                baseController.payments.add(new Payment(paymentID, clientNumber, amount, payDate));

                closeStage();
            }
            else
            {
                alert.setHeaderText("Error");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            }
        }
    }
    private boolean emptyCheck(String thisAmount)
    {
        if(thisAmount.isEmpty())
        {
            errorMessage = "Amount Paid is empty";
            amountPaid.clear();
            return false;
        }
        return true;
    }
    private boolean errorCheck(String thisAmount)
    {
        if(!baseController.errorValidationCheck(baseController.letterArray, thisAmount, '.'))
        {
            errorMessage = "Amount Paid is not a number";
            amountPaid.clear();
            return false;
        }
        return true;
    }
    private boolean valueCheck(double thisAmount, double moneyOwed)
    {
        if(thisAmount > moneyOwed)
        {
            errorMessage = "Amount Paid exceeds the amount owed to the client";
            amountPaid.clear();
            return false;
        }
        return true;
    }
    private void closeStage()
    {
        Stage thisStage = (Stage) cancel.getScene().getWindow();
        thisStage.close();
    }
}

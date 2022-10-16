package za.nmu.wrrv.rent;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Payment
{
    private static final ObservableList<Payment> paymentList = FXCollections.observableArrayList();
    private final IntegerProperty paymentID = new SimpleIntegerProperty();
    private final IntegerProperty clientNumber = new SimpleIntegerProperty();
    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final Property<Date> payDate = new SimpleObjectProperty<>();

    public Payment(int paymentID, int clientNumber, double amount, Date payDate)
    {
        this.paymentID.set(paymentID);
        this.clientNumber.set(clientNumber);
        this.amount.set(amount);
        this.payDate.setValue(payDate);
    }
    public static ObservableList<Payment> getPayments() throws SQLException
    {
        String sql = "SELECT * FROM Payments";
        ResultSet result = RentACar.statement.executeQuery(sql);

        while(result.next())
        {
            int thisPaymentID = result.getInt("paymentID");
            int thisClientNumber = result.getInt("clientNumber");
            double thisAmount = result.getDouble("amount");
            Date thisPayDate = result.getDate("payDate");

            paymentList.add(new Payment(thisPaymentID, thisClientNumber, thisAmount, thisPayDate));
        }
        return paymentList;
    }

    public int getClientNumber()
    {
        return clientNumber.get();
    }
    public void setClientNumber(int clientNumber)
    {
        this.clientNumber.set(clientNumber);
    }

    public double getAmount()
    {
        return amount.get();
    }

    public Date getPayDate()
    {
        return payDate.getValue();
    }
}

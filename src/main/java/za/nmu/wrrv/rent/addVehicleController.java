package za.nmu.wrrv.rent;

import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class addVehicleController implements Initializable
{
    @FXML
    protected ChoiceBox<Integer> clientNumber;
    @FXML
    protected TextField registrationNumber;
    @FXML
    protected ChoiceBox<String> plateExtension;
    @FXML
    protected TextField vehicleMake;
    @FXML
    protected TextField vehicleModel;
    @FXML
    protected TextField vehicleColour;
    @FXML
    protected Spinner<Integer> vehicleSeats;
    @FXML
    protected DatePicker registrationExpirationDate;
    @FXML
    protected CheckBox vehicleInsurance;
    @FXML
    protected TextField costMultiplier;
    @FXML
    protected DatePicker vehicleStartDate;
    @FXML
    protected DatePicker vehicleEndDate;
    @FXML
    protected Button cancel;
    @FXML
    protected Button addVehicle;

    private final Alert alert = new Alert(Alert.AlertType.ERROR);
    private String errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        plateExtension.getItems().addAll("WP", "ZN", "MP", "EC", "L", "GP", "NC", "FS", "NW");
        plateExtension.setValue("EC");

        for(Client thisClient : manageClientsController.clients)
        {
            clientNumber.getItems().add(thisClient.getClientNumber());
        }

        SpinnerValueFactory<Integer> seats = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24);
        seats.setValue(1);
        vehicleSeats.setValueFactory(seats);

        clientNumber.addEventHandler(EventType.ROOT, event -> {
            if(clientNumber.getSelectionModel().getSelectedIndex() != -1)
            {
                registrationNumber.setVisible(true);
                plateExtension.setVisible(true);
                registrationExpirationDate.setVisible(true);
                vehicleInsurance.setVisible(true);
                vehicleMake.setVisible(true);
                vehicleModel.setVisible(true);
                vehicleColour.setVisible(true);
                vehicleSeats.setVisible(true);
                vehicleStartDate.setVisible(true);
                vehicleEndDate.setVisible(true);
                costMultiplier.setVisible(true);

                addVehicle.setVisible(true);
            }
        });

        registrationNumber.setVisible(false);
        plateExtension.setVisible(false);
        registrationExpirationDate.setVisible(false);
        vehicleInsurance.setVisible(false);
        vehicleMake.setVisible(false);
        vehicleModel.setVisible(false);
        vehicleColour.setVisible(false);
        vehicleSeats.setVisible(false);
        vehicleStartDate.setVisible(false);
        vehicleEndDate.setVisible(false);
        costMultiplier.setVisible(false);

        addVehicle.setVisible(false);
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
    protected void onAdd(MouseEvent mouseEvent) throws SQLException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            String regNum = registrationNumber.getText().toUpperCase() + " " + plateExtension.getSelectionModel().getSelectedItem();
            int clientNumber =  this.clientNumber.getSelectionModel().getSelectedItem();
            Date regExp = Date.valueOf(registrationExpirationDate.getValue());
            boolean insured = vehicleInsurance.selectedProperty().getValue();
            String make = vehicleMake.getText();
            String model = vehicleModel.getText();
            String colour = vehicleColour.getText();
            int seats = vehicleSeats.getValue();
            Date start = Date.valueOf(vehicleStartDate.getValue());
            Date end = Date.valueOf(vehicleEndDate.getValue());
            double costMulti = Double.parseDouble(costMultiplier.getText());

            String registrationCheck = "SELECT vehicleRegistration FROM Vehicle WHERE vehicleRegistration = \'" + regNum + "\'";
            ResultSet result = RentACar.statement.executeQuery(registrationCheck);

            if(result.next())
            {
                errorMessage = "Registration Number: This vehicle already exists in the table";
                registrationNumber.clear();
                alert.setHeaderText("Error");
                alert.setContentText(errorMessage);
                alert.showAndWait();

                mouseEvent.consume();
            }

            if(emptyChecks(regExp, make, colour, start, end, costMulti) & errorChecks(regExp, make, colour, start, end, costMulti))
            {

                String sql = "INSERT INTO Vehicle " +
                        "(vehicleRegistration, clientNumber, registrationExpiryDate, insured, make, model, colour, seats, startDate, endDate, costMultiplier)" +
                        "VALUES (\'"+ regNum +"\',\'"+ clientNumber +"\',\'"+ regExp +"\',\'"+ insured +"\',\'"+ make +"\',\'"+ model +"\',\'"+ colour +"\',\'"+ seats +"\',\'"+ start +"\',\'"+ end +"\',\'"+ costMulti +"\')";
                RentACar.statement.executeUpdate(sql);

                Vehicle newVehicle = new Vehicle(regNum, clientNumber, regExp, insured, make, model, colour, seats, start, end, costMulti);
                Vehicle.vehicleList.add(newVehicle);

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
    private boolean emptyChecks(Date regExp, String make, String colour, Date start, Date end, double costMulti)
    {
        if(regExp.toString().isEmpty())
        {
            errorMessage = "Registration Expiration Date is empty";
            registrationExpirationDate.setValue(null);
            return false;
        }

        if(make.isEmpty())
        {
            errorMessage = "Vehicle Make is empty";
            vehicleMake.clear();
            return false;
        }

        if(colour.isEmpty())
        {
            errorMessage = "Vehicle Colour is empty";
            vehicleColour.clear();
            return false;
        }

        if(start.toString().isEmpty())
        {
            errorMessage = "Rental Start Date is empty";
            vehicleStartDate.setValue(null);
            return false;
        }

        if(end.toString().isEmpty())
        {
            errorMessage = "Rental End Date is empty";
            vehicleEndDate.setValue(null);
            return false;
        }

        if(String.valueOf(costMulti).isEmpty())
        {
            errorMessage = "Cost Multiplier is empty";
            costMultiplier.clear();
            return false;
        }

        return true;
    }
    private boolean errorChecks(Date regExp, String make, String colour, Date start, Date end, double costMulti)
    {
        if(regExp.before(Date.valueOf(LocalDate.now())))
        {
            errorMessage = "Registration Expiration Date: Registration Date cannot be a date in the past";
            registrationExpirationDate.setValue(null);
            return false;
        }

        if(!baseController.errorValidationCheck(baseController.numberArray, make, '-'))
        {
            errorMessage = "Vehicle Make: " + make + " is in the incorrect format";
            vehicleMake.clear();
            return false;
        }

        String[] colours = colour.split(",");
        for(String thisColour : colours)
        {
            if(!baseController.errorValidationCheck(baseController.numberArray, thisColour, '-'))
            {
                errorMessage = "Vehicle Colour: " + thisColour + " is in the incorrect format";
                vehicleColour.clear();
                return false;
            }
            if(Paint.valueOf(colour) == null)
            {
                errorMessage = "Vehicle Colour: " + thisColour + " is not a colour";
                vehicleColour.clear();
                return false;
            }
        }

        if(start.after(end))
        {
            errorMessage = "Rental Start Date: Rental start date cannot be after rental end date";
            vehicleStartDate.setValue(null);
            return false;
        }

        if(end.before(start))
        {
            errorMessage = "Rental End Date: Rental end date cannot be before rental start date";
            vehicleEndDate.setValue(null);
            return false;
        }

        if(!baseController.errorValidationCheck(baseController.letterArray, String.valueOf(costMulti), '.'))
        {
            errorMessage = "Cost Multiplier: " + costMulti + " is not a number";
            costMultiplier.clear();
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

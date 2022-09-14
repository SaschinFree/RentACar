package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class updateVehicleController implements Initializable
{
    @FXML
    protected Label clientNumber;
    @FXML
    protected Label registrationNumber;
    @FXML
    protected DatePicker registrationExpirationDate;
    @FXML
    protected Label vehicleMake;
    @FXML
    protected Label vehicleSeats;
    @FXML
    protected Label vehicleModel;
    @FXML
    protected TextField vehicleColour;
    @FXML
    protected CheckBox vehicleInsurance;
    @FXML
    protected DatePicker vehicleStartDate;
    @FXML
    protected DatePicker vehicleEndDate;
    @FXML
    protected TextField costMultiplier;
    @FXML
    protected CheckBox disableVehicle;
    @FXML
    protected Button cancel;

    private final Alert alert = new Alert(Alert.AlertType.ERROR);
    private String errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        clientNumber.setText(String.valueOf(manageVehiclesController.thisVehicle.getClientNumber()));
        registrationNumber.setText(manageVehiclesController.thisVehicle.getVehicleRegistration());
        vehicleMake.setText(manageVehiclesController.thisVehicle.getMake());
        vehicleModel.setText(manageVehiclesController.thisVehicle.getModel());
        vehicleSeats.setText(String.valueOf(manageVehiclesController.thisVehicle.getSeats()));

        registrationExpirationDate.setValue(LocalDate.parse(manageVehiclesController.thisVehicle.getRegistrationExpiryDate().toString()));
        vehicleStartDate.setValue(LocalDate.parse(manageVehiclesController.thisVehicle.getStartDate().toString()));
        vehicleEndDate.setValue(LocalDate.parse(manageVehiclesController.thisVehicle.getEndDate().toString()));

        vehicleColour.setText(manageVehiclesController.thisVehicle.getColour());
        vehicleInsurance.setSelected(manageVehiclesController.thisVehicle.isInsured());
        costMultiplier.setText(String.valueOf(manageVehiclesController.thisVehicle.getCostMultiplier()));
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
    protected void onUpdate(MouseEvent mouseEvent) throws SQLException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            if(disableVehicle.selectedProperty().getValue())
            {
                String updateDisable = "UPDATE Vehicle SET active = false WHERE vehicleRegistration = \'" + manageVehiclesController.thisVehicle.getVehicleRegistration() + "\'";
                RentACar.statement.executeUpdate(updateDisable);

                manageVehiclesController.thisVehicle.setActive(false);
                Vehicle.vehicleList.removeAll(manageVehiclesController.thisVehicle);


                Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                newAlert.setHeaderText("Successful");
                newAlert.setContentText("Vehicle removed");
                newAlert.showAndWait();

                closeStage();
            }
            else
            {
                Date regExp = Date.valueOf(registrationExpirationDate.getValue());
                boolean insured = vehicleInsurance.selectedProperty().getValue();
                String colour = vehicleColour.getText();
                Date start = Date.valueOf(vehicleStartDate.getValue());
                Date end = Date.valueOf(vehicleEndDate.getValue());
                double costMulti = Double.parseDouble(costMultiplier.getText());

                if(emptyChecks(regExp, colour, start, end, costMulti) & errorChecks(regExp, colour, start, end, costMulti))
                {
                    String sql = "UPDATE Vehicle " +
                            "SET registrationExpiryDate = \'" + regExp + "\', insured = \'" + insured + "\', colour = \'" + colour + "\', startDate = \'" + start + "\', endDate = \'" + end + "\', costMultiplier = \'" + costMulti + "\'" +
                            "WHERE vehicleRegistration = \'" + registrationNumber.getText() + "\'";
                    RentACar.statement.executeUpdate(sql);

                    manageVehiclesController.thisVehicle.setRegistrationExpiryDate(regExp);
                    manageVehiclesController.thisVehicle.setInsured(insured);
                    manageVehiclesController.thisVehicle.setColour(colour);
                    manageVehiclesController.thisVehicle.setStartDate(start);
                    manageVehiclesController.thisVehicle.setEndDate(end);
                    manageVehiclesController.thisVehicle.setCostMultiplier(costMulti);

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
    }
    private boolean emptyChecks(Date regExp, String colour, Date start, Date end, double costMulti)
    {
        if(regExp.toString().isEmpty())
        {
            errorMessage = "Registration Expiration Date is empty";
            registrationExpirationDate.setValue(null);
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
    private boolean errorChecks(Date regExp, String colour, Date start, Date end, double costMulti)
    {
        if(regExp.before(Date.valueOf(LocalDate.now())))
        {
            errorMessage = "Registration Expiration Date cannot be a date in the past";
            registrationExpirationDate.setValue(null);
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

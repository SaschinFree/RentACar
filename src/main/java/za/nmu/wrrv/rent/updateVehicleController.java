package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class updateVehicleController implements Initializable
{
    @FXML
    protected Label clientSurnameName;
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
    protected CheckBox deleteVehicle;
    @FXML
    protected Button cancel;
    @FXML
    protected Button updateVehicle;

    private final Alert alert = new Alert(Alert.AlertType.ERROR);
    private String errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        registrationExpirationDate.setConverter(baseController.dateConverter);
        vehicleStartDate.setConverter(baseController.dateConverter);
        vehicleEndDate.setConverter(baseController.dateConverter);

        Client thisClient = baseController.clients.stream().filter(client -> client.getClientNumber() == manageVehiclesController.thisVehicle.getClientNumber()).toList().get(0);

        clientSurnameName.textProperty().bind(thisClient.surnameProperty().concat(", " + thisClient.getFirstName()));
        registrationNumber.textProperty().bind(manageVehiclesController.thisVehicle.vehicleRegistrationProperty());
        vehicleMake.textProperty().bind(manageVehiclesController.thisVehicle.makeProperty());
        vehicleModel.textProperty().bind(manageVehiclesController.thisVehicle.modelProperty());
        vehicleSeats.textProperty().bind(manageVehiclesController.thisVehicle.seatsProperty().asString());

        registrationExpirationDate.setValue(LocalDate.parse(manageVehiclesController.thisVehicle.getRegistrationExpiryDate().toString()));
        vehicleStartDate.setValue(LocalDate.parse(manageVehiclesController.thisVehicle.getStartDate().toString()));
        vehicleEndDate.setValue(LocalDate.parse(manageVehiclesController.thisVehicle.getEndDate().toString()));

        vehicleColour.setText(manageVehiclesController.thisVehicle.getColour());

        String isInsured = manageVehiclesController.thisVehicle.isInsured();
        vehicleInsurance.setSelected(isInsured.equals("Yes"));
        costMultiplier.setText(String.valueOf(manageVehiclesController.thisVehicle.getCostMultiplier()));
    }

    @FXML
    protected void keyClicked(KeyEvent keyEvent) throws SQLException
    {
        switch(keyEvent.getCode())
        {
            case ESCAPE -> closeStage();
            case ENTER -> onUpdate();
        }
    }
    @FXML
    protected void buttonClicked(MouseEvent mouseEvent) throws SQLException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();

            switch (buttonId)
            {
                case "cancel" -> closeStage();
                case "updateVehicle" -> onUpdate();
            }
        }
    }
    @FXML
    protected void buttonHover(MouseEvent mouseEvent)
    {
        baseController.changeStyle((Button) mouseEvent.getSource());
    }

    private void onUpdate() throws SQLException
    {
        if(deleteVehicle.selectedProperty().getValue())
        {
            String delete = "UPDATE Vehicle SET active = false WHERE vehicleRegistration = \'" + manageVehiclesController.thisVehicle.getVehicleRegistration() + "\'";
            RentACar.statement.executeUpdate(delete);

            int index = 0;
            for(Vehicle vehicle : baseController.vehicles)
            {
                if(vehicle.getVehicleRegistration().equals(manageVehiclesController.thisVehicle.getVehicleRegistration()))
                {
                    vehicle.setActive(false);
                    baseController.vehicles.set(index, vehicle);
                    break;
                }
                index++;
            }
            Vehicle.vehicleList.removeAll(manageVehiclesController.thisVehicle);

            baseController.deleteBookings(manageVehiclesController.thisVehicle);

            Alert deleteVehicle = new Alert(Alert.AlertType.INFORMATION);
            ((Stage) deleteVehicle.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
            deleteVehicle.setHeaderText("Vehicle Deleted Successfully");
            deleteVehicle.showAndWait();

            closeStage();
        }
        else
        {
            String regExpString = registrationExpirationDate.getEditor().getText();
            regExpString = regExpString.replace("/", "-");

            boolean insured = vehicleInsurance.selectedProperty().getValue();

            String isInsured = "No";
            if(insured)
                isInsured = "Yes";

            String colour = vehicleColour.getText();

            String startDateString = vehicleStartDate.getEditor().getText();
            startDateString = startDateString.replace("/", "-");

            String endDateString = vehicleEndDate.getEditor().getText();
            endDateString = endDateString.replace("/", "-");

            String costMultiString = costMultiplier.getText();

            if(baseController.dateCheck(registrationExpirationDate, regExpString) & baseController.dateCheck(vehicleStartDate, startDateString) & baseController.dateCheck(vehicleEndDate, endDateString))
            {
                if(emptyChecks(regExpString, colour, startDateString, endDateString, costMultiString) & errorChecks(Date.valueOf(regExpString), colour, Date.valueOf(startDateString), Date.valueOf(endDateString), costMultiString))
                {
                    Date regExp = Date.valueOf(regExpString);
                    Date start = Date.valueOf(startDateString);
                    Date end = Date.valueOf(endDateString);
                    double costMulti = Double.parseDouble(costMultiString);

                    String sql = "UPDATE Vehicle " +
                            "SET registrationExpiryDate = \'" + regExp + "\', insured = \'" + insured + "\', colour = \'" + colour + "\', startDate = \'" + start + "\', endDate = \'" + end + "\', costMultiplier = \'" + costMulti + "\'" +
                            "WHERE vehicleRegistration = \'" + registrationNumber.getText() + "\'";
                    RentACar.statement.executeUpdate(sql);

                    manageVehiclesController.thisVehicle.setRegistrationExpiryDate(regExp);
                    manageVehiclesController.thisVehicle.setInsured(isInsured);
                    manageVehiclesController.thisVehicle.setColour(colour);
                    manageVehiclesController.thisVehicle.setStartDate(start);
                    manageVehiclesController.thisVehicle.setEndDate(end);
                    manageVehiclesController.thisVehicle.setCostMultiplier(costMulti);

                    int index = 0;
                    for(Vehicle vehicle : baseController.vehicles)
                    {
                        if(vehicle.getVehicleRegistration().equals(manageVehiclesController.thisVehicle.getVehicleRegistration()))
                        {
                            baseController.vehicles.set(index, manageVehiclesController.thisVehicle);

                            break;
                        }
                        else
                            index += 1;
                    }

                    Alert updateVehicle = new Alert(Alert.AlertType.INFORMATION);
                    ((Stage) updateVehicle.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
                    updateVehicle.setHeaderText("Vehicle Updated Successfully");
                    updateVehicle.showAndWait();
                    closeStage();
                }
                else
                {
                    ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
                    alert.setHeaderText(errorMessage);
                    alert.showAndWait();
                }
            }
            else
            {
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
                alert.setHeaderText("Date is in incorrect format");
                alert.showAndWait();
            }
        }
    }
    private boolean emptyChecks(String regExp, String colour, String start, String end, String costMulti)
    {
        if(regExp.isEmpty())
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

        if(start.isEmpty())
        {
            errorMessage = "Rental Start Date is empty";
            vehicleStartDate.setValue(null);
            return false;
        }

        if(end.isEmpty())
        {
            errorMessage = "Rental End Date is empty";
            vehicleEndDate.setValue(null);
            return false;
        }

        if(costMulti.isEmpty())
        {
            errorMessage = "Cost Multiplier is empty";
            costMultiplier.clear();
            return false;
        }

        return true;
    }
    private boolean errorChecks(Date regExp, String colour, Date start, Date end, String costMulti)
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
            if(!baseController.errorValidationCheck(baseController.numberArray, thisColour) | !baseController.symbolCheck(thisColour))
            {
                errorMessage = "Vehicle Colour: " + thisColour + " is in the incorrect format";
                vehicleColour.clear();
                return false;
            }
            try
            {
                Color.valueOf(thisColour);
            }
            catch (Exception e)
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

        if(end.after(regExp))
        {
            errorMessage = "Rental End Date: Rental end date cannot be after registration expiry date";
            vehicleEndDate.setValue(null);
            return false;
        }

        if(!baseController.errorValidationCheck(baseController.letterArray, costMulti) | !baseController.symbolCheck(costMulti, '.'))
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

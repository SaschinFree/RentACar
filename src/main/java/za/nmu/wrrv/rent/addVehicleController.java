package za.nmu.wrrv.rent;

import javafx.event.EventType;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class addVehicleController implements Initializable
{
    @FXML
    protected ChoiceBox<String> clientSurnameName;
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
    private int clientNumber;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        registrationExpirationDate.setConverter(baseController.dateConverter);
        vehicleStartDate.setConverter(baseController.dateConverter);
        vehicleEndDate.setConverter(baseController.dateConverter);

        plateExtension.getItems().addAll("WP", "ZN", "MP", "EC", "L", "GP", "NC", "FS", "NW");
        plateExtension.setValue("EC");

        List<String> sortedClient = new ArrayList<>();
        for(Client thisClient : baseController.clients)
        {
            sortedClient.add(thisClient.getSurname() + ", " + thisClient.getFirstName());
        }

        Collections.sort(sortedClient);

        for(String thisClient : sortedClient)
        {
            clientSurnameName.getItems().add(thisClient);
        }

        SpinnerValueFactory<Integer> seats = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24);
        seats.setValue(1);
        vehicleSeats.setValueFactory(seats);

        clientSurnameName.addEventHandler(EventType.ROOT, event -> {
            if(clientSurnameName.getSelectionModel().getSelectedIndex() != -1)
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

                costMultiplier.setText("1.0");
                costMultiplier.setVisible(true);

                addVehicle.setVisible(true);

                String[] thisClient = clientSurnameName.getSelectionModel().getSelectedItem().split(", ");
                Client selectedClient = baseController.clients.stream().filter(client -> client.isActive() && client.getSurname().equals(thisClient[0]) && client.getFirstName().equals(thisClient[1])).toList().get(0);

                clientNumber = selectedClient.getClientNumber();
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
    protected void keyClicked(KeyEvent keyEvent) throws SQLException
    {
        switch(keyEvent.getCode())
        {
            case ESCAPE -> closeStage();
            case ENTER -> onAdd();
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
                case "addVehicle" -> onAdd();
            }
        }
    }
    @FXML
    protected void buttonHover(MouseEvent mouseEvent)
    {
        baseController.changeStyle((Button) mouseEvent.getSource());
    }


    private void onAdd() throws SQLException
    {
        String regNum = registrationNumber.getText().toUpperCase() + " " + plateExtension.getSelectionModel().getSelectedItem();

        String registrationCheck = "SELECT vehicleRegistration FROM Vehicle WHERE vehicleRegistration = \'" + regNum + "\'";
        ResultSet result = RentACar.statement.executeQuery(registrationCheck);

        if(result.next())
        {
            registrationNumber.clear();
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
            alert.setHeaderText("Registration Number: This vehicle already exists in the table");
            alert.showAndWait();
        }
        else
        {
            int clientNumber =  this.clientNumber;

            String regExpString = registrationExpirationDate.getEditor().getText();
            regExpString = regExpString.replace("/", "-");

            boolean insured = vehicleInsurance.selectedProperty().getValue();

            String isInsured = "No";
            if(insured)
                isInsured = "Yes";

            String make = vehicleMake.getText();
            String model = vehicleModel.getText();
            String colour = vehicleColour.getText();
            int seats = vehicleSeats.getValue();

            String startDateString = vehicleStartDate.getEditor().getText();
            startDateString = startDateString.replace("/", "-");

            String endDateString = vehicleEndDate.getEditor().getText();
            endDateString = endDateString.replace("/", "-");


            String costMultiString = costMultiplier.getText();

            if(baseController.dateCheck(registrationExpirationDate, regExpString) && baseController.dateCheck(vehicleStartDate, startDateString) && baseController.dateCheck(vehicleEndDate, endDateString))
            {
                if(emptyChecks(regExpString, make, colour, startDateString, endDateString, costMultiString) && errorChecks(regNum, Date.valueOf(regExpString), make, colour, Date.valueOf(startDateString), Date.valueOf(endDateString), costMultiString))
                {
                    Date regExp = Date.valueOf(regExpString);
                    Date start = Date.valueOf(startDateString);
                    Date end = Date.valueOf(endDateString);
                    double costMulti = Math.round(Double.parseDouble(costMultiString) * 100.0) / 100.0;

                    String sql = "INSERT INTO Vehicle " +
                            "(vehicleRegistration, clientNumber, registrationExpiryDate, insured, make, model, colour, seats, startDate, endDate, costMultiplier, active)" +
                            "VALUES (\'"+ regNum +"\',\'"+ clientNumber +"\',\'"+ regExp +"\',\'"+ insured +"\',\'"+ make +"\',\'"+ model +"\',\'"+ colour +"\',\'"+ seats +"\',\'"+ start +"\',\'"+ end +"\',\'"+ costMulti +"\', Yes)";
                    RentACar.statement.executeUpdate(sql);

                    Vehicle newVehicle = new Vehicle(regNum, clientNumber, regExp, isInsured, make, model, colour, seats, start, end, costMulti);
                    Vehicle.vehicleList.add(newVehicle);

                    Alert vehicleAdded = new Alert(Alert.AlertType.INFORMATION);
                    ((Stage) vehicleAdded.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
                    vehicleAdded.setHeaderText("Vehicle Added Successfully");
                    vehicleAdded.showAndWait();
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
    private boolean emptyChecks(String regExp, String make, String colour, String start, String end, String costMulti)
    {
        if(registrationNumber.getText().isEmpty())
        {
            errorMessage = "Registration Number is empty";
            registrationNumber.clear();
            return false;
        }
        if(regExp.isEmpty())
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
    private boolean errorChecks(String regNum, Date regExp, String make, String colour, Date start, Date end, String costMulti)
    {
        if(plateExtension.getSelectionModel().getSelectedItem().equals("L"))
        {
            if(regNum.length() > 8)
            {
                errorMessage = "Registration Number: Registration Number cannot be longer than 7 characters";
                registrationNumber.clear();
                return false;
            }
        }
        else
        {
            if(regNum.length() > 9)
            {
                errorMessage = "Registration Number: Registration Number cannot be longer than 7 characters";
                registrationNumber.clear();
                return false;
            }
        }
        if(!baseController.symbolCheck(registrationNumber.getText()))
        {
            errorMessage = "Registration Number: Registration Number cannot contain any special characters";
            registrationNumber.clear();
            return false;
        }
        if(regExp.before(Date.valueOf(LocalDate.now())))
        {
            errorMessage = "Registration Expiration Date: Registration Date cannot be a date in the past";
            registrationExpirationDate.setValue(null);
            return false;
        }

        if(!baseController.errorValidationCheck(baseController.numberArray, make) | !baseController.symbolCheck(make, '-'))
        {
            errorMessage = "Vehicle Make: " + make + " is in the incorrect format";
            vehicleMake.clear();
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

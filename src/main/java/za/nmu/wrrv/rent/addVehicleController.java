package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class addVehicleController implements Initializable
{
    @FXML
    protected TextField registrationNumber;
    @FXML
    protected TextField vehicleMake;
    @FXML
    protected TextField vehicleModel;
    @FXML
    protected TextField vehicleColour;
    @FXML
    protected Spinner<Integer> vehicleSeats;
    @FXML
    protected DatePicker licenceExpirationDate;
    @FXML
    protected CheckBox vehicleInsurance;
    @FXML
    protected DatePicker vehicleStartDate;
    @FXML
    protected DatePicker vehicleEndDate;
    @FXML
    protected Button cancel;
    @FXML
    protected Button addVehicle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        SpinnerValueFactory<Integer> seats = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24);
        seats.setValue(1);

        vehicleSeats.setValueFactory(seats);
    }
}

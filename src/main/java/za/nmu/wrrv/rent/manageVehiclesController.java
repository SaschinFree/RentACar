package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class manageVehiclesController implements Initializable
{
    @FXML
    protected Button search;
    @FXML
    protected TableColumn<Vehicle, String> vehicleRegistration;
    @FXML
    protected TableColumn<Vehicle, Integer> clientNumber;
    @FXML
    protected TableColumn<Vehicle, Date> registrationExpiryDate;
    @FXML
    protected TableColumn<Vehicle, Boolean> insured;
    @FXML
    protected TableColumn<Vehicle, String> make;
    @FXML
    protected TableColumn<Vehicle, String> model;
    @FXML
    protected TableColumn<Vehicle, String> colour;
    @FXML
    protected TableColumn<Vehicle, Integer> seats;
    @FXML
    protected TableColumn<Vehicle, Date> startDate;
    @FXML
    protected TableColumn<Vehicle, Date> endDate;
    @FXML
    protected TableColumn<Vehicle, Double> costMultiplier;
    @FXML
    protected Button addVehicle;
    @FXML
    protected Button updateVehicle;
    @FXML
    protected Button back;
    @FXML
    protected TextField searchQuery;
    @FXML
    protected TableView<Vehicle> vehicleTable;

    protected static Vehicle thisVehicle;
    protected static boolean vehicleUpdated;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        if(loginController.thisUser.isAdmin())
            addVehicle.setVisible(false);

        vehicleUpdated = false;

        updateVehicle.setVisible(false);

        vehicleTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        vehicleRegistration.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
        clientNumber.setCellValueFactory(new PropertyValueFactory<>("clientNumber"));
        registrationExpiryDate.setCellValueFactory(new PropertyValueFactory<>("registrationExpiryDate"));
        insured.setCellValueFactory(new PropertyValueFactory<>("insured"));
        make.setCellValueFactory(new PropertyValueFactory<>("make"));
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        colour.setCellValueFactory(new PropertyValueFactory<>("colour"));
        seats.setCellValueFactory(new PropertyValueFactory<>("seats"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        costMultiplier.setCellValueFactory(new PropertyValueFactory<>("costMultiplier"));

        vehicleTable.setItems(baseController.vehicles);
    }

    @FXML
    protected void vehicleSelected(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            if(!loginController.thisUser.isAdmin())
            {
                thisVehicle = vehicleTable.getSelectionModel().getSelectedItem();
                updateVehicle.setVisible(true);
            }
        }
    }

    @FXML
    protected void buttonClicked(MouseEvent mouseEvent) throws IOException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Button thisButton = (Button) mouseEvent.getSource();
            String buttonId = thisButton.getId();
            switch (buttonId)
            {
                case "search" -> onSearch();
                case "addVehicle" -> baseController.newScreen("addVehicle", "Add A Vehicle");
                case "updateVehicle" ->
                        {
                            if(thisVehicle != null)
                                baseController.newScreen("updateVehicle", "Update A Vehicle");
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

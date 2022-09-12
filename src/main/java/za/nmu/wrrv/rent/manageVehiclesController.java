package za.nmu.wrrv.rent;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class manageVehiclesController implements Initializable
{
    @FXML
    protected Button search;
    @FXML
    protected TableColumn vehicleRegistration;
    @FXML
    protected TableColumn clientNumber;
    @FXML
    protected TableColumn registrationExpiryDate;
    @FXML
    protected TableColumn insured;
    @FXML
    protected TableColumn model;
    @FXML
    protected TableColumn make;
    @FXML
    protected TableColumn colour;
    @FXML
    protected TableColumn seats;
    @FXML
    protected TableColumn startDate;
    @FXML
    protected TableColumn endDate;
    @FXML
    protected TableColumn costMultiplier;
    @FXML
    protected Button addVehicle;
    @FXML
    protected Button updateVehicle;
    @FXML
    protected TextField searchQuery;
    @FXML
    protected TableView vehicleTable;

    private static ObservableList<Vehicle> vehicles;
    static
    {
        try
        {
            vehicles = Vehicle.getVehicles();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        vehicleTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        vehicleRegistration.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
        clientNumber.setCellValueFactory(new PropertyValueFactory<>("clientNumber"));
        registrationExpiryDate.setCellValueFactory(new PropertyValueFactory<>("registrationExpiryDate"));
        insured.setCellValueFactory(new PropertyValueFactory<>("insured"));
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        make.setCellValueFactory(new PropertyValueFactory<>("make"));
        colour.setCellValueFactory(new PropertyValueFactory<>("colour"));
        seats.setCellValueFactory(new PropertyValueFactory<>("seats"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        costMultiplier.setCellValueFactory(new PropertyValueFactory<>("costMultiplier"));

        vehicleTable.setItems(vehicles);
    }

    @FXML
    protected void backToMenu(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            baseController.nextScene(baseController.userLoggedOn);
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
                case "search":
                    onSearchClicked(searchQuery);
                    break;
                case "addVehicle":
                    addVehicle();
                    break;
                case "updateVehicle":
                    break;
            }
        }
    }

    @FXML
    protected void onSearchClicked(TextField thisQuery)
    {

    }

    private void addVehicle() throws IOException
    {
        FXMLLoader addVehicleLoader = new FXMLLoader(RentACar.class.getResource("addVehicle.fxml"));
        Scene addVehicleScene = new Scene(addVehicleLoader.load());
        Stage addVehicleStage = new Stage();

        addVehicleStage.setScene(addVehicleScene);
        addVehicleStage.setTitle("Add New Vehicle");
        addVehicleStage.setResizable(false);
        addVehicleStage.initModality(Modality.WINDOW_MODAL);
        addVehicleStage.initOwner(RentACar.mainStage);

        addVehicleStage.show();
    }
}

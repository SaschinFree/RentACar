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
import java.sql.Date;
import java.sql.SQLException;
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
    protected TextField searchQuery;
    @FXML
    protected TableView<Vehicle> vehicleTable;

    protected static Vehicle thisVehicle;
    protected static boolean vehicleUpdated;

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

        vehicleTable.setItems(vehicles);
    }

    @FXML
    protected void vehicleSelected(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            thisVehicle = vehicleTable.getSelectionModel().getSelectedItem();
            updateVehicle.setVisible(true);
        }
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
            switch (buttonId)
            {
                case "search" -> onSearchClicked(searchQuery);
                case "addVehicle" -> addVehicle();
                case "updateVehicle" -> updateVehicle();
            }
        }
    }
    private void updateVehicle() throws IOException
    {
        if(thisVehicle != null)
        {
            FXMLLoader updateVehicleLoader = new FXMLLoader(RentACar.class.getResource("updateVehicle.fxml"));
            Scene updateVehicleScene = new Scene(updateVehicleLoader.load());
            Stage updateVehicleStage = new Stage();

            updateVehicleStage.setScene(updateVehicleScene);
            updateVehicleStage.setTitle("Update A Vehicle");
            updateVehicleStage.setResizable(false);
            updateVehicleStage.initModality(Modality.WINDOW_MODAL);
            updateVehicleStage.initOwner(RentACar.mainStage);

            updateVehicleStage.showAndWait();
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

        addVehicleStage.showAndWait();
    }
}

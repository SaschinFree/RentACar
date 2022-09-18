package za.nmu.wrrv.rent;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class manageVehiclesController implements Initializable
{
    @FXML
    protected ChoiceBox<String> searchFilter;
    @FXML
    protected TextField searchQuery;
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
    protected TableView<Vehicle> vehicleTable;

    protected static Vehicle thisVehicle;
    protected static boolean vehicleUpdated;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        searchFilter.getItems().addAll(
                "vehicleRegistration",
                "clientNumber",
                "registrationExpiryDate",
                "insured",
                "make",
                "model",
                "colour",
                "seats",
                "startDate",
                "endDate",
                "costMultiplier");

        searchFilter.setValue("vehicleRegistration");

        searchFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, stringSingleSelectionModel, t1) ->
        {
            searchQuery.clear();

            switch(searchFilter.getSelectionModel().getSelectedItem())
            {
                case "vehicleRegistration" -> searchQuery.setPromptText("ABC123 EC or CUSTOM MP etc");
                case "clientNumber" -> searchQuery.setPromptText("1");
                case "registrationExpiryDate", "startDate", "endDate" -> searchQuery.setPromptText("YYYY/MM/DD or YYYY-MM-DD");
                case "insured" -> searchQuery.setPromptText("Yes or No");
                case "make" -> searchQuery.setPromptText("Volkswagen or BMW etc");
                case "model" -> searchQuery.setPromptText("Polo or 530i etc");
                case "colour" -> searchQuery.setPromptText("Red or Colour,Colour etc");
                case "seats" -> searchQuery.setPromptText("2");
                case "costMultiplier" -> searchQuery.setPromptText("0.0");
            }
        });

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
                if(thisVehicle != null)
                    updateVehicle.setVisible(true);
            }
        }
    }

    @FXML
    protected void buttonClicked(MouseEvent mouseEvent) throws IOException, SQLException
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
    private void onSearch() throws SQLException
    {
        ObservableList<Vehicle> filteredList = Vehicle.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText());
        vehicleTable.setItems(filteredList);
    }
}

package za.nmu.wrrv.rent;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
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

public class manageVehiclesController implements Initializable, EventHandler<Event>
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
    protected TableColumn<Vehicle, String> insured;
    @FXML
    protected TableColumn<Vehicle, String> make;
    @FXML
    protected TableColumn<Vehicle, String> model;
    @FXML
    protected TableColumn<Vehicle, Date> startDate;
    @FXML
    protected TableColumn<Vehicle, Date> endDate;
    @FXML
    protected TableView<Vehicle> vehicleTable;
    @FXML
    protected Button addVehicle;
    @FXML
    protected Button updateVehicle;
    @FXML
    protected Button back;

    protected static Vehicle thisVehicle;
    protected static boolean vehicleUpdated;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        search.setOnAction(this::handle);
        addVehicle.setOnAction(this::handle);
        updateVehicle.setOnAction(this::handle);
        back.setOnAction(this::handle);

        search.setTooltip(new Tooltip("Alt+S"));
        addVehicle.setTooltip(new Tooltip("Alt+A"));
        updateVehicle.setTooltip(new Tooltip("Alt+U"));
        back.setTooltip(new Tooltip("Alt+B"));

        searchFilter.getItems().addAll(
                "None",
                "vehicleRegistration",
                "clientNumber",
                "registrationExpiryDate",
                "insured",
                "make",
                "model",
                "colour",
                "seats",
                "startDate",
                "endDate");
        searchFilter.setValue("None");

        searchFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, stringSingleSelectionModel, t1) ->
        {
            searchQuery.clear();

            switch(searchFilter.getSelectionModel().getSelectedItem())
            {
                case "None" -> searchQuery.setPromptText("Search");
                case "vehicleRegistration" -> searchQuery.setPromptText("ABC123 EC or CUSTOM MP etc");
                case "clientNumber" -> searchQuery.setPromptText("1");
                case "registrationExpiryDate", "startDate", "endDate" -> searchQuery.setPromptText("YYYY-MM-DD");
                case "insured" -> searchQuery.setPromptText("Yes or No");
                case "make" -> searchQuery.setPromptText("Volkswagen or BMW etc");
                case "model" -> searchQuery.setPromptText("Polo or 530i etc");
                case "colour" -> searchQuery.setPromptText("Red or Colour,Colour etc");
                case "seats" -> searchQuery.setPromptText("2");
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
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        vehicleTable.setItems(baseController.vehicles);
    }
    @Override
    public void handle(Event event)
    {
        Button thisButton = (Button) event.getSource();
        String buttonId = thisButton.getId();
        switch (buttonId)
        {
            case "search" ->
                    {
                        try
                        {
                            onSearch();
                        }
                        catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }
            case "addVehicle" ->
                    {
                        try
                        {
                            baseController.newScreen("addVehicle", "Add A Vehicle");
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
            case "updateVehicle" ->
                    {
                        if(thisVehicle != null)
                        {
                            try
                            {
                                baseController.newScreen("updateVehicle", "Update A Vehicle");
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
            case "back" -> baseController.nextScene(baseController.userLoggedOn);
        }
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
        if(searchFilter.getSelectionModel().getSelectedItem().equals("None"))
            vehicleTable.setItems(baseController.vehicles);
        else
        {
            ObservableList<Vehicle> filteredList = Vehicle.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText(), "");
            vehicleTable.setItems(filteredList);
        }
    }
}

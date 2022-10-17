package za.nmu.wrrv.rent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import net.ucanaccess.converters.Functions;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Year;
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

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        setupMnemonics();

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
                case "None" ->
                        {
                            searchQuery.setPromptText("Search");
                            vehicleTable.setItems(baseController.vehicles);
                        }
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
            case "search" -> onSearch();
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

    private void setupMnemonics()
    {
        search.setMnemonicParsing(true);
        addVehicle.setMnemonicParsing(true);
        updateVehicle.setMnemonicParsing(true);
        back.setMnemonicParsing(true);

        search.setOnAction(this::handle);
        addVehicle.setOnAction(this::handle);
        updateVehicle.setOnAction(this::handle);
        back.setOnAction(this::handle);

        search.setTooltip(new Tooltip("Alt+S"));
        addVehicle.setTooltip(new Tooltip("Alt+A"));
        updateVehicle.setTooltip(new Tooltip("Alt+U"));
        back.setTooltip(new Tooltip("Alt+B"));
    }

    private void onSearch()
    {
        if(searchFilter.getSelectionModel().getSelectedItem().equals("None"))
            vehicleTable.setItems(baseController.vehicles);
        else
        {
            if(searchFilter.getSelectionModel().getSelectedItem().equals("registrationExpiryDate") || searchFilter.getSelectionModel().getSelectedItem().equals("startDate") || searchFilter.getSelectionModel().getSelectedItem().equals("endDate"))
                vehicleTable.setItems(dateValid(searchQuery.getText()));
            else
            {
                ObservableList<Vehicle> filteredList = Vehicle.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), searchQuery.getText());
                vehicleTable.setItems(filteredList);
            }
        }
    }
    private ObservableList<Vehicle> dateValid(String date)
    {
        date = date.replace("/", "-");

        if(baseController.dateCheck(date))
        {
            String[] split = date.split("-");
            if(Functions.isNumeric(split[0]) && Functions.isNumeric(split[1]) && Functions.isNumeric(split[2]))
            {
                switch (Integer.parseInt(split[1]))
                {
                    case 1,3,5,7,8,10,12 ->
                            {
                                if(Integer.parseInt(split[2]) > 0 && Integer.parseInt(split[2]) <= 31)
                                    return Vehicle.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), date);
                            }
                    case 4,6,9,11 ->
                            {
                                if(Integer.parseInt(split[2]) > 0 && Integer.parseInt(split[2]) <= 30)
                                    return Vehicle.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), date);
                            }
                    case 2 ->
                            {
                                if(Year.of(Integer.parseInt(split[0])).isLeap())
                                {
                                    if(Integer.parseInt(split[2]) > 0 && Integer.parseInt(split[2]) <= 29)
                                        return Vehicle.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), date);
                                }
                                else
                                {
                                    if(Integer.parseInt(split[2]) > 0 && Integer.parseInt(split[2]) <= 28)
                                        return Vehicle.searchQuery(searchFilter.getSelectionModel().getSelectedItem(), date);
                                }
                            }
                }
            }
        }
        return null;
    }
}

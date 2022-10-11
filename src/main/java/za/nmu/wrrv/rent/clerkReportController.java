package za.nmu.wrrv.rent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.net.URL;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class clerkReportController implements Initializable
{
    @FXML
    protected VBox adminBox;
    @FXML
    protected RadioButton expiredB;
    @FXML
    protected RadioButton overdueV;
    @FXML
    protected ScrollPane scrollPane;
    @FXML
    protected VBox insideScroll;
    @FXML
    protected HBox regBox;
    @FXML
    protected Label regLabel;
    @FXML
    protected Label reg;
    @FXML
    protected HBox overdueBox;
    @FXML
    protected Label overdueLabel;
    @FXML
    protected Label overdue;
    @FXML
    protected HBox makeModelBox;
    @FXML
    protected Label makeModelLabel;
    @FXML
    protected Label makeModel;
    @FXML
    protected HBox nameSurnameBox;
    @FXML
    protected Label nameSurnameLabel;
    @FXML
    protected Label nameSurname;
    @FXML
    protected HBox contactBox;
    @FXML
    protected Label contactLabel;
    @FXML
    protected Label details;
    @FXML
    protected Line line;
    @FXML
    protected Button back;

    protected final List<String> clients = new ArrayList<>();
    protected boolean expiredSelected = false;
    protected boolean overdueSelected = false;
    protected ObservableList<Booking> selectedReport = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        scrollPane.setVisible(false);

        attachEventHandler();

    }
    @FXML
    protected void buttonClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            baseController.nextScene(baseController.userLoggedOn);
        }
    }

    private void attachEventHandler()
    {
        adminBox.addEventHandler(EventType.ROOT, event ->
        {
            if(expiredB.selectedProperty().getValue())
            {
                if(!expiredSelected)
                {
                    scrollPane.setContent(null);
                    overdueV.selectedProperty().setValue(false);
                    expiredSelected = true;
                    overdueSelected = false;

                    selectedReport = FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.isIsBeingRented().equals("No") && booking.getEndDate().before(Date.valueOf(LocalDate.now()))).toList());

                    if(selectedReport.size() > 0)
                    {
                        VBox insideScrollCopy = new VBox();
                        insideScrollCopy.setBackground(insideScroll.getBackground());
                        insideScrollCopy.setPrefSize(insideScroll.getPrefWidth(), insideScroll.getPrefHeight());

                        insideScroll = insideScrollCopy;

                        for(Booking thisBooking : selectedReport)
                        {
                            Client thisClient = baseController.clients.stream().filter(client -> client.getClientNumber() == thisBooking.getClientNumber()).toList().get(0);

                            HBox numBox = new HBox();
                            numBox.setPrefSize(regBox.getPrefWidth(), regBox.getPrefHeight());

                            Label bookingNumLabel = new Label("Booking Number");
                            bookingNumLabel.setPrefWidth(regLabel.getPrefWidth());
                            bookingNumLabel.setFont(Font.font(String.valueOf(regLabel.getFont()), regLabel.getFont().getSize()));

                            Label num = new Label(String.valueOf(thisBooking.getBookingNumber()));
                            num.setFont(Font.font(String.valueOf(reg.getFont()), reg.getFont().getSize()));

                            numBox.getChildren().addAll(bookingNumLabel, num);

                            HBox nameSurnameBoxCopy = new HBox();
                            nameSurnameBoxCopy.setPrefSize(regBox.getPrefWidth(), regBox.getPrefHeight());

                            Label nameSurnameLabelCopy = new Label("Client Name & Surname");
                            nameSurnameLabelCopy.setPrefWidth(regLabel.getPrefWidth());
                            nameSurnameLabelCopy.setFont(Font.font(String.valueOf(regLabel.getFont()), regLabel.getFont().getSize()));

                            Label nameSurnameCopy = new Label(thisClient.getFirstName() + " " + thisClient.getSurname());
                            nameSurnameCopy.setFont(Font.font(String.valueOf(reg.getFont()), reg.getFont().getSize()));

                            nameSurnameBoxCopy.getChildren().addAll(nameSurnameLabelCopy, nameSurnameCopy);

                            HBox contactBoxCopy = new HBox();
                            contactBoxCopy.setPrefSize(regBox.getPrefWidth(), regBox.getPrefHeight());

                            Label contactLabelCopy = new Label("Client Contact Details");
                            contactLabelCopy.setPrefWidth(regLabel.getPrefWidth());
                            contactLabelCopy.setFont(Font.font(String.valueOf(regLabel.getFont()), regLabel.getFont().getSize()));

                            Label contact = new Label(thisClient.getContactNumber() + ", " + thisClient.getEmail());
                            contact.setFont(Font.font(String.valueOf(reg.getFont()), reg.getFont().getSize()));

                            contactBoxCopy.getChildren().addAll(contactLabelCopy, contact);

                            Line lineCopy = new Line();
                            lineCopy.setStartX(line.getStartX());
                            lineCopy.setEndX(line.getEndX());
                            lineCopy.setStroke(line.getStroke());
                            line.setStrokeWidth(line.getStrokeWidth());

                            if(thisBooking.isHasPaid().equals("Yes"))
                            {
                                HBox costBox = new HBox();
                                costBox.setPrefSize(regBox.getPrefWidth(), regBox.getPrefHeight());

                                Label costLabel = new Label("Booking Cost");
                                costLabel.setPrefWidth(regLabel.getPrefWidth());
                                costLabel.setFont(Font.font(String.valueOf(regLabel.getFont()), regLabel.getFont().getSize()));

                                Label cost = new Label(String.valueOf(thisBooking.getCost()));
                                cost.setFont(Font.font(String.valueOf(reg.getFont()), reg.getFont().getSize()));

                                costBox.getChildren().addAll(costLabel, cost);

                                HBox hasPaidBox = new HBox();
                                hasPaidBox.setPrefSize(regBox.getPrefWidth(), regBox.getPrefHeight());

                                Label paidLabel = new Label("Booking Paid For");
                                paidLabel.setPrefWidth(regLabel.getPrefWidth());
                                paidLabel.setFont(Font.font(String.valueOf(regLabel.getFont()), regLabel.getFont().getSize()));

                                Label paid = new Label(thisBooking.isHasPaid());
                                paid.setFont(Font.font(String.valueOf(reg.getFont()), reg.getFont().getSize()));

                                hasPaidBox.getChildren().addAll(paidLabel, paid);

                                insideScroll.getChildren().addAll(numBox, nameSurnameBoxCopy, contactBoxCopy, costBox, hasPaidBox, lineCopy);
                            }
                            else
                            {
                                insideScroll.getChildren().addAll(numBox, nameSurnameBoxCopy, contactBoxCopy, lineCopy);
                            }
                        }
                        scrollPane.setContent(insideScroll);
                        scrollPane.setVisible(true);
                    }
                }
            }
            if(overdueV.selectedProperty().getValue())
            {
                if(!overdueSelected)
                {
                    scrollPane.setContent(null);
                    expiredB.selectedProperty().setValue(false);
                    overdueSelected = true;
                    expiredSelected = false;

                    selectedReport = FXCollections.observableArrayList(baseController.bookings.stream().filter(booking -> booking.isActive() && booking.isIsBeingRented().equals("Yes") && booking.getEndDate().before(Date.valueOf(LocalDate.now()))).toList());

                    if(selectedReport.size() > 0)
                    {
                        Booking booking = selectedReport.get(0);
                        Vehicle thisVehicle = baseController.vehicles.stream().filter(vehicle -> vehicle.getVehicleRegistration().equals(booking.getVehicleRegistration())).toList().get(0);
                        Client thisClient = baseController.clients.stream().filter(client -> client.getClientNumber() == booking.getClientNumber()).toList().get(0);

                        Duration difference = Duration.between(booking.getEndDate().toLocalDate().atStartOfDay(), LocalDate.now().atStartOfDay());
                        double days = Double.parseDouble(String.valueOf(difference.toDays()));

                        VBox insideScrollCopy = new VBox();
                        insideScrollCopy.setBackground(insideScroll.getBackground());
                        insideScrollCopy.setPrefSize(insideScroll.getPrefWidth(), insideScroll.getPrefHeight());

                        insideScroll = insideScrollCopy;

                        reg.setText(booking.getVehicleRegistration());
                        overdue.setText(String.valueOf((int) days));

                        makeModel.setText(thisVehicle.getMake() + " " + thisVehicle.getModel());
                        nameSurname.setText(thisClient.getFirstName() + " " + thisClient.getSurname());
                        details.setText(thisClient.getContactNumber() + ", " + thisClient.getEmail());

                        insideScroll.getChildren().addAll(regBox, overdueBox, makeModelBox, nameSurnameBox, contactBox, line);

                        if(selectedReport.size() > 1)
                        {
                            for(int i = 1; i < selectedReport.size(); i++)
                            {
                                Booking thisBooking = selectedReport.get(i);
                                thisVehicle = baseController.vehicles.stream().filter(vehicle -> vehicle.getVehicleRegistration().equals(thisBooking.getVehicleRegistration())).toList().get(0);
                                thisClient = baseController.clients.stream().filter(client -> client.getClientNumber() == thisBooking.getClientNumber()).toList().get(0);

                                difference = Duration.between(thisBooking.getEndDate().toLocalDate().atStartOfDay(), LocalDate.now().atStartOfDay());
                                days = Double.parseDouble(String.valueOf(difference.toDays()));

                                HBox regBoxCopy = new HBox();
                                regBoxCopy.setPrefSize(regBox.getPrefWidth(), regBox.getPrefHeight());

                                Label regLabelCopy = new Label("Vehicle Registration");
                                regLabelCopy.setPrefWidth(regLabel.getPrefWidth());
                                regLabelCopy.setFont(Font.font(String.valueOf(regLabel.getFont()), regLabel.getFont().getSize()));

                                Label regCopy = new Label(thisBooking.getVehicleRegistration());
                                regCopy.setFont(Font.font(String.valueOf(reg.getFont()), reg.getFont().getSize()));

                                regBoxCopy.getChildren().addAll(regLabelCopy, regCopy);

                                HBox overdueBoxCopy = new HBox();
                                overdueBoxCopy.setPrefSize(overdueBox.getPrefWidth(), overdueBox.getPrefHeight());

                                Label overdueLabelCopy = new Label("Days Overdue");
                                overdueLabelCopy.setPrefWidth(overdueLabel.getPrefWidth());
                                overdueLabelCopy.setFont(Font.font(String.valueOf(overdueLabel.getFont()), overdueLabel.getFont().getSize()));

                                Label overdueCopy = new Label(String.valueOf((int) days));
                                overdueCopy.setFont(Font.font(String.valueOf(overdue.getFont()), overdue.getFont().getSize()));

                                overdueBoxCopy.getChildren().addAll(overdueLabelCopy, overdueCopy);

                                HBox makeModelBoxCopy = new HBox();
                                makeModelBoxCopy.setPrefSize(makeModelBox.getPrefWidth(), makeModelBox.getPrefHeight());

                                Label makeModelLabelCopy = new Label("Vehicle Make & Model");
                                makeModelLabelCopy.setPrefWidth(makeModelLabel.getPrefWidth());
                                makeModelLabelCopy.setFont(Font.font(String.valueOf(makeModelLabel.getFont()), makeModelLabel.getFont().getSize()));

                                Label makeModelCopy = new Label(thisVehicle.getMake() + " " + thisVehicle.getModel());
                                makeModelCopy.setFont(Font.font(String.valueOf(makeModel.getFont()), makeModel.getFont().getSize()));

                                makeModelBoxCopy.getChildren().addAll(makeModelLabelCopy, makeModelCopy);

                                HBox nameSurnameBoxCopy = new HBox();
                                nameSurnameBoxCopy.setPrefSize(nameSurnameBox.getPrefWidth(), nameSurnameBox.getPrefHeight());

                                Label nameSurnameLabelCopy = new Label("Client Name & Surname");
                                nameSurnameLabelCopy.setPrefWidth(nameSurnameLabel.getPrefWidth());
                                nameSurnameLabelCopy.setFont(Font.font(String.valueOf(nameSurnameLabel.getFont()), nameSurnameLabel.getFont().getSize()));

                                Label nameSurnameCopy = new Label(thisClient.getFirstName() + " " + thisClient.getSurname());
                                nameSurnameCopy.setFont(Font.font(String.valueOf(nameSurname.getFont()), nameSurname.getFont().getSize()));

                                nameSurnameBoxCopy.getChildren().addAll(nameSurnameLabelCopy, nameSurnameCopy);

                                HBox contactBoxCopy = new HBox();
                                contactBoxCopy.setPrefSize(contactBox.getPrefWidth(), contactBox.getPrefHeight());

                                Label contactLabelCopy = new Label("Client Contact Details");
                                contactLabelCopy.setPrefWidth(contactLabel.getPrefWidth());
                                contactLabelCopy.setFont(Font.font(String.valueOf(contactLabel.getFont()), contactLabel.getFont().getSize()));

                                Label contactCopy = new Label(thisClient.getContactNumber() + ", " + thisClient.getEmail());
                                contactCopy.setFont(Font.font(String.valueOf(details.getFont()), details.getFont().getSize()));

                                contactBoxCopy.getChildren().addAll(contactLabelCopy, contactCopy);

                                Line lineCopy = new Line();
                                lineCopy.setStartX(line.getStartX());
                                lineCopy.setEndX(line.getEndX());
                                lineCopy.setStroke(line.getStroke());
                                line.setStrokeWidth(line.getStrokeWidth());

                                insideScroll.getChildren().addAll(regBoxCopy, overdueBoxCopy, makeModelBoxCopy, nameSurnameBoxCopy, contactBoxCopy, lineCopy);

                            }
                        }
                        scrollPane.setContent(insideScroll);
                        scrollPane.setVisible(true);
                    }
                }
            }
        });
    }
}

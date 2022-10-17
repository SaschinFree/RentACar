package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class addClientController implements Initializable
{
    @FXML
    protected RadioButton nationalityZA;
    @FXML
    protected RadioButton nationalityOther;
    @FXML
    protected TextField clientID;
    @FXML
    protected TextField firstName;
    @FXML
    protected TextField surname;
    @FXML
    protected TextField contactNumber;
    @FXML
    protected TextField email;
    @FXML
    protected DatePicker licenceExpiryDate;
    @FXML
    protected TextField streetNumber;
    @FXML
    protected TextField streetName;
    @FXML
    protected TextField suburb;
    @FXML
    protected TextField city;
    @FXML
    protected TextField postalCode;
    @FXML
    protected CheckBox isCompany;
    @FXML
    protected TextField companyName;
    @FXML
    protected Button cancel;
    @FXML
    protected Button addClient;

    private final Alert alert = new Alert(Alert.AlertType.ERROR);
    private String errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        licenceExpiryDate.setConverter(baseController.dateConverter);

        nationalityZA.setFocusTraversable(false);
        nationalityOther.setFocusTraversable(false);

        clientID.setVisible(false);
        companyName.setVisible(false);
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
    protected void natSelected(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            RadioButton thisRadioButton = (RadioButton) mouseEvent.getSource();
            String buttonId = thisRadioButton.getId();
            switch(buttonId)
            {
                case "nationalityZA" ->
                        {
                            nationalityOther.setSelected(false);
                            clientID.setPromptText("ID Number");
                        }
                case "nationalityOther" ->
                        {
                            nationalityZA.setSelected(false);
                            clientID.setPromptText("Passport Number");
                        }
            }
            clientID.setVisible(true);
        }
    }
    @FXML
    protected void compSelected(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            companyName.setVisible(isCompany.selectedProperty().getValue());
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
                case "addClient" -> onAdd();
            }
        }
    }

    private void onAdd() throws SQLException
    {
        String clientID = this.clientID.getText();

        String fName =  firstName.getText();
        String sName = surname.getText();
        String number = contactNumber.getText();
        String email = this.email.getText();

        String licenceString = licenceExpiryDate.getEditor().getText();
        licenceString = licenceString.replace("/", "-");


        String strNum = streetNumber.getText();
        String strName = streetName.getText();
        String sub = suburb.getText();
        String city = this.city.getText();
        String postCode = postalCode.getText();

        String compName = "Private";

        if(isCompany.selectedProperty().getValue())
            compName = companyName.getText();

        List<Client> duplicate = baseController.clients.stream().filter(client -> client.isActive() && client.getClientID().equals(clientID)).toList();
        if(duplicate.size() > 0)
        {
            this.clientID.clear();

            firstName.clear();
            surname.clear();
            contactNumber.clear();
            this.email.clear();
            licenceExpiryDate.setValue(null);
            streetNumber.clear();
            streetName.clear();
            suburb.clear();
            this.city.clear();
            postalCode.clear();

            companyName.clear();
            isCompany.setSelected(false);
            companyName.setVisible(false);

            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
            alert.setHeaderText("Client ID: This client already exists in the table");
            alert.showAndWait();
        }
        else
        {
            if(baseController.dateCheck(licenceExpiryDate, licenceString))
            {
                if(emptyChecks(clientID, fName, sName, number, email, licenceString, strNum, strName, sub, city, postCode) && errorChecks(clientID, fName, sName, number, email, licenceString, strNum, strName, sub, city, postCode))
                {
                    Date licence = Date.valueOf(licenceString);

                    duplicate = baseController.clients.stream().filter(client -> client.isActive() && client.getContactNumber().equals(number)).toList();
                    if(duplicate.size() > 0 && !duplicate.get(0).getClientID().equals(manageClientsController.thisClient.getClientID()))
                    {
                        contactNumber.clear();
                        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
                        alert.setHeaderText("This number already exists in the table");
                        alert.showAndWait();
                    }
                    else
                    {
                        duplicate = baseController.clients.stream().filter(client -> client.isActive() && client.getEmail().equals(email)).toList();
                        if(duplicate.size() > 0 && !duplicate.get(0).getClientID().equals(manageClientsController.thisClient.getClientID()))
                        {
                            this.email.clear();
                            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
                            alert.setHeaderText("This email address already exists in the table");
                            alert.showAndWait();
                        }
                        else
                        {
                            addClient(clientID, fName, sName, number, email, licence, strNum, strName, sub, city, postCode, compName);
                            Alert clientAdded = new Alert(Alert.AlertType.INFORMATION);
                            ((Stage) clientAdded.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
                            clientAdded.setHeaderText("Client Added Successfully");
                            clientAdded.showAndWait();
                            closeStage();
                        }
                    }
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
    private void addClient(String clientID, String fName, String sName, String number, String email, Date licence, String strNum, String strName, String sub, String city, String postCode, String compName) throws SQLException
    {
        String insertClient = "INSERT INTO Client " +
                "(clientID, firstName, surname, contactNumber, email, licenceExpiryDate, streetNumber, streetName, suburb, city, postalCode, companyName, moneyOwed, active) " +
                "VALUES (\'" + clientID + "\',\'" + fName + "\',\'" + sName + "\',\'" + number + "\',\'" + email + "\',\'" + licence + "\',\'" + strNum + "\',\'" + strName + "\',\'" + sub + "\',\'" + city + "\',\'" + postCode + "\',\'" + compName + "\',\'" + 0 + "\', Yes)";
        RentACar.statement.executeUpdate(insertClient);

        String getID = "SELECT clientNumber FROM Client WHERE clientID = \'" + clientID + "\'";
        ResultSet anotherResult = RentACar.statement.executeQuery(getID);

        int clientNumber = 0;
        if(anotherResult.next())
            clientNumber = anotherResult.getInt("clientNumber");

        Client thisClient = new Client(clientNumber, clientID, fName, sName, number, email, licence, strNum, strName, sub, city, postCode, compName, 0);
        baseController.clients.add(thisClient);
    }
    private boolean emptyChecks(String clientID, String fName, String sName, String number, String email, String licence, String strNum, String strName, String sub, String city, String postCode)
    {
        if(clientID.isEmpty())
        {
            errorMessage = "Client ID is empty";
            this.clientID.clear();
            return false;
        }
        if(fName.isEmpty())
        {
            errorMessage = "First Name is empty";
            firstName.clear();
            return false;
        }

        if(sName.isEmpty())
        {
            errorMessage = "Surname is empty";
            surname.clear();
            return false;
        }

        if(number.isEmpty())
        {
            errorMessage = "Contact Number is empty";
            contactNumber.clear();
            return false;
        }

        if(email.isEmpty())
        {
            errorMessage = "Email Address is empty";
            this.email.clear();
            return false;
        }

        if(licence.isEmpty())
        {
            errorMessage = "Licence Expiration Date is empty";
            licenceExpiryDate.setValue(null);
            return false;
        }

        if(strNum.isEmpty())
        {
            errorMessage = "Street Number is empty";
            streetNumber.clear();
            return false;
        }

        if(strName.isEmpty())
        {
            errorMessage = "Street Name is empty";
            streetName.clear();
            return false;
        }

        if(sub.isEmpty())
        {
            errorMessage = "Suburb is empty";
            suburb.clear();
            return false;
        }

        if(city.isEmpty())
        {
            errorMessage = "City is empty";
            this.city.clear();
            return false;
        }

        if(postCode.isEmpty())
        {
            errorMessage = "Postal Code is empty";
            postalCode.clear();
            return false;
        }

        return true;
    }
    private boolean errorChecks(String clientID, String fName, String sName, String number, String email, String licence, String strNum, String strName, String sub, String city, String postCode)
    {
        if(!baseController.symbolCheck(clientID))
        {
            errorMessage = "Client ID is in the incorrect format";
            this.clientID.clear();
            return false;
        }
        if(!baseController.errorValidationCheck(baseController.numberArray, fName) | !baseController.symbolCheck(fName, '-'))
        {
            errorMessage = "First Name is in the incorrect format";
            firstName.clear();
            return false;
        }

        if(!baseController.errorValidationCheck(baseController.numberArray, sName) | !baseController.symbolCheck(sName, ' '))
        {
            errorMessage = "Surname is in the incorrect format";
            surname.clear();
            return false;
        }

        if(!baseController.errorValidationCheck(baseController.letterArray, number) | !baseController.symbolCheck(number))
        {
            errorMessage = "Contact Number is in the incorrect format";
            contactNumber.clear();
            return false;
        }
        if(number.length() != 10)
        {
            errorMessage = "Contact Number is not 10 digits";
            contactNumber.clear();
            return false;
        }
        if(!number.startsWith("0"))
        {
            errorMessage = "Contact Number should start with a 0";
            contactNumber.clear();
            return false;
        }

        if(!email.contains("@"))
        {
            errorMessage = "Email Address should contain an '@' symbol";
            this.email.clear();
            return false;
        }

        int i = email.indexOf("@");

        String beforeAt = email.substring(0, i);
        String afterAt = email.substring(i + 1);

        if(beforeAt.length() == 0)
        {
            errorMessage = "before the '@' sign, the email address should contain at least 1 character";
            this.email.clear();
            return false;
        }
        if(!baseController.symbolCheck(beforeAt, '.', '-', '_'))
        {
            errorMessage = "before the '@' sign, the email address should not contain any symbols, except '.', '-', or '_'";
            this.email.clear();
            return false;
        }
        if(!baseController.errorValidationCheck(baseController.numberArray, afterAt) && !baseController.symbolCheck(afterAt, '.'))
        {
            errorMessage = "After the '@' sign, the email address should not contain any numbers and symbols, except '.'";
            this.email.clear();
            return false;
        }

        if(!baseController.dateCheck(licenceExpiryDate, licence))
        {
            errorMessage = "Licence Expiration Date is in the incorrect format";
            licenceExpiryDate.setValue(null);
            return false;
        }
        if(Date.valueOf(licence).before(Date.valueOf(LocalDate.now())))
        {
            errorMessage = "Licence Expiration Date cannot be a past date";
            licenceExpiryDate.setValue(null);
            return false;
        }

        if(!baseController.symbolCheck(String.valueOf(strNum)))
        {
            errorMessage = "Street Number is in the incorrect format";
            streetNumber.clear();
            return false;
        }

        if(!baseController.symbolCheck(strName, ' '))
        {
            errorMessage = "Street Name is in the incorrect format";
            streetName.clear();
            return false;
        }

        if(!baseController.errorValidationCheck(baseController.numberArray, sub) | !baseController.symbolCheck(sub, ' '))
        {
            errorMessage = "Suburb is in the incorrect format";
            suburb.clear();
            return false;
        }

        if(!baseController.errorValidationCheck(baseController.numberArray, city) | !baseController.symbolCheck(city, ' '))
        {
            errorMessage = "City is in the incorrect format";
            this.city.clear();
            return false;
        }

        if(!baseController.errorValidationCheck(baseController.letterArray, String.valueOf(postCode)) | !baseController.symbolCheck(String.valueOf(postCode)))
        {
            errorMessage = "Postal Code is in the incorrect format";
            postalCode.clear();
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

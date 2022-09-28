package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

        clientID.setVisible(false);
        companyName.setVisible(false);
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
    protected void onCancel(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            closeStage();
        }
    }
    @FXML
    protected void onAdd(MouseEvent mouseEvent) throws SQLException
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            String clientID = this.clientID.getText();

            String fName =  firstName.getText();
            String sName = surname.getText();
            String number = contactNumber.getText();
            String email = this.email.getText();

            String licenceString = licenceExpiryDate.getEditor().getText();
            licenceString = licenceString.replace("/", "-");


            String strNumString = streetNumber.getText();
            String strName = streetName.getText();
            String sub = suburb.getText();
            String city = this.city.getText();
            String postCodeString = postalCode.getText();

            String compName = "Private";

            if(isCompany.selectedProperty().getValue())
                compName = companyName.getText();

            String clientCheck = "SELECT clientID FROM Client WHERE clientID = \'" + clientID + "\'";
            ResultSet result = RentACar.statement.executeQuery(clientCheck);

            if(result.next())
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

                alert.setHeaderText("Client ID: This client already exists in the table");
                alert.showAndWait();

                mouseEvent.consume();
            }

            if(baseController.dateCheck(licenceExpiryDate, licenceString))
            {
                Date licence = Date.valueOf(licenceString);

                if(emptyChecks(clientID, fName, sName, number, email, licence, strNumString, strName, sub, city, postCodeString) & errorChecks(clientID, fName, sName, number, email, licence, Integer.parseInt(strNumString), strName, sub, city, Integer.parseInt(postCodeString)))
                {
                    int strNum = Integer.parseInt(strNumString);
                    int postCode = Integer.parseInt(postCodeString);

                    if(isCompany.selectedProperty().getValue())
                    {
                        if(!baseController.errorValidationCheck(baseController.numberArray, compName) & !baseController.symbolCheck(compName))
                        {
                            companyName.clear();
                            alert.setHeaderText("Company Name is in the incorrect format");
                            alert.showAndWait();

                            mouseEvent.consume();
                        }
                    }

                    String insertClient = "INSERT INTO Client " +
                            "(clientNumber, clientID, firstName, surname, contactNumber, email, licenceExpiryDate, streetNumber, streetName, suburb, city, postalCode, companyName, moneyOwed, active) " +
                            "VALUES (\'" + baseController.clients.size() + 1 + "\',\'" + clientID + "\',\'" + fName + "\',\'" + sName + "\',\'" + number + "\',\'" + email + "\',\'" + licence + "\',\'" + strNum + "\',\'" + strName + "\',\'" + sub + "\',\'" + city + "\',\'" + postCode + "\',\'" + compName + "\',\'" + 0 + "\', Yes)";
                    RentACar.statement.executeUpdate(insertClient);

                    Client thisClient = new Client(baseController.clients.size() + 1, clientID, fName, sName, number, email, licence, strNum, strName, sub, city, postCode, compName, 0);
                    baseController.clients.add(thisClient);

                    closeStage();
                }
                else
                {
                    alert.setHeaderText(errorMessage);
                    alert.showAndWait();
                }
            }
            else
            {
                alert.setHeaderText("Date is in incorrect format");
                alert.showAndWait();
            }
        }
    }
    private boolean emptyChecks(String clientID, String fName, String sName, String number, String email, Date licence, String strNum, String strName, String sub, String city, String postCode)
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

        if(licence.toString().isEmpty())
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
    private boolean errorChecks(String clientID, String fName, String sName, String number, String email, Date licence, int strNum, String strName, String sub, String city, int postCode)
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

        if(number.length() < 10)
        {
            errorMessage = "Contact Number is too short";
            contactNumber.clear();
            return false;
        }
        if(number.length() > 10)
        {
            errorMessage = "Contact Number is too long";
            contactNumber.clear();
            return false;
        }
        if(!number.startsWith("0"))
        {
            errorMessage = "Contact Number should start with a 0";
            contactNumber.clear();
            return false;
        }
        if(!baseController.errorValidationCheck(baseController.letterArray, number) | !baseController.symbolCheck(number))
        {
            errorMessage = "Contact Number is in the incorrect format";
            contactNumber.clear();
            return false;
        }

        if(!email.contains("@"))
        {
            errorMessage = "Email Address should contain and @ symbol";
            this.email.clear();
            return false;
        }
        if(!email.contains("."))
        {
            errorMessage = "Email Address should contain a '.' after the @ symbol";
            this.email.clear();
            return false;
        }

        if(licence.before(Date.valueOf(LocalDate.now())))
        {
            errorMessage = "Licence Expiration Date cannot be a past date";
            licenceExpiryDate.setValue(null);
            return false;
        }

        if(!baseController.errorValidationCheck(baseController.letterArray, String.valueOf(strNum)) | !baseController.symbolCheck(String.valueOf(strNum)))
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

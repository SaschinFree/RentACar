package za.nmu.wrrv.rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class updateClientController implements Initializable
{
    @FXML
    protected Label clientID;
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
    protected CheckBox isDelete;
    @FXML
    protected Button cancel;
    @FXML
    protected Button updateClient;

    private final Alert alert = new Alert(Alert.AlertType.ERROR);
    private String errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        licenceExpiryDate.setConverter(baseController.dateConverter);

        clientID.textProperty().bind(manageClientsController.thisClient.clientIDProperty());
        firstName.setText(manageClientsController.thisClient.getFirstName());
        surname.setText(manageClientsController.thisClient.getSurname());
        contactNumber.setText(manageClientsController.thisClient.getContactNumber());
        email.setText(manageClientsController.thisClient.getEmail());
        licenceExpiryDate.setValue(manageClientsController.thisClient.getLicenceExpiryDate().toLocalDate());
        streetNumber.setText(manageClientsController.thisClient.getStreetNumber());
        streetName.setText(manageClientsController.thisClient.getStreetName());
        suburb.setText(manageClientsController.thisClient.getSuburb());
        city.setText(manageClientsController.thisClient.getCity());
        postalCode.setText(manageClientsController.thisClient.getPostalCode());

        if(!manageClientsController.thisClient.getCompanyName().equals("Private"))
        {
            isCompany.setSelected(true);
            companyName.setText(manageClientsController.thisClient.getCompanyName());
        }
        else
            companyName.setVisible(false);
    }
    @FXML
    protected void keyClicked(KeyEvent keyEvent) throws SQLException
    {
        switch(keyEvent.getCode())
        {
            case ESCAPE -> closeStage();
            case ENTER -> onUpdate();
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

            switch(buttonId)
            {
                case "cancel" -> closeStage();
                case "updateClient" -> onUpdate();
            }
        }
    }
    private void onUpdate() throws SQLException
    {
        if(isDelete.selectedProperty().getValue())
        {
            String deleteClient = "UPDATE Client SET active = No WHERE clientID = \'" + manageClientsController.thisClient.getClientID() + "\'";
            RentACar.statement.executeUpdate(deleteClient);

            manageClientsController.thisClient.setActive(false);
            baseController.clients.removeAll(manageClientsController.thisClient);
            closeStage();
        }
        else
        {
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

            if(baseController.dateCheck(licenceExpiryDate, licenceString))
            {
                if(emptyChecks(fName, sName, number, email, licenceString, strNum, strName, sub, city, postCode) & errorChecks(fName, sName, number, email, Date.valueOf(licenceString), strNum, strName, sub, city, postCode))
                {
                    Date licence = Date.valueOf(licenceString);

                    if(isCompany.selectedProperty().getValue())
                    {
                        if(!baseController.errorValidationCheck(baseController.numberArray, compName) & !baseController.symbolCheck(compName))
                        {
                            companyName.clear();
                            alert.setHeaderText("Company Name is in the incorrect format");
                            alert.showAndWait();
                        }
                        else
                        {
                            updateClient(fName, sName, number, email, licence, strNum, strName, sub, city, postCode, compName);
                            closeStage();
                        }
                    }
                    else
                    {
                        updateClient(fName, sName, number, email, licence, strNum, strName, sub, city, postCode, compName);
                        closeStage();
                    }
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
    private void updateClient(String fName, String sName, String number, String email, Date licence, String strNum, String strName, String sub, String city, String postCode, String compName) throws SQLException
    {
        String updateClient = "UPDATE Client " +
                "SET firstName = \'" + fName + "\', surname = \'" + sName + "\', contactNumber = \'" + number + "\', email = \'" + email + "\', licenceExpiryDate = \'" + licence + "\', streetNumber = \'" + strNum + "\', streetName = \'" + strName + "\', suburb = \'" + sub + "\', city = \'" + city + "\', postalCode = \'" + postCode + "\', companyName = \'" + compName + "\'" +
                "WHERE clientID = \'" + manageClientsController.thisClient.getClientID() + "\'";
        RentACar.statement.executeUpdate(updateClient);

        manageClientsController.thisClient.setFirstName(fName);
        manageClientsController.thisClient.setSurname(sName);
        manageClientsController.thisClient.setContactNumber(number);
        manageClientsController.thisClient.setEmail(email);
        manageClientsController.thisClient.setLicenceExpiryDate(licence);
        manageClientsController.thisClient.setStreetNumber(strNum);
        manageClientsController.thisClient.setStreetName(strName);
        manageClientsController.thisClient.setSuburb(sub);
        manageClientsController.thisClient.setCity(city);
        manageClientsController.thisClient.setPostalCode(postCode);
        manageClientsController.thisClient.setCompanyName(compName);
    }
    private boolean emptyChecks(String fName, String sName, String number, String email, String licence, String strNum, String strName, String sub, String city, String postCode)
    {
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
    private boolean errorChecks(String fName, String sName, String number, String email, Date licence, String strNum, String strName, String sub, String city, String postCode)
    {
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

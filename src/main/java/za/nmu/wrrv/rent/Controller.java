package za.nmu.wrrv.rent;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class Controller
{
    @FXML
    private TextField user;
    @FXML
    private PasswordField pass;

    @FXML
    protected void onCancel(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
            user.getScene().getWindow().hide();
    }

    @FXML
    protected void onLogin(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            Alert alert;
            if(user.getText().equals("user") && pass.getText().equals("pass"))
            {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Login successful");

                user.getScene().getWindow().hide();
            }
            else
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setHeaderText("Login failed");

                if(user.getText().isEmpty() | pass.getText().isEmpty())
                {
                    if(user.getText().isEmpty())
                        alert.setContentText("Username is empty");
                    else
                    {
                        if(pass.getText().isEmpty())
                            alert.setContentText("Password is empty");
                        else
                            alert.setContentText("Username and Password is empty");
                    }
                }
                else
                    alert.setContentText("Username and/or password is incorrect");
                user.clear();
                pass.clear();
            }
            alert.showAndWait();
        }
    }
}
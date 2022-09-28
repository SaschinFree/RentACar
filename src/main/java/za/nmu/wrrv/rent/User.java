package za.nmu.wrrv.rent;

import javafx.beans.property.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User
{
    private IntegerProperty ID = new SimpleIntegerProperty();
    private StringProperty username = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private BooleanProperty admin = new SimpleBooleanProperty();

    public User(int ID, String username, String password, boolean admin)
    {
        this.ID.set(ID);
        this.username.set(username);
        this.password.set(password);
        this.admin.set(admin);
    }
    public static User getUser(String username) throws SQLException
    {
        String sql = "SELECT * FROM User WHERE username = \'"+username+"\'";
        ResultSet result = RentACar.statement.executeQuery(sql);

        if(result.next())
        {
            int thisID = result.getInt("ID");
            String thisPassword = result.getString("password");
            String thisUsername = result.getString("username");
            boolean isAdmin = result.getBoolean("isAdmin");

            return new User(thisID, thisUsername, thisPassword, isAdmin);
        }
        return null;
    }

    public int getID() {
        return ID.get();
    }
    public IntegerProperty IDProperty() {
        return ID;
    }
    public void setID(int ID) {
        this.ID.set(ID);
    }

    public String getUsername() {
        return username.get();
    }
    public StringProperty usernameProperty() {
        return username;
    }
    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }
    public StringProperty passwordProperty() {
        return password;
    }
    public void setPassword(String password) {
        this.password.set(password);
    }

    public boolean isAdmin() {
        return admin.get();
    }
    public BooleanProperty isAdminProperty() {
        return admin;
    }
    public void setAdmin(boolean isAdmin) {
        this.admin.set(isAdmin);
    }

    @Override
    public String toString()
    {
        String isAdmin = "No";
        if(isAdmin())
            isAdmin = "Yes";

        return "ID: " + getID() +
                "Username: " + getUsername() +
                "Password: " + getPassword() +
                "Admin: " + isAdmin;
    }
}

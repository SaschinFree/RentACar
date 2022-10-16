package za.nmu.wrrv.rent;

import javafx.beans.property.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User
{
    private final IntegerProperty ID = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final BooleanProperty admin = new SimpleBooleanProperty();

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

    public String getPassword() {
        return password.get();
    }

    public boolean isAdmin() {
        return admin.get();
    }
}

package za.nmu.wrrv.rent;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Settings
{
    private static final ObservableList<Settings> settingsList = FXCollections.observableArrayList();
    private final StringProperty settingID = new SimpleStringProperty();
    private final DoubleProperty settingValue = new SimpleDoubleProperty();

    public Settings(String settingID, double settingValue)
    {
        this.settingID.set(settingID);
        this.settingValue.set(settingValue);
    }
    public static ObservableList<Settings> getSettings() throws SQLException
    {
        String sql = "SELECT * FROM Settings";
        ResultSet result = RentACar.statement.executeQuery(sql);

        while(result.next())
        {
            String thisSettingID = result.getString("settingID");
            double thisSettingValue = result.getDouble("settingValue");

            settingsList.add(new Settings(thisSettingID, thisSettingValue));
        }
        return settingsList;
    }

    public String getSettingID()
    {
        return settingID.get();
    }
    public StringProperty settingIDProperty()
    {
        return settingID;
    }
    public void setSettingID(String settingId)
    {
        this.settingID.set(settingId);
    }

    public double getSettingValue()
    {
        return settingValue.get();
    }
    public DoubleProperty settingValueProperty()
    {
        return settingValue;
    }
    public void setSettingValue(double settingValue)
    {
        this.settingValue.set(settingValue);
    }
}

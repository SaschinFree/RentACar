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
    private final StringProperty settingName = new SimpleStringProperty();
    private final DoubleProperty settingValue = new SimpleDoubleProperty();

    public Settings(String settingName, double settingValue)
    {
        this.settingName.set(settingName);
        this.settingValue.set(settingValue);
    }
    public static ObservableList<Settings> getSettings() throws SQLException
    {
        String sql = "SELECT * FROM Settings";
        ResultSet result = RentACar.statement.executeQuery(sql);

        while(result.next())
        {
            String thisSettingName = result.getString("settingName");
            double thisSettingValue = result.getDouble("settingValue");

            settingsList.add(new Settings(thisSettingName, thisSettingValue));
        }
        return settingsList;
    }
    public static Settings getSetting(String settingName) throws SQLException
    {
        String sql = "SELECT * FROM Settings WHERE settingName = \'" + settingName + "\'";
        ResultSet result = RentACar.statement.executeQuery(sql);

        if(result.next())
        {
            String thisSettingName = result.getString("settingName");
            double thisSettingValue = result.getDouble("settingValue");

            return new Settings(thisSettingName, thisSettingValue);
        }
        return null;
    }

    public String getSettingName()
    {
        return settingName.get();
    }
    public StringProperty settingNameProperty()
    {
        return settingName;
    }
    public void setSettingName(String settingName)
    {
        this.settingName.set(settingName);
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

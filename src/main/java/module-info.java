module za.nmu.wrrv.rent {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens za.nmu.wrrv.rent to javafx.fxml;
    exports za.nmu.wrrv.rent;
}
module com.example.yugiohlistas {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.yugiohlistas to javafx.fxml;
    exports com.example.yugiohlistas;
}
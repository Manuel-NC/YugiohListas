module com.example.yugiohlistas {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.net.http;

    exports com.example.yugiohlistas;
    exports com.example.yugiohlistas.Controlador;
    exports com.example.yugiohlistas.Estructuras;
    exports com.example.yugiohlistas.Modelo;
    exports com.example.yugiohlistas.Vista;
}
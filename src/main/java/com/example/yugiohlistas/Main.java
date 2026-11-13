package com.example.yugiohlistas;
import com.example.yugiohlistas.Controlador.YugiohControlador;
import com.example.yugiohlistas.Vista.YugiohGUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        YugiohGUI vista = new YugiohGUI();
        YugiohControlador controlador = new YugiohControlador(vista);

        vista.construirInterfaz(stage);
        controlador.iniciar();
    }

    public static void main(String[] args) {
        launch();
    }

}

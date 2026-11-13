package com.example.yugiohlistas.Vista;

import com.example.yugiohlistas.Modelo.CartaYugioh;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;

public class YugiohGUI {

    private ToggleGroup grupoFiltros;
    private ToggleButton btnTodas, btnNivel, btnAtributo, btnAtk, btnDef;
    private ComboBox<Integer> comboNivel;
    private ComboBox<String> comboAtributo;
    private CheckBox chkLowToHigh;
    private Button btnMute;

    private CartaView vistaAnterior;
    private CartaView vistaActual;
    private CartaView vistaSiguiente;

    public void construirInterfaz(Stage stage) {
        BorderPane root = new BorderPane();

        URL urlFondo = getClass().getResource("/com/example/yugiohlistas/fondo.jpg");
        if (urlFondo == null) urlFondo = getClass().getResource("/fondo.jpg");

        if (urlFondo != null) {
            BackgroundImage bgImg = new BackgroundImage(
                    new Image(urlFondo.toExternalForm()),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true)
            );
            root.setBackground(new Background(bgImg));
        } else {
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #0d111a, #171e2e);");
        }

        root.setTop(crearBarraSuperior());
        root.setCenter(crearPanelCentral());

        Scene scene = new Scene(root, 1140, 720);
        stage.setTitle("Yu-Gi-Oh! 25th Anniversary Tin: Dueling Heroes Mega Pack");
        stage.setScene(scene);
        stage.show();
    }

    private HBox crearBarraSuperior() {
        HBox barra = new HBox(10);
        barra.setPadding(new Insets(14, 22, 14, 22));
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setStyle(
                "-fx-background-color: rgba(10, 13, 20, 0.85); " +
                        "-fx-border-color: rgba(255, 255, 255, 0.12); " +
                        "-fx-border-width: 0 0 1 0;"
        );

        grupoFiltros = new ToggleGroup();

        btnTodas = crearBotonFiltro("Todas");
        btnNivel = crearBotonFiltro("Nivel");
        btnAtributo = crearBotonFiltro("Atributo");
        btnAtk = crearBotonFiltro("ATK");
        btnDef = crearBotonFiltro("DEF");

        comboNivel = new ComboBox<>();
        for (int i = 1; i <= 12; i++) comboNivel.getItems().add(i);
        comboNivel.setValue(4);
        configurarVisibilidad(comboNivel, false);

        comboAtributo = new ComboBox<>();
        comboAtributo.getItems().addAll("DARK", "LIGHT", "EARTH", "WATER", "FIRE", "WIND", "DIVINE");
        comboAtributo.setValue("DARK");
        configurarVisibilidad(comboAtributo, false);

        chkLowToHigh = new CheckBox("Low to high");
        chkLowToHigh.setStyle("-fx-text-fill: #e2e8f0; -fx-font-weight: bold;");
        configurarVisibilidad(chkLowToHigh, false);

        HBox filtrosIzquierda = new HBox(8, btnTodas, btnNivel, comboNivel, btnAtributo, comboAtributo, btnAtk, btnDef, chkLowToHigh);
        filtrosIzquierda.setAlignment(Pos.CENTER_LEFT);

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        btnMute = new Button("🔊");
        btnMute.setPrefSize(42, 42);
        btnMute.setStyle(
                "-fx-background-color: #1e2536; -fx-text-fill: #f8fafc; -fx-font-size: 16px; " +
                        "-fx-background-radius: 21; -fx-border-color: rgba(255, 255, 255, 0.18); " +
                        "-fx-border-radius: 21; -fx-cursor: hand;"
        );

        barra.getChildren().addAll(filtrosIzquierda, espaciador, btnMute);
        return barra;
    }

    private HBox crearPanelCentral() {
        HBox centro = new HBox(32);
        centro.setAlignment(Pos.CENTER);
        centro.setPadding(new Insets(20));

        vistaAnterior = new CartaView(false);
        vistaActual = new CartaView(true);
        vistaSiguiente = new CartaView(false);

        centro.getChildren().addAll(vistaAnterior, vistaActual, vistaSiguiente);
        return centro;
    }

    private ToggleButton crearBotonFiltro(String texto) {
        ToggleButton btn = new ToggleButton(texto);
        btn.setToggleGroup(grupoFiltros);
        String estiloInactivo = "-fx-background-color: #1e2536; -fx-text-fill: #cbd5e1; -fx-font-weight: bold; -fx-padding: 7 15; -fx-background-radius: 6; -fx-cursor: hand;";
        String estiloActivo = "-fx-background-color: #3b82f6; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-padding: 7 15; -fx-background-radius: 6; -fx-cursor: hand;";

        btn.setStyle(estiloInactivo);
        btn.selectedProperty().addListener((obs, was, isNow) -> btn.setStyle(isNow ? estiloActivo : estiloInactivo));
        return btn;
    }

    public void actualizarSubcontroles() {
        configurarVisibilidad(comboNivel, btnNivel.isSelected());
        configurarVisibilidad(comboAtributo, btnAtributo.isSelected());
        configurarVisibilidad(chkLowToHigh, btnAtk.isSelected() || btnDef.isSelected());
    }

    private void configurarVisibilidad(Control c, boolean visible) {
        c.setVisible(visible);
        c.setManaged(visible);
    }

    public void actualizarCarrusel(CartaYugioh actual, CartaYugioh anterior, CartaYugioh siguiente, int pos, int total) {
        vistaActual.mostrarCarta(actual, pos, total);
        vistaAnterior.mostrarCarta(anterior, 0, 0);
        vistaSiguiente.mostrarCarta(siguiente, 0, 0);
    }

    public void actualizarIconoMute(boolean muteado) {
        btnMute.setText(muteado ? "🔇" : "🔊");
    }

    // Getters para que el Controlador registre los eventos
    public ToggleGroup getGrupoFiltros() { return grupoFiltros; }
    public ToggleButton getBtnTodas() { return btnTodas; }
    public ToggleButton getBtnNivel() { return btnNivel; }
    public ToggleButton getBtnAtributo() { return btnAtributo; }
    public ToggleButton getBtnAtk() { return btnAtk; }
    public ToggleButton getBtnDef() { return btnDef; }
    public ComboBox<Integer> getComboNivel() { return comboNivel; }
    public ComboBox<String> getComboAtributo() { return comboAtributo; }
    public CheckBox getChkLowToHigh() { return chkLowToHigh; }
    public Button getBtnMute() { return btnMute; }
    public CartaView getVistaAnterior() { return vistaAnterior; }
    public CartaView getVistaSiguiente() { return vistaSiguiente; }
}
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

/**
 * Vista principal de la aplicación encargada de construir y gestionar
 * la ventana, la barra superior de filtros, el fondo y el carrusel central.
 */
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

    /**
     * Arma la estructura completa de la ventana principal (BorderPane), configura
     * la imagen de fondo, enlaza el reescalado dinámico al cambiar el alto de la
     * ventana y muestra la escena en el escenario de JavaFX.
     *
     * @param stage Escenario principal proporcionado por JavaFX.
     */
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
        HBox panelCentral = crearPanelCentral();
        root.setCenter(panelCentral);

        Scene scene = new Scene(root, 1280, 760);

        scene.heightProperty().addListener((obs, viejoAlto, nuevoAlto) -> {
            double h = nuevoAlto.doubleValue();
            vistaAnterior.ajustarEscala(h);
            vistaActual.ajustarEscala(h);
            vistaSiguiente.ajustarEscala(h);
            panelCentral.setSpacing(Math.max(28, h * 0.045));
        });

        stage.setTitle("Yu-Gi-Oh Card Info");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Construye la barra de navegación superior con los botones de filtro excluyentes,
     * los subcontroles desplegables (nivel, atributo y ordenamiento) y el botón de silencio.
     *
     * @return Contenedor HBox configurado para la parte superior de la ventana.
     */
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

        btnTodas = crearBotonFiltro("All");
        btnNivel = crearBotonFiltro("Level");
        btnAtributo = crearBotonFiltro("Attribute");
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

    /**
     * Crea el contenedor central del carrusel e instancia las tres vistas de carta:
     * la miniatura anterior, la carta principal del centro y la miniatura siguiente.
     *
     * @return Contenedor HBox centrado con las tres vistas del carrusel.
     */
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

    /**
     * Crea un botón de filtro estilizado, lo agrupa en el ToggleGroup para que sea
     * excluyente y cambia su color automáticamente al activarse o desactivarse.
     *
     * @param texto Etiqueta visible del botón.
     * @return ToggleButton configurado y con listener de estilo.
     */
    private ToggleButton crearBotonFiltro(String texto) {
        ToggleButton btn = new ToggleButton(texto);
        btn.setToggleGroup(grupoFiltros);
        String estiloInactivo = "-fx-background-color: #1e2536; -fx-text-fill: #cbd5e1; -fx-font-weight: bold; -fx-padding: 7 15; -fx-background-radius: 6; -fx-cursor: hand;";
        String estiloActivo = "-fx-background-color: #3b82f6; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-padding: 7 15; -fx-background-radius: 6; -fx-cursor: hand;";

        btn.setStyle(estiloInactivo);
        btn.selectedProperty().addListener((obs, era, es) -> btn.setStyle(es ? estiloActivo : estiloInactivo));
        return btn;
    }

    /**
     * Muestra u oculta los subcontroles específicos (selector de nivel, selector de
     * atributo o casilla de ordenamiento) según el botón de filtro activo.
     */
    public void actualizarSubcontroles() {
        configurarVisibilidad(comboNivel, btnNivel.isSelected());
        configurarVisibilidad(comboAtributo, btnAtributo.isSelected());
        configurarVisibilidad(chkLowToHigh, btnAtk.isSelected() || btnDef.isSelected());
    }

    /**
     * Controla tanto la visibilidad visual como el espacio ocupado en el diseño
     * (managed) de un control para que la barra superior se reacomode sin huecos.
     *
     * @param c       Control de JavaFX a mostrar u ocultar.
     * @param visible true para hacerlo visible y reservar su espacio; false para ocultarlo por completo.
     */
    private void configurarVisibilidad(Control c, boolean visible) {
        c.setVisible(visible);
        c.setManaged(visible);
    }

    /**
     * Actualiza simultáneamente las tres cartas visibles en el carrusel junto con
     * el indicador numérico de posición en la tarjeta central.
     *
     * @param actual    Carta seleccionada actualmente en el centro.
     * @param anterior  Carta previa en la lista circular (miniatura izquierda).
     * @param siguiente Carta posterior en la lista circular (miniatura derecha).
     * @param pos       Posición actual dentro de la lista activa (base 1).
     * @param total     Total de cartas disponibles en la lista activa.
     */
    public void actualizarCarrusel(CartaYugioh actual, CartaYugioh anterior, CartaYugioh siguiente, int pos, int total) {
        vistaActual.mostrarCarta(actual, pos, total);
        vistaAnterior.mostrarCarta(anterior, 0, 0);
        vistaSiguiente.mostrarCarta(siguiente, 0, 0);
    }

    /**
     * Cambia el ícono del botón de audio según el estado actual de silencio.
     *
     * @param muteado true para mostrar el ícono silenciado; false para mostrar sonido activo.
     */
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
package com.example.yugiohlistas.Vista;

import com.example.yugiohlistas.Modelo.CartaYugioh;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Componente visual que representa una carta dentro del carrusel.
 * Puede comportarse como la carta central (con su panel de detalles)
 * o como una de las cartas laterales de navegación rápida.
 */
public class CartaView extends HBox {

    private final double PROPORCION_CARTA = 421.0 / 614.0;

    private final boolean esPrincipal;
    private final ImageView imgCarta;
    private VBox panelFicha;
    private String urlActual;

    private Label lblNombre;
    private Label lblSubtitulo;
    private Label lblStats;
    private TextArea txtDescripcion;
    private Label lblContador;

    /**
     * Construye una vista de carta configurando su tamaño, diseño y eventos
     * según sea la carta principal del centro o una carta lateral.
     *
     * @param esPrincipal true para mostrar la carta grande junto con su ficha de información;
     *                    false para mostrar únicamente la miniatura lateral interactiva.
     */
    public CartaView(boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
        setAlignment(Pos.CENTER);

        imgCarta = new ImageView();
        imgCarta.setPreserveRatio(true);
        imgCarta.setSmooth(true);

        if (esPrincipal) {
            setSpacing(24);
            panelFicha = construirFichaDetalles();
            ajustarEscala(720); // Tamano base inicial
            getChildren().addAll(imgCarta, panelFicha);
        } else {
            ajustarEscala(720);
            setOpacity(0.82);
            setCursor(Cursor.HAND);

            setOnMouseEntered(e -> {
                setOpacity(1.0);
                setScaleX(1.05);
                setScaleY(1.05);
            });
            setOnMouseExited(e -> {
                setOpacity(0.82);
                setScaleX(1.0);
                setScaleY(1.0);
            });

            getChildren().add(imgCarta);
        }
    }

    /**
     * Recalcula proporcionalmente las dimensiones de la imagen de la carta
     * y de su panel de detalles en función del alto actual de la ventana.
     *
     * @param altoVentana Altura actual en píxeles de la escena principal.
     */
    public void ajustarEscala(double altoVentana) {
        if (esPrincipal) {
            // La carta central ocupa alrededor del 60% del alto de la ventana
            double altoCarta = Math.max(380, altoVentana * 0.60);
            double anchoCarta = altoCarta * PROPORCION_CARTA;

            imgCarta.setFitHeight(altoCarta);
            imgCarta.setFitWidth(anchoCarta);

            if (panelFicha != null) {
                panelFicha.setPrefHeight(altoCarta);
                panelFicha.setMaxHeight(altoCarta);
                panelFicha.setPrefWidth(Math.max(360, altoCarta * 0.92));
            }
            setSpacing(Math.max(20, altoVentana * 0.032));
        } else {
            // Las cartas laterales ocupan 33% del alto de la ventana
            double altoMini = Math.max(200, altoVentana * 0.33);
            double anchoMini = altoMini * PROPORCION_CARTA;

            imgCarta.setFitHeight(altoMini);
            imgCarta.setFitWidth(anchoMini);
        }
    }

    /**
     * Crea y estiliza el panel derecho de la carta principal que contiene
     * el nombre, subtítulo, estadísticas (ATK/DEF), descripción y contador.
     *
     * @return Contenedor VBox con todos los controles de texto configurados.
     */
    private VBox construirFichaDetalles() {
        VBox ficha = new VBox(12);
        ficha.setPadding(new Insets(20));
        ficha.setStyle(
                "-fx-background-color: rgba(18, 22, 33, 0.85); " +
                        "-fx-border-color: rgba(255, 255, 255, 0.14); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 12; " +
                        "-fx-background-radius: 12;"
        );

        lblNombre = new Label();
        lblNombre.setWrapText(true);
        lblNombre.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 21px; -fx-font-weight: bold;");

        lblSubtitulo = new Label();
        lblSubtitulo.setWrapText(true);
        lblSubtitulo.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-font-weight: bold;");

        lblStats = new Label();
        lblStats.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 15px; -fx-font-weight: bold;");

        txtDescripcion = new TextArea();
        txtDescripcion.setEditable(false);
        txtDescripcion.setWrapText(true);
        VBox.setVgrow(txtDescripcion, javafx.scene.layout.Priority.ALWAYS);
        txtDescripcion.setStyle(
                "-fx-control-inner-background: #0f131d; " +
                        "-fx-background-color: #0f131d; " +
                        "-fx-text-fill: #cbd5e1; " +
                        "-fx-font-size: 13.5px; " +
                        "-fx-border-color: rgba(255, 255, 255, 0.08); " +
                        "-fx-border-radius: 8;"
        );

        lblContador = new Label();
        lblContador.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px; -fx-font-weight: bold;");

        ficha.getChildren().addAll(lblNombre, lblSubtitulo, lblStats, txtDescripcion, lblContador);
        return ficha;
    }

    /**
     * Actualiza la vista con los datos e imagen de una carta específica.
     * Si la carta es nula (lista vacía), muestra un mensaje de "Sin resultados".
     *
     * @param carta    Objeto CartaYugioh a mostrar en pantalla.
     * @param posicion Número de posición actual de la carta dentro de la lista activa.
     * @param total    Cantidad total de cartas en la lista activa.
     */
    public void mostrarCarta(CartaYugioh carta, int posicion, int total) {
        if (carta == null) {
            urlActual = null;
            imgCarta.setImage(null);
            if (esPrincipal) {
                lblNombre.setText("Sin resultados");
                lblSubtitulo.setText("-");
                lblStats.setText("");
                txtDescripcion.setText("No se encontraron cartas.");
                lblContador.setText("(0/0)");
            }
            return;
        }

        cargarImagenRemota(carta.getUrlImagen());

        if (esPrincipal) {
            lblNombre.setText(carta.getNombre());
            String nivelTxt = carta.getNivel() > 0 ? "  •  Nivel " + carta.getNivel() : "";
            String atributoTxt = !carta.getAtributo().isEmpty() ? carta.getAtributo() + "  •  " : "";
            lblSubtitulo.setText(atributoTxt + "[" + carta.getRaza() + " / " + carta.getTipo() + "]" + nivelTxt);

            if (carta.esMonstruo()) {
                String atkTxt = carta.getAtk() < 0 ? "?" : String.valueOf(carta.getAtk());
                String defTxt = carta.getDef() < 0 ? "?" : String.valueOf(carta.getDef());
                lblStats.setText("ATK " + atkTxt + "   /   DEF " + defTxt);
            } else {
                lblStats.setText(carta.getTipo());
            }

            txtDescripcion.setText(carta.getDescripcion());
            lblContador.setText("(" + posicion + "/" + total + ")");
        }
    }

    /**
     * Descarga la imagen de la carta desde su URL en un hilo secundario para no
     * congelar la interfaz, y la asigna al ImageView en el hilo principal de JavaFX
     * verificando que siga correspondiendo a la carta actualmente seleccionada.
     *
     * @param url Dirección web de la imagen a descargar.
     */
    private void cargarImagenRemota(String url) {
        this.urlActual = url;
        if (url == null || url.isEmpty()) {
            imgCarta.setImage(null);
            return;
        }

        imgCarta.setImage(null);
        Thread hilo = new Thread(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                try (InputStream is = conn.getInputStream()) {
                    Image img = new Image(is);
                    if (!img.isError()) {
                        Platform.runLater(() -> {
                            if (url.equals(this.urlActual)) {
                                imgCarta.setImage(img);
                            }
                        });
                    }
                }
            } catch (Exception ignored){}
        });
        hilo.start();
    }
}
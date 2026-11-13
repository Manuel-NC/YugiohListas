package com.example.yugiohlistas.Vista;

import com.example.yugiohlistas.Modelo.CartaYugioh;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CartaView extends HBox {

    private static final Map<String, Image> CACHE_IMAGENES = new ConcurrentHashMap<>();

    private final boolean esPrincipal;
    private final ImageView imgCarta;
    private String urlActual;

    private Label lblNombre;
    private Label lblSubtitulo;
    private Label lblStats;
    private TextArea txtDescripcion;
    private Label lblContador;

    public CartaView(boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
        setAlignment(Pos.CENTER);

        imgCarta = new ImageView();
        imgCarta.setPreserveRatio(true);
        imgCarta.setSmooth(true);

        DropShadow sombra = new DropShadow(22, Color.rgb(0, 0, 0, 0.75));
        imgCarta.setEffect(sombra);

        if (esPrincipal) {
            setSpacing(24);
            imgCarta.setFitWidth(285);
            imgCarta.setFitHeight(415);

            VBox panelFicha = construirFichaDetalles();
            getChildren().addAll(imgCarta, panelFicha);
        } else {
            imgCarta.setFitWidth(145);
            imgCarta.setFitHeight(212);
            setOpacity(0.80);
            setCursor(Cursor.HAND);

            // Efecto visual al pasar el mouse para indicar que se puede dar clic
            setOnMouseEntered(e -> {
                setOpacity(1.0);
                setScaleX(1.05);
                setScaleY(1.05);
            });
            setOnMouseExited(e -> {
                setOpacity(0.80);
                setScaleX(1.0);
                setScaleY(1.0);
            });

            getChildren().add(imgCarta);
        }
    }

    private VBox construirFichaDetalles() {
        VBox ficha = new VBox(12);
        ficha.setPrefWidth(370);
        ficha.setMaxHeight(415);
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
        lblNombre.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px; -fx-font-weight: bold;");

        lblSubtitulo = new Label();
        lblSubtitulo.setWrapText(true);
        lblSubtitulo.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-font-weight: bold;");

        lblStats = new Label();
        lblStats.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 15px; -fx-font-weight: bold;");

        txtDescripcion = new TextArea();
        txtDescripcion.setEditable(false);
        txtDescripcion.setWrapText(true);
        txtDescripcion.setPrefRowCount(9);
        txtDescripcion.setStyle(
                "-fx-control-inner-background: #0f131d; " +
                        "-fx-background-color: #0f131d; " +
                        "-fx-text-fill: #cbd5e1; " +
                        "-fx-font-size: 13px; " +
                        "-fx-border-color: rgba(255, 255, 255, 0.08); " +
                        "-fx-border-radius: 8;"
        );

        lblContador = new Label();
        lblContador.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px; -fx-font-weight: bold;");

        ficha.getChildren().addAll(lblNombre, lblSubtitulo, lblStats, txtDescripcion, lblContador);
        return ficha;
    }

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

    private void cargarImagenRemota(String url) {
        this.urlActual = url;
        if (url == null || url.isEmpty()) {
            imgCarta.setImage(null);
            return;
        }

        if (CACHE_IMAGENES.containsKey(url)) {
            imgCarta.setImage(CACHE_IMAGENES.get(url));
            return;
        }

        imgCarta.setImage(null);
        Thread hilo = new Thread(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                try (InputStream is = conn.getInputStream()) {
                    Image img = new Image(is);
                    if (!img.isError()) {
                        CACHE_IMAGENES.put(url, img);
                        Platform.runLater(() -> {
                            if (url.equals(this.urlActual)) {
                                imgCarta.setImage(img);
                            }
                        });
                    }
                }
            } catch (Exception ignored) {}
        });
        hilo.setDaemon(true);
        hilo.start();
    }
}
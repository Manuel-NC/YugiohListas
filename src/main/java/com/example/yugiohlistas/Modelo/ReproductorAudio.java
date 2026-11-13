package com.example.yugiohlistas.Modelo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Clase encargada de cargar y reproducir en bucle la música de fondo
 * de la aplicación, así como gestionar su estado de silencio (mute).
 */
public class ReproductorAudio {
    private MediaPlayer mediaPlayer;
    private boolean muteado = false;

    /**
     * Carga el archivo de audio desde los recursos del proyecto, configura
     * la reproducción infinita a volumen moderado e inicia la pista.
     *
     * @param rutaRecurso Ruta interna dentro de la carpeta resources del archivo de audio.
     */
    public ReproductorAudio(String rutaRecurso) {
        try {
            URL recurso = getClass().getResource(rutaRecurso);
            if (recurso != null) {
                Media media = new Media(recurso.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.setVolume(0.35);
                mediaPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("No se pudo iniciar la música de fondo: " + e.getMessage());
        }
    }

    /**
     * Alterna entre silenciar y activar el sonido de la música de fondo
     * sin detener el avance de la reproducción.
     *
     * @return true si el audio quedó silenciado; false si quedó activo.
     */
    public boolean alternarMute() {
        muteado = !muteado;
        if (mediaPlayer != null) {
            mediaPlayer.setMute(muteado);
        }
        return muteado;
    }
}

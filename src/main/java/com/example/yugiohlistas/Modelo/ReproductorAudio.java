package com.example.yugiohlistas.Modelo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class ReproductorAudio {
    private MediaPlayer mediaPlayer;
    private boolean muteado = false;

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

    public boolean alternarMute() {
        muteado = !muteado;
        if (mediaPlayer != null) {
            mediaPlayer.setMute(muteado);
        }
        return muteado;
    }
}

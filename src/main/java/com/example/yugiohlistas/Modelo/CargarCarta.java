package com.example.yugiohlistas.Modelo;

import com.example.yugiohlistas.Estructuras.ListaCircularDoble;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CargarCarta {

    public ListaCircularDoble<CartaYugioh> cargarTodasLasCartas(String rutaRecurso) {
        ListaCircularDoble<CartaYugioh> listaCartas = new ListaCircularDoble<>();

        try (InputStream is = getClass().getResourceAsStream(rutaRecurso);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String linea = br.readLine(); // Omite la fila de encabezados
            while ((linea = br.readLine()) != null) {
                CartaYugioh carta = parsearLineaCSV(linea);
                if (carta != null) {
                    listaCartas.insertaFin(carta);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar cartas desde el CSV: " + e.getMessage());
        }

        return listaCartas;
    }

    private CartaYugioh parsearLineaCSV(String linea) {
        String[] cols = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if (cols.length < 9) return null;

        return new CartaYugioh(
                limpiar(cols[0]),
                limpiar(cols[1]),
                limpiar(cols[2]).toUpperCase(),
                parsearInt(cols[3]),
                limpiar(cols[4]),
                parsearInt(cols[5]),
                parsearInt(cols[6]),
                limpiar(cols[7]),
                limpiar(cols[8])
        );
    }

    private String limpiar(String s) {
        return s.replaceAll("^\"|\"$", "").trim();
    }

    private int parsearInt(String s) {
        try {
            return Integer.parseInt(limpiar(s));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
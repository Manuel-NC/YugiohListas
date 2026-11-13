package com.example.yugiohlistas.Modelo;

import com.example.yugiohlistas.Estructuras.ListaCircularDoble;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CargarCarta {

    private static final String RUTA_CSV = "/com/example/yugiohlistas/cartasYugioh.csv";

    public ListaCircularDoble<CartaYugioh> cargarTodasLasCartas() {
        ListaCircularDoble<CartaYugioh> listaCartas = new ListaCircularDoble<>();

        try (InputStream is = getClass().getResourceAsStream(RUTA_CSV)) {
            if (is == null) {
                System.err.println("No se encontró el recurso: " + RUTA_CSV);
                return listaCartas;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String linea = br.readLine(); // Saltar encabezado
                while ((linea = br.readLine()) != null) {
                    CartaYugioh carta = parsearLineaCSV(linea);
                    if (carta != null) {
                        listaCartas.insertaFin(carta);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer cartasYugioh.csv: " + e.getMessage());
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
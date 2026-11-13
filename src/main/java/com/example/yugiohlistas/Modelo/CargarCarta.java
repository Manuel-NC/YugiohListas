package com.example.yugiohlistas.Modelo;

import com.example.yugiohlistas.Estructuras.ListaCircularDoble;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Clase encargada de leer el archivo CSV de cartas y cargarlas
 * dentro de una lista circular doble.
 */
public class CargarCarta {

    private final String RUTA_CSV = "/com/example/yugiohlistas/cartasYugioh.csv";

    /**
     * Abre el archivo CSV desde los recursos del proyecto, lee cada línea
     * ignorando el encabezado y almacena todas las cartas válidas en la lista.
     *
     * @return ListaCircularDoble con todas las cartas cargadas desde el archivo.
     */
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

    /**
     * Divide un renglón del CSV en sus 9 columnas respetando las comas
     * que vienen dentro de comillas (como en las descripciones) y construye
     * el objeto CartaYugioh correspondiente.
     *
     * @param linea Renglón de texto leído del archivo CSV.
     * @return Objeto CartaYugioh con los datos extraídos, o null si la línea está incompleta.
     */
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

    /**
     * Elimina las comillas dobles del inicio y final de un campo,
     * además de quitar espacios en blanco sobrantes en las orillas.
     *
     * @param s Texto de una columna del CSV
     * @return Texto limpio listo para usarse.
     */
    private String limpiar(String s) {
        return s.replaceAll("^\"|\"$", "").trim();
    }

    /**
     * Convierte a entero el texto de columnas numéricas (Nivel, ATK, DEF).
     * Si el campo viene vacío, devuelve 0.
     *
     * @param s Texto de la columna numérica.
     * @return Valor entero convertido, o 0 si no es un número válido.
     */
    private int parsearInt(String s) {
        try {
            return Integer.parseInt(limpiar(s));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
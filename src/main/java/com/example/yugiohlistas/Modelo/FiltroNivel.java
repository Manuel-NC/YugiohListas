package com.example.yugiohlistas.Modelo;

import com.example.yugiohlistas.Estructuras.ListaCircularDoble;
import com.example.yugiohlistas.Estructuras.NodoDoble;

/**
 * Clase encargada de filtrar las cartas de la lista principal
 * según su nivel o rango numérico.
 */
public class FiltroNivel {

    /**
     * Recorre la lista base completa y construye una nueva lista circular doble
     * únicamente con las cartas cuyo nivel coincida exactamente con el buscado.
     *
     * @param listaBase    Lista circular doble original con todas las cartas cargadas.
     * @param nivelBuscado Número de nivel o rango por el cual se desea filtrar.
     * @return Nueva ListaCircularDoble con las cartas que coinciden con el nivel.
     */
    public ListaCircularDoble<CartaYugioh> filtrar(ListaCircularDoble<CartaYugioh> listaBase, int nivelBuscado) {
        ListaCircularDoble<CartaYugioh> resultado = new ListaCircularDoble<>();
        if (listaBase == null || listaBase.estaVacia()) return resultado;

        NodoDoble<CartaYugioh> actual = listaBase.getInicio();
        int total = listaBase.getTamano();

        for (int i = 0; i < total; i++) {
            if (actual.getDato().getNivel() == nivelBuscado) {
                resultado.insertaFin(actual.getDato());
            }
            actual = actual.getSig();
        }
        return resultado;
    }
}
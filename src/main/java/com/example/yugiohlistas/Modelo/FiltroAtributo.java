package com.example.yugiohlistas.Modelo;

import com.example.yugiohlistas.Estructuras.ListaCircularDoble;
import com.example.yugiohlistas.Estructuras.NodoDoble;

/**
 * Clase encargada de filtrar las cartas de la lista principal
 * según su atributo (DARK, LIGHT, FIRE, WATER, EARTH, WIND, DIVINE, SPELL o TRAP).
 */
public class FiltroAtributo {

    /**
     * Recorre la lista base completa y construye una nueva lista circular doble
     * únicamente con las cartas cuyo atributo coincida con el solicitado.
     *
     * @param listaBase       Lista circular doble original con todas las cartas cargadas.
     * @param atributoBuscado Nombre del atributo por el cual se desea filtrar.
     * @return Nueva ListaCircularDoble con las cartas que coinciden con el atributo.
     */
    public ListaCircularDoble<CartaYugioh> filtrar(ListaCircularDoble<CartaYugioh> listaBase, String atributoBuscado) {
        ListaCircularDoble<CartaYugioh> resultado = new ListaCircularDoble<>();
        if (listaBase == null || listaBase.estaVacia() || atributoBuscado == null) return resultado;

        NodoDoble<CartaYugioh> actual = listaBase.getInicio();
        int total = listaBase.getTamano();

        for (int i = 0; i < total; i++) {
            if (actual.getDato().getAtributo().equalsIgnoreCase(atributoBuscado)) {
                resultado.insertaFin(actual.getDato());
            }
            actual = actual.getSig();
        }
        return resultado;
    }
}

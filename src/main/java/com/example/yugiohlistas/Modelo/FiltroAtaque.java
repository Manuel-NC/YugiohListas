package com.example.yugiohlistas.Modelo;

import com.example.yugiohlistas.Estructuras.ListaCircularDoble;
import com.example.yugiohlistas.Estructuras.NodoDoble;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Clase encargada de extraer las cartas de la lista principal y ordenarlas
 * según sus puntos de ataque (ATK), ya sea de menor a mayor o viceversa.
 */
public class FiltroAtaque {

    /**
     * Recorre la lista base, copia sus cartas a una lista auxiliar para ordenarlas
     * por valor de ataque (ATK) y las devuelve en una nueva ListaCircularDoble.
     *
     * @param listaBase Lista circular doble original con todas las cartas cargadas.
     * @param lowToHigh true para ordenar de menor a mayor ataque; false para mayor a menor.
     * @return Nueva ListaCircularDoble con las cartas ya ordenadas por ATK.
     */
    public ListaCircularDoble<CartaYugioh> filtrarYOrdenar(ListaCircularDoble<CartaYugioh> listaBase, boolean lowToHigh) {
        ListaCircularDoble<CartaYugioh> resultado = new ListaCircularDoble<>();
        if (listaBase == null || listaBase.estaVacia()) return resultado;

        ArrayList<CartaYugioh> cartas = new ArrayList<>();
        NodoDoble<CartaYugioh> actual = listaBase.getInicio();
        int total = listaBase.getTamano();

        for (int i = 0; i < total; i++) {
            //if (actual.getDato().esMonstruo()) {
                cartas.add(actual.getDato());
            //}
            actual = actual.getSig();
        }

        Comparator<CartaYugioh> comp = Comparator.comparingInt(CartaYugioh::getAtk);
        cartas.sort(lowToHigh ? comp : comp.reversed());

        for (CartaYugioh c : cartas) {
            resultado.insertaFin(c);
        }
        return resultado;
    }
}
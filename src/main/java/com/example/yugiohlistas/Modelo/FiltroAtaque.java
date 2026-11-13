package com.example.yugiohlistas.Modelo;

import com.example.yugiohlistas.Estructuras.ListaCircularDoble;
import com.example.yugiohlistas.Estructuras.NodoDoble;
import java.util.ArrayList;
import java.util.Comparator;

public class FiltroAtaque {

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
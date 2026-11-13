package com.example.yugiohlistas.Modelo;

import com.example.yugiohlistas.Estructuras.ListaCircularDoble;
import com.example.yugiohlistas.Estructuras.NodoDoble;
import java.util.ArrayList;
import java.util.Comparator;

public class FiltroDefensa {

    public ListaCircularDoble<CartaYugioh> filtrarYOrdenar(ListaCircularDoble<CartaYugioh> listaBase, boolean lowToHigh) {
        ListaCircularDoble<CartaYugioh> resultado = new ListaCircularDoble<>();
        if (listaBase == null || listaBase.estaVacia()) return resultado;

        ArrayList<CartaYugioh> monstruos = new ArrayList<>();
        NodoDoble<CartaYugioh> actual = listaBase.getInicio();
        int total = listaBase.getTamano();

        for (int i = 0; i < total; i++) {
            if (actual.getDato().esMonstruo()) {
                monstruos.add(actual.getDato());
            }
            actual = actual.getSig();
        }

        Comparator<CartaYugioh> comp = Comparator.comparingInt(CartaYugioh::getDef);
        monstruos.sort(lowToHigh ? comp : comp.reversed());

        for (CartaYugioh c : monstruos) {
            resultado.insertaFin(c);
        }
        return resultado;
    }
}
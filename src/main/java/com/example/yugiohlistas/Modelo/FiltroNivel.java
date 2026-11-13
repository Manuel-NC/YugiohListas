package com.example.yugiohlistas.Modelo;

import com.example.yugiohlistas.Estructuras.ListaCircularDoble;
import com.example.yugiohlistas.Estructuras.NodoDoble;

public class FiltroNivel {

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
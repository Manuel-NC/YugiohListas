package com.example.yugiohlistas.Modelo;

import com.example.yugiohlistas.Estructuras.ListaCircularDoble;
import com.example.yugiohlistas.Estructuras.NodoDoble;

public class FiltroAtributo {

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

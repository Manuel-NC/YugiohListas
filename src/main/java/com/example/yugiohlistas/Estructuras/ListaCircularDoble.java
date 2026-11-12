package com.example.yugiohlistas.Estructuras;

public class ListaCircularDoble<T> {
    private NodoDoble<T> inicio;
    private NodoDoble<T> fin;

    public ListaCircularDoble() {
        this.inicio = null;
        this.fin = null;
    }

    public void insertaInicio(T dato) {
        NodoDoble<T> n = new NodoDoble<>(dato);

        if (inicio == null) {
            inicio = fin = n;
            n.setSig(inicio);
            n.setAnt(inicio);
        } else {
            n.setSig(inicio);
            inicio.setAnt(n);
            inicio = n;
            fin.setSig(inicio);
            n.setAnt(fin);
        }
    }

    public void insertaFin(T dato) {
        NodoDoble<T> n = new NodoDoble<>(dato);

        if (inicio == null) {
            inicio = fin = n;
            n.setSig(inicio);
            n.setAnt(inicio);
        } else {
            n.setSig(inicio);
            inicio.setAnt(n);
            fin.setSig(n);
            n.setAnt(fin);
            fin = n;
        }
    }

    public T eliminaInicio() {
        T dato = null;

        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        } else if (inicio == fin) {
            dato = inicio.getDato();
            inicio = fin = null;
        } else {
            dato = inicio.getDato();
            fin.setSig(inicio.getSig());
            inicio = inicio.getSig();
            inicio.setAnt(fin);
        }
        return dato;
    }

    public T eliminaFin() {
        T dato = null;

        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        } else if (inicio == fin) {
            dato = fin.getDato();
            inicio = fin = null;
        } else {
            NodoDoble<T> r;
            dato = fin.getDato();
            r = fin.getAnt();
            r.setSig(inicio);
            inicio.setAnt(r);
            fin = r;
        }
        return dato;
    }

    public String mostrarIterativo() { // LISTA CIRCULAR DOBLE
        String cadena = "";
        if (inicio != null) {
            NodoDoble<T> r = inicio;
            while ( r != fin){
                cadena += r.getDato() + " ";
                r = r.getSig();
            }
            cadena += r.getDato();
        } else {
            return "Lista vacia";
        }
        return cadena;
    }

    // mostrarRecursivo
    public String mostrarRecursivo() {
        if (inicio == null) {
            return "Lista vacía";
        }
        return mostrarRecursivo(inicio, inicio).trim();
    }

    private String mostrarRecursivo(NodoDoble<T> actual, NodoDoble<T> primero) {
        if (actual.getSig() == primero) {
            return actual.getDato() + " ";
        }
        return actual.getDato() + " " + mostrarRecursivo(actual.getSig(), primero);
    }

    // eliminaX
    public T eliminaX(T x) {
        if (inicio == null) return null;

        // Caso: está en el inicio
        if (inicio.getDato().equals(x)) {
            return eliminaInicio();
        }

        // Caso: está en el fin
        if (fin.getDato().equals(x)) {
            return eliminaFin();
        }

        // Buscar en medio
        NodoDoble<T> r = inicio.getSig();
        while (r != inicio) {
            if (r.getDato().equals(x)) {
                r.getAnt().setSig(r.getSig());
                r.getSig().setAnt(r.getAnt());
                return r.getDato();
            }
            r = r.getSig();
        }
        return null; // no se encontró
    }

    // buscar (iterativo)
    public int buscar(T x) {
        if (inicio == null) return -1;

        int pos = 0;
        NodoDoble<T> r = inicio;
        do {
            if (r.getDato().equals(x)) {
                return pos;
            }
            r = r.getSig();
            pos++;
        } while (r != inicio);

        return -1;
    }

    // buscar recursivo
    public int buscarRecursivo(T x) {
        if (inicio == null)
            return -1;
        return buscarRecursivo(inicio, x, 0, inicio);
    }

    private int buscarRecursivo(NodoDoble<T> actual, T x, int pos, NodoDoble<T> primero) {
        if (actual.getDato().equals(x)) {
            return pos;
        }
        if (actual.getSig() == primero) {
            return -1;
        }
        return buscarRecursivo(actual.getSig(), x, pos + 1, primero);
    }

    // eliminaPosicion
    public T eliminaPosicion(int posicion) {
        if (inicio == null || posicion < 0) return null;

        // Contar nodos
        int tam = 0;
        NodoDoble<T> aux = inicio;
        do {
            tam++;
            aux = aux.getSig();
        } while (aux != inicio);

        if (posicion >= tam) return null;

        if (posicion == 0) {
            return eliminaInicio();
        }
        if (posicion == tam - 1) {
            return eliminaFin();
        }

        // Ir a la posición
        NodoDoble<T> r = inicio;
        for (int i = 0; i < posicion; i++) {
            r = r.getSig();
        }

        // Desconectar el nodo
        r.getAnt().setSig(r.getSig());
        r.getSig().setAnt(r.getAnt());
        return r.getDato();
    }

    // insertaenPosicion
    public void insertaenPosicion(T dato, int posicion) {
        if (posicion < 0) return;

        if (inicio == null || posicion == 0) {
            insertaInicio(dato);
            return;
        }

        // Contar nodos
        int tam = 0;
        NodoDoble<T> aux = inicio;
        do {
            tam++;
            aux = aux.getSig();
        } while (aux != inicio);

        if (posicion >= tam) {
            insertaFin(dato);
            return;
        }

        // Ir al nodo anterior a la posición
        NodoDoble<T> r = inicio;
        for (int i = 0; i < posicion - 1; i++) {
            r = r.getSig();
        }

        NodoDoble<T> n = new NodoDoble<>(dato);
        n.setSig(r.getSig());
        n.setAnt(r);
        r.getSig().setAnt(n);
        r.setSig(n);
    }

    // ordenarLista
    public void ordenarLista() {
        // Lista vacía o con un solo elemento ya está ordenada
        if (inicio == null || inicio == fin) return;

        boolean cambiado;
        do {
            cambiado = false;
            NodoDoble<T> actual = inicio;

            // Recorremos mientras el siguiente no vuelva al inicio
            while (actual.getSig() != inicio) {
                NodoDoble<T> siguiente = actual.getSig();

                Comparable<T> a = (Comparable<T>) actual.getDato();

                if (a.compareTo(siguiente.getDato()) > 0) {
                    // Intercambio de datos
                    T temp = actual.getDato();
                    actual.setDato(siguiente.getDato());
                    siguiente.setDato(temp);

                    cambiado = true;
                }
                actual = actual.getSig();
            }

        } while (cambiado);
    }

    public void eliminarPares() {
        NodoDoble<T> r = inicio;
        while (r != fin) {
            if ((Integer) r.getDato() % 2 == 0) {
                if (r == inicio) {
                    eliminaInicio();
                } else {
                    r.getAnt().setSig(r.getSig());
                    r.getSig().setAnt(r.getAnt());
                }
            }
            r = r.getSig();
        }

        if (r == fin) {
            if ((Integer) r.getDato() % 2 == 0) {
                eliminaFin();
            }
        }
    }

    public NodoDoble<T> getInicio() {
        return inicio;
    }

    public boolean estaVacia() {
        return inicio == null;
    }

    public int getTamano() {
        if (inicio == null) return 0;
        int contador = 1;
        NodoDoble<T> r = inicio;
        while (r != fin) {
            contador++;
            r = r.getSig();
        }
        return contador;
    }

}

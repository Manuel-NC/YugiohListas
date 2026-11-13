package com.example.yugiohlistas.Controlador;

import com.example.yugiohlistas.Estructuras.ListaCircularDoble;
import com.example.yugiohlistas.Estructuras.NodoDoble;
import com.example.yugiohlistas.Modelo.*;
import com.example.yugiohlistas.Vista.YugiohGUI;

public class YugiohControlador {
    private final ListaCircularDoble<CartaYugioh> listaPrincipal;
    private ListaCircularDoble<CartaYugioh> listaActiva;
    private NodoDoble<CartaYugioh> nodoActual;
    private int posicionActual;

    private final FiltroNivel filtroNivel;
    private final FiltroAtributo filtroAtributo;
    private final FiltroAtaque filtroAtaque;
    private final FiltroDefensa filtroDefensa;
    private final ReproductorAudio audio;
    private final YugiohGUI vista;

    public YugiohControlador(YugiohGUI vista) {
        this.vista = vista;
        this.filtroNivel = new FiltroNivel();
        this.filtroAtributo = new FiltroAtributo();
        this.filtroAtaque = new FiltroAtaque();
        this.filtroDefensa = new FiltroDefensa();

        CargarCarta cargador = new CargarCarta();
        this.listaPrincipal = cargador.cargarTodasLasCartas();
        this.audio = new ReproductorAudio("/com/example/yugiohlistas/Yugioh-GodsAnger.mp3");
    }

    public void iniciar() {
        registrarEventos();
        vista.getBtnTodas().setSelected(true);
        aplicarFiltro();
    }

    private void registrarEventos() {
        // Clic directamente sobre la carta anterior o siguiente para girar el carrusel
        vista.getVistaAnterior().setOnMouseClicked(e -> anteriorCarta());
        vista.getVistaSiguiente().setOnMouseClicked(e -> siguienteCarta());

        // Filtros excluyentes
        vista.getGrupoFiltros().selectedToggleProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo == null) {
                viejo.setSelected(true);
                return;
            }
            vista.actualizarSubcontroles();
            aplicarFiltro();
        });

        vista.getComboNivel().setOnAction(e -> aplicarFiltro());
        vista.getComboAtributo().setOnAction(e -> aplicarFiltro());
        vista.getChkLowToHigh().setOnAction(e -> aplicarFiltro());

        // Boton de mute
        vista.getBtnMute().setOnAction(e -> {
            boolean muteado = audio.alternarMute();
            vista.actualizarIconoMute(muteado);
        });
    }

    private void aplicarFiltro() {
        if (vista.getBtnTodas().isSelected()) {
            listaActiva = listaPrincipal;
        } else if (vista.getBtnNivel().isSelected()) {
            listaActiva = filtroNivel.filtrar(listaPrincipal, vista.getComboNivel().getValue());
        } else if (vista.getBtnAtributo().isSelected()) {
            listaActiva = filtroAtributo.filtrar(listaPrincipal, vista.getComboAtributo().getValue());
        } else if (vista.getBtnAtk().isSelected()) {
            listaActiva = filtroAtaque.filtrarYOrdenar(listaPrincipal, vista.getChkLowToHigh().isSelected());
        } else if (vista.getBtnDef().isSelected()) {
            listaActiva = filtroDefensa.filtrarYOrdenar(listaPrincipal, vista.getChkLowToHigh().isSelected());
        }

        nodoActual = listaActiva.getInicio();
        posicionActual = 1;
        refrescarVista();
    }

    private void siguienteCarta() {
        if (nodoActual == null || listaActiva.estaVacia()) return;
        nodoActual = nodoActual.getSig();
        posicionActual = (posicionActual % listaActiva.getTamano()) + 1;
        refrescarVista();
    }

    private void anteriorCarta() {
        if (nodoActual == null || listaActiva.estaVacia()) return;
        nodoActual = nodoActual.getAnt();
        posicionActual = (posicionActual == 1) ? listaActiva.getTamano() : posicionActual - 1;
        refrescarVista();
    }

    private void refrescarVista() {
        if (nodoActual == null || listaActiva.estaVacia()) {
            vista.actualizarCarrusel(null, null, null, 0, 0);
        } else {
            vista.actualizarCarrusel(
                    nodoActual.getDato(),
                    nodoActual.getAnt().getDato(),
                    nodoActual.getSig().getDato(),
                    posicionActual,
                    listaActiva.getTamano()
            );
        }
    }
}
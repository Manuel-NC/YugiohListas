package com.example.yugiohlistas.Modelo;

public class CartaYugioh {
    private String nombre;
    private String tipo;
    private String atributo;
    private int nivel;
    private String raza;
    private int atk;
    private int def;
    private String descripcion;
    private String urlImagen;

    public CartaYugioh(String nombre, String tipo, String atributo, int nivel,
                       String raza, int atk, int def, String descripcion, String urlImagen) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.atributo = atributo;
        this.nivel = nivel;
        this.raza = raza;
        this.atk = atk;
        this.def = def;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
    }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getAtributo() { return atributo; }
    public int getNivel() { return nivel; }
    public String getRaza() { return raza; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public String getDescripcion() { return descripcion; }
    public String getUrlImagen() { return urlImagen; }

    public boolean esMonstruo() {
        return tipo != null && tipo.toLowerCase().contains("monster");
    }
}

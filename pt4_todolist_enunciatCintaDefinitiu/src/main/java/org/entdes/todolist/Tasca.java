package org.entdes.todolist;

import java.io.Serializable;

public class Tasca implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private boolean completada = false;
    private String descripcio;

    private static int idCounter = 0;

    public Tasca(String descripcio) {
        this.id = ++idCounter;
        this.descripcio = descripcio;
    }

    public int getId() {
        return id;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    @Override
    public String toString() {
        return descripcio + ": " + (completada ? "Completada" : "Pendent");
    }


}
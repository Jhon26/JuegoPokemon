package com.example.tomas.juego_pokemon;

import android.app.Application;


public class Aplicacion extends Application {


    String miPuntuacion="", puntuacionAmigo="";
    boolean avanzar=false;

    public boolean getAvanzar() {
        return avanzar;
    }

    public void setAvanzar(boolean avanzar) {
        this.avanzar = avanzar;
    }

    public void setMiPuntuacion(String miPuntuacion){
        this.miPuntuacion = miPuntuacion;
    }

    public String getMiPuntuacion(){
        return miPuntuacion;
    }

    public String getPuntuacionAmigo() {
        return puntuacionAmigo;
    }

    public void setPuntuacionAmigo(String puntuacionAmigo) {
        this.puntuacionAmigo = puntuacionAmigo;
    }

}

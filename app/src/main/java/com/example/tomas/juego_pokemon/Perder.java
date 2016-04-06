package com.example.tomas.juego_pokemon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class Perder extends AppCompatActivity {

    public static Activity actividad;//Referencia a la instancia actual de la actividad (para cerrarla desde otras actividades)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_perder);
        actividad=this;

        //Si el cliente es el que termina
        if(getIntent().getExtras().getString("actividad").equals("cliente")) {
            //Se manda la puntuación a la clase aplicacion
            ((Aplicacion) getApplication()).setMiPuntuacion(getIntent().getExtras().getString("puntuacion"));

            //Avisa al hilo del cliente, por medio de la clase Aplicacion, que ya se tiene la puntuación
            ((Aplicacion) getApplication()).setAvanzar(true);
            esperarJugador();
        }

        //Si el servidor es el que termina
        if(getIntent().getExtras().getString("actividad").equals("servidor")) {
            //Se manda la puntuación a la clase aplicacion
            ((Aplicacion) getApplication()).setMiPuntuacion(getIntent().getExtras().getString("puntuacion"));

            //Avisa al hilo del servidor, por medio de la clase Aplicacion, que ya se tiene la puntuación
            ((Aplicacion) getApplication()).setAvanzar(true);

            //Se espera por el puntaje del rival y luego lanza la actividad Puntuacion
            esperarJugador();
        }
    }

    //Hilo que espera a que el otro jugador haya finalizado también el juego
    public void esperarJugador(){
        Thread thread;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String mensaje = "";
                while (true) {
                    //Si el otro jugador también terminó, se inicia la actividad Puntuación
                    if (!(((Aplicacion) getApplication()).getPuntuacionAmigo().equals(""))) {
                        Intent i = new Intent(Perder.this, Puntuacion.class);
                        Bundle bundle = getIntent().getExtras();
                        i.putExtra("puntuacion", bundle.getString("puntuacion"));
                        i.putExtra("actividad", getIntent().getExtras().getString("actividad"));
                        startActivity(i);
                        break;
                    }
                }
            }
        };
        thread=new Thread(runnable);
        thread.start();
    }

}


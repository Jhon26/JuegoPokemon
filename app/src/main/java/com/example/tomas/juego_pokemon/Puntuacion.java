package com.example.tomas.juego_pokemon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Puntuacion extends AppCompatActivity {

    public static TextView textView, textView2;
    Button volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion);

        //Se inicializan las variables
        textView = (TextView) findViewById(R.id.textView3);
        textView2 = (TextView) findViewById(R.id.textView5);
        volver = (Button) findViewById(R.id.button4);

        //Se pone la puntuación del jugador actual
        textView.setText("Tu puntuación: " + getIntent().getExtras().getString("puntuacion"));

        //Se pone la puntuación del amigo
        textView2.setText("Puntuacion de tu amigo: "+((Aplicacion) getApplication()).getPuntuacionAmigo());

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se reinician los datos de las puntuaciones y demás
                ((Aplicacion) getApplication()).setMiPuntuacion("");
                ((Aplicacion) getApplication()).setPuntuacionAmigo("");
                ((Aplicacion) getApplication()).setAvanzar(false);

                //Se reinician los hilos del cliente o del servidor dependiendo del rol del dispositivo
                if(getIntent().getExtras().getString("actividad").equals("cliente")){
                    Cliente.thread.interrupt();
                }else if(getIntent().getExtras().getString("actividad").equals("servidor")){
                    Servidor.thread.interrupt();
                }

                //Se cierran las actividades por arriba de la de Inicio en la pila de actividades para regresar a esta
                finish();
                Perder.actividad.finish();
                Jugar.actividad.finish();
                Principal.actividad.finish();
                if(getIntent().getExtras().getString("actividad").equals("cliente")){
                    Cliente.actividad.finish();
                }else if(getIntent().getExtras().getString("actividad").equals("servidor")){
                    Servidor.actividad.finish();
                }

            }
        });

    }

}

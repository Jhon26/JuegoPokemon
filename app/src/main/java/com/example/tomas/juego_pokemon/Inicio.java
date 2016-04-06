package com.example.tomas.juego_pokemon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class Inicio extends AppCompatActivity {

    public static Activity actividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        actividad=this;

        Button opcionCliente = (Button) findViewById(R.id.button);
        Button opcionServidor = (Button) findViewById(R.id.button2);

        //Al presionar la opcion de cliente se abre la activity del cliente
        opcionCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, Cliente.class);
                startActivity(intent);
            }
        });

        //Al presionar la opcion del servidor se abre la activity del servidor
        opcionServidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, Servidor.class);
                startActivity(intent);
            }
        });

    }

}
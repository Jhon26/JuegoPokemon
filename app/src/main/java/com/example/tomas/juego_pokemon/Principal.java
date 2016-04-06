package com.example.tomas.juego_pokemon;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class Principal extends AppCompatActivity {
    private Button jugar, creditos, listapokemon, salir;
    private MediaPlayer reproductor;
    public static Activity actividad;//Referencia a la instancia actual de la actividad (para cerrarla desde otras actividades)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se inicializan los componentes
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_principal);
        jugar = (Button) findViewById(R.id.btnjugar);
        creditos = (Button) findViewById(R.id.btnacerca);
        listapokemon = (Button) findViewById(R.id.btnlista);
        salir = (Button) findViewById(R.id.btnsalir);
        PokemonDB.cargarDatos(getApplicationContext());
        reproductor = MediaPlayer.create(this, R.raw.musicafondo);
        reproductor.setLooping(true);
        reproductor.start();
        actividad=this;

        jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PokemonDB.isWin()) {
                    Toast.makeText(getApplication(), getResources().getString(R.string.msg_ganaste), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Principal.this, Jugar.class);
                    intent.putExtra("actividad", getIntent().getExtras().getString("actividad"));
                    startActivity(intent);
                }

            }
        });

        listapokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform = new Intent(Principal.this, Pokedex.class);
                startActivity(nuevoform);
            }
        });

        creditos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform = new Intent(Principal.this, Creditos.class);
                startActivity(nuevoform);

            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Cliente c = new Cliente();
                    Servidor s = new Servidor();
                    Inicio i = new Inicio();
                    Bundle bundle = getIntent().getExtras();

                    //Se cierran las actividades por debajo de la Principal en la pila de actividades para cerrar la app
                    if(bundle.getString("actividad").equals("cliente")) {
                        c.actividad.finish();
                    }
                    if(bundle.getString("actividad").equals("servidor")){
                        s.actividad.finish();
                    }
                    i.actividad.finish();
                    finish();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reproductor.isPlaying()) {
            reproductor.stop();
            reproductor.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reproductor.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        reproductor.pause();
    }
}

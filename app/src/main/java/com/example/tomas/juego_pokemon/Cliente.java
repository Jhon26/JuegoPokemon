package com.example.tomas.juego_pokemon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente extends AppCompatActivity {

    public static Activity actividad;//Referencia a la instancia actual de la actividad (para cerrarla desde otras actividades)
    static Thread thread;//Hilo que crea los sockets
    Socket cliente;
    public boolean abierta=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        actividad=this;
        abierta = true;

        //Se incializan variables de referencia a los componentes de la interfaz
        final EditText editText = (EditText) findViewById(R.id.editText2);
        Button button = (Button) findViewById(R.id.button3);
        final TextView textView = (TextView) findViewById(R.id.textView6);

        //Acción del botón UNIRSE
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ipServidor = editText.getText().toString();//Se obtiene la IP del servidor ingresada

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        cliente = null;
                        String puntuacionServidor;

                        //Conectarse como cliente a un puerto y una IP específica
                        try {
                            //Se crea el socket cliente con la IP ingresada por el usuario
                            cliente = new Socket(ipServidor, 4449);

                            //Se incializan las variables de entrada y salida
                            BufferedReader entrada= new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                            PrintWriter salida = new PrintWriter(cliente.getOutputStream());

                            //Se lanza la actividad que muestra el menú
                            Intent intent = new Intent(Cliente.this, Principal.class);
                            intent.putExtra("actividad", "cliente");//Se manda el rol del jugador actual hasta la otra actividad
                            startActivity(intent);

                            //Espera a que se calcule su propia puntuación
                            while(true){
                                if(((Aplicacion) getApplication()).getAvanzar()){
                                    //Se recibe la puntuación desde la clase Aplicacion
                                    String puntuacion = ((Aplicacion) getApplication()).getMiPuntuacion();

                                    //Escribe su puntuación al servidor
                                    salida.write(puntuacion);
                                    salida.flush();
                                    //salida.close();MALO!!!!: CERRABA EL SOCKET TAMBIEN
                                    cliente.shutdownOutput();

                                    break;
                                }
                            }

                            //Espera a que el servidor envie la puntuación
                            while(true) {
                                puntuacionServidor = entrada.readLine();
                                //Si sí se recibió la puntuación del servidor se manda a la clase Aplicacion para que se lea en
                                //la actividad Perder y así proceder a abrir la actividad Puntuacion para mostrar las puntuaciones
                                if(!((puntuacionServidor.equals(""))||(puntuacionServidor=="")||(puntuacionServidor==null)||(puntuacionServidor.equals(null)))){
                                    //Cuando la envie se lleva a la clase Aplicacion
                                    ((Aplicacion) getApplication()).setPuntuacionAmigo(puntuacionServidor);
                                    break;
                                }
                            }

                        }//Excepción para indicar que no hay servidores escuchando en ese puerto o en esa IP
                        catch (UnknownHostException e) {
                            //Imprimir excepción en el output del IDE
                            e.printStackTrace();
                            /*//Dar a conocer el error al usuario poniendo el mensaje en el textView
                            textView.setText("Ingrese correctamente los datos");*/
                        }//Excepcion que indica problemas al enviar o recibir datos
                        catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }
}

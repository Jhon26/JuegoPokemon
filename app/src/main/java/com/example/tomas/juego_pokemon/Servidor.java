package com.example.tomas.juego_pokemon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Scanner;

public class Servidor extends AppCompatActivity {

    static Thread thread;//Hilo que crea los sockets
    ServerSocket serverSocket;
    Socket cliente = null;
    String IP;//Guarda la IP del dispositivo que ejecute esté actuando como servidor
    public static Activity actividad;//Referencia a la instancia actual de la actividad (para cerrarla desde otras actividades)
    public boolean abierta=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor);
        actividad=this;
        abierta = true;
        TextView textView = (TextView) findViewById(R.id.textView);
        IP = wifiIpAddress(this);
        textView.setText(textView.getText().toString()+IP);
        initThread();//Se inicia el hilo para crear los sockets con la IP proporcionada
    }

    //Cuando se para la actividad (finaliza la app), se cierra el socket servidor
    @Override
    public void onStop() {
        super.onStop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initThread(){
        thread = new Thread (new Runnable() {
            @Override
            public void run() {

                serverSocket = null;
                String puntuacionCliente;

                try {
                    //Se crea el socket servidor
                    InetAddress inetAddress = InetAddress.getByName(IP);
                    serverSocket = new ServerSocket(4449, 50, inetAddress);

                    //Se acepta el cliente
                    cliente = serverSocket.accept();

                    //Se incializan las variables de entrada y salida
                    Scanner entrada = new Scanner(cliente.getInputStream());
                    PrintWriter salida = new PrintWriter(cliente.getOutputStream());

                    //Se inicia la actividad principal que muestra el menú del juego
                    Intent intent = new Intent(Servidor.this, Principal.class);
                    intent.putExtra("actividad", "servidor");//Se manda el rol del jugador actual hasta la otra actividad
                    startActivity(intent);

                    //Espera la puntuacion del cliente
                    while(true){
                        //Si hay datos de entrada se guardan y se envian a la clase Aplicacion para que otra actividad los pueda leer
                        if (entrada.hasNext()) {
                            puntuacionCliente = entrada.nextLine();
                            ((Aplicacion) getApplication()).setPuntuacionAmigo(puntuacionCliente);
                            break;//Rompe el ciclo al haber leido los datos
                        }
                    }

                    //Espera a que se calcule su propia puntuación
                    while(true) {
                        //True = ya el servidor llegó a la actividad Perder y por lo tanto ya tiene
                        //su puntaje asignado en la actividad Aplicacion. False = No ha llegado
                        if (((Aplicacion) getApplication()).getAvanzar()) {
                            //Escribe su puntuación al cliente
                            String puntuacion = ((Aplicacion) getApplication()).getMiPuntuacion();
                            salida.println(puntuacion);
                            salida.close();


                            break;
                        }
                    }
                //Excepcion al momento de crear las variables de entrada y salida
                } catch (IOException x) {
                    x.printStackTrace();
                }
            }
        });
        thread.start();
    }

    //Método que obtiene la IP del dispositivo desde el cual se está llamando
    protected String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        //Se obtiene la IP normalmente
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Se convierte de Little Endian a Big Endian si es necesario
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        //Se convierte esa IP a String
        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }
}

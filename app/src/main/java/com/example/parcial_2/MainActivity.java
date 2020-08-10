package com.example.parcial_2;
import androidx.appcompat.app.AppCompatActivity;

/*
La informacion del las librerias y logica ajena
usada en el programa se encuentra en el
Leeme.txt incluido en la carpeta

 */
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    public ImageButton el0, el1, el2, el3;
    public Button reiniciar, salir;
    public int imagenes[];
    public ImageButton [] botonera = new ImageButton[4];
    public int fondo;
    public ArrayList<Integer> arrayBarajado;
    public ImageButton primero;
    public int numeroPrimero, numeroSegundo;
    public boolean bloqueo = false;
    public final Handler handler = new Handler();
    public Button before;
    public Button after;
    int aciertos=0;
    int puntuacion=0;
    ImageView imageView;
    TextView textoPuntuacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        before = (Button) findViewById(R.id.btbefore);
        after = (Button) findViewById(R.id.btafter);
        after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextGame();
            }
        });
        cargarImagenes();
        iniciar();

    }
    public void nextGame(){
        Intent i = new Intent(this, stage2.class);
        startActivity(i);
    }
    public void cargarImagenes() {
        imagenes = new int[]{
                R.drawable.la0,
                R.drawable.la1,
        };

        fondo = R.drawable.fondo;
    }

    public void iniciar(){
        arrayBarajado = barajar(imagenes.length*2);
        cargarBotones();

        //MOSTRAMOS LA IMAGEN
        for(int i=0; i<botonera.length; i++) {
            botonera[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            botonera[i].setImageResource(imagenes[arrayBarajado.get(i)]);
        }

        //OCULTAMOS LAS IMAGEN
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < botonera.length; i++) {
                    botonera[i].animate().rotationY(360f).setDuration(1000);
                    botonera[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
                    botonera[i].setImageResource(fondo);
                }
            }
        }, 2500);

        //AÑADIMOS LOS EVENTOS A LOS BOTONES DEL JUEGO
        for(int i=0; i <arrayBarajado.size(); i++){
            final int j=i;
            botonera[i].setEnabled(true);
            botonera[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!bloqueo)
                        comprobar(j, botonera[j]);
                }
            });
        }
        aciertos=0;
        puntuacion=0;
        textoPuntuacion.setText("Puntuación: " + puntuacion);
    }
    public ArrayList<Integer> barajar(int longitud) {
        ArrayList resultadoA = new ArrayList<Integer>();
        for(int i=0; i<longitud; i++)
            resultadoA.add(i % longitud/2);
        Collections.shuffle(resultadoA);
        return  resultadoA;
    }
    public void cargarBotones(){
        el0 = (ImageButton) findViewById(R.id.boton00);
        botonera[0] = el0;
        el1 = (ImageButton) findViewById(R.id.boton01);
        botonera[1] = el1;
        el2 = (ImageButton) findViewById(R.id.boton02);
        botonera[2] = el2;
        el3 = (ImageButton) findViewById(R.id.boton03);
        botonera[3] = el3;
        textoPuntuacion = (TextView)findViewById(R.id.textoPuntuacion);
        textoPuntuacion.setText("Puntuación: " + puntuacion);
    }
    public void comprobar(int i, final ImageButton imgb){
        if(primero==null){
            primero = imgb;
            primero.setScaleType(ImageView.ScaleType.CENTER_CROP);
            primero.animate().rotationY(360f).setDuration(1000);
            primero.setImageResource(imagenes[arrayBarajado.get(i)]);
            primero.setEnabled(false);
            numeroPrimero=arrayBarajado.get(i);
        }else{
            bloqueo=true;
            imgb.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgb.animate().rotationY(360f).setDuration(1000);
            imgb.setImageResource(imagenes[arrayBarajado.get(i)]);
            imgb.setEnabled(false);
            numeroSegundo=arrayBarajado.get(i);
            if(numeroPrimero==numeroSegundo){
                //reiniciamos
                primero=null;
                bloqueo=false;
                //aumentamos los aciertos y la puntuación
                aciertos++;
                puntuacion++;
                textoPuntuacion.setText("Puntuación: " + puntuacion);
                if(aciertos==2){
                    Toast toast = Toast.makeText(getApplicationContext(), "Has ganado!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }else{
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        primero.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        primero.animate().rotationY(360f).setDuration(1000);
                        primero.setImageResource(R.drawable.fondo);
                        imgb.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imgb.animate().rotationY(360f).setDuration(1000);
                        imgb.setImageResource(R.drawable.fondo);
                        primero.setEnabled(true);
                        imgb.setEnabled(true);
                        primero = null;
                        bloqueo = false;
                        if (puntuacion > 0) {
                            puntuacion--;
                            textoPuntuacion.setText("Puntuación: " + puntuacion);
                        }
                    }
                }, 1000);//al cabo de un segundo
            }
        }

    }
}

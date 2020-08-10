package com.example.parcial_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class stage3 extends Activity {
    //cambiar esto
    public ImageButton el0, el1, el2, el3, el4, el5, el6, el7;
    public Button reiniciar, salir;
    public int imagenes[];
    //cambiar esto
    public ImageButton [] botonera = new ImageButton[8];
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
    TextView textoPuntuacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cambiar esto
        setContentView(R.layout.layout_stage3);
        before = (Button) findViewById(R.id.btbefore);
        after = (Button) findViewById(R.id.btafter);
        after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextGame();
            }
        });
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postGame();
            }
        });
        cargarImagenes();
        iniciar();
    }
    //cambiar esto
    public void nextGame(){
        Intent i = new Intent(this, stage5.class);
        startActivity(i);
    }
    public void postGame(){
        Intent i = new Intent(this,stage2.class);
        startActivity(i);
    }
    //cambiar esto
    public void cargarImagenes() {
        imagenes = new int[]{
                R.drawable.la0,
                R.drawable.la1,
                R.drawable.la2,
                R.drawable.la3,
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
    //cambiar esto
    public void cargarBotones(){
        el0 = (ImageButton) findViewById(R.id.boton00);
        botonera[0] = el0;
        el1 = (ImageButton) findViewById(R.id.boton01);
        botonera[1] = el1;
        el2 = (ImageButton) findViewById(R.id.boton02);
        botonera[2] = el2;
        el3 = (ImageButton) findViewById(R.id.boton03);
        botonera[3] = el3;
        el4 = (ImageButton) findViewById(R.id.boton04);
        botonera[4] = el4;
        el5 = (ImageButton) findViewById(R.id.boton05);
        botonera[5] = el5;
        el6 = (ImageButton) findViewById(R.id.boton06);
        botonera[6] = el6;
        el7 = (ImageButton) findViewById(R.id.boton07);
        botonera[7] = el7;
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

package com.example.xp.ahorcado;

import android.annotation.TargetApi;
import android.app.FragmentContainer;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String palabraOculta = "CONO";
    int numeroFallos = 0;
    Handler mHandler = new Handler ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.ventanaJuego, new VentanaAhorcado()).commit();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            hideVirtualButtons();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        palabraOculta = escogePalabra();
        String barras = "";
        for (int i = 0; i < palabraOculta.length(); i++) {
            barras += "_ ";
        }
        ((TextView) findViewById(R.id.palabraConGuiones)).setText(barras);
    }


    @TargetApi(19)
    private void hideVirtualButtons() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // In KITKAT (4.4) and next releases, hide the virtual buttons
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hideVirtualButtons();
            }
        }
    }


    public void botonPulsado(View vista) {
        Button boton = (Button) findViewById(vista.getId());
        boton.setVisibility(View.INVISIBLE);
        chequeaLetra(boton.getText().toString());

    }

    public void botonReset(View vista) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void chequeaLetra(String letra) {
        letra = letra.toUpperCase();
        ImageView imagenAhorcado = ((ImageView) findViewById(R.id.imagenAhorcado));
        TextView textoGuiones = ((TextView) findViewById(R.id.palabraConGuiones));
        String palabraConGuiones = textoGuiones.getText().toString();

        boolean acierto = false;

        for (int i = 0; i < palabraOculta.length(); i++) {
            if (palabraOculta.charAt(i) == letra.charAt(0)) {
                //quita el guión bajo de la letra correspondiente
                palabraConGuiones = palabraConGuiones.substring(0, 2 * i)
                        + letra
                        + palabraConGuiones.substring(2 * i + 1);
                acierto = true;
            }
        }
        //chequeo si se ha terminado la partida porque ha acertado todas las letras
        if (!palabraConGuiones.contains("_")) {
            imagenAhorcado.setImageResource(R.drawable.acertastetodo);
            this.findViewById(R.id.lin1).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.lin2).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.lin3).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.lin4).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.botonR).setVisibility(View.VISIBLE);
        }
        textoGuiones.setText(palabraConGuiones);

        if (!acierto) {
            if (!palabraConGuiones.contains("_")) {
                reiniciaJuego();
            } else {
                numeroFallos++;
                switch (numeroFallos) {
                    case 0:
                        imagenAhorcado.setImageResource(R.drawable.ahorcado_0);
                        break;
                    case 1:
                        imagenAhorcado.setImageResource(R.drawable.ahorcado_1);
                        break;
                    case 2:
                        imagenAhorcado.setImageResource(R.drawable.ahorcado_2);
                        break;
                    case 3:
                        imagenAhorcado.setImageResource(R.drawable.ahorcado_3);
                        break;
                    case 4:
                        imagenAhorcado.setImageResource(R.drawable.ahorcado_4);
                        break;
                    case 5:
                        imagenAhorcado.setImageResource(R.drawable.ahorcado_5);
                        break;
                    default:
                        imagenAhorcado.setImageResource(R.drawable.ahorcado_fin);
                        this.findViewById(R.id.lin1).setVisibility(View.INVISIBLE);
                        this.findViewById(R.id.lin2).setVisibility(View.INVISIBLE);
                        this.findViewById(R.id.lin3).setVisibility(View.INVISIBLE);
                        this.findViewById(R.id.lin4).setVisibility(View.INVISIBLE);
                        this.findViewById(R.id.lin5).setVisibility(View.INVISIBLE);
                        this.findViewById(R.id.botonR).setVisibility(View.VISIBLE);
                        break;
                }
                if(numeroFallos == 6){
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            ImageView imagenAhorcado = ((ImageView) findViewById(R.id.imagenAhorcado));
                            imagenAhorcado.setImageResource(R.drawable.pikachu);
                        }
                        public void botonfuera(View vista){
                            Button boton = (Button) findViewById(vista.getId());
                            boton.setVisibility(View.INVISIBLE);
                        }
                    }, 200);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            ImageView imagenAhorcado = ((ImageView) findViewById(R.id.imagenAhorcado));
                            imagenAhorcado.setImageResource(R.drawable.pikachu);
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }, 2500);
                }
            }
        }

    }

    private void reiniciaJuego() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private String escogePalabra() {
        String auxiliar = "";
        String[] listaPalabras = {"CETYS", "hola", "adios", "cono", "desCONOzco", "iCONO", "diaCONO", "CONOcida", "CONOcimiento"};
        Random r = new Random();
        auxiliar = listaPalabras[r.nextInt(listaPalabras.length)];
        auxiliar = auxiliar.toUpperCase();
        return auxiliar;
    }

}

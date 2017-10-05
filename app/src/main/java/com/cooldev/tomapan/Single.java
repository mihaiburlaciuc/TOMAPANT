package com.cooldev.tomapan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tomapan.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Single extends Activity {

    EditText tari, orase, munti, ape, plante, animale, nume;
    TextView litere, timer;
    List<String> tari_txt;
    List<String> orase_txt;
    List<String> munti_txt;
    List<String> ape_txt;
    List<String> plante_txt;
    List<String> animale_txt;
    List<String> nume_txt;
    String[][] lista_toate = new String[7][];
    String litera;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            int ramase = Integer.parseInt(timer.getText().toString()) - 1;
            timer.setText(Integer.toString(ramase));
            if(Integer.parseInt(timer.getText().toString()) > 0)
                handler.postDelayed(runnable, 1000);
            else
            {
                SendAnswers(null);
                handler.postDelayed(runnable, 1000);
            }
        }
    };

    public void verifica(EditText lista, List<String> lista_txt, final int catelea, int textID)
    {
        lista_txt = new ArrayList<String>();
        try{
            InputStream in_s;
            BufferedReader reader;
            in_s = (InputStream) this.getResources().openRawResource(textID);
            reader = new BufferedReader(new InputStreamReader(in_s));
            String line;
            while ((line = reader.readLine()) != null)
                lista_txt.add(line);
            lista_toate[catelea] = lista_txt.toArray(new String[lista_txt.size()]);
        }
        catch(IOException e)
        {
            Log.e("ceva", "nu mere");
            e.printStackTrace();
        }
        String de_cautat = lista.getText().toString().toUpperCase();
        if (!de_cautat.equals(""))
        {
            String lit = litera;
            int este = Arrays.binarySearch(lista_toate[catelea], de_cautat);
            if (este >= 0 && de_cautat.charAt(0) == lit.charAt(0))
            {
                lista.setBackgroundColor(Color.parseColor("#2aff00"));
                /*ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lista.getLayoutParams();
                double dpValue = 6.5; // margin in dips
                float d = getApplicationContext().getResources().getDisplayMetrics().density;
                int margin = (int)(dpValue * d); // margin in pixels
                p.setMargins(0, 0, 0, margin);*/
                lista.requestLayout();
            }
            else
            {
                lista.setBackgroundColor(Color.parseColor("#ff3300"));
                /*ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lista.getLayoutParams();
                double dpValue = 6.5; // margin in dips
                float d = getApplicationContext().getResources().getDisplayMetrics().density;
                int margin = (int)(dpValue * d); // margin in pixels
                p.setMargins(0, 0, 0, margin);*/
                lista.requestLayout();
                if (de_cautat.charAt(0) != lit.charAt(0))
                    Toast.makeText(getApplicationContext(), "Raspunsul trebuie sa inceapa cu litera " + lit + "!", Toast.LENGTH_LONG).show();
                else if (este < 0)
                    Toast.makeText(getApplicationContext(), "Raspunsul nu a fost gasit in baza de date!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void seteaza(final EditText lista, final List<String> lista_txt, final int catelea, final int textID)
    {
        lista.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {
                    verifica(lista, lista_txt, catelea, textID);
                }
            }
        });
        lista.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    verifica(lista, lista_txt, catelea, textID);
                    return true;
                }
                return false;
            }
        });
    }

    /*public void seteaza(View view, List<String> lista_txt, final int catelea, int textID)
    {
        lista_txt = new ArrayList<String>();
        final EditText lista = (EditText)view;
        try{
            InputStream in_s;
            BufferedReader reader;
            in_s = (InputStream) this.getResources().openRawResource(textID);
            reader = new BufferedReader(new InputStreamReader(in_s));
            String line;
            while ((line = reader.readLine()) != null)
                lista_txt.add(line);
            lista_toate[catelea] = lista_txt.toArray(new String[lista_txt.size()]);
        }
        catch(IOException e)
        {
            Log.e("ceva", "nu mere");
            e.printStackTrace();
        }
        lista.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {

                    for (int i = 0; i < lista_toate[catelea].length; i++)
                        Log.e("ba", lista_toate[catelea][i]);
                    String de_cautat = lista.getText().toString().toUpperCase();
                    if (!de_cautat.equals(""))
                    {
                        int este = Arrays.binarySearch(lista_toate[catelea], de_cautat);
                        if (este >= 0 && de_cautat.charAt(0) == litera.charAt(0))
                        {
                            lista.setBackgroundColor(Color.parseColor("#2aff00"));
                            //ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lista.getLayoutParams();
                            //double dpValue = 6.5; // margin in dips
                            //float d = getApplicationContext().getResources().getDisplayMetrics().density;
                            //int margin = (int)(dpValue * d); // margin in pixels
                            //p.setMargins(0, 0, 0, margin);
                            lista.requestLayout();
                        }
                        else {
                            lista.setBackgroundColor(Color.parseColor("#ff3300"));
                            //ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lista.getLayoutParams();
                            //double dpValue = 6.5; // margin in dips
                            //float d = getApplicationContext().getResources().getDisplayMetrics().density;
                            //int margin = (int)(dpValue * d); // margin in pixels
                            //p.setMargins(0, 0, 0, margin);
                            lista.requestLayout();
                            if(lista.getId() != R.id.nume)
                                lista.setBottom(7);
                            if (de_cautat.charAt(0) != litera.charAt(0))
                                Toast.makeText(getApplicationContext(), "Raspunsul trebuie sa inceapa cu litera " + litera + "!", Toast.LENGTH_LONG).show();
                            else if (este < 0)
                                Toast.makeText(getApplicationContext(), "Raspunsul nu a fost gasit in baza de date!", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }
        });
    }*/

    Drawable[] backs = new Drawable[7];
    int pdL, pdT, pdR, pdB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        tari = (EditText)findViewById(R.id.tari);
        orase = (EditText)findViewById(R.id.orase);
        munti = (EditText)findViewById(R.id.munti);
        ape = (EditText)findViewById(R.id.ape);
        plante = (EditText)findViewById(R.id.plante);
        animale = (EditText)findViewById(R.id.animale);
        nume = (EditText)findViewById(R.id.nume);
        litere = (TextView)findViewById(R.id.litere);
        Intent intent = getIntent();
        litere.setText(intent.getStringExtra("litera"));
        timer = (TextView)findViewById(R.id.timer);
        litera = litere.getText().toString();

        backs[0] = tari.getBackground();
        backs[1] = orase.getBackground();
        backs[2] = munti.getBackground();
        backs[3] = ape.getBackground();
        backs[4] = plante.getBackground();
        backs[5] = animale.getBackground();
        backs[6] = nume.getBackground();

        pdL = tari.getPaddingLeft();
        pdT = tari.getPaddingTop();
        pdR = tari.getPaddingRight();
        pdB = tari.getPaddingBottom();

        seteaza(tari, tari_txt, 0, R.raw.tari);
        seteaza(orase, orase_txt, 1, R.raw.orase);
        seteaza(munti, munti_txt, 2, R.raw.munti);
        seteaza(ape, ape_txt, 3, R.raw.ape);
        seteaza(plante, plante_txt, 4, R.raw.plante);
        seteaza(animale, animale_txt, 5, R.raw.animale);
        seteaza(nume, nume_txt, 6, R.raw.nume);
        handler.postDelayed(runnable, 1000);
    }

    public void restart(View view, int t)
    {
        EditText casuta = (EditText)view;
        casuta.setText("");
        casuta.setBackground(backs[t]);
        casuta.requestLayout();
    }

    public void SendAnswers(View view)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt(26);
        int ceva = (int)'A' + randomNum;
        String s = "";
        s += (char)ceva;
        litera = s;
        litere.setText(litera);
        timer.setText("90");
        restart(tari, 0);
        restart(orase, 1);
        restart(munti, 2);
        restart(ape, 3);
        restart(plante, 4);
        restart(animale, 5);
        restart(nume, 6);
        //handler.postDelayed(runnable, 1000);
    }
}

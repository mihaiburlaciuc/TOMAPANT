package com.cooldev.tomapan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Game extends Activity {

    String litera_curenta;
    TextView timer, litera;
    EditText tari, orase, munti, ape, plante, animale, nume;
    boolean runda = true;
    boolean trimise = false;
    boolean oprit = false;
    boolean[] bine = new boolean[7];
    Intent intent;
    String name, room;
    String site_ul = "http://eragon.herokuapp.com";
    Queue<String> coada, coada2;
    Queue <Boolean> coada3;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            int ramase = Integer.parseInt(timer.getText().toString()) - 1;
            timer.setText(Integer.toString(ramase));
            if(Integer.parseInt(timer.getText().toString()) > 0)
                handler.postDelayed(runnable, 1000);
            else
            {
                if (!bine[0])
                    tari.setText("");
                if (!bine[1])
                    orase.setText("");
                if (!bine[2])
                    munti.setText("");
                if (!bine[3])
                    ape.setText("");
                if (!bine[4])
                    plante.setText("");
                if (!bine[5])
                    animale.setText("");
                if (!bine[6])
                    nume.setText("");
                handler.removeCallbacks(runnable);
                SendAnswers(null);
            }
        }
    };

    public void CheckRound()
    {
        String site = site_ul + "/checkround";
        String current_action = "CheckRound";
        String[] primele = new String[2];
        primele[0] = site;
        primele[1] = current_action;
        String urmatoarele[] = new String[5];
        urmatoarele[0] = "3";
        urmatoarele[1] = "player_id";
        urmatoarele[2] = name;
        urmatoarele[3] = "room";
        urmatoarele[4] = room;
        new ATask().execute(primele, urmatoarele);
    }

    public void openRezultat()
    {
        Intent incercare = new Intent(this, Rezultat.class);
        //AICI TREBUIE PUSE NISTE CHESTII DACA ARATAM CA A CASTIGAT!
        //incercare.putExtra("nume", player_id);
        incercare.putExtra("room", room);
        //incercare.putExtra("litera", litera);
        this.startActivity(incercare);
        finish();
    }

    public void GetScore()
    {
        String site = site_ul + "/getscore";
        String current_action = "GetScore";
        String[] primele = new String[2];
        primele[0] = site;
        primele[1] = current_action;
        String urmatoarele[] = new String[5];
        urmatoarele[0] = "3";
        urmatoarele[1] = "player_id";
        urmatoarele[2] = name;
        urmatoarele[3] = "room";
        urmatoarele[4] = room;
        new ATask().execute(primele, urmatoarele);
    }

    public void StartAgain()
    {
        String site = site_ul + "/startagain";
        String current_action = "StartAgain";
        String[] primele = new String[2];
        primele[0] = site;
        primele[1] = current_action;
        String urmatoarele[] = new String[3];
        urmatoarele[0] = "1";
        urmatoarele[1] = "room";
        urmatoarele[2] = room;
        new ATask().execute(primele, urmatoarele);
    }

    public void CheckAll()
    {
        String site = site_ul + "/checkall";
        String current_action = "CheckAll";
        String[] primele = new String[2];
        primele[0] = site;
        primele[1] = current_action;
        String urmatoarele[] = new String[5];
        urmatoarele[0] = "3";
        urmatoarele[1] = "player_id";
        urmatoarele[2] = name;
        urmatoarele[3] = "room";
        urmatoarele[4] = room;
        new ATask().execute(primele, urmatoarele);
    }

    @Override
    protected void onStop() {
        oprit = true;
        handler.removeCallbacks(runnable);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        oprit = true;
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    public void SendAnswers(View view)
    {
        boolean toate = true;
        for(int i = 0; i < 7; i++)
            if(!bine[i])
                toate = false;
        if(toate || view == null)
        {
            trimise = true;
            runda = false;
            String site = site_ul + "/sendans";
            String current_action = "SendAns";
            String[] primele = new String[2];
            primele[0] = site;
            primele[1] = current_action;
            String urmatoarele[] = new String[19];
            urmatoarele[0] = "17";
            urmatoarele[1] = "player_id";
            urmatoarele[2] = name;
            urmatoarele[3] = "room";
            urmatoarele[4] = room;
            urmatoarele[5] = "rasp0";
            urmatoarele[6] = tari.getText().toString().toUpperCase();
            urmatoarele[7] = "rasp1";
            urmatoarele[8] = orase.getText().toString().toUpperCase();
            urmatoarele[9] = "rasp2";
            urmatoarele[10] = munti.getText().toString().toUpperCase();
            urmatoarele[11] = "rasp3";
            urmatoarele[12] = ape.getText().toString().toUpperCase();
            urmatoarele[13] = "rasp4";
            urmatoarele[14] = plante.getText().toString().toUpperCase();
            urmatoarele[15] = "rasp5";
            urmatoarele[16] = animale.getText().toString().toUpperCase();
            urmatoarele[17] = "rasp6";
            urmatoarele[18] = nume.getText().toString().toUpperCase();

            if(view != null)
                Toast.makeText(getApplicationContext(), "Raspunsuri trimise!", Toast.LENGTH_LONG).show();
            handler.removeCallbacks(runnable);
            new ATask().execute(primele, urmatoarele);
        }
        else
            Toast.makeText(getApplicationContext(), "Trebuie sa completezi corect toate campurile!", Toast.LENGTH_LONG).show();
    }

    List<String> tari_txt = new ArrayList<String>();
    List<String> orase_txt = new ArrayList<String>();
    List<String> munti_txt = new ArrayList<String>();
    List<String> ape_txt = new ArrayList<String>();
    List<String> plante_txt = new ArrayList<String>();
    List<String> animale_txt = new ArrayList<String>();
    List<String> nume_txt = new ArrayList<String>();
    String[][] lista_toate = new String[7][];

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
            String lit = litera.getText().toString();
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
                bine[catelea] = true;
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
                bine[catelea] = false;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        coada = new LinkedList<String>();
        coada2 = new LinkedList <String> ();
        coada3 = new LinkedList <Boolean> ();
        litera = (TextView)findViewById(R.id.litere);
        timer = (TextView)findViewById(R.id.timer);
        tari = (EditText)findViewById(R.id.tari);
        orase = (EditText)findViewById(R.id.orase);
        munti = (EditText)findViewById(R.id.munti);
        ape = (EditText)findViewById(R.id.ape);
        plante = (EditText)findViewById(R.id.plante);
        animale = (EditText)findViewById(R.id.animale);
        nume = (EditText)findViewById(R.id.nume);

        backs[0] = tari.getBackground();
        backs[1] = orase.getBackground();
        backs[2] = munti.getBackground();
        backs[3] = ape.getBackground();
        backs[4] = plante.getBackground();
        backs[5] = animale.getBackground();
        backs[6] = nume.getBackground();

        seteaza(tari, tari_txt, 0, R.raw.tari);
        seteaza(orase, orase_txt, 1, R.raw.orase);
        seteaza(munti, munti_txt, 2, R.raw.munti);
        seteaza(ape, ape_txt, 3, R.raw.ape);
        seteaza(plante, plante_txt, 4, R.raw.plante);
        seteaza(animale, animale_txt, 5, R.raw.animale);
        seteaza(nume, nume_txt, 6, R.raw.nume);

        intent = getIntent();
        room = intent.getStringExtra("room");
        name = intent.getStringExtra("nume");
        litera_curenta = intent.getStringExtra("litera");
        litera.setText(litera_curenta);
        final Handler handlerul = new Handler();
        handlerul.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                CheckRound();
            }
        }, 1000);
        handler.postDelayed(runnable, 1000);
    }

    //trebuie modificat!!!
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    String culoare(String s)
    {
        if(s.equals("0"))
            return "#ff3300";
        else if(s.equals("5"))
            return "#0033cc";
        else
            return "#2aff00";
    }

    Drawable[] backs = new Drawable[7];

    public void restart(View view, int t)
    {
        EditText casuta = (EditText)view;
        casuta.setText("");
        casuta.setBackground(backs[t]);
        /*ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) casuta.getLayoutParams();
        p.setMargins(0, 0, 0, 0);*/
        casuta.requestLayout();
    }

    public class ATask extends AsyncTask<String[], Void, String> {
        String ceva = "";
        @Override
        protected String doInBackground(String[]... urls) {
            //try {
            if(!oprit)
            {
                try {
                    String site = urls[0][0];
                    String current_action = urls[0][1];
                    Integer cate = Integer.parseInt(urls[1][0]);
                    HashMap <String, String> hash = new HashMap<String, String>();
                    for(int i = 1; i <= cate; i += 2)
                    {
                        String a = urls[1][i];
                        String b = urls[1][i + 1];
                        hash.put(a, b);
                        Log.e("ceva", a + " " + b);
                    }
                    Log.e("rasp", site);
                    URL obj = new URL(site);
                    try {
                        Log.e("rasp", obj.toString());
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type",
                                "application/x-www-form-urlencoded");
                        //con.setRequestProperty("User-Agent", USER_AGENT);
                        // For POST only - START
                        con.setDoOutput(true);
                        OutputStream os = con.getOutputStream();
                        os.write(getPostDataString(hash).getBytes());
                        os.flush();
                        os.close();
                        // For POST only - END
                        int responseCode = con.getResponseCode();
                        Log.e("rasp", "response code-ul e " + Integer.toString(responseCode));
                        if (responseCode == HttpURLConnection.HTTP_OK) { //success
                            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    con.getInputStream()));
                            String inputLine;
                            StringBuffer response = new StringBuffer();
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();
                            // print result
                            coada.add(current_action);
                            coada2.add(response.toString());
                            coada3.add(true);
                        }
                        else {
                            Log.e("rasp", "POST request not worked");
                            coada3.add(false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        coada3.add(false);
                    }
                } catch (MalformedURLException e) {
                    Log.e("naspa", "E corupt!");
                    coada3.add(false);
                }
            }
            //} catch (Exception e) {
            // Log.e("rasp", "aia e");
            //}
            return ceva;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(!oprit)
            {
                Boolean success = coada3.poll();
                if (success) {
                    String actiune = coada.poll();
                    String rezultat = coada2.poll();
                    Log.e("succes", "nu e oprit!");
                    if (!rezultat.equals("Wrong room!")) {
                        if (actiune.equals("CheckRound")) {
                            if (rezultat.equals("Runda terminata") && !trimise) {
                                Log.e("ce", "da");
                                if (!bine[0])
                                    tari.setText("");
                                if (!bine[1])
                                    orase.setText("");
                                if (!bine[2])
                                    munti.setText("");
                                if (!bine[3])
                                    ape.setText("");
                                if (!bine[4])
                                    plante.setText("");
                                if (!bine[5])
                                    animale.setText("");
                                if (!bine[6])
                                    nume.setText("");
                                handler.removeCallbacks(runnable);
                                SendAnswers(null);
                            } else if (rezultat.equals("Runda neterminata")) {
                                final Handler handlerul = new Handler();
                                handlerul.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
                                        CheckRound();
                                    }
                                }, 500);
                            }
                        } else if (actiune.equals("SendAns")) {
                            Log.e("ceva", "raspunsuri trimise!");
                            CheckAll();
                        } else if (actiune.equals("CheckAll")) {
                            Log.e("rezultat", rezultat);
                            if (rezultat.equals("Au trimis toti"))
                                GetScore();
                            else if (rezultat.equals("Nu inca")) {
                                final Handler handlerul = new Handler();
                                handlerul.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
                                        CheckAll();
                                    }
                                }, 500);
                            } else {
                                //Toast.makeText(Game.this, rezultat, Toast.LENGTH_LONG).show();
                                String tot[] = rezultat.split("\\s+");
                                for (int i = 0; i < tot.length; i++)
                                    Toast.makeText(Game.this, tot[i] + " s-a deconectat!", Toast.LENGTH_LONG).show();
                                Log.e("ceva", "nu au trimis toti");
                                final Handler handlerul = new Handler();
                                handlerul.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
                                        CheckAll();
                                    }
                                }, 500);
                            }
                        } else if (actiune.equals("GetScore")) {
                            rezultat = rezultat.substring(3);
                            String punctaje[] = rezultat.split("\\s+");
                            findViewById(R.id.mainLayout).requestFocus();
                            tari.setBackgroundColor(Color.parseColor(culoare(punctaje[0])));
                            orase.setBackgroundColor(Color.parseColor(culoare(punctaje[1])));
                            munti.setBackgroundColor(Color.parseColor(culoare(punctaje[2])));
                            ape.setBackgroundColor(Color.parseColor(culoare(punctaje[3])));
                            plante.setBackgroundColor(Color.parseColor(culoare(punctaje[4])));
                            animale.setBackgroundColor(Color.parseColor(culoare(punctaje[5])));
                            nume.setBackgroundColor(Color.parseColor(culoare(punctaje[6])));
                            int suma = 0;
                            for (int i = 0; i < 7; i++)
                                suma += Integer.parseInt(punctaje[i]);
                            Toast.makeText(Game.this, "Ai obtinut " + Integer.toString(suma) + " puncte!", Toast.LENGTH_LONG).show();
                            final Handler handlerul = new Handler();
                            handlerul.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000m
                                    StartAgain();
                                }
                            }, 2000);
                        } else if (actiune.equals("StartAgain")) {
                            if (rezultat.substring(0, 2).equals("Ye")) {
                                trimise = false;
                                rezultat = rezultat.substring(3);
                                litera_curenta = rezultat;
                                litera.setText(litera_curenta);
                                timer.setText("90");
                                for (int i = 0; i < 7; i++)
                                    bine[i] = false;
                                restart(tari, 0);
                                restart(orase, 1);
                                restart(munti, 2);
                                restart(ape, 3);
                                restart(plante, 4);
                                restart(animale, 5);
                                restart(nume, 6);
                                handler.postDelayed(runnable, 1000);
                                CheckRound();
                            } else if (rezultat.equals("Nu a reinceput!")) {
                                final Handler handlerul = new Handler();
                                handlerul.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
                                        StartAgain();
                                    }
                                }, 500);
                            } else if (rezultat.equals("End"))
                                openRezultat();
                        }
                    }
                }
                else
                {
                    Toast.makeText(Game.this, "Eroare la conexiunea cu serverul!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

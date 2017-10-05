package com.cooldev.tomapan;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tomapan.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BeforeGame extends Activity {

    Queue<String> coada, coada2;
    Queue <Boolean> coada3;
    ListView lista;
    String site_ul = "http://eragon.herokuapp.com";
    String room_id, name, room_name, player_id;
    Intent intent;
    Handler handler;
    TextView nume_camera;
    Boolean parola;
    ImageButton btn_intra, btn_iesi;
    Boolean intrat = false;
    Boolean gata = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_game);
        coada = new LinkedList<String>();
        coada2 = new LinkedList <String> ();
        coada3 = new LinkedList <Boolean> ();
        lista = (ListView)findViewById(R.id.lista);
        btn_intra = (ImageButton)findViewById(R.id.btn_intra);
        btn_iesi = (ImageButton)findViewById(R.id.btn_iesi);
        intent = getIntent();
        name = intent.getStringExtra("nume");
        room_id = intent.getStringExtra("room");
        room_name = intent.getStringExtra("room_name");
        Log.e("ala", room_id);
        nume_camera = (TextView)findViewById(R.id.nume_camera);
        nume_camera.setText(room_name);
        ViewNames();
    }

    public void CheckStart()
    {
        String site = site_ul + "/checkstart";
        String current_action = "CheckStart";
        String[] primele = new String[2];
        primele[0] = site;
        primele[1] = current_action;
        String urmatoarele[] = new String[3];
        urmatoarele[0] = "1";
        urmatoarele[1] = "room";
        urmatoarele[2] = room_id;
        new BeforeGame.ATask().execute(primele, urmatoarele);
    }

    public void openGame(String litera)
    {
        gata = true;
        Intent incercare = new Intent(this, Game.class);
        incercare.putExtra("nume", player_id);
        incercare.putExtra("room", room_id);
        incercare.putExtra("litera", litera);
        this.startActivity(incercare);
        finish();
    }

    public void ViewNames()
    {
        String site = site_ul + "/viewnames";
        String current_action = "ViewNames";
        String[] primele = new String[2];
        primele[0] = site;
        primele[1] = current_action;
        String urmatoarele[] = new String[3];
        urmatoarele[0] = "1";
        urmatoarele[1] = "room";
        urmatoarele[2] = room_id;
        new BeforeGame.ATask().execute(primele, urmatoarele);
    }

    public void Join(View view)
    {
        if(!intrat)
        {
            intrat = true;
            String site = site_ul + "/join";
            String current_action = "Join";
            String[] primele = new String[2];
            primele[0] = site;
            primele[1] = current_action;
            String urmatoarele[] = new String[5];
            urmatoarele[0] = "3";
            urmatoarele[1] = "room";
            urmatoarele[2] = room_id;
            urmatoarele[3] = "name";
            urmatoarele[4] = name;
            new BeforeGame.ATask().execute(primele, urmatoarele);
        }
    }

    public void Leave(View view)
    {
        if(intrat)
        {
            intrat = false;
            String site = site_ul + "/leave";
            String current_action = "Leave";
            String[] primele = new String[2];
            primele[0] = site;
            primele[1] = current_action;
            String urmatoarele[] = new String[3];
            urmatoarele[0] = "1";
            urmatoarele[1] = "room";
            urmatoarele[2] = room_id;
            new BeforeGame.ATask().execute(primele, urmatoarele);
        }
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

    public class ATask extends AsyncTask<String[], Void, String> {

        String ceva = "";
        @Override
        protected String doInBackground(String[]... urls) {
            //try {
            try {
                String site = urls[0][0];
                String current_action = urls[0][1];
                Integer cate = Integer.parseInt(urls[1][0]);
                HashMap<String, String> hash = new HashMap<String, String>();
                for(int i = 1; i <= cate; i += 2)
                {
                    String a = urls[1][i];
                    String b = urls[1][i + 1];
                    hash.put(a, b);
                    Log.e("rasp", a + " " + b);
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
                    else
                    {
                        Log.e("rasp", "POST request not worked");
                        coada3.add(false);
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                    coada3.add(false);
                }
            }
            catch (MalformedURLException e)
            {
                Log.e("naspa", "E corupt!");
                coada3.add(false);
            }
            //} catch (Exception e) {
            // Log.e("rasp", "aia e");
            //}
            return ceva;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (!gata) {
                Boolean success = coada3.poll();
                if (success) {
                    String actiune = coada.poll();
                    String rezultat = coada2.poll();
                    Log.e("succes", "nu e oprit!");
                    Log.e("succes", "am intrat");
                    if (!rezultat.equals("Wrong room!"))
                    {
                        if (actiune.equals("CheckStart"))
                        {
                            if (!rezultat.equals("Nu"))
                            {
                                handler.removeCallbacksAndMessages(null);
                                openGame(rezultat);
                            }
                        }
                        else if (actiune.equals("Join"))
                        {
                            if (rezultat.charAt(0) == 'D')
                            {
                                btn_iesi.setVisibility(View.VISIBLE);
                                btn_intra.setVisibility(View.INVISIBLE);
                                player_id = rezultat.substring(2);
                                ViewNames();
                            }
                            else
                                Toast.makeText(BeforeGame.this, rezultat, Toast.LENGTH_LONG).show();
                        }
                        else if (actiune.equals("ViewNames"))
                        {
                            String tot[] = rezultat.split("\\s+");
                            for (int i = 0; i < tot.length; i++)
                            {
                                Log.e("bosso", tot[i]);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(BeforeGame.this, android.R.layout.simple_list_item_1, android.R.id.text1, tot);
                            lista.setAdapter(adapter);
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 0.5s = 500ms
                                    CheckStart();
                                    ViewNames();
                                }
                            }, 500);
                        }
                        else if (actiune.equals("Leave"))
                        {
                            if (rezultat.equals("Gata!"))
                            {
                                btn_intra.setVisibility(View.VISIBLE);
                                btn_iesi.setVisibility(View.INVISIBLE);
                            }
                        }
                        else
                        {
                            Toast.makeText(BeforeGame.this, rezultat, Toast.LENGTH_LONG).show();
                            Log.e("ceva", "No idea, boss!");
                        }
                        /*else {
                    Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_LONG).show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           // Do something after 1s = 1000ms
                            CheckStart();
                         }
                    }, 1000);
                    }*/
                    }
                }
                else
                {
                    Toast.makeText(BeforeGame.this, "Eroare la conexiunea cu serverul!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

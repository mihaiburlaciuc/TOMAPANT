package com.cooldev.tomapan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.os.AsyncTask;
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

public class Create extends Activity {

    Intent intent;
    CheckBox privata;
    EditText parola;
    TextView pass_txt;
    EditText room;
    EditText max_players;
    EditText runde;
    TextView curenti, totali;
    ImageButton btn_creeaza, btn_sterge;
    ListView lista;
    String player_id, room_id, name;
    String site_ul = "https://eragon.herokuapp.com";
    TextView status_conexiune;
    Handler handler;
    Queue <String> coada, coada2;
    Queue <Boolean> coada3;
    boolean in_camera = false;

    @Override
    public void onBackPressed()
    {
        if(in_camera)
            Delete(null);
        finish();
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
        new ATask().execute(primele, urmatoarele);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        coada = new LinkedList <String> ();
        coada2 = new LinkedList <String> ();
        coada3 = new LinkedList <Boolean> ();
        room = (EditText)findViewById(R.id.room);
        max_players = (EditText)findViewById(R.id.max_players);
        runde = (EditText)findViewById(R.id.runde);
        privata = (CheckBox)findViewById(R.id.privata);
        pass_txt = (TextView)findViewById(R.id.pass_txt);
        parola = (EditText)findViewById(R.id.parola);
        curenti  = (TextView)findViewById(R.id.curenti);
        totali = (TextView)findViewById(R.id.totali);
        btn_creeaza = (ImageButton)findViewById(R.id.btn_creeaza);
        btn_sterge = (ImageButton)findViewById(R.id.btn_sterge);
        status_conexiune = (TextView)findViewById(R.id.status_conexiune);
        lista = (ListView)findViewById(R.id.lista);
        intent = getIntent();
        name = intent.getStringExtra("name");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    public void openGame(String litera)
    {
        Intent incercare = new Intent(this, Game.class);
        incercare.putExtra("nume", player_id);
        incercare.putExtra("room", room_id);
        incercare.putExtra("litera", litera);
        this.startActivity(incercare);
        finish();
    }

    public void CheckPlayers()
    {
        String site = site_ul + "/checkplayers";
        String current_action = "CheckPlayers";
        String[] primele = new String[2];
        primele[0] = site;
        primele[1] = current_action;
        String urmatoarele[] = new String[3];
        urmatoarele[0] = "1";
        urmatoarele[1] = "room";
        urmatoarele[2] = room_id;
        new ATask().execute(primele, urmatoarele);
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
        new ATask().execute(primele, urmatoarele);
    }

    public void Creeare(View view)
    {

        if(!in_camera)
        {
            if(room.getText().toString().equals("") || runde.getText().toString().equals("") || max_players.getText().toString().equals("") || (privata.isChecked() && parola.getText().toString().equals("")))
                Toast.makeText(getApplicationContext(), "Completeaza toate spatiile!", Toast.LENGTH_LONG).show();
            else if(Integer.parseInt(runde.getText().toString()) > 15)
                Toast.makeText(getApplicationContext(), "Maxim 15 runde!", Toast.LENGTH_LONG).show();
            else
            {
                in_camera = true;
                status_conexiune.setText("Se conecteaza...");
                String site = site_ul + "/create";
                String current_action = "Create";
                String[] primele = new String[2];
                primele[0] = site;
                primele[1] = current_action;
                String[] urmatoarele = new String[13];
                urmatoarele[0] = "11";
                urmatoarele[1] = "nume_camera";
                urmatoarele[2] = room.getText().toString();
                urmatoarele[3] = "host";
                urmatoarele[4] = name;
                urmatoarele[5] = "privata";
                urmatoarele[7] = "parola";
                if (privata.isChecked()) {
                    urmatoarele[6] = "true";
                    urmatoarele[8] = parola.getText().toString();
                } else {
                    urmatoarele[6] = "nu";
                    urmatoarele[8] = "";
                }
                urmatoarele[9] = "runda_max";
                urmatoarele[10] = runde.getText().toString();
                urmatoarele[11] = "max_players";
                urmatoarele[12] = max_players.getText().toString();
                new ATask().execute(primele, urmatoarele);
            }
        }
    }

    public void disable_all()
    {
        room.setEnabled(false);
        max_players.setEnabled(false);
        runde.setEnabled(false);
        privata.setEnabled(false);
        pass_txt.setEnabled(false);
        parola.setEnabled(false);
        btn_creeaza.setVisibility(View.INVISIBLE);
        btn_sterge.setVisibility(View.VISIBLE);
        lista.setEnabled(true);
    }

    public void enable_all()
    {
        room.setEnabled(true);
        max_players.setEnabled(true);
        runde.setEnabled(true);
        privata.setEnabled(true);
        pass_txt.setEnabled(true);
        parola.setEnabled(true);
        btn_creeaza.setVisibility(View.VISIBLE);
        btn_sterge.setVisibility(View.INVISIBLE);
        lista.setAdapter(null);
    }

    public void Show_pass(View view)
    {
        if(privata.isChecked())
        {
            pass_txt.setVisibility(View.VISIBLE);
            parola.setVisibility(View.VISIBLE);
        }
        else
        {
            pass_txt.setVisibility(View.INVISIBLE);
            parola.setVisibility(View.INVISIBLE);
        }
    }

    public void Delete(View view)
    {
        if(in_camera)
        {
            in_camera = false;
            status_conexiune.setText("Se conecteaza...");
            String site = site_ul + "/delete";
            String current_action = "Delete";
            String[] primele = new String[2];
            primele[0] = site;
            primele[1] = current_action;
            String[] urmatoarele = new String[5];
            urmatoarele[0] = "3";
            urmatoarele[1] = "name";
            urmatoarele[2] = name;
            urmatoarele[3] = "room";
            urmatoarele[4] = room_id;
            new ATask().execute(primele, urmatoarele);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                HashMap <String, String> hash = new HashMap<String, String>();
                for(int i = 1; i <= cate; i += 2)
                {
                    String a = urls[1][i];
                    String b = urls[1][i + 1];
                    hash.put(a, b);
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
        protected void onPostExecute(String result)
        {
            Boolean success = coada3.poll();
            status_conexiune.setText("");
            if(success)
            {
                String actiune = coada.poll();
                String rezultat = coada2.poll();
                Log.e("succes", "nu e oprit!");
                Log.e("succes", "am intrat");
                if(!rezultat.equals("Wrong room!"))
                {
                    if (actiune.equals("Create"))
                    {
                        room_id = rezultat;
                        Toast.makeText(Create.this, "Camera a fost creata! ", Toast.LENGTH_LONG).show();
                        player_id = "0";
                        totali.setText(max_players.getText().toString());
                        disable_all();
                        CheckStart();
                     }
                     else if (actiune.equals("CheckStart"))
                     {
                         if(rezultat.equals("Nu"))
                         {
                             handler = new Handler();
                             handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                   // Do something after 0.5s = 500ms
                                    CheckPlayers();
                                }
                            }, 500);
                         }
                         else
                            openGame(rezultat);
                     }
                     else if(actiune.equals("CheckPlayers"))
                     {
                         curenti.setText(rezultat);
                         ViewNames();
                     }
                     else if(actiune.equals("ViewNames"))
                     {
                         String tot[] = rezultat.split("\\s+");
                         for(int i = 0; i < tot.length; i++)
                         {
                             Log.e("bosso", tot[i]);
                         }
                         ArrayAdapter<String> adapter = new ArrayAdapter<String>(Create.this, android.R.layout.simple_list_item_1, android.R.id.text1, tot);
                         lista.setAdapter(adapter);
                         CheckStart();
                     }
                     else if(actiune.equals("Delete"))
                     {
                         if(rezultat.equals("Succes"))
                         {
                             in_camera = false;
                             status_conexiune.setText("");
                             Toast.makeText(Create.this, "Camera a fost stearsa!", Toast.LENGTH_LONG).show();
                             handler.removeCallbacksAndMessages(null);
                             enable_all();
                         }
                         else
                         {
                             Toast.makeText(Create.this, rezultat, Toast.LENGTH_LONG).show();
                             Log.e("ceva", "iar a intrat pe asta!");
                         }
                     }
                     else
                     {
                         Toast.makeText(Create.this, rezultat, Toast.LENGTH_LONG).show();
                         Log.e("ceva", "No idea, boss!");
                     }/*else {
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
                Toast.makeText(Create.this, "Eroare la conexiunea cu serverul!", Toast.LENGTH_LONG).show();
            }
        }
    }
}

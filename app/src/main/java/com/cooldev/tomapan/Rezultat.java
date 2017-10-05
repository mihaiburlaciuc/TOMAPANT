package com.cooldev.tomapan;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rezultat extends Activity {

    ListView lista_nume, lista_punctaj;
    Intent intent;
    String site = "http://eragon.herokuapp.com/getresults";
    String room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezultat);
        lista_nume = (ListView)findViewById(R.id.lista_nume);
        lista_punctaj = (ListView)findViewById(R.id.lista_punctaj);
        intent = getIntent();
        room = intent.getStringExtra("room");
        new ATask().execute();
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

    public class ATask extends AsyncTask<String, Void, String> {
        String rez = "";
        @Override
        protected String doInBackground(String... urls) {
            //try {
            try {
                Log.e("rasp", site);
                URL obj = new URL(site);
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("room", room);
                Log.e("ceva", room);
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
                        rez = response.toString();
                        Log.e("ceva", rez);
                    }
                    else {
                        Log.e("rasp", "POST request not worked");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                Log.e("naspa", "E corupt!");
            }
            //} catch (Exception e) {
            // Log.e("rasp", "aia e");
            //}
            return rez;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(!rez.equals(""))
            {
                String tot[] = rez.split("\\s+");
                ArrayList<String> nume = new ArrayList<>();
                ArrayList<String> punctaje = new ArrayList<>();
                //for(int i = 0; i < tot.length; i++)
                //Log.e("ceva", tot[i]);
                //Log.e("ceva", " ba ");
                int nr_jucatori = Integer.parseInt(tot[0]);
                String[][] matrice = new String[2][nr_jucatori];

                for (int i = 1; i < nr_jucatori * 2; i += 2) {
                    matrice[0][i / 2] = tot[i];
                    matrice[1][i / 2] = tot[i + 1];
                }
                boolean sortat = false;
                while (!sortat) {
                    sortat = true;
                    for (int i = 0; i < nr_jucatori - 1; i++) {
                        if (Integer.parseInt(matrice[1][i]) < Integer.parseInt(matrice[1][i + 1])) {
                            sortat = false;
                            String temp1 = matrice[1][i + 1];
                            String temp2 = matrice[0][i + 1];
                            matrice[1][i + 1] = matrice[1][i];
                            matrice[0][i + 1] = matrice[0][i];
                            matrice[1][i] = temp1;
                            matrice[0][i] = temp2;
                        }
                    }
                }
                ArrayAdapter<String> adapter_nume = new ArrayAdapter<String>(Rezultat.this, android.R.layout.simple_list_item_1, android.R.id.text1, matrice[0]);
                ArrayAdapter<String> adapter_punctaje = new ArrayAdapter<String>(Rezultat.this, android.R.layout.simple_list_item_1, android.R.id.text1, matrice[1]);
                lista_nume.setAdapter(adapter_nume);
                lista_punctaj.setAdapter(adapter_punctaje);
            }
            else
                Toast.makeText(Rezultat.this, "Eroare la conexiunea cu serverul!", Toast.LENGTH_LONG).show();
        }
    }
}

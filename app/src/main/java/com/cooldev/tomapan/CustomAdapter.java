package com.cooldev.tomapan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

/**
 * Created by vally on 12.02.2016.
 */
class CustomAdapter extends ArrayAdapter<String> {

    Context contextRooms;
    CustomAdapter(Context context, String[] camere) {
        super(context, R.layout.custom_row, camere);
        contextRooms = context;
        coada = new LinkedList <String>();
        coada2 = new LinkedList <String> ();
        coada3 = new LinkedList <Boolean> ();
    }
    boolean intrat = false;
    String player_id, room_id;
    String site_ul = "http://eragon.herokuapp.com";
    Queue<String> coada, coada2;
    Queue <Boolean> coada3;


    static class ViewHolder {
        TextView camera;
        TextView runde;
        TextView players;
        TextView max_players;
        ImageView privata;
        Button Buton;
        String room_id;
    }
    ViewHolder ceva;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        String variabile[] = getItem(position).split("\\s+");
        for(int i = 0; i <= 5; i++)
            Log.e("yer mum", variabile[i]);

        //if(convertView == null)
        //{
            LayoutInflater linflater = LayoutInflater.from(contextRooms);
            convertView = linflater.inflate(R.layout.custom_row, parent, false);
            holder = new ViewHolder();
            holder.camera = (TextView) convertView.findViewById(R.id.Nume);
            holder.camera.setTypeface(Typeface.SERIF);
            holder.runde = (TextView) convertView.findViewById(R.id.runde);
            holder.players = (TextView) convertView.findViewById(R.id.players);
            holder.max_players = (TextView) convertView.findViewById(R.id.max_players);
            holder.privata = (ImageView) convertView.findViewById(R.id.privata);
            holder.Buton = (Button) convertView.findViewById(R.id.Buton);
            //for(int i = 0; i <= 5; i++)
              //  Log.e("yer mum", variabile[i]);
            holder.camera.setText(variabile[0]);
            if (!variabile[1].equals("true"))
                holder.privata.setVisibility(View.INVISIBLE);
            holder.runde.setText(variabile[2]);
            holder.players.setText(variabile[3]);
            holder.max_players.setText(variabile[4]);
            holder.room_id = variabile[5];
            Log.e("ala", holder.room_id);

            holder.Buton.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    int playeri = Integer.parseInt(holder.players.getText().toString());
                    int maximi = Integer.parseInt(holder.max_players.getText().toString());
                    if(playeri < maximi)
                    {
                        if(holder.privata.getVisibility() == View.VISIBLE)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(contextRooms);
                            builder.setTitle("Introduceti parola:");
                             // Set up the input
                            final EditText input = new EditText(contextRooms);
                            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            builder.setView(input);
                            // Set up the buttons
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String site = site_ul + "/checkpass";
                                String current_action = "CheckPass";
                                String[] primele = new String[2];
                                primele[0] = site;
                                primele[1] = current_action;
                                String urmatoarele[] = new String[5];
                                urmatoarele[0] = "3";
                                urmatoarele[1] = "room";
                                urmatoarele[2] = holder.room_id;
                                urmatoarele[3] = "parola";
                                urmatoarele[4] = input.getText().toString();
                                new ATask((ViewHolder) v.getTag()).execute(primele, urmatoarele);
                            }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {dialog.cancel();
                                        }
                                    });
                                    builder.show();
                        }
                        else
                        {
                            String site = site_ul + "/checkstart";
                            String current_action = "CheckStart";
                            String[] primele = new String[2];
                            primele[0] = site;
                            primele[1] = current_action;
                            String urmatoarele[] = new String[3];
                            urmatoarele[0] = "1";
                            urmatoarele[1] = "room";
                            urmatoarele[2] = holder.room_id;
                            new ATask((ViewHolder) v.getTag()).execute(primele, urmatoarele);
                        }
                    }
                    else
                    {
                        Toast.makeText(contextRooms, "Camera este plina!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            convertView.setTag(holder);
            holder.Buton.setTag(holder);
        //}
        //else
          //  holder = (ViewHolder) convertView.getTag();
        return convertView;
    }



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
        String altceva = "";
        ViewHolder myHolder;
        public ATask(ViewHolder view) {
            myHolder = view;
            ceva = myHolder;
            room_id = myHolder.room_id;
        }
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
                    Log.e("bola", a + b);
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
            return altceva;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Boolean success = coada3.poll();
            if(success)
            {
                String actiune = coada.poll();
                String rezultat = coada2.poll();
                if(!rezultat.equals("Wrong room!"))
                {
                    if(actiune.equals("CheckPass"))
                    {
                        if(rezultat.equals("OK"))
                        {
                            String site = site_ul + "/checkstart";
                            String current_action = "CheckStart";
                            String[] primele = new String[2];
                            primele[0] = site;
                            primele[1] = current_action;
                            String urmatoarele[] = new String[3];
                            urmatoarele[0] = "1";
                            urmatoarele[1] = "room";
                            urmatoarele[2] = myHolder.room_id;
                            new ATask(myHolder).execute(primele, urmatoarele);
                        }
                        else
                            Toast.makeText(contextRooms, rezultat, Toast.LENGTH_LONG).show();
                    }
                    else if(actiune.equals("CheckStart"))
                    {
                        if(rezultat.equals("Nu"))
                        {
                            Intent incercare = new Intent(contextRooms, BeforeGame.class);
                            incercare.putExtra("nume", Rooms.name);
                            incercare.putExtra("room", myHolder.room_id);
                            incercare.putExtra("room_name", myHolder.camera.getText().toString());
                            incercare.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            contextRooms.startActivity(incercare);
                        }
                        else
                            Toast.makeText(contextRooms, "Jocul a inceput deja!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(contextRooms, "Camera nu mai exista!", Toast.LENGTH_LONG).show();
                }
            }
            else
                Toast.makeText(contextRooms, "Eroare la conexiunea la server!", Toast.LENGTH_LONG).show();
        }
    }
}

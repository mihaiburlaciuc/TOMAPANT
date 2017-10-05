package com.cooldev.tomapan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import android.app.Activity;
import android.widget.Toast;

import com.tomapan.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Multi extends Activity {

    HashMap<String, String> hash;
    boolean oprit = false;
    EditText name;
    EditText room;
    EditText max_players;
    EditText privata;
    EditText runde;
    String site, answer, player_id, room_id;
    String site_ul = "http://eragon.herokuapp.com";
    boolean creeaza = false;

    @Override
    protected void onStop() {
        oprit = true;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        oprit = true;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);
        name = (EditText)findViewById(R.id.name);
        //room = (EditText)findViewById(R.id.room);
        //max_players = (EditText)findViewById(R.id.max_players);
        //privata = (EditText)findViewById(R.id.privata);
        //runde = (EditText)findViewById(R.id.runde);
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

    public void Open(View view)
    {
        Intent incercare = new Intent(this, Game.class);
        incercare.putExtra("nume", "1");
        incercare.putExtra("room", room.getText().toString());
        incercare.putExtra("litera", "A");
        this.startActivity(incercare);
    }

    public void openRooms(View view)
    {

        if(name.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(), "Scrie mai intai un nume!", Toast.LENGTH_LONG).show();
        else
        {
            Intent incercare = new Intent(this, Rooms.class);
            incercare.putExtra("name", name.getText().toString());
            this.startActivity(incercare);
        }
    }

    public void Create_room(View view)
    {
        if(name.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(), "Scrie mai intai un nume!", Toast.LENGTH_LONG).show();
        else
        {
            Intent incercare = new Intent(this, Create.class);
            incercare.putExtra("name", name.getText().toString());
            this.startActivity(incercare);
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
}

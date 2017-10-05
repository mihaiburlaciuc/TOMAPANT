package com.cooldev.tomapan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import android.app.Activity;

import com.tomapan.R;

import java.util.Random;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Single(View view)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt(26);
        int ceva = (int)'A' + randomNum;
        String s = "";
        s += (char)ceva;
        Intent incercare = new Intent(this, Single.class);
        incercare.putExtra("litera", s);
        this.startActivity(incercare);
    }

    public void Multi(View view)
    {
        Intent incercare = new Intent(this, Multi.class);
        this.startActivity(incercare);
    }

    public void Reguli(View view)
    {
        Intent incercare = new Intent(this, Reguli.class);
        this.startActivity(incercare);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}

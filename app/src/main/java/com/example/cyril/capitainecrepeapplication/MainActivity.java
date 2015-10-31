package com.example.cyril.capitainecrepeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public final static String ACTION = "ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    public void finish() {
        System.out.println("MainActivity.finish");
        super.finish();
    }

    public void listeDesPlats(View v) {
        Intent intent = new Intent(this, ListePlatsFragActivity.class);
        intent.putExtra(ACTION, "LISTE");
        startActivity(intent);
    }

    public void verifierQuantite(View v) {
        Intent intent = new Intent(this, ListePlatsFragActivity.class);
        intent.putExtra(ACTION, "QUANTITE");
        startActivity(intent);
    }

    public void commanderUnPlat(View v) {
        Intent intent = new Intent(this, CommandeActivity.class);
        startActivity(intent);
    }

    public void logout(View v) {
        System.out.println("Logout");
        finish();
    }

}

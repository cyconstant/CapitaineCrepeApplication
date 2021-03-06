package com.example.cyril.capitainecrepeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // connexion au serveur
        service = new Intent(MainActivity.this, SocketService.class);
        startService(service);
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

    public void commanderUnPlat(View v) {
        Intent intent = new Intent(this, CommandeActivity.class);
        startActivity(intent);
    }

    public void ajouterUnPlat(View v) {
        Intent intent = new Intent(this, CuisineActivity.class);
        startActivity(intent);
    }

    public void logout(View v) {
        System.out.println("Logout");
        finish();
    }

    @Override
    public void finish() {
        System.out.println("MainActivity.finish");
        super.finish();
    }

    @Override
    protected void onDestroy() {
        System.out.println("MainActivity.onDestroy");
        stopService(service);
        super.onDestroy();
    }

}

package com.example.cyril.capitainecrepeapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ListePlatsFragActivity extends AppCompatActivity {

    private ListePlatsActivityFragment frag1;
    private FragmentManager fragmentManager;

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private ReadMessages readMessages;
    private String listeDesPlats = "";
    private Socket socket;

    private class StartNetwork extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            System.out.println("StartNetwork.onPreExecute");
        }

        @Override
        protected Boolean doInBackground(Void... v) {
            System.out.println("StartNetwork.doInBackground");
            PrintWriter writer;
            try {
                socket = new Socket("10.0.2.2", 7777);
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("LISTE");
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean b) {
            if (b) {
                System.out.println("Connected to server\n");
                readMessages = new ReadMessages();
                readMessages.execute();
            } else {
                System.out.println("Could not connect to server\n");
            }
        }
    }

    private class ReadMessages extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... v) {
            System.out.println("ReadMessages.doInBackground");
            String message;
            try {
                while (!((message = reader.readLine()).equals("FINLISTE"))) {
                    listeDesPlats += message + "\n";
                }
            } catch (IOException e) {
                System.out.println("ReadMessages Exception");
                return null;
            }
            System.out.println("listeDesPlats =" + listeDesPlats);
            return listeDesPlats;
        }

        @Override
        protected void onPostExecute(String message) {
            System.out.println("ReadMessages.onPostExecute");
            if (message != null) {
                displayMessage();
            }
        }

    }

    private void displayMessage() {
        // On affiche les plats
        frag1.afficherLesPlats(listeDesPlats);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listeplats_frag);

        // Initialisation du gestionnaire de fragments
        fragmentManager = getFragmentManager();

        // On initialise le fragment en le récupérant si il existe déjà
        // en le créant sinon
        frag1 = (ListePlatsActivityFragment) fragmentManager.findFragmentById(R.id.layout_fragment);
        if (frag1 == null) {
            System.out.println("Creation d'un nouveau fragment");
            frag1 = new ListePlatsActivityFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_fragment, frag1);
            transaction.commit();
        } else {
            System.out.println("Recup du fragment existant");
        }
    }


    @Override
    protected void onPause() {
        // On attache le fragment pour conserver les données

        if (!frag1.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_fragment, frag1);
            transaction.commit();
        }
        // On appelle la méthode de la super-classe
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quantite, menu);
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
    protected void onStart() {
        super.onStart();
        System.out.println("ListePlatsFragActivity.onStart 1");
        new StartNetwork().execute();
        System.out.println("ListePlatsFragActivity.onStart 2");
    }

    @Override
    public void finish() {
        System.out.println("ListePlatsFragActivity.finish");

        if (readMessages != null) {
            readMessages.cancel(true);
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.finish();
    }
}

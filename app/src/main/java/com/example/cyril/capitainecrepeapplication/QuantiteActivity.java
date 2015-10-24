package com.example.cyril.capitainecrepeapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class QuantiteActivity extends AppCompatActivity {

    private TextView textViewQuantite;
    private PrintWriter writer = new PrintWriter(System.out, true);
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private ReadMessages readMessages;

    private boolean finDeLaListe = false;
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
            try {
                socket = new Socket("10.0.2.2", 7777);
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("QUANTITE");
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

    private class ReadMessages extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... v) {
            while (!finDeLaListe) {
                try {
                    String message = reader.readLine();
                    publishProgress(message);
                } catch (IOException e) {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... messages) {
            int lastMessage = messages.length - 1;
            for (int i = 0; i < lastMessage; i++) {
                displayMessage(messages[i] + "\n");
            }

            if (messages[lastMessage].equals("FINLISTE")) {
                finDeLaListe = true;
            } else {
                displayMessage(messages[lastMessage]);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantite);

        textViewQuantite = (TextView) findViewById(R.id.textViewQuantite);
        //Intent data = getIntent();
        System.out.println("QuantiteActivity.onCreate");
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
        System.out.println("QuantiteActivity.onStart 1");
        new StartNetwork().execute();
        System.out.println("QuantiteActivity.onStart 2");
    }

    @Override
    public void finish() {
        System.out.println("QuantiteActivity.finish");
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

    private void displayMessage(String message) {
        // On ajoute un plat a la liste
        listeDesPlats += message + "\n";
        // On affiche les plats
        textViewQuantite.setText(listeDesPlats);

    }
}

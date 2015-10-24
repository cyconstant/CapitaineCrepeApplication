package com.example.cyril.capitainecrepeapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommandeActivity extends AppCompatActivity {

    private EditText nomDuPlatACommander;
    private TextView textViewCommande;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private PrintWriter writer;
    private ReadMessages readMessages;

    private boolean finDuMessage = false;
    private String lePlat = "";

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
                //writer.println("COMMANDE"+ "crepe au jambon");
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
            while (!finDuMessage) {
                if (!lePlat.equals("")) {
                    writer.println("COMMANDE " + lePlat);
                    try {
                        String message = reader.readLine();
                        publishProgress(message);
                    } catch (IOException e) {
                        break;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... messages) {
            String message = messages[0];
            if (message != null) {
                System.out.println("message = " + message);
                displayMessage(message + "\n");
                if (message.contains("command")) {
                    System.out.println("commande OK");
                    finDuMessage = true;
                } else if (message.contains("puis"))

                    System.out.println("commande non OK plat epuise");
                // pour test
                finDuMessage = false;
                lePlat = "";
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        nomDuPlatACommander = (EditText) findViewById(R.id.nomDuPlatACommander);
        textViewCommande = (TextView) findViewById(R.id.textViewCommande);
        System.out.println("CommandeActivity.onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_commande, menu);
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
        new StartNetwork().execute();
    }

    @Override
    public void finish() {
        System.out.println("CommandeActivity.finish");
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
        // Afficher la reponse du serveur
        textViewCommande.setText(message);

    }

    public void validerCommande(View v) {
        lePlat = nomDuPlatACommander.getText().toString();
    }
}

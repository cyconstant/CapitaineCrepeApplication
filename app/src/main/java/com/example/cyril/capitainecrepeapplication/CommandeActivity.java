package com.example.cyril.capitainecrepeapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommandeActivity extends AppCompatActivity {

    private InformationFragment fragInfo;
    private PlatsDispoActivityFragment fragPlatsDispo;
    private FragmentManager fragmentManager;

    private EditText nomDuPlatACommander;

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private PrintWriter writer;
    private ReadMessages readMessages;
    private String listeDesPlats = "";
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

            /* Afficher les plats disponibles */
            writer.println("LISTE");
            String message;
            try {
                while (!((message = reader.readLine()).equals("FINLISTE"))) {
                    listeDesPlats += message + "\n";
                }
            } catch (IOException e) {
                System.out.println("ReadMessages Exception");
                return null;
            }

            return listeDesPlats;
        }

        @Override
        protected void onPostExecute(String listeDesPlats) {
            displayMessage(listeDesPlats);
        }

    }

    private void displayMessage(String message) {
        if (message != null) {
            fragPlatsDispo.afficherPlatsDispo(listeDesPlats);
        }

    }

    private class ReadMessagesCommande extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... v) {

            System.out.println("ReadMessagesCommande.doInBackground");

            /* Prendre la commande */
            writer.println("COMMANDE " + lePlat);
            String retourServeur = "";
            if (!lePlat.equals("")) {
                try {
                    retourServeur = reader.readLine();
                } catch (IOException e) {
                    return null;
                }
            }

            return retourServeur;
        }

        @Override
        protected void onPostExecute(String retourServeur) {
            displayMessageCommande(retourServeur);
        }

    }

    private void displayMessageCommande(String message) {
        if (message != null) {
            fragInfo.afficherInformation(message);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        nomDuPlatACommander = (EditText) findViewById(R.id.nomDuPlatACommander);

        // Initialisation du gestionnaire de fragments
        fragmentManager = getFragmentManager();

        // On initialise le fragment en le récupérant si il existe déjà
        // en le créant sinon
        fragInfo = (InformationFragment) fragmentManager.findFragmentById(R.id.layout_fragment_info);
        if (fragInfo == null) {
            System.out.println("Creation d'un nouveau InformationFragment");
            fragInfo = new InformationFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_fragment_info, fragInfo);
            transaction.commit();
        } else {
            System.out.println("Recup du InformationFragment existant");
        }

        fragPlatsDispo = (PlatsDispoActivityFragment) fragmentManager.findFragmentById(R.id.layout_fragment_plats_dispo);
        if (fragPlatsDispo == null) {
            System.out.println("Creation d'un nouveau PlatsDispoActivityFragment");
            fragPlatsDispo = new PlatsDispoActivityFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_fragment_plats_dispo, fragPlatsDispo);
            transaction.commit();
        } else {
            System.out.println("Recup du PlatsDispoActivityFragment existant");
        }
    }

    @Override
    protected void onPause() {
        // On attache le fragment pour conserver les données

        if (!fragInfo.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_fragment_info, fragInfo);
            transaction.commit();
        }

        if (!fragPlatsDispo.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_fragment_plats_dispo, fragPlatsDispo);
            transaction.commit();
        }

        // On appelle la méthode de la super-classe
        super.onPause();
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
        /*if (readMessages != null) {
            readMessages.cancel(true);
            System.out.println("readMessages.cancel(true)");
        }*/
        if (socket != null) {
            try {
                socket.close();
                System.out.println("socket.close();");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.finish();
    }


    public void validerCommande(View v) {
        lePlat = nomDuPlatACommander.getText().toString();
        if (!lePlat.equals("")) {
            /* commander le plat */
            ReadMessagesCommande readMessagesCommande = new ReadMessagesCommande();
            readMessagesCommande.execute();
            nomDuPlatACommander.setText("");

            /* mettre a jour les plats disponibles */
            listeDesPlats = "";
            readMessages = new ReadMessages();
            readMessages.execute();
        }
    }
}

package com.example.cyril.capitainecrepeapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandeActivity extends AppCompatActivity {

    private InformationFragment fragInfo;
    private PlatsDispoActivityFragment fragPlatsDispo;
    private FragmentManager fragmentManager;
    private EditText nomDuPlatACommander;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private ReadMessages readMessages;
    private String lePlat = "";

    SocketService mService;
    boolean mBound = false;

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to SocketService, cast the IBinder and get SocketService instance
            SocketService.LocalBinder binder = (SocketService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void doBindService() {
        // Bind to SocketService
        Intent intent = new Intent(this, SocketService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;
    }

    private void doUnbindService() {
        if (mBound) {
            // Unbind from the service
            unbindService(mConnection);
            mBound = false;
        }
    }


    private class ReadMessages extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... v) {

            System.out.println("ReadMessages.doInBackground");
            System.out.println("mBound=" + mBound);

            String listeDesPlats = "";
            System.out.println("listeDesPlats=" + listeDesPlats);

            // attendre de recuperer le service
            while (mService == null) {
                SystemClock.sleep(100);
                System.out.println("mService=" + mService);
            }
            System.out.println("mService=" + mService);

            reader = mService.getReader();

            /* Demander les plats disponibles */
            mService.sendMessage("LISTE");
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
            //fragPlatsDispo.afficherPlatsDispo(listeDesPlats);
            fragPlatsDispo.afficherPlatsDispo(message);
        }
    }

    private class ReadMessagesCommande extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... v) {

            System.out.println("ReadMessagesCommande.doInBackground");

            /* Prendre la commande */
            mService.sendMessage("COMMANDE " + lePlat);
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
        doBindService();
        readMessages = new ReadMessages();
        readMessages.execute();

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
    }

    @Override
    public void finish() {
        System.out.println("CommandeActivity.finish");
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    public void validerCommande(View v) {
        lePlat = nomDuPlatACommander.getText().toString();
        if (!lePlat.equals("")) {
            /* commander le plat */
            ReadMessagesCommande readMessagesCommande = new ReadMessagesCommande();
            readMessagesCommande.execute();
            nomDuPlatACommander.setText("");

            /* mettre a jour les plats disponibles */
            readMessages = new ReadMessages();
            readMessages.execute();
        }
    }
}

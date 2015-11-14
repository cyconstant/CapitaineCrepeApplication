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

public class CuisineActivity extends AppCompatActivity {

    private InformationFragment fragInfo;
    private QuantiteFragActivityFragment fragQuantite;
    private FragmentManager fragmentManager;

    private EditText nomDuPlatAAjouter;

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private ReadMessages readMessages;
    private String quantiteEtNomDuPlat = "";
    private String listeDesPlats = "";

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

    private class ReadMessages extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... demande) {

            System.out.println("ReadMessages.doInBackground");
            System.out.println("mBound=" + mBound);

            // attendre de recuperer le service
            while (mService == null) {
                SystemClock.sleep(100);
                System.out.println("mService=" + mService);
            }
            System.out.println("mService=" + mService);

            reader = mService.getReader();
            /* Demander la quantite des plats dispos */
            //mService.sendMessage("QUANTITE");
            mService.sendMessage(demande[0]);
            String message;
            try {
                while (!((message = reader.readLine()).equals("FINLISTE"))) {
                    listeDesPlats += message + "\n";
                }
            } catch (IOException e) {
                System.out.println("ReadMessages Exception");
                return null;
            }

            return demande[0];
        }

        @Override
        protected void onPostExecute(String demande) {
            System.out.println("ReadMessages.onPostExecute");
            if (demande.equalsIgnoreCase("QUANTITE")) {
                if (listeDesPlats != null) {
                    fragQuantite.afficherPlatsDispo(listeDesPlats);
                }
            } else if (demande.equalsIgnoreCase("AJOUT")) {
                fragInfo.afficherInformation(listeDesPlats);
            }
        }

    }


    private class ReadMessagesQuantite extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... v) {
            String retourServeur = "";
            if (!quantiteEtNomDuPlat.equals("")) {
                try {
                    mService.sendMessage("AJOUT " + quantiteEtNomDuPlat);
                        retourServeur = reader.readLine();
                    } catch (IOException e) {
                        return null;
                    }
            }
            return retourServeur;
        }

        @Override
        protected void onPostExecute(String retourServeur) {
            System.out.println("ReadMessagesQuantite.onPostExecute");
            if (retourServeur != null) {
                fragInfo.afficherInformation(retourServeur);
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine);

        System.out.println("CuisineActivity.onCreate");
        doBindService();
        nomDuPlatAAjouter = (EditText) findViewById(R.id.nomDuPlatAAjouter);

        readMessages = new ReadMessages();
        readMessages.execute("QUANTITE");

        // Initialisation du gestionnaire de fragments
        fragmentManager = getFragmentManager();

        // On initialise les fragments en les récupérant si ils existent déjà
        // en les créant sinon
        fragInfo = (InformationFragment) fragmentManager.findFragmentById(R.id.layout_fragment_info);
        if (fragInfo == null) {
            System.out.println("Creation d'un nouveau fragment fragInfo");
            fragInfo = new InformationFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_fragment_info, fragInfo);
            transaction.commit();
        } else {
            System.out.println("Recup du fragment fragInfo existant");
        }

        fragQuantite = (QuantiteFragActivityFragment) fragmentManager.findFragmentById(R.id.layout_fragment_quantite);
        if (fragQuantite == null) {
            System.out.println("Creation d'un nouveau fragQuantite");
            fragQuantite = new QuantiteFragActivityFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_fragment_quantite, fragQuantite);
            transaction.commit();
        } else {
            System.out.println("Recup du fragment fragment fragQuantite");
        }


    }

    @Override
    protected void onPause() {
        // On attache les fragments pour conserver les données
        if (!fragInfo.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_fragment_info, fragInfo);
            transaction.commit();
        }

        if (!fragQuantite.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_fragment_quantite, fragQuantite);
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
        System.out.println("CuisineActivity.finish");
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    public void validerAjout(View v) {
        quantiteEtNomDuPlat = nomDuPlatAAjouter.getText().toString();
        if (!quantiteEtNomDuPlat.equals("")) {
            /* ajouter le plat */
            ReadMessagesQuantite readMessagesQuantite = new ReadMessagesQuantite();
            readMessagesQuantite.execute();
            nomDuPlatAAjouter.setText("");

            /* mettre a jour les quantites */
            listeDesPlats = "";
            readMessages = new ReadMessages();
            readMessages.execute("QUANTITE");
        }
    }
}

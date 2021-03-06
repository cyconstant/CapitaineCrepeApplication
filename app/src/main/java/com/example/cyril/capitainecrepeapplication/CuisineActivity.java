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

public class CuisineActivity extends AppCompatActivity {

    private InformationFragment fragInfo;
    private QuantiteActivityFragment fragQuantite;
    private FragmentManager fragmentManager;

    private EditText nomDuPlatAAjouter;
    private String listeDesPlats = "";
    private String retourServeur = "";

    SocketService mService;
    boolean mBound = false;

    public String getNomDuPlatAAjouter() {
        return nomDuPlatAAjouter.getText().toString();
    }

    public void setNomDuPlatAAjouter(String quantiteEtNom) {
        nomDuPlatAAjouter.setText(quantiteEtNom);
    }

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

            String requete = demande[0];
            /* attendre de recuperer le service */
            while (mService == null) {
                SystemClock.sleep(100);
                System.out.println("mService=" + mService);
            }

            /* Afficher la quantite des plats dispos */
            if (requete.equalsIgnoreCase("QUANTITE")) {
                String message;
                mService.sendMessage("QUANTITE");
                while (!((message = mService.readLine()).equals("FINLISTE"))) {
                    listeDesPlats += message + "\n";
                }
            /* Ajouter un plat */
            } else if (requete.equalsIgnoreCase("AJOUT ")) {
                mService.sendMessage("AJOUT " + getNomDuPlatAAjouter());
                retourServeur = mService.readLine();
            }
            return requete;
        }


        @Override
        protected void onPostExecute(String requete) {
            System.out.println("ReadMessages.onPostExecute");
            displayMessage(requete);
        }

    }

    private void displayMessage(String requete) {
        if (requete.equalsIgnoreCase("QUANTITE")) {
            fragQuantite.afficherPlatsDispo(listeDesPlats);
        } else if (requete.equalsIgnoreCase("AJOUT ")) {
            fragInfo.afficherInformation(retourServeur);
            setNomDuPlatAAjouter("");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine);

        System.out.println("CuisineActivity.onCreate");
        doBindService();
        nomDuPlatAAjouter = (EditText) findViewById(R.id.nomDuPlatAAjouter);

        ReadMessages readMessages = new ReadMessages();
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

        fragQuantite = (QuantiteActivityFragment) fragmentManager.findFragmentById(R.id.layout_fragment_quantite);
        if (fragQuantite == null) {
            System.out.println("Creation d'un nouveau fragQuantite");
            fragQuantite = new QuantiteActivityFragment();
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
        System.out.println("CuisineActivity.onStart");
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
        if (!getNomDuPlatAAjouter().equals("") && getNomDuPlatAAjouter().length() > 2) {
            /* ajouter le plat */
            ReadMessages readMessagesQuantite = new ReadMessages();
            readMessagesQuantite.execute("AJOUT ");

            /* mettre a jour les quantites */
            listeDesPlats = "";
            ReadMessages readMessages = new ReadMessages();
            readMessages.execute("QUANTITE");

        }
        // mise a jour compteur du fragment :
        fragQuantite.setCompteurDeClics(0);
    }
}
